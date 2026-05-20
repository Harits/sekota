# Sekota Web - Technical Requirements

This document outlines the core technical requirements and constraints for the Sekota Web platform.

## 🛠️ Technology Stack
- **Frontend**: Kotlin Multiplatform (Compose HTML/Web).
- **State Management**: Shared KMP logic with Coroutines and Flow.
- **Networking**: Ktor Client (Shared across Web/Desktop).
- **Design Source**: Figma (Design Tokens & UI components).

## 📋 Quality Standards
- **Clean Architecture**: Separation of UI, Domain, and Data layers.
- **Responsiveness**: All UI components must be mobile-first and fluid.
- **Error Handling**: Graceful degradation and user-friendly error messages for all network calls.
- **Consistency**: Strict adherence to design tokens defined in Figma.

## ⚙️ Integration Requirements
- **OpenProject**: Bi-directional sync for Work Packages.
- **Jules**: Automated session triggers for complex tasks.
- **Local Persistence**: Use of persistent storage (e.g., SQLDelight) if required for offline support.
