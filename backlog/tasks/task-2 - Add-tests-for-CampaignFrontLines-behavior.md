---
id: TASK-2
title: Add tests for CampaignFrontLines behavior
status: Done
assignee: []
created_date: '2026-02-01 10:17'
updated_date: '2026-02-01 10:18'
labels:
  - tests
  - frontline
  - campaign
dependencies: []
references:
  - src/main/java/pwcg/campaign/context/CampaignFrontLines.java
  - src/main/java/pwcg/campaign/context/CampaignFrontLinesData.java
priority: medium
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Create unit tests for CampaignFrontLines advance behavior and data round-trip (toData/fromData), ensuring baseline copy and directional shift logic.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Tests verify CampaignFrontLines.fromBaseline copies independent front line positions.
- [x] #2 Tests verify advanceFrontLines moves advancing side toward enemy and retreating side away.
- [x] #3 Tests verify toData/fromData preserves map identifier and line counts.
<!-- AC:END -->
