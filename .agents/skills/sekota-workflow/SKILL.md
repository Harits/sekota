---
name: sekota-workflow
description: Procedures for OpenProject task synchronization, Jules AI coding sessions, Wasm container builds, and SMT PM Framework repo-native governance in the Sekota project.
---

# Sekota Project Workflow & Toolchain Runbook

## 1. OpenProject Optimistic Lock Sync
When updating work packages in OpenProject:
1. Always fetch the latest lockVersion:
   ```bash
   GET /api/v3/work_packages/{id}
   ```
2. Send PATCH payload including the retrieved lockVersion:
   ```json
   {
     "lockVersion": <latest_version>,
     "_links": { "status": { "href": "/api/v3/statuses/<id>" } },
     "comment": { "raw": "Completed implementation sub-task." }
   }
   ```
3. Update `.openproject/backlogs/sprint_*.json` with the new lockVersion.

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

## 3. SMT PM Framework & Repo-Native Lifecycle Governance
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
