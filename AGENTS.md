# Android Studio Agent Instructions - Sekota Web Development

Welcome! This repository is the hub for developing the **Sekota Web** platform. You will act as an autonomous coding assistant, leveraging Figma for design, Jules for automated development sessions, and OpenProject for task management.

## 📁 Directory Structure & Context

- **`.figma/`**:
  - `usage_guide.md`: Instructions for fetching design specs via Figma MCP.
  - `.env.example`: Template for Figma API credentials.
- **`.jules/`**:
  - `api_reference.md`: Documentation for triggering AI coding sessions.
  - `.env.example`: Template for Jules API credentials.
- **`.openproject/`**:
  - `work_packages_api.md`: Reference for syncing tasks and status updates.
  - `.env.example`: Template for OpenProject API credentials.
  - `backlogs/`: Local storage for `product_backlog.md` and `sprint_*.json` files.

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
- **Maintain Consistency**: Refer to `.figma/usage_guide.md` to ensure design tokens (colors, fonts) match the Figma file.

### 3. Development with Jules
- **Start Sessions**: For complex refactoring or new feature implementations, trigger Jules sessions using `create_session`.
- **Collaborate**: Provide context and prompts to Jules based on the Figma designs and OpenProject requirements.
- Refer to `.jules/api_reference.md` for session management.

### 4. Implementation (KMP & Web)
- Write clean, maintainable Kotlin/Kotlin Multiplatform code.
- Ensure all network requests are handled gracefully with proper logging.

## ⚠️ Important Notes
- **Environment**: Never commit real `.env` files. Ensure they are present in `.gitignore`.
- **Optimistic Locking**: Always fetch the latest `lockVersion` from OpenProject before sending a `PATCH` request.
- **Jules v1alpha**: Be aware that Jules API is in alpha and subject to change.
