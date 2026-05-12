# Use Case Specification

## UC-1: Sync Local Backlog to OpenProject
- **Actor**: AS Agent / User
- **Precondition**: Local `sprint_*.json` is updated.
- **Main Flow**:
  1. System reads the local JSON file.
  2. System iterates through work packages.
  3. System checks for `opId`. 
  4. If `opId` is null, System sends `POST` to create.
  5. If `opId` exists, System sends `PATCH` to update using `lockVersion`.
- **Postcondition**: Local `opId` and `lockVersion` are updated after successful sync.

## UC-2: Fetch Design Context from Figma
- **Actor**: AS Agent
- **Precondition**: Figma `fileKey` and `nodeId` are known.
- **Main Flow**:
  1. System calls `get_design_context` MCP tool.
  2. System retrieves CSS, screenshot, and metadata.
  3. System applies styling and layout to Kotlin Compose Web components.

## UC-3: Implement Landing Page Hero Section
- **Actor**: AS Agent
- **Precondition**: Design context for `15:1509` (Header - HERO SECTION) is fetched.
- **Main Flow**:
  1. System creates `HeroSection.kt` in `composeApp`.
  2. System implements the background with overlay and blur as per Figma specs.
  3. System renders the "Trusted Intelligence." heading with gradient text.
  4. System adds the subtext and "Konsultasi Strategis" CTA button.
- **Postcondition**: Hero section matches Figma design and is responsive.
