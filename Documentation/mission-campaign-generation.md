# Mission and campaign generation overview

## Scope
This document summarizes how campaigns and missions are generated in PWCG TC, with emphasis on unit placement and flight instructions/waypoints. References call out the classes/methods that build each step and the TCData inputs they rely on.

## Campaign generation flow
1. **Inputs and validation**
   - `CampaignGeneratorModel` holds user inputs (company name, date, player, mode) and validates them in `validateCampaignInputs()` (`pwcg.campaign.CampaignGeneratorModel`).
2. **Campaign creation and context selection**
   - `CampaignGenerator.generate()` orchestrates setup (`pwcg.campaign.CampaignGenerator`):
     - `setMapForNewCampaign()` picks the initial map based on the player company base (`Company.determineCurrentBaseAnyMap`) and calls `PWCGContext.getInstance().changeContext(...)`.
     - `createCampaign()` initializes `Campaign` data and configs.
     - `setCampaignAces()` loads historical aces from `PWCGContext.getInstance().getAceManager().loadFromHistoricalAces(...)`.
3. **Company staffing and equipment**
   - `CampaignGenerator.staffCompanies()` gets all active companies on the start date via `PWCGContext.getInstance().getCompanyManager().getActiveCompanies(...)`.
   - For each, `CampaignCompanyGenerator.createCompany(...)`:
     - `createCompanyStaff(...)` uses `InitialCompanyStaffer` to build `CompanyPersonnel` and adds it to `campaign.getPersonnelManager()` (`pwcg.campaign.personnel.InitialCompanyStaffer`).
     - `createCompanyEquipment(...)` uses `InitialCompanyEquipper` and `EquipmentWeightCalculator` to build `Equipment` and adds it to `campaign.getEquipmentManager()` (`pwcg.campaign.resupply.InitialCompanyEquipper`).
4. **Replacements and depots**
   - `createPersonnelReplacements()` and `createEquipmentReplacements()` iterate active services to populate replacement pools (`CampaignPersonnelManager.createPersonnelReplacements`, `CampaignEquipmentManager.createEquipmentDepot`).
5. **Persist campaign**
   - `CampaignInitialWriter.doInitialCampaignWrite(...)` writes the campaign and stores it in `PWCGContext` (`pwcg.campaign.CampaignInitialWriter`).

## Mission generation flow
1. **Mission initialization**
   - `MissionGenerator.makeMission(...)` creates `MissionOptions`, `MissionWeather`, and selects a `Skirmish` via `SkirmishBuilder.chooseBestSkirmish()` when available (`pwcg.mission.MissionGenerator`).
   - `buildMission(...)` constructs the mission objective, borders, and `Mission` instance, then calls `Mission.generate()`.
2. **Objective selection (what to fight over)**
   - `MissionObjectiveBuilder.buildMissionObjective()`:
     - Uses `MissionSidesGenerator` to select defending/assaulting countries.
     - Uses skirmish data if provided, otherwise finds a front position near the player company (`FrontLinesForMap.findClosestFrontCoordinateForSide(...)`) and expands search radius until it finds an objective (`pwcg.mission.MissionObjectiveBuilder`).
     - Candidate objective types include railroad stations, towns, airfields, and bridges via map `GroupManager` finders (e.g., `RailroadStationFinder`, `TownFinder`, `BridgeFinder`).
3. **Mission boundaries**
   - `MissionBorderBuilder.buildCoordinateBox()` builds a mission box centered on the objective using the configured mission box size (`ConfigItemKeys.MissionBoxSizeKey`).
   - `StructureBorderBuilder.buildBorderForMission()` expands the structure box based on CPU/structure settings (`ConfigItemKeys.SimpleConfigStructuresKey`, `KeepGroupSpreadKey`) to decide which fixed positions are included.
4. **Fixed structures and airfields**
   - `MissionBlockBuilder.buildFixedPositionsForMission()` collects rail stations, bridges, airfield blocks, and standalone blocks inside the structure borders from `GroupManager` (`pwcg.mission.MissionBlockBuilder`).
   - `MissionAirfieldBuilder.buildFieldsForPatrol()` gathers airfields inside the structure borders from `AirfieldManager`.
5. **Ground unit placement**
   - `MissionGroundUnitBuilder.generateGroundUnitsForMission()` orchestrates the ground units:
     - **Front battle line**: `MissionBattleBuilder.generateBattle()` uses `FrontFixedUnitSegmentsBuilder.generateAssault(...)` to place opposing units along the front segment near the objective (`pwcg.mission.ground.builder.MissionBattleBuilder`).
     - **Trains**: `MissionTrainBuilder.generateMissionTrains()` picks the closest railroad station to the objective for each side (within radius) and uses `TrainUnitBuilder.createTrainUnit()` to spawn a train (`pwcg.mission.ground.MissionTrainBuilder`).
     - **Truck convoys**: `MissionTruckConvoyBuilder.generateMissionTrucks()` picks the closest bridge near the objective for each side, building convoys via `TruckConvoyBuilder`, and attaches artillery transports via `TruckUnitTransportBuilder` (`pwcg.mission.ground.MissionTruckConvoyBuilder`).
     - **Drifters (barges)**: `MissionDrifterBuilder.generateMissionDrifters()` selects barge positions from `DrifterManager` (loaded from map `BargePositions` locations) and spawns them via `DrifterUnitBuilder` (`pwcg.mission.ground.MissionDrifterBuilder`).
   - `MissionGroundUnitBuilder.finalizeGroundUnits()` removes duplicate unit collections with `GroundUnitPositionDuplicateDetector`.
