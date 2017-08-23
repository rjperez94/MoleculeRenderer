import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/** Program to render a molecule on the graphics pane from different possible
 *  perspectives.
 *  A molecule consists of a collection of atoms.
 *  Each atom has a type (eg, Carbon, or Hydrogen, or Oxygen, ..),
 *  and a three dimensional position in the molecule (x, y, z).
 *
 *  Each molecule is described in a file by a list of atoms and their positions.
 *  The molecule is rendered by drawing a colored circle for each atom.
 *  The size and color of each atom is determined by the type of the atom.
 * 
 *  To make sure that the nearest atoms appear in front of the furthest atoms,
 *  the atoms must be rendered in order from the furthest away to the nearest.
 *  Each viewing perspective imposes a different ordering on the atoms.
 *
 *  The description of the size and color for rendering the different types of
 *  atoms is stored in the file "element-table.txt" which should be read and
 *  stored in a Map.  When an atom is rendered, the type should be looked up in
 *  the map to find the size and color to pass to the atom's render() method
 * 
 *  A molecule can be rendered from different perspectives, and the program
 *  provides four buttons to control the perspective of the rendering:
 *   "Front" renders the molecule from the front (perspective = 0 degrees)
 *   "Back" renders the molecule from the back (perspective = 180 degrees)
 *   "Left" renders the molecule from the left (perspective = -90 degrees)
 *   "Right" renders the molecule from the right (perspective = 90 degrees)
 *   "PanLeft" decreases the perspective of the rendering by 5 degrees,
 *   "PanRight" increases the perspective of the rendering by 5 degrees,
 */

public class MoleculeRenderer implements UIButtonListener {

    // Fields
    // Map containing the size and color of each atom type.
    private Map<String,Element> elementTable; 

    // The collection of the atoms in the current molecule
    private List<Atom> molecule;  

    private double currentAngleHor = 0.0;    //current horizontal viewing angle (in degrees)
    private double panStep = 5.0;
    
    
    private double zoomFactor; 
    private double currentAngleVer = 0.0;   //current vertical viewing angle (in degrees)

    // Constructor:
    /** Set up the Graphical User Interface and read the file of element data of
     *  each possible type of atom into a Map: where the type is the key
     *  and an ElementInfo object is the value (containing size and color).
     */
    public MoleculeRenderer() {
        UI.addButton("Read", this);
        UI.addButton("FromFront", this);
        
        UI.addButton("FromBack", this);
        UI.addButton("FromLeft", this);
        UI.addButton("FromRight", this);
        UI.addButton("FromTop", this);
        UI.addButton("FromBottom", this);
        UI.addButton("PanRight", this);
        UI.addButton("PanLeft", this);
        
        UI.addButton("TiltTop", this);
        UI.addButton("TiltBottom", this);
        UI.addButton("ZoomIn", this);
        UI.addButton("ZoomOut", this);
        
        elementTable = new HashMap<String, Element>();
        readElementTable();    //  Read the element table first
    }

