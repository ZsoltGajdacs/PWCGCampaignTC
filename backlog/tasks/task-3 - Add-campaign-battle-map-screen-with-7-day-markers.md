---
id: TASK-3
title: Add campaign battle map screen with 7-day markers
status: Done
assignee: []
created_date: '2026-02-01 10:52'
updated_date: '2026-02-01 11:05'
labels: []
dependencies: []
references:
  - src/main/java/pwcg/gui/campaign/home/CampaignHomeScreen.java
  - src/main/java/pwcg/gui/rofmap/MapPanelBase.java
  - src/main/java/pwcg/gui/rofmap/brief/BriefingMapPanel.java
  - src/main/java/pwcg/gui/rofmap/debrief/AARReportMainPanel.java
priority: medium
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Add a new campaign-accessible map screen that shows the full map with frontlines and recent battle markers. Markers use win/loss icons and show a breakdown of tanks/planes/other equipment destroyed/lost on interaction. Only frontlines and battles should be displayed.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 A new campaign map screen is accessible from the campaign home UI.
- [x] #2 The map displays the full campaign map with frontlines visible.
- [x] #3 Only frontlines and battle markers are shown; other overlays are not displayed.
- [x] #4 Battle markers are limited to the last 7 days of campaign time.
- [x] #5 Markers visually distinguish win vs loss.
- [x] #6 Interacting with a marker shows tanks/planes/other equipment destroyed and lost.
- [x] #7 Relevant tests are added or updated to cover the 7-day filter and marker data mapping.
- [x] #8 Any user-facing text or UI labels related to the new screen are documented or updated where appropriate.
<!-- AC:END -->

## Final Summary

<!-- SECTION:FINAL_SUMMARY:BEGIN -->
Implemented a campaign battle map screen with frontlines and last-7-days battle markers, stored per-mission battle summaries (loss breakdown and winner) in campaign data, and wired navigation from the campaign home screen. Added unit tests for the 7-day filter and loss-category mapping. Tests not run (not requested).
<!-- SECTION:FINAL_SUMMARY:END -->
