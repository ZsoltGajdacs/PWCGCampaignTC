---
id: TASK-5
title: Add battle mission type selection and fortification scaling
status: In Progress
assignee: []
created_date: '2026-02-01 15:13'
updated_date: '2026-02-01 15:13'
labels: []
dependencies: []
references:
  - src/main/java/pwcg/gui/campaign/mission/CampaignMissionScreen.java
  - src/main/java/pwcg/gui/rofmap/brief/BriefingRoleChooser.java
  - src/main/java/pwcg/gui/campaign/mission/MissionGeneratorHelper.java
  - src/main/java/pwcg/gui/campaign/home/GuiMissionInitiator.java
  - src/main/java/pwcg/mission/MissionGenerator.java
  - src/main/java/pwcg/mission/options/MissionOptions.java
  - src/main/java/pwcg/mission/ground/builder/MissionBattleBuilderFactory.java
  - src/main/java/pwcg/mission/ground/builder/FrontFixedUnitSegmentsBuilder.java
  - src/main/java/pwcg/mission/target/FrontBattleSizeGenerator.java
  - src/main/java/pwcg/campaign/context/PWCGDirectoryProductManager.java
priority: high
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Introduce explicit battle mission type selection for Mission With Role, random selection for Mission, and support fortification battle types that scale unit composition by objective density. Reuse existing StaticObjects when available; add new only when needed. Mission generation should carry the chosen battle mission type into ground battle builders.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 Mission With Role UI includes a battle mission type selector with choices Attack, Defense, Attack Fortification, Defend Fortification.
- [ ] #2 Mission generation honors the selected battle mission type from Mission With Role and randomly chooses a battle mission type when using Mission.
- [ ] #3 The selected battle mission type is stored in mission options and available to ground battle generation.
- [ ] #4 Fortification battles place MG, AA, AT, and artillery units using objective-density-scaled size tiers (small/medium/large) with counts: small 4 MG, 0 AA, 2 AT, 1 ARTY; medium 6 MG, 1 AA, 5 AT, 2 ARTY; large 8 MG, 4 AA, 10 AT, 4 ARTY.
- [ ] #5 Existing StaticObjects are reused for fortification units when available; new StaticObjects are added only when no suitable existing object exists.
- [ ] #6 Updated mission briefing/ground battle generation continues to function for non-fortification missions.
<!-- AC:END -->

## Implementation Plan

<!-- SECTION:PLAN:BEGIN -->
1) Add BattleMissionType enum and store it in MissionOptions; default to random when unset.
2) Add Mission With Role UI selector and pass chosen battle mission type through MissionGeneratorHelper -> GuiMissionInitiator -> MissionGenerator.
3) Plumb battle mission type into Mission and expose getter.
4) Prepare ground battle builder selection hooks; keep existing assault builder as default.
5) Add objective-density-to-fortification-size mapping and fortification unit count constants (follow-up integration into builders).
<!-- SECTION:PLAN:END -->
