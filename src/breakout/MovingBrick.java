package breakout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * This class is a brick that moves
 */
public class MovingBrick extends Brick {
    public static final double BASE_SPEED = 50;
    private double mySpeed;
    public static final Color COLOR = Color.TAN;

    public MovingBrick(double x, double y, double speed){
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        mySpeed = speed;
        this.setFill(COLOR);
    }

    public MovingBrick(double x, double y){
        super(x, y);
        mySpeed = BASE_SPEED;
        this.setFill(COLOR);
    }

    @Override
    public void updatePosition(double elapsedTime){
        this.setX(this.getX() + (mySpeed)*elapsedTime);
    }

    public void bounceHorizontal(){
        mySpeed = -1*mySpeed;
    }


    /**
     * Bounce horizontally if this contacts another brick
     * @param shape that is potentially intersected
     */
    @Override
    public void onIntersect(Shape shape) {
        super.onIntersect(shape);
        if(this.determineIntersectionType(shape) == -1 && shape instanceof Brick){
            bounceHorizontal();
        }
    }

    /**
     * If this contacts the edge of the screen, bounce horizontally
     * @param screenWidth
     * @param screenHeight
     */
    @Override
    public void onWallContact(int screenWidth, int screenHeight) {
        if(this.getX() < 0 || this.getX() > screenWidth - this.getBoundsInLocal().getWidth()){
            this.bounceHorizontal();
        }
    }
}
