---
id: TASK-1
title: Add unit tests for frontline advance logic
status: Done
assignee: []
created_date: '2026-02-01 09:57'
updated_date: '2026-02-01 10:00'
labels:
  - tests
  - frontline
  - aar
dependencies: []
references:
  - src/main/java/pwcg/aar/campaign/update/FrontlineAdvanceCalculator.java
  - src/main/java/pwcg/aar/campaign/update/FrontlineAdvancePolicy.java
  - src/main/java/pwcg/campaign/context/CampaignFrontLines.java
priority: medium
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Add JUnit tests for new dynamic frontline advance calculations, including weighted kill/loss ratios, breakthrough/advance thresholds, and wipe ratio gating. Ensure tests cover tanks/planes/other weighting and tie-break behavior.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Tests verify weighted kill/loss ratio logic for tanks, planes, and other categories.
- [x] #2 Tests verify breakthrough requires ≥70% wipe and ≥3:1 ratio; advance triggers at ≥2:1 even if wipe <70%.
- [x] #3 Tests verify no-advance outcome when ratios below threshold.
- [x] #4 Tests validate advance distance selection (20km vs 10km).
<!-- AC:END -->
