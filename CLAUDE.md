# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PWCG (Persistent World Campaign Generator) is a Java desktop application that generates dynamic, persistent campaigns for IL-2 Sturmovik Tank Crew and Great Battles series flight simulators. The project creates a living world where AI pilots move, fight, and progress even outside player missions through the "virtual waypoints" system.

## Build & Run Commands

```bash
# Run the application
./gradlew run

# Build fat JAR (PWCGTC.jar)
./gradlew jar

# Run unit tests only
./gradlew test

# Run integration tests (may delete config files)
./gradlew test -Dtest.profile=integration

# Run a single test class
./gradlew test --tests "pwcg.campaign.CampaignGeneratorTest"

# Deploy to IL-2 installation (auto-detected)
./gradlew deployIL2

# Windows release pipeline (Windows only)
./gradlew releaseWindows
```

**Java 21 required.** Set `JAVA_HOME` or use `./gradlew -Dorg.gradle.java.home=/path/to/jdk21`.

## Architecture

### Global Context Pattern
```
PWCGContext.getInstance()  → TCContext (lazy singleton)
  ├── CompanyManager      (company data)
  ├── AceManager          (historical aces)
  ├── AirfieldManager     (airfield data)
  ├── DirectoryManager    (file paths)
  └── GroupManager        (per-map blocks, bridges, etc.)
```

All managers and data loading go through `PWCGContext`. Tests frequently call `PWCGContext.getInstance().changeContext(...)` to select a map before constructing domain objects.

### Key Package Domains

| Package | Purpose |
|---------|---------|
| `pwcg.gui` | Swing UI with three-panel layout (`PwcgMainScreen`, `PwcgThreePanelUI`) |
| `pwcg.campaign` | Campaign management, personnel, equipment, context |
| `pwcg.mission` | Mission generation, ground units, AI flights, waypoints |
| `pwcg.aar` | After-Action Reports (phases 1-5), combat results processing |
| `pwcg.core` | Configuration, utilities, path handling, logging |
| `pwcg.product.bos` | Game-specific adapter (Battle of Stalingrad) |

### Entry Points
- **GUI:** `pwcg.gui.maingui.PwcgMain` (Gradle `mainClassName`)
- **Deploy:** `pwcg.dev.deploy.DeployPwcgTC`

### Data-Driven Design
All campaign and mission variation is driven by JSON configuration in `TCData/`:
- `TCData/Input/` - Aircraft, tanks, companies, configuration
- `TCData/Input/<MapName>/` - Map-specific data (front lines, locations)
- `TCData/Names/` - Pilot name pools
- Access via: `PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir()`

## Cross-Platform Requirements (Mandatory)

- **No hardcoded paths:** Never use `\\` or `/` separators, drive letters like `D:/`, or OS-specific commands
- **Use java.nio:** Prefer `Path`, `Paths.get()`, `Files.*` for filesystem operations
- **Path normalization:** If you must use String paths, normalize at boundaries with `pwcg.core.utils.PWCGPath.normalize(...)`
- **OS-specific code:** Windows-only tasks must be guarded by OS checks (`onlyIf { isWindows }`)

## Campaign & Mission Generation Flow

**Campaign generation** (`CampaignGenerator.generate()`):
1. Validate inputs via `CampaignGeneratorModel`
2. Select map based on player company base
3. Staff companies via `InitialCompanyStaffer`
4. Equip companies via `InitialCompanyEquipper`
5. Create replacement pools for personnel and equipment

**Mission generation** (`MissionGenerator.makeMission()`):
1. Select objective via `MissionObjectiveBuilder` (railroad, town, airfield, bridge)
2. Build mission boundaries via `MissionBorderBuilder`
3. Place ground units: battles, trains, convoys, barges (`MissionGroundUnitBuilder`)
4. Place tank platoons via `MissionPlatoonBuilder`
5. Generate AI flights via `AIFlightPlanner` and `FlightFactory`
6. Build waypoints per flight type (`*WaypointFactory` classes)

See `Documentation/mission-campaign-generation.md` for detailed class references.

## Test Structure

- **Unit tests:** `src/test/java/pwcg/**` - Run with `./gradlew test`
- **Integration tests:** `src/test/java/integration/**` - Run with `-Dtest.profile=integration`
- **Stack:** JUnit 5 + Mockito 5.2.0
- **Warning:** Integration tests may delete config files via `PWCGDirectoryUserManager`

## Important Conventions

- **TestDriver:** Deployment is blocked when `TestDriver` is enabled. Don't ship changes that accidentally enable it.
- **Working directory:** Many systems assume repo root is the working directory. Override via `--root <dir>`, `-Dpwcg.root=<dir>`, or `PWCG_ROOT` environment variable.
- **Game data formats:** If external game formats require Windows-style paths in file *contents*, isolate that formatting to the smallest boundary while keeping local disk IO platform-independent.
