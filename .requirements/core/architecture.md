# Sekota Web - Architecture Standards

This document defines the architectural patterns and directory structures required for the Sekota Web platform. Adherence to these standards ensures the codebase is maintainable, testable, and intent-revealing.

## 🏛️ Core Principles

### 1. Clean Architecture
The codebase is divided into layers with a strict **Inward Dependency Rule**:
- **Domain Layer**: The heart of the application. Contains Entities, Use Case (Interactors), and Repository Interfaces. It must have **zero** dependencies on external libraries (except Coroutines/Flow) or other layers.
- **Data Layer**: Implementation of the Domain's Repository Interfaces. Handles networking (Ktor), persistence, and DTO-to-Entity mapping.
- **Presentation Layer**: UI (Compose) and State Management (ViewModels/StateHolders).

### 2. Screaming Architecture (Package by Feature)
The project structure must "scream" its functional purpose. Avoid top-level technical packages like `fragments`, `models`, or `services`. Instead, group code by **Feature**.

---

## 📂 Directory Structure

### In the `shared` Module (Logic & Data)
Logic that can be shared across platforms (Android, Web, iOS).

```text
shared/src/commonMain/kotlin/com/sekota/features/
└── [feature_name]/
    ├── domain/
    │   ├── model/         # Domain Entities (data classes)
    │   ├── repository/    # Repository Interfaces
    │   └── usecase/       # Single-purpose business logic classes
    └── data/
        ├── remote/        # Ktor API services & DTOs
        ├── repository/    # Repository Implementations
        └── mapper/        # Extension functions to map DTO <-> Entity
```

### In the `composeApp` Module (UI)
Platform-specific or shared UI implementation.

```text
composeApp/src/commonMain/kotlin/com/sekota/features/
└── [feature_name]/
    ├── presentation/
    │   ├── [Feature]ViewModel.kt  # State management
    │   ├── [Feature]Contract.kt   # UI State, Actions, and Effects
    │   └── [Feature]Screen.kt     # Main Compose entry point
    └── components/                # Feature-specific UI components
```

### 3. UI Previews
To ensure consistent design and rapid iteration:
- **Mandatory Previews**: Every file containing a `@Composable` intended for display (Screen, Section, or Component) must have a corresponding `@Preview`.
- **Desktop Focus**: Use `@Preview(device = DESKTOP)` for all top-level elements to simulate the primary web target.
- **Material Wrapping**: Always wrap preview content in a `MaterialTheme` or appropriate theme provider to ensure correct styling.

---

## 🛠️ Implementation Rules

1.  **Use Cases**: Every business action (e.g., `GetCatalogItems`, `AddToCart`) should be a separate Use Case class.
2.  **Mappers**: Never leak Data DTOs into the Domain or Presentation layers. Use mappers in the Data layer.
3.  **State Management**: Use `StateFlow` for UI state and `SharedFlow` for one-time effects (navigation, toasts).
4.  **Dependency Injection**: (e.g., Koin) should be used to provide implementations of Repository interfaces to Use Cases, and Use Cases to ViewModels.
5.  **Screaming Packages**: If a feature is small, keep it simple, but as soon as it grows, split it into the standard structure above.

---

## ✅ Checklist for New Features
- [ ] Is the package named after a functional feature (e.g., `catalog`)?
- [ ] Does the Domain layer have zero dependencies on Data?
- [ ] Are all network models (DTOs) mapped to Domain Entities?
- [ ] Is there a Use Case for each business operation?
- [ ] Does the UI "scream" what it does?
