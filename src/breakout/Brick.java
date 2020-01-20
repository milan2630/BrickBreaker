package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.List;

/**
 * This class represents the bricks in the breakout game which are broken to win the game
 */
public class Brick extends InteractiveRectangle {
    public static final int DEFAULT_WIDTH = 40;
    public static final int DEFAULT_HEIGHT = 8;
    public static final Color COLOR = Color.BLACK;
    private int requiredHits;

    public Brick(double x, double y, int width, int height){
        super(x, y, width, height);
        requiredHits = 1;
        this.setFill(COLOR);
    }

    public Brick(double x, double y){
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        requiredHits = 1;
        this.setFill(COLOR);
    }

    public Brick(double x, double y, int strength){
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        requiredHits = strength;
        this.setFill(COLOR);
    }

    public Brick(double x, double y, double strength){
        this(x, y, (int) strength);
    }



    public int getRequiredHits(){
        return requiredHits;
    }


    @Override
    public void onIntersect(Shape shape) {
        if(this.determineIntersectionType(shape) != 0 && shape instanceof Ball){
            requiredHits -= ((Ball) shape).getWeight();
        }
    }

    /**
     * When a brick is destroyed, remove the brick from the display and also from the lists that are keeping track of objects
     * @param objectList keeps track of all objects
     * @param ballList keeps track of all balls
     * @param brickList keeps track of all bricks
     * @param root controls the display
     */
    public void handleDestruction(List<InteractiveRectangle> objectList, List<Ball> ballList, List<Brick> brickList, Group root){
        root.getChildren().remove(this);
        objectList.remove(this);
        brickList.remove(this);
    }

    /**
     * When a brick makes contact with the wall do nothing because normal bricks do not move.
     * This method is overriden in subclasses
     */
    @Override
    public void onWallContact(int screenWidth, int screenHeight) {
        return;
    }

    /**
     * Bricks do not move so the brick does nothing over time
     * This method is overriden in subclasses
     */
    @Override
    public void updatePosition(double elapsedTime) {
        return;
    }
}
