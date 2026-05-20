# Sekota Web - Scrum Guide

This document outlines the Agile Scrum framework adapted for the Sekota Web project, focusing on the collaboration between human developers and AI agents.

## 👥 Scrum Roles

- **Product Owner (PO)**: Usually the User. Defines the vision, manages the Product Backlog, and accepts completed work.
- **Scrum Master (SM)**: Facilitated by the Agent. Ensures the team follows Scrum principles, clears blockers, and manages the sprint artifacts.
- **Developers**: The Agent and any human contributors. Responsible for delivering "Done" increments of the product.

---

## 📄 Scrum Artifacts

### 1. Product Backlog
- **Location**: `.openproject/backlogs/product_backlog.md`
- **Purpose**: The single source of truth for all required work.
- **Format**: User Stories with OpenProject IDs and status.

### 2. Sprint Backlog
- **Location**: `.openproject/backlogs/sprint_*.json`
- **Purpose**: The subset of Product Backlog items selected for the current Sprint.
- **Format**: JSON tasks with estimates (Story Points) and assignments.

### 3. Increment
- **Definition**: The sum of all completed backlog items during a sprint, which must meet the **Definition of Done (DoD)**.

---

## 🔄 Scrum Ceremonies

### 1. Sprint Planning
- **Goal**: Select high-priority items from the Product Backlog.
- **Agent Action**:
    1. Read `product_backlog.md`.
    2. Suggest items for the next sprint based on capacity and priority.
    3. Create a new `sprint_X.json`.

### 2. Daily Standup (Status Update)
- **Goal**: Sync on progress and identify blockers.
- **Agent Action**:
    - Provide a concise status update via `notify_user` at the start of every major task phase.
    - Reference specific Work Package IDs.

### 3. Sprint Review & Retrospective
- **Goal**: Demo the increment and improve the process.
- **Agent Action**:
    - Update `product_backlog.md` status to `[x]`.
    - Provide a summary of velocity and any technical debt identified.

---

## ✅ Definition of Done (DoD)
A User Story is "Done" only when:
- [ ] Code follows [Architecture Standards](architecture.md).
- [ ] UI has a [Desktop Preview](architecture.md#3-ui-previews).
- [ ] All Work Package sub-tasks are marked as "Done" in OpenProject.
- [ ] The walkthrough for the task is updated.
- [ ] The Product Owner (User) approves the implementation.

---

## 📏 Estimation
Use Fibonacci Story Points (1, 2, 3, 5, 8) to estimate complexity:
- **1**: Trivial change (text update, color change).
- **2**: Simple component or logic.
- **3**: Medium feature or complex component.
- **5**: Large feature involving multiple layers (Domain, Data, UI).
- **8**: Major infrastructure or cross-cutting concern.
