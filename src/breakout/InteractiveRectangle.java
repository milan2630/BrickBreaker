package breakout;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.List;

public abstract class InteractiveRectangle extends Rectangle {
    public InteractiveRectangle(double x, double y, int defaultWidth, int defaultHeight) {
        super(x, y, defaultWidth, defaultHeight);
    }

    public abstract void onIntersect(Shape shape, List<InteractiveRectangle> objectList, List<Ball> ballList);
    public abstract void onWallContact(int screenWidth, int screenHeight);
    public abstract void updatePosition(double elapsedTime);


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
