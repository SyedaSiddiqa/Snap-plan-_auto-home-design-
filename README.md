# 🏠 Snap Plan Pro — House Layout Designer

**Snap Plan Pro** is a desktop application for designing house layouts. Users log in, define their requirements (bedrooms, bathrooms, kitchens, garden), arrange rooms, furniture and structural elements on a 2D blueprint, then switch to an interactive **3D view** of the same plan. The finished design can be saved as a blueprint and exported as a **House Design Report**.

📎 **LinkedIn post:** https://www.linkedin.com/feed/update/urn:li:activity:7389669432152940544/

---

## ✨ Features

- **Login / Sign Up screen** — entry point of the application; the logged-in user and role (e.g. *admin – Designer*) are carried into the final report.
- **Home Planner input form** — specify the number of bedrooms, bathrooms and kitchens, and whether to include a garden.
- **2D Blueprint Designer** — colour-coded, labelled elements on a canvas titled *House Blueprint*.
- **3D View** — one-click toggle (*Show 3D View* / *Show 2D View*) that renders the same plan as a 3D scene with floor, objects, roof, lighting and sky background.
- **Element palette (side panel)**
  - **Rooms:** Bedroom, Bathroom, Kitchen, Garden
  - **Furniture:** Bed, Sofa, Table, Chair, Cabinet, Desk
  - **Structure:** Surface (floor), Roof, Walls
- **Save Blueprint** — store the current design.
- **Generate Report** — opens a *House Design Report* window with floor-plan specifications (bedrooms, bathrooms, kitchens, garden, user) and the blueprint image.
- **Clear All** — reset the canvas and start over.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | **Java** (JDK 24) |
| UI Framework | **JavaFX 24.0.1** (`javafx.controls`, `javafx.fxml`, `javafx.graphics`) |
| 2D Blueprint | JavaFX 2D scene graph — colour-coded, labelled shapes |
| **3D Rendering** | **JavaFX 3D API** — `SubScene`, `PerspectiveCamera`, `Box` primitives, `PhongMaterial`, lighting, 3D transforms (rotate / translate) |
| Layout & Controls | `VBox`, `Insets`, `Label`, `TextField`, `PasswordField`, `CheckBox`, `Button` |
| Dialogs | JavaFX `Alert` / secondary `Stage` windows |
| Architecture | Modular Java project (`module-info.java`) — module `autohome`, package `autohome` |
| IDE | **IntelliJ IDEA** |
| Other JavaFX modules available | `javafx.web`, `javafx.media`, `javafx.swing` (bundled with the SDK) |

### 🧊 3D View

The 3D view is built with the **built-in JavaFX 3D API** — no external 3D engine (such as Three.js or Unity) is used.

- Rooms, furniture, floor, walls and roof are rendered as 3D **`Box`** shapes.
- Colours and shading use **`PhongMaterial`**, with lighting for the shaded look and highlights.
- A **`PerspectiveCamera`** in a **`SubScene`** provides depth and perspective, over a sky-blue background.
- The *Show 3D View* / *Show 2D View* button switches between the two views of the same plan.

---

## 📁 Project Structure

```
snap plan pro/
├── sdk/                     # JavaFX SDK 24 (bin, lib, legal, src.zip)
├── snapplan/
│   ├── src/
│   │   ├── MainApp.java     # Application entry point (extends Application)
│   │   ├── LoginScreen.java # Login / Sign Up screen
│   │   └── HomePlanner.java # Requirements form (rooms + garden option)
│   ├── module-info/src/
│   │   └── module-info.java # module autohome { requires javafx.controls, javafx.fxml; }
│   └── out/                 # Compiled classes
├── snappropic/              # Screenshots used in this README
└── snap plan pro.iml        # IntelliJ module file
```

### Flow of the application

```
MainApp  ──►  LoginScreen  ──►  HomePlanner  ──►  Blueprint Designer (2D ⇄ 3D)  ──►  Report
```

---

## 🚀 Getting Started

### Prerequisites
- **JDK 24** (or a JDK compatible with JavaFX 24)
- **JavaFX SDK 24.0.1** (already bundled in the `sdk/` folder)
- **IntelliJ IDEA** (recommended)

### Run in IntelliJ IDEA
1. Open the `snap plan pro` folder as a project.
2. Make sure the library **`lib`** points to `sdk/lib` (*File → Project Structure → Libraries*).
3. Add these **VM options** in *Run → Edit Configurations*:
   ```
   --module-path "sdk/lib" --add-modules javafx.controls,javafx.fxml
   ```
4. Run `MainApp.java`.

### Run from the command line
```bash
# Compile
javac --module-path sdk/lib --add-modules javafx.controls,javafx.fxml -d out snapplan/src/*.java

# Run
java --module-path sdk/lib --add-modules javafx.controls,javafx.fxml -cp out autohome.MainApp
```

---

## 🎯 Usage

1. Launch the app and **log in** (or sign up).
2. Enter the number of **bedrooms, bathrooms, kitchens** and choose whether to include a **garden**.
3. In the designer, add **rooms, furniture and structure** from the right-hand panel.
4. Click **Show 3D View** to see the plan in 3D, and **Show 2D View** to return.
5. Use **Save Blueprint** to save, **Generate Report** to create the design report, or **Clear All** to start fresh.

---

## 🔮 Future Improvements

- Real wall drawing with doors and windows
- Orbit / zoom camera controls in the 3D view
- Export blueprint to PDF / PNG
- Persistent user accounts with database storage
- Furniture rotation, resizing and snapping to a grid
- Realistic textures and 3D models for furniture

---

## 👩‍💻 Author

**Syeda**
🔗 LinkedIn post: https://www.linkedin.com/feed/update/urn:li:activity:7389669432152940544/

---

*Built with Java & JavaFX.*
