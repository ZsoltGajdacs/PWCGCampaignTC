---
id: TASK-4
title: Replace campaign screen radio selectors with buttons
status: In Progress
assignee: []
created_date: '2026-02-01 14:36'
updated_date: '2026-02-01 14:37'
labels: []
dependencies: []
references:
  - src/main/java/pwcg/gui/campaign/home/CampaignHomeScreen.java
  - src/main/java/pwcg/gui/campaign/home/ChalkboardSelector.java
priority: medium
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Update the main campaign home screen selector to use buttons instead of radio selects while preserving existing behavior, and add a persistent active visual state consistent with the chalkboard styling.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 Selector controls on the main campaign screen are buttons, not radio selects.
- [ ] #2 Button clicks trigger the same center/right panel swaps as before with no behavior changes.
- [ ] #3 The currently selected button shows a persistent active style consistent with existing chalkboard colors/fonts.
- [ ] #4 No platform-specific paths or OS-specific logic introduced.
<!-- AC:END -->