    /** Respond to button presses.
     *  Most of the presses will set the currentAngle and sort the list of molecules
     *  using the appropriate comparator
     */
    public void buttonPerformed(String button) {
        if (button.equals("Read")) { 
            currentAngleHor = 0;
            currentAngleVer = 0;
            String filename = UIFileChooser.open();
            readMoleculeFile(filename);
            Collections.sort(molecule, new BackToFrontComparator());
        }
        else if (button.equals("FromFront")) {  // set the angle and sort from back to front
            currentAngleHor = 0;
            currentAngleVer = 0;
            Collections.sort(molecule, new BackToFrontComparator());
        }
        
        else if (button.equals("FromBack")) {  // set the angle and sort from front to back
            currentAngleVer = 0;
            currentAngleHor = -180;
            Collections.sort(molecule, new FrontToBackComparator());
        }
        else if (button.equals("FromLeft")) {  // set the angle and sort from right to left
            currentAngleVer = 0;
            currentAngleHor = -90;
            Collections.sort(molecule, new RightToLeftComparator());
        }
        else if (button.equals("FromRight")) {  // set the angle and sort from left to right
            currentAngleVer = 0;
            currentAngleHor = 90;
            Collections.sort(molecule, new LeftToRightComparator());
        }
        else if (button.equals("FromTop")) {  // set the angle and sort from bottom to top
            currentAngleHor = 0;
            currentAngleVer = -90;
            Collections.sort(molecule, new BottomToTopComparator());
        }
        else if (button.equals("FromBottom")) {  // set the angle and sort from top to bottom
            currentAngleHor = 0;
            currentAngleVer = 90;
            Collections.sort(molecule, new TopToBottomComparator());
        }
        else if (button.equals("PanRight")) {  // set the angle and pan right
            currentAngleHor += panStep;
            Collections.sort(molecule, new PanComparator());
        }
        else if (button.equals("PanLeft")) {  // set the angle and sort from top to bottom
            currentAngleHor -= panStep;
            Collections.sort(molecule, new PanComparator());
        }
        
        else if (button.equals("TiltTop")) {  // set the angle and pan right
            currentAngleVer += panStep;
            Collections.sort(molecule, new TiltComparator());
        }
        else if (button.equals("TiltBottom")) {  // set the angle and sort from top to bottom
            currentAngleVer -= panStep;
            Collections.sort(molecule, new TiltComparator());
        }
        else if (button.equals("ZoomIn")) {  // set the zoom factor and zoom in
            zoomFactor = 15;
            zoom();
        }
        else if (button.equals("ZoomOut")) {  // set the zoom factor and zoom out
            zoomFactor = -15;
            zoom();
        }

        // render the molecule according to the current ordering
        render();
    }

    /** Reads the molecule data from a file containing one line for each atom in
     *  the molecule.
     *  Each line contains an atom type and the 3D coordinates of the atom.
     *  For each atom, it constructs an Atom object,
     *   and adds it to the List of Atoms in the molecule.
     *  To get the color and the size of each atom, it has to look up the type
     *   of the atom in the Map of elements.
     */
    public void readMoleculeFile(String fname) {
        try {
            
            Scanner scan = new Scanner(new File(fname));
            
            molecule = new ArrayList<Atom>();
            
            String atom = "";
            Color color;
            int x = 0;  int y = 0;  int z = 0;  double radius = 0;
            
            while (scan.hasNext()) {
                atom = scan.next();
                x = scan.nextInt();
                y = scan.nextInt();
                z = scan.nextInt();
                
                Element elem = elementTable.get(atom);
                color = elem.color;
                radius = elem.radius;
                
                molecule.add(new Atom(x, y, z, color, radius));
            }

            scan.close();
        } 
        catch(IOException ex) {
            UI.println("Reading molecule file " + fname + " failed");
        }
    }

    /** (Completion) Reads a file containing radius and color information about each type of
     *  atom and stores the info in a Map, using the atom type as a key
     */
    private void readElementTable() {
        UI.println("Reading the element table file ...");
        try {
            
            Scanner scan = new Scanner(new File("element-table.txt"));
            
            while (scan.hasNext()) {
                Element elem = new Element (scan);
                elementTable.put(elem.name, elem);
            }

            scan.close();
            UI.println("Done reading "+elementTable.size()+" elements");
        } catch (IOException ex) {
            UI.println("Reading element table file FAILED");
            UI.println("Program will now close");
            UI.sleep(2000);
            UI.quit();
        }
    }

    /** Render the molecule, according the the current ordering of Atoms in the List.
     *  The Atom's render() method needs the current perspective angle 
     */
    public void render() {
        UI.clearGraphics();
        for(Atom atom : molecule) {
            atom.render(currentAngleHor,currentAngleVer);
        }
        UI.repaintGraphics();
    }
    
    
    public void zoom() {
        for(Atom atom : molecule) {
            double zoomBy = atom.getRad()+zoomFactor;
            atom.newRad(zoomBy);
        }
    }

