import ecs100.*;
import java.awt.Color;
import java.util.*;

/** Represents information about an atom in a molecule.
 *  The information includes
 *   - the 3D position of the atom
 *     (relative to the molecule).
 *     x is measured from the left to the right
 *     y is measured from the top to the bottom
 *     z is measured from the front to the back.
 *   - The color the atom should be rendered.
 *   - The radius the atom should be rendered.
 *  The positions come from the molecule file,
 *  the last two values come from the elementTable file.
 */

public class Atom {

    // coordinates of center of atom, relative to top left front corner
    private double x;       // distance to the right
    private double y;       // distance down
    private double z;       // distance away
    private Color color; // color of the atom
    private double radius;  // radius

    /** Constructor: requires the position, color, and radius */
    public Atom (double x, double y, double z, Color color, double radius) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.radius = radius;
    }

    public double getX(){return x;}

    public double getY(){return y;}

    public double getZ(){return z;}
    
    public double getRad(){ return radius;}     /*# CHALLENGE*/
    
    public void newRad(double zoomFactor){ radius =+zoomFactor;}     /*# CHALLENGE*/

    /** Return negative number if this atom is behind other, when viewed
     *  from the specified angle (in degrees). 
     *  0 degrees corresponds to viewing from the front;
     *  90 degrees corresponds to viewing from the right;
     */
    public int furtherLtoR (Atom other, double angle) {     
        double radian = angle * Math.PI / 180;
        double otherDist = other.z*Math.cos(radian) - other.x*Math.sin(radian);
        double thisDist = z*Math.cos(radian) - x*Math.sin(radian);
        return (int)(otherDist-thisDist);
    }
    
    /** Return negative number if this atom is behind other, when viewed
     *  from the specified angle (in degrees). 
     *  0 degrees corresponds to viewing from the front;
     *  90 degrees corresponds to viewing from the top;
     */
    public int furtherTtoB (Atom other, double angle) {      /*# CHALLENGE*/
        double radian = angle * Math.PI / 180;
        double otherDist = other.z*Math.cos(radian) + other.y*Math.sin(radian);
        double thisDist = z*Math.cos(radian) + y*Math.sin(radian);
        return (int)(otherDist-thisDist);
    }

    /** Render the atom on the graphics pane, from the specified angle (degrees)
     *  with the specified size and color
     *  The angle is an angle in the horizontal plane, corresponding
     *  to an angle as the user walks around a model of the molecule.
     *  0 degrees corresponds to viewing from the front;
     *  90 degrees corresponds to viewing from the right;
     */
    public void render(double angle1, double angle2 /*# CHALLENGE*/) {
        double radian1 = angle1 * Math.PI / 180;    //hor
        double radian2 = angle2 * Math.PI / 180;    //ver /*# CHALLENGE*/
        double left = 0;
        double top = 0;
        double diam =  radius*2;

        // The vertical coordinate on the graphics pane is the y coordinate of the
        // atom
        top = (y * Math.cos(radian2) - z * Math.sin(radian2)) - radius; /*# CHALLENGE*/

        // The horizontal coordinate on the graphics pane is given by x, z,
        // and the angle.
        // angle is 0 if we are looking from the front.
        // horiz coordinate = x * cos(radian) + z * sin(radian)
        left = (x* Math.cos(radian1) + z * Math.sin(radian1)) - radius;
        

        UI.setColor(color);
        UI.fillOval(left+400, top+250, diam, diam, false);
        UI.setColor(Color.black);
        UI.drawOval(left+400, top+250, diam, diam, false);

        // Note, you do not need to understand the mathematics of this (though it is
        // fairly straightforward if you draw a diagram).
    }

}
