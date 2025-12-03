# Snap2PDF – Image to PDF Android Application

 Snap2PDF is a modern Android mobile application built using Kotlin and Jetpack Compose. It enables users to scan documents, import images, enhance them using Google ML Kit, and convert them into clean, high-quality PDF files. Designed with a simple and intuitive UI and powered by a reactive MVVM architecture, Snap2PDF delivers fast, smooth, and offline-friendly document processing.

---
## Screenshots

<table>
  <tr>
    <th>HomeScreen</th>
    <th>Scanning Screen</th>
    <th>Preview Screen</th>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/7a0bb09d-cdc1-4e29-9742-35248bbb73d9" width="250"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/7c2fad46-d657-4280-9a11-0371f7a1d031" width="250"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/e1f4fa58-9e69-4ff3-b79e-ef2836b5097b"  width="250"/>
    </td>
  </tr>
</table>
<table>
  <tr>
    <th>SavePDFScreen Screen</th>
    <th>Search Screen</th>
    <th>SettingsScreen</th>
  </tr>
  <tr>
    <td align="center">
      <img  src="https://github.com/user-attachments/assets/16f54f33-5341-4c68-847d-276383569be3" width="250"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/faa32eea-e0c0-40b7-b6b1-7e7df5032588" width="250"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/cae6652c-8493-4ac1-be0b-b37d85bc65bf" width="250"/>
    </td>
  </tr>
</table>


# Model-View-ViewModel

MVVM is a software architectural pattern that separates the presentation layer (View) from the business logic (Model), introducing a ViewModel to manage UI-related data. This design pattern improves maintainability, testability, and scalability of Android applications.

# Core Features

Scan Documents — Capture photos using the camera with auto edges detection and smart cropping

Import Images — Pick multiple images from the gallery for PDF creation

ML Kit Enhancements — Automatic perspective correction, cleanup, and image enhancement

PDF Generation — Convert one or many images into a single high-quality PDF

Reordering & Editing — Modify image order and apply enhancements before export

Local Storage — Save generated PDFs using Room for offline access

Share Files — Share or open PDFs directly from the app

Error/Empty State Handling — Clean UI states for missing files or permission issues

Dark/Light Mode Support — Respects system theme preferences

Tech Stack
Layer	Tool / Library
Language	Kotlin
UI Toolkit	Jetpack Compose
Architecture	MVVM
DI	Koin
Image Scanning	Google ML Kit (Document Scanner & Vision APIs)
Local Storage	Room (for saved PDFs & history)
State Handling	StateFlow, Coroutines, ViewModel
Navigation	Jetpack Compose Navigation
Image Loading	Coil
File Handling	Storage Access Framework (SAF)
Project Structure
com.snap2pdf

## Project Structure

```text
│
├── data
│   ├── local          # Room database, DAOs, entities for saved PDFs
│   ├── model          # Models for images, PDFs, processing states
│   └── repository     # Repository interface and implementations
│
├── di                 # Koin modules for ViewModels, Repositories, Database
│
├── ml
│   ├── scanner        # Google ML Kit scanning + preprocessing logic
│   └── utils          # Image processing, cropping, enhancement helpers
│
├── ui
│   ├── screens
│   │   ├── home       # Main home screen
│   │   ├── scan       # Capture/import images
│   │   ├── edit       # Crop, enhance, reorder images
│   │   ├── preview    # Final PDF preview
│   │   └── history    # Saved PDFs listing
│   └── components     # Reusable Composables (buttons, cards, dialogs)
│
├── navigation         # Navigation graph and screen routes
│
├── utils              # Constants, formatters, file helpers, permission utils
│
└── MainActivity.kt    # App entry point with NavHost
```