    // Private comparator classes
    // You will need a comparator class for each different direction
    // used in the buttonPerformed method.
    //
    // Each comparator class should be a Comparator of Atoms, and will define
    // a compare method that compares two atoms.
    // Each comparator should have a compare method.
    // Most of the comparators do not need an explicit constructor and have no fields.
    // However, the comparator for the pan methods may need a field and a constructor

    /** Comparator that will order atoms from back to front */
    private class BackToFrontComparator implements Comparator<Atom> {
        /**
         * Uses the z coordinates of the two atoms
         * larger z means towards the back,
         * smaller z means towards the front
         * Returns
         *  negative if atom1 is more to the back than atom2, (
         *  0 if they are in the same plane,
         *  positive if atom1 is more to the front than atom2.
         */
        
        public int compare(Atom a1, Atom a2){
            if (a1.getZ() > a2.getZ()) {
                return 1;
            }
            else if (a1.getZ() < a2.getZ()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
    
    /** Comparator that will order atoms from front to back  Vice Versa of BackToFront*/
    private class FrontToBackComparator implements Comparator<Atom> {
        
        public int compare(Atom a1, Atom a2){
            if (a1.getZ() < a2.getZ()) {
                return 1;
            }
            else if (a1.getZ() > a2.getZ()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
    
    /** Comparator that will order atoms from right to left */
    private class RightToLeftComparator implements Comparator<Atom> {
        /**
         * Uses the x coordinates of the two atoms
         * larger x means towards the right,
         * smaller x means towards the left
         * Returns
         *  negative if atom1 is more to the right than atom2, (
         *  0 if they are in the same plane,
         *  positive if atom1 is more to the left than atom2.
         */
        
        public int compare(Atom a1, Atom a2){
            if (a1.getX() > a2.getX()) {
                return 1;
            }
            else if (a1.getX() < a2.getX()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
    
    /** Comparator that will order atoms from left to right  Vice Versa of RightToLeft*/
    private class LeftToRightComparator implements Comparator<Atom> {
        
        public int compare(Atom a1, Atom a2){
            if (a1.getX() < a2.getX()) {
                return 1;
            }
            else if (a1.getX() > a2.getX()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
    
    /** Comparator that will order atoms from right to left */
    private class BottomToTopComparator implements Comparator<Atom> {
        /**
         * Uses the y coordinates of the two atoms
         * larger y means towards the bottom,
         * smaller y means towards the top
         * Returns
         *  negative if atom1 is more to the bottom than atom2, (
         *  0 if they are in the same plane,
         *  positive if atom1 is more to the top than atom2.
         */
        
        public int compare(Atom a1, Atom a2){
            if (a1.getY() > a2.getY()) {
                return 1;
            }
            else if (a1.getY() < a2.getY()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
    
    /** Comparator that will order atoms from left to right  Vice Versa of BottomToTop*/
    private class TopToBottomComparator implements Comparator<Atom> {
        
        public int compare(Atom a1, Atom a2){
            if (a1.getY() < a2.getY()) {
                return 1;
            }
            else if (a1.getY() > a2.getY()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
    
    /** Comparator that will order atoms when panning .*/
    private class PanComparator implements Comparator<Atom> {
        
        public int compare(Atom a1, Atom a2){
            if (a1.furtherLtoR(a2,currentAngleHor) > 0) {
                return 1;
            }
            else if (a1.furtherLtoR(a2,currentAngleHor) < 0) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
    
    /** Comparator that will order atoms when tilting .*/
    
    private class TiltComparator implements Comparator<Atom> {
        
        public int compare(Atom a1, Atom a2){
            if (a1.furtherTtoB(a2,currentAngleVer) > 0) {
                return 1;
            }
            else if (a1.furtherTtoB(a2,currentAngleVer) < 0) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    public static void main(String args[]) {
        new MoleculeRenderer();
    }
}
