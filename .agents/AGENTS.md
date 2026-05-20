# Android Studio Agent Instructions - Sekota Web Development

Welcome! This repository is the hub for developing the **Sekota Web** platform. You will act as an autonomous coding assistant, leveraging Figma for design, Jules for automated development sessions, and OpenProject for task management.

## 📁 Directory Structure & Context

- **`.agents/`**:
  - `AGENTS.md`: This file (entry point for agent instructions).
  - `architecture_standards.md`: Rules for Clean and Screaming Architecture.
  - `development_requirements.md`: Technical stack and quality standards.
- **`.figma/`**:
  - `usage_guide.md`: Instructions for fetching design specs via Figma MCP.
  - `.env.example`: Template for Figma API credentials.
  - `designs/`: Local exports of Figma frames (JPG, SVG) for offline reference.
- **`.jules/`**:
  - `api_reference.md`: Documentation for triggering AI coding sessions.
  - `.env.example`: Template for Jules API credentials.
- **`.openproject/`**:
  - `work_packages_api.md`: Reference for syncing tasks and status updates.
  - `.env.example`: Template for OpenProject API credentials.
  - `backlogs/`: Local storage for `product_backlog.md` and `sprint_*.json` files.
- **`.requirements/`**:
  - `landing_page_requirements.md`: Component-level specs for the main landing page.
  - `use_case_specification.md`: Functional flow details.
  - `diagrams/`: PlantUML source files for architecture and data models.

## 🛠️ Your Core Workflow

### 1. Planning with OpenProject
- **Sync Tasks**: Fetch Work Packages from OpenProject to understand the current sprint.
- **Backlog Management**: 
  - Update `product_backlog.md` with new user stories.
  - Manage the current sprint in `sprint_*.json`.
- **Sync Logic**: Use the local backlog files as the source to update OpenProject via the API.
- **Update Status**: Post comments or update `lockVersion` after completing sub-tasks.
- Refer to `.openproject/work_packages_api.md` for payloads.

### 2. Design Reference with Figma
- **Fetch UI/UX**: Use Figma MCP tools (e.g., `get_design_context`) to get screenshots and CSS/Metadata for the target UI components.
- **Local Reference**: Check `.figma/designs/` for static exports.
  - **Screenshots**: `Landing Page.jpg`, `Catalog.jpg`, `Merchandise.jpg`, `Book - Details.jpg`.
  - **Assets**: `Logo_Sekota 1.svg`, `Icon_Sekota.svg`, and partner logos (`Ecoflow`, `Sociara`, `Veridia`, `Ascendio`).
- **Main Design File**: `Sekota-Landing-Page`
  - **File Key**: `oZqXuSsN7u7S73vfjWXQIN`
  - **Landing Page Frame**: `15:1465`
- **Maintain Consistency**: Refer to `.figma/usage_guide.md` to ensure design tokens (colors, fonts) match the Figma file.

### 3. Development with Jules
- **Start Sessions**: Trigger Jules sessions for implementation using the `create_session` endpoint.
- **Branch Selection**:
  - Explicitly select the target **Branch** (e.g., `main`, `develop`, or a feature branch) before starting a session.
  - Ensure the `source` path in the Jules payload reflects the correct branch context if supported, or specify it in the prompt.
- **Finishing Strategy**:
  - **Sequential**: Complete one Jules session, verify the work, and update the OpenProject status before starting the next job. Use this for interdependent tasks.
  - **Parallel**: Start multiple independent Jules sessions simultaneously to increase development velocity. Ensure each session uses a unique feature branch to avoid merge conflicts.
- **Collaborate**: Provide context and prompts based on Figma designs and OpenProject requirements.
- Refer to `.jules/api_reference.md` for session management.

### 4. Implementation (KMP & Web)
- Write clean, maintainable Kotlin/Kotlin Multiplatform code.
- **Architecture**: Strictly follow the principles in `.agents/architecture_standards.md`.
  - Use **Screaming Architecture**: Package by feature (e.g., `com.sekota.features.catalog`).
  - Use **Clean Architecture**: Maintain clear boundaries between Domain, Data, and Presentation.
- **UI Development & Previews**: 
  - Mandate the use of `@Preview(device = DESKTOP)` for all top-level Composables (screens, sections, and complex components).
  - Ensure all UI files include a preview for rapid iteration.
- Ensure all network requests are handled gracefully with proper logging.

## ⚠️ Important Notes
- **Environment**: Never commit real `.env` files. Ensure they are present in `.gitignore`.
- **Optimistic Locking**: Always fetch the latest `lockVersion` from OpenProject before sending a `PATCH` request.
- **Jules v1alpha**: Be aware that Jules API is in alpha and subject to change.
