# UI Modernization Plan (Swing)

## Current Swing UI snapshot
- Entry point: `pwcg.gui.maingui.PwcgMain` creates `PwcgMainScreen` and initializes `PwcgThreePanelUI` for the left/center/right layout.
- The three-panel layout swaps screens via `PwcgThreePanelUI.setLeft/Center/RightPanel` and the campaign context stack.
- Data-entry flows for new campaigns and crew members use `CampaignGeneratorDataEntryGUI` and `NewCrewMemberDataEntryGUI`, which expose **Next/Previous Step** buttons tied to workflow state (`CampaignGeneratorState`, `NewCrewMemberState`).
- Paginated log screens rely on `pwcg.gui.utils.PageTurner` for **Next/Previous Page** navigation.

## Goals
- Faster, more responsive UI (avoid EDT blocking, reduce heavy repaint/layout churn).
- Streamline data-entry by removing **Next/Previous Step** buttons in campaign/crew member creation.
- Preserve cross-platform behavior and current visual styling where possible.

## Option A: Incremental Swing improvements (lowest risk)
- **Responsiveness**
  - Move long-running work (campaign load, map image decoding, report generation) off the EDT using `SwingWorker` and progress indicators.
  - Add lightweight placeholders while images/large text panels load, then swap in results.
  - Review high-frequency `refresh()` calls in `PwcgThreePanelUI` to minimize re-layout and repaint.
- **Replace Next/Previous Step buttons**
  - Replace the `createNextStepWidget` blocks in `CampaignGeneratorDataEntryGUI` and `NewCrewMemberDataEntryGUI` with a single, always-visible form that validates inline.
  - Provide a left-side step list or tabbed section for optional fields instead of sequential buttons; allow direct selection of sections.
  - Add a single **Create/Finish** action, enabled only when required fields are valid.
- **UI polish**
  - Consolidate button creation through `PWCGButtonFactory` to standardize focus/hover states.
  - Prefer `CardLayout` only for optional panels, not for primary navigation.

## Option B: JavaFX migration (moderate risk, higher UX upside)
- Introduce JavaFX alongside Swing using `JFXPanel` for new screens, then migrate high-traffic flows first (campaign/crew creation).
- Build forms in FXML with a view-model layer; use JavaFX `Task`/`Service` for background work.
- Replace sequential buttons with a JavaFX stepper or accordion control that lets users jump to sections and validates in real time.
- Maintain the Swing shell (`PwcgMainScreen`) while progressively moving panels to JavaFX until a full shell swap is viable.

## Option C: Web UI (largest change, most flexibility)
- Expose the campaign APIs via an internal HTTP layer and host a local SPA (React/Vue/Svelte) inside an embedded web view (JCEF/JavaFX WebView) or external browser.
- Use SPA patterns: single-page forms, inline validation, progressive disclosure for optional sections, and list virtualization.
- Replace Next/Previous data-entry buttons with section navigation and a single **Submit** flow.
- Provides the best path for responsive rendering and future UI theming, but requires an API boundary and deployment changes.

## Recommendation
Start with **Option A** to remove the Next/Previous data-entry buttons and improve EDT responsiveness quickly, then evaluate **Option B** for selective migration of the data-entry flows. **Option C** is best when the team is ready to invest in a new frontend stack and API surface.
