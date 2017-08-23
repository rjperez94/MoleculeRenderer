# MoleculeRenderer

## Compiling Java files using Eclipse IDE

1. Download this repository as ZIP
2. Create new `Java Project` in `Eclipse`
3. Right click on your `Java Project` --> `Import`
4. Choose `General` --> `Archive File`
5. Put directory where you downloaded ZIP in `From archive file`
6. Put `ProjectName/src` in `Into folder`
7. Click `Finish`
8. Move the `elements and molecules/element-table.txt` from {ProjectName}/src to the root of your Java Project i.e. {ProjectName}

### Linking the UI Library

8. Right click on your `Java Project` --> `Build Path` --> `Add External Archives`
9. Select `ecs100.jar` and link it to the project. That JAR will be in the directory where you downloaded ZIP

## Running the program

1. Right click on your `Java Project` --> `Run As` --> `Java Application` --> `Molecule Renderer`
2. It will load elements from `element-table.txt`. These elements are those used in this program. You may add more as you wish
3. Click `Read` to load a molecule file e.g. `molecule1.txt`
4. Molecule will be drawn on screen

## Features

<strong>Make sure that a molecule is loaded before using these features</strong>

### View From

- Front
- Back
- Left
- Right
- Top
- Bottom

### Pan

- Left
- Right

### Tilt

- Top
- Bottom

### Zoom

- In
- Out