6. **Platoon (tank) placement**
   - `MissionBattleBuilderFactory.getPlatoonBuilder(...)` selects `MissionPlatoonBuilder` (or `AmphibiousPlatoonBuilder`) based on `AmphibiousAssaultManager`.
   - `Mission.createTankUnits()` calls the platoon builder to create player and AI platoons; `MissionPlatoons` holds and later finalizes them (`MissionUnitFinalizer`).
7. **AI flights**
   - `MissionFlights.generateFlights()` calls `AiFlightBuilder.createAiFlights(...)`.
   - `AIFlightPlanner.createFlightBuildInformationForMission()` decides the number of allied/axis flights and picks flight types and plane types (`PlaneTypeFactory`), using the closest airfield for the side as a home base.
   - `FlightFactory.buildFlight(...)` maps each `FlightTypes` to a package (bombing, dive-bombing, ground attack, patrol, CAP, cargo/para drops) and creates flights.

## Target selection (ground and air)
### Ground targets
- For ground-attack style flights, `GroundTargetDefinitionFactory.buildTargetDefinition(...)` uses `TargetDefinitionBuilder`.
- `TargetDefinitionBuilder`:
  - Collects ground targets via `GroundTargetDefinitionCollector`, which currently builds infantry/ground targets by scanning mission ground units (`TargetDefinitionBuilderInfantry`).
  - Applies preference ordering with `TargetDefinitionPreferenceBuilder`, which can inject:
    - Test driver overrides (`TestDriver.getTestPlayerTacticalTargetType()`).
    - Skirmish preferences (`Skirmish.getTargetTypeForFlightType(...)`).
    - Role-based targets from `FlightInformation`.
- `TargetDefinitionBuilderInfantry` filters ground units and ensures airfield targets are only included when near a relevant airfield (`AirfieldFinder.getAirfieldsWithinRadiusBySide(...)`).

### Air targets
- CAP/Patrol packages use `TargetDefinitionBuilderAirToAir`, which chooses:
  - Patrol flights: `TargetLocatorAir.getFrontCoordinate()`.
  - CAP flights: `TargetLocatorAir.getBattleCoordinate()`.

## Waypoint and instruction generation
### Flight waypoint package
- Each `Flight` initializes a `WaypointPackage` and `VirtualWaypointPackage`.
- `WaypointPackage` holds ordered `IMissionPointSet` objects and links them sequentially in `finalize(...)`.

### Waypoint sets by flight type
- Each flight type builds waypoints in `createWaypoints()` by adding:
  1. **Activate**: `MissionPointSetFactory.createFlightActivate(...)` (mission begin triggers).
  2. **Begin**: `MissionPointSetFactory.createFlightBegin(...)` (takeoff or airstart).
  3. **Mission route**: A flight-specific factory builds target route sets:
     - `CAPWaypointFactory`, `PatrolFrontWaypointFactory`, `BombingWaypointFactory`, `DiveBombingWaypointFactory`, `GroundAttackWaypointFactory`, `ParaDropWaypointFactory`, `CargoDropWaypointFactory`.
  4. **End**: `MissionPointSetFactory.createFlightEnd(...)` creates a terminal waypoint via `TerminalWaypointGenerator`.

### Ingress/egress and target runs
- `IngressWaypointFactory` uses `IngressWaypointAtTarget` to place an ingress waypoint based on target and home airfield orientation.
- `GroundAttackWaypointHelper` builds the target approach/final/egress sequence for attack/bombing flight types, anchoring the run around `TargetDefinition.getPosition()`.
- `EgressWaypointGenerator` sets a point away from the target toward the home base.

### Waypoint patterns (CAP/patrol search)
- CAP patrol patterns are generated with `WaypointPatternFactory` and pattern builders (`CircleWaypointPattern`, `CrossWaypointPattern`, `CreepingLinePattern`) to create search legs around target/intercept areas.

### Virtual waypoints (spawn/activation)
- For virtual flights, `VirtualWaypointPackage.buildVirtualWaypoints()` uses `VirtualWaypointGenerator` and `VirtualWaypointPlotter` to convert mission points into virtual waypoints.
- `VirtualWaypointFlightResolver.resolveForAttackFlight(...)` links virtual spawns to attack triggers and adjusts payloads for spawn-after-target scenarios.

## Data sources and inputs (TCData)
- `PWCGDirectoryProductManager` resolves the TCData root and input directories (`TCData/Input/`) via `PWCGTCDataLocator`.
- Common mission/campaign data sources:
  - Map locations and front lines: `TCData/Input/<MapName>/<YYYYMMDD>/FrontLines.json`, `MapLocations.json` (via `FrontLinesForMap`, `GroupManager`).
  - Blocks/bridges/rail stations/airfields: `TCData/Input/Blocks.json` and map-specific group data (`GroundObjectIOJson`).
  - Drifter/barge positions: `TCData/Input/<MapName>/BargePositions.json` (via `DrifterManager`).
  - Airfield hotspots: `TCData/Input/AirfieldHotspots.json` (via `AirfieldHotSpotsIOJson`).
  - Company, aircraft, tanks, vehicles, static objects: `TCData/Input/Company`, `Aircraft`, `Tanks`, `Vehicles`, `StaticObjects` (directory manager helpers).

## Key classes to reference when extending mission logic
- Mission orchestration: `MissionGenerator`, `Mission`, `MissionObjectiveBuilder`.
- Unit placement: `MissionGroundUnitBuilder`, `MissionBattleBuilder`, `MissionTrainBuilder`, `MissionTruckConvoyBuilder`, `MissionDrifterBuilder`, `MissionPlatoonBuilder`.
- Flight planning: `AIFlightPlanner`, `FlightFactory`, flight type packages (`*Package`), and waypoint factories.
- Target selection: `TargetDefinitionBuilder`, `TargetDefinitionPreferenceBuilder`, `TargetDefinitionBuilderInfantry`, `TargetDefinitionBuilderAirToAir`.
