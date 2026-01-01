<p align="center">
  <img src="docs/Crocodile-logo.png" alt="Crocodile Banner">
</p>

# Crocodile Directory Tool

Crocodile is a Swing-based utility for inspecting and bulk editing directory trees. It provides a lightweight UI for scanning folders, searching by name, deleting or emptying matching directories, and planning reorder/flatten operations.

## Features
- Choose a root directory and automatically list every subdirectory in a table view.
- Filter the table by directory name to target specific folders before acting.
- Delete or empty selected directories, or process all directories matching the search term in one action.
- Popup menu actions for adding, renaming, moving contents, deleting intermediate directories, and reordering branches.
- Dialogs for flattening directory structures and computing reorder plans without touching the filesystem directly.
- Modern FlatLaf look-and-feel with MigLayout-based layout for a responsive Swing UI.

## Getting started
### Prerequisites
- Java 17+ (matching the parent `noor-parent` settings).
- Maven 3.9+.
- The shared parent project `noor-parent` must be available one level up from this module or installed in your local Maven repository because `pom.xml` references `../noor-parent/pom.xml`.

### Build
```bash
mvn clean package
```

### Run
`DirectoryToolLauncher` exposes two entry points:
- `DirectoryToolLauncher.open()` launches the tool as a standalone window on the Swing event thread.
- `DirectoryToolLauncher.openModal(JFrame owner)` embeds the UI inside a modal dialog owned by an existing Swing frame.

You can quickly try the tool by adding a small bootstrap class under `src/test/java` or another module that calls `DirectoryToolLauncher.open()` in a `main` method once dependencies are resolved.

## Project structure
- `src/main/java/org/ln/noor/directory/view` – Swing UI components such as `DirectoryToolView`, table models, renderers, and dialogs.
- `src/main/java/org/ln/noor/directory` – The controller orchestrating user actions and preference handling.
- `src/main/java/org/ln/noor/directory/service` – Pure services for directory statistics, filesystem operations, flattening, and reorder planning.
- `src/main/java/org/ln/noor/directory/util` – Utilities for recursive delete/empty operations and path helpers.
- `src/main/resources/icons` – Application icons used by the UI.

## Icons
The project bundles `croc.png` and `mine_32.png` under `src/main/resources/icons/`. These are packaged into the application jar and displayed within the UI.

## License
This project inherits licensing from the parent `noor-parent` project. Consult that repository for details.
