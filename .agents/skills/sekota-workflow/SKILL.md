---
name: sekota-workflow
description: Procedures for OpenProject task synchronization, Jules AI coding sessions, Wasm container builds, and SMT PM Framework repo-native governance in the Sekota project.
---

# Sekota Project Workflow & Toolchain Runbook

## 1. OpenProject Optimistic Lock, Status Transition & Hierarchy Sync
When updating or creating work packages in OpenProject:
1. **Check Allowed Status Transitions**:
   - Query the form schema to verify permitted statuses for the user's role:
     ```bash
     POST /api/v3/work_packages/{id}/form
     ```
   - Note: For `User story` types under standard Member/Observer roles, direct transition to `Closed` (12) may be prohibited. Move to `In testing` (ID: 9) with 100% progress when children are closed.
2. **Always fetch the latest lockVersion**:
   ```bash
   GET /api/v3/work_packages/{id}
   ```
3. **Send PATCH payload including the retrieved lockVersion**:
   ```json
   {
     "lockVersion": <latest_version>,
     "_links": { "status": { "href": "/api/v3/statuses/<id>" } },
     "comment": { "raw": "Completed implementation sub-task." }
   }
   ```
4. **Provision Sprint Version If Missing**:
   - Check available versions with `GET /api/v3/projects/{id}/versions`.
   - If the sprint version does not exist, create it:
     ```json
     POST /api/v3/versions
     {
       "name": "Sprint N: <Name>",
       "startDate": "<YYYY-MM-DD>",
       "endDate": "<YYYY-MM-DD>",
       "status": "open",
       "_links": { "definingProject": { "href": "/api/v3/projects/<id>" } }
     }
     ```
5. **Parent-Child Linking**:
   - Link child tasks to the parent User Story using `_links.parent = { "href": "/api/v3/work_packages/<parent_id>" }`.
6. **Update Local Backlog**:
   - Update `.openproject/backlogs/sprint_*.json` with official OpenProject IDs and commit changes.

## 2. Web & Container Deployment Routine
When building or deploying container images:
1. Generate the Wasm production executable:
   ```bash
   ./gradlew :composeApp:wasmJsBrowserDistribution --no-daemon
   ```
2. Sync compiled output to the staging directory:
   ```bash
   cp -R composeApp/build/dist/wasmJs/productionExecutable/* web-dist/
   ```
3. Launch container service via compose or podman:
   ```bash
   docker compose up --build -d
   # or
   ./run-local-podman.sh
   ```

## 3. Multiplatform CMS Runbook (Desktop, Android & CLI)
To launch and test the Sekota CMS prototype across supported non-web targets:
1. **Desktop macOS (JVM CMP)**:
   ```bash
   ./gradlew :composeApp:run
   ```
2. **Android Emulator / Device (AdminActivity)**:
   ```bash
   ./gradlew :androidApp:installDebug
   adb shell am start -n com.sekota/com.sekota.AdminActivity
   ```
3. **Kotlin CLI Engine (`:cli`)**:
   ```bash
   ./gradlew :cli:run --args="books list"
   ./gradlew :cli:run --args="products list"
   ./gradlew :cli:run --args="merch list"
   ```

## 4. SMT PM Framework & Repo-Native Lifecycle Governance
When delivering features across SMT Gate cycles (G0 through G5) or maintaining project health:

### Repo-Native Directory Conventions:
- `docs/01_project_governance/`:
  - `PROJECT_CHARTER.md` (`PM-FRM-03`): Scope, boundaries, Tiering (Tier M), tolerance thresholds.
  - `DEFINITION_OF_DONE.md` (`PM-FRM-04`): Clean Architecture, ISO 27001 controls, `@Preview(device = DESKTOP)`.
  - `PROJECT_CONTROL_BOOK.md` (`PM-REG-01`): Single source of truth for Gate log, Risk & Issue register, and Assumptions.
- `docs/02_technical_specifications/`:
  - `FUNCTIONAL_SPECIFICATION_DOCUMENT.md` (`PM-FRM-06`): Architecture, actors, use case specs (UC-XX).
  - `diagrams/`: PlantUML source files (`.puml`) for use cases and components.
- `docs/03_project_management/sprints/`:
  - `MILESTONE_REVIEW_SPRINT_*.md` (`PM-FRM-05`): Sprint audit, DoD compliance, and gate transition notes.

### Gate Evaluation Protocol:
- **G0 (Pitching)**: Opportunity Brief & Solution Feasibility.
- **G1 (Charter & Handover)**: Project Charter & DoD formalization.
- **G2 (Baseline)**: FSD & PlantUML specs locked in Git.
- **G3 (Pre-Acceptance)**: Sprint DoD verification, `@Preview` checks, automated tests (`./gradlew check`).
- **G4 (BAST / Release)**: User acceptance, credential offboarding, knowledge harvesting (`PM-FRM-21`).
- **G5 (Benefit Realization)**: Post-implementation review of client and business value.
