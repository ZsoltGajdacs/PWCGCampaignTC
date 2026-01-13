# Copilot instructions (PWCGCampaignTC)

## Big picture
- Java desktop (Swing) app built with Gradle.
- Content is data-driven from `TCData/` (especially `TCData/Input/`, `TCData/Names/`, `TCData/Images/`).
- Many systems assume the working directory is the repo root, but this can be overridden via `--root <dir>` (or `-Dpwcg.root=<dir>` / `PWCG_ROOT`).

## Key entrypoints
- GUI app entrypoint: `pwcg.gui.maingui.PwcgMain` (Gradle `mainClassName`).
- Deploy entrypoint: `pwcg.dev.deploy.DeployPwcgTC` (invoked by the Gradle `deployTC` task).

## Architecture & data flow (how things are wired)
- Global singleton context: `PWCGContext.getInstance()` builds a `TCContext` once and initializes maps/managers.
  - Tests frequently call `PWCGContext.getInstance().changeContext(...)` to select a map before constructing domain objects.
- File locations come from the directory manager (see `pwcg.campaign.context.PWCGDirectoryProductManager`).
  - Prefer `PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir()` / `getPwcgNamesDir()` rather than hardcoding paths.
  - Examples of the pattern:
    - `BlockDefinitionManager` reads `Blocks.json` from the input dir.
    - `AirfieldHotSpotsIOJson` reads `AirfieldHotspots.json` from the input dir.
    - `CrewMemberNames` loads `TCData/Names/*.txt` at startup.

## Build / run workflow (important platform caveats)
- Run the app: `./gradlew run`
- Build the fat jar: `./gradlew jar` (the `jar { from { runtimeClasspath zipTree(...) } }` block produces an “uber jar”).
- Windows packaging pipeline is explicit: use `./gradlew releaseWindows` (Windows-only; Launch4j/7-Zip).
- Java version note: target is Java 8; set `JAVA_HOME` to a JDK 8 install (or pass `./gradlew -Dorg.gradle.java.home=/path/to/jdk8 ...`).

## Tests
- Test stack: JUnit 5 + Mockito (see `build.gradle`).
- Unit tests live under `src/test/java/pwcg/**`.
- Integration tests live under `src/test/java/integration/**` and may delete user config files via `PWCGDirectoryUserManager` (tests call cleanup helpers).
- By default, `./gradlew test` excludes integration tests.
- Integration profile: `./gradlew test -Dtest.profile=integration`.

## Project-specific conventions
- Paths are commonly assembled with Windows separators (`"\\"`) in code; keep this consistent unless you’re doing a deliberate cross-platform refactor.
- Deployment is blocked when `TestDriver` is enabled; don’t ship changes that accidentally enable it.
