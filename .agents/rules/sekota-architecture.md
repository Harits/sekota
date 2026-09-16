---
description: Sekota architectural standards, Clean and Screaming Architecture, and Compose preview rules.
trigger: always_on
---

# Sekota Development & Architecture Standards

1. **Clean & Screaming Architecture**:
   - Place all domain logic in `shared/src/commonMain/kotlin/com/sekota/features/<feature>/domain/`.
   - Domain layer must remain pure Kotlin (entities, usecases, repository interfaces) with zero dependencies on Ktor or outer layers.
   - Implement data repositories and network clients in `shared/.../features/<feature>/data/`. Use mappers to prevent DTO leakage into domain.
   - Structure presentation code in `composeApp/src/commonMain/kotlin/com/sekota/`.

2. **Mandatory Desktop Previews**:
   - Every top-level composable (Screen, Section, Component) MUST include an `@Preview(device = DESKTOP)`.
   - Always wrap previews in `MaterialTheme` or appropriate theme container.

3. **Design System & Typography**:
   - Colors: Ink Navy (`#0D1F2D`), Brand Teal (`#00B5C8`), Brand Green (`#60BD65`), Surface Pure (`#FFFFFF`), Surface Alt (`#F7FAFB`). Never use pure `#000000`.
   - Typography: Headlines use `Montserrat` (bold/track-tight), body copy and tracked uppercase eyebrows use `DM Sans`.
