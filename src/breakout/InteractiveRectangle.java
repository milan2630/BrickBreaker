package breakout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


/**
 * This is an abstract class that all the objects in play implement and holds methods that must be implemented for
 * the objects to interact properly in the field of play
 */
public abstract class InteractiveRectangle extends Rectangle {
    public InteractiveRectangle(double x, double y, int defaultWidth, int defaultHeight) {
        super(x, y, defaultWidth, defaultHeight);
    }

    /**
     * Perform an action when this object intersects another
     * @param shape the other object that is intersected by this one
     */
    public abstract void onIntersect(Shape shape);

    /**
     * Perform an action when this object makes contact with the edges of the screen
     * @param screenWidth
     * @param screenHeight
     */
    public abstract void onWallContact(int screenWidth, int screenHeight);

    /**
     * Update the position of this object as time passes
     * @param elapsedTime
     */
    public abstract void updatePosition(double elapsedTime);


    /**
     * Determine if this object is not intersecting another, or intersecting it horizontally or vertically
     * @param shape the other object that is being checked for intersection with this one
     * @return 1 if it is a vertical intersection, 0 if no intersection, and -1 if a horizontal intersection
     */
    public int determineIntersectionType(Shape shape){
        Shape intersection = Shape.intersect(this, shape);
        if(intersection.getBoundsInLocal().getWidth() != -1){
            if(intersection.getBoundsInLocal().getWidth() > intersection.getBoundsInLocal().getHeight()){
                return 1;
            }
            else {
                return -1;
            }
        }
        else{
            return 0;
        }
    }

}
