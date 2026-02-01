---
id: TASK-5.1
title: Wire BattleMissionType into ground battle and fortifications
status: Done
assignee:
  - GitHub Copilot
created_date: '2026-02-01 15:21'
updated_date: '2026-02-01 15:31'
labels: []
dependencies: []
references:
  - src/main/java/pwcg/mission/ground/builder/MissionBattleBuilderFactory.java
  - src/main/java/pwcg/mission/ground/builder/MissionBattleBuilder.java
  - src/main/java/pwcg/mission/ground/builder/FrontFixedUnitSegmentsBuilder.java
  - src/main/java/pwcg/mission/ground/MissionGroundUnitBuilder.java
  - src/main/java/pwcg/mission/Mission.java
  - src/main/java/pwcg/mission/MissionObjectiveBuilder.java
  - src/main/java/pwcg/mission/target/FrontSegmentDefinitionGenerator.java
  - src/main/java/pwcg/campaign/context/PWCGDirectoryProductManager.java
  - src/main/java/pwcg/campaign/io/json/StaticObjectIOJson.java
parent_task_id: TASK-5
priority: high
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Implement ground battle builder selection based on BattleMissionType (assault vs fortification) and add objective-density-based fortification sizing with unit composition using StaticObjects where possible.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Ground battle generation selects appropriate builder based on `BattleMissionType` (assault vs fortification variants).
- [x] #2 Objective density is computed and mapped to small/medium/large fortification sizes.
- [x] #3 Fortification unit counts follow the specified tier values for MG/AA/AT/ARTY.
- [x] #4 StaticObjects are reused for fortification units when available; fallback objects are defined only if needed.
- [x] #5 Non-fortification mission generation remains unchanged.
<!-- AC:END -->

## Implementation Plan

<!-- SECTION:PLAN:BEGIN -->
1) Add BattleMissionType routing in MissionBattleBuilderFactory so fortification types choose a new fortification builder; keep assault builder for Attack/Defense and amphibious override intact.
2) Define objective-density metric using nearby objective candidates (towns/bridges/airfields/railroad) around the mission objective; map low/med/high to small/medium/large tiers.
3) Implement fortification unit composition helper: tiered counts for MG/AA/AT/ARTY, reuse existing StaticObjects when present and add fallbacks only if missing.
4) Build fortification ground unit collections using the new helper and wire to mission ground unit builder; ensure non-fortification path unchanged.
<!-- SECTION:PLAN:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Implemented fortification battle builder selection via MissionBattleBuilderFactory using BattleMissionType; fortification path uses FortificationBattleBuilder with density-based size tiers.

Objective density computed via nearby objective counts within 30km filtered by front proximity; sizes map to small/medium/large tiers with specified unit counts.

Fortification units created per segment with total counts distributed across segments; defending side receives MG/AT/AA/ARTY units while assault side retains standard assault units.

Static emplacements reuse existing StaticObjects (mg_position and art_position_*); AT/AA reuse artillery positions as generic gun pits. VehicleDefinitionManager now loads StaticObjects and handles null names.
<!-- SECTION:NOTES:END -->

## Final Summary

<!-- SECTION:FINAL_SUMMARY:BEGIN -->
Implemented fortification battle path selection, density-based fortification sizing, and tiered unit composition with static emplacements. Static objects are loaded into the vehicle definition manager and reused for fortification visuals; non-fortification missions continue using the assault builder.
<!-- SECTION:FINAL_SUMMARY:END -->
