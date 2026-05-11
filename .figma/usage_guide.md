# Figma Usage Guide for Sekota Web

This guide explains how to use the Figma MCP tools to fetch design specifications for the Sekota Web development.

## 🛠️ Core Tools

### 1. `get_screenshot`
Use this tool to see the visual design of a specific node or the entire file.
- **Input**: `fileKey`, `nodeId`.
- **Output**: A URL to a PNG screenshot.

### 2. `get_design_context`
This is the primary tool for design-to-code workflows. It returns reference code, a screenshot, and metadata.
- **Input**: `fileKey`, `nodeId`.
- **Adaptation**: Adapt the returned CSS/JSON to the project's frontend framework.

### 3. `get_metadata`
Use this to get an overview of the document structure (layer names, positions, sizes).
- **Input**: `fileKey`, `nodeId`.

### 4. `get_variable_defs`
Fetch design tokens like colors, spacing, and typography variables.
- **Input**: `fileKey`, `nodeId`.

## 🔄 Workflow

1. **Identify the Node**: Use the Figma web UI or `get_metadata` to find the ID of the component you want to implement.
2. **Fetch Context**: Call `get_design_context` for that Node ID.
3. **Analyze & Implement**: Use the screenshot for visual reference and the code snippets for layout/style guidance.
4. **Iterate**: If the UI needs adjustment, re-fetch the screenshot to verify the latest design.
