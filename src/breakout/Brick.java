package breakout;

import javafx.scene.Group;
import javafx.scene.shape.Shape;

import java.util.List;

public class Brick extends InteractiveRectangle {
    public static final int DEFAULT_WIDTH = 50;
    public static final int DEFAULT_HEIGHT = 10;
    private int requiredHits;

    public Brick(double x, double y, int width, int height){
        super(x, y, width, height);
        requiredHits = 1;
    }

    public Brick(double x, double y){
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        requiredHits = 1;
    }

    public Brick(double x, double y, int strength){
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        requiredHits = strength;
    }

    public int getRequiredHits(){
        return requiredHits;
    }

    public void setRequiredHits(int requiredHits) {
        this.requiredHits = requiredHits;
    }

    @Override
    public void onIntersect(Shape shape, List<InteractiveRectangle> objectList, List<Ball> ballList) {
        if(this.determineIntersectionType(shape) != 0 && shape instanceof Ball){
            requiredHits -= ((Ball) shape).getWeight();
        }
    }

    public void handleDestruction(List<InteractiveRectangle> objectList, List<Ball> ballList, List<Brick> brickList, Group root){
        root.getChildren().remove(this);
        objectList.remove(this);
        brickList.remove(this);
    }

    @Override
    public void onWallContact(int screenWidth, int screenHeight) {
        return;
    }

    @Override
    public void updatePosition(double elapsedTime) {
        return;
    }
}
