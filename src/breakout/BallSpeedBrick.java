package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.List;

/**
 * This class is a brick that affects the speed of the balls in play when one of these bricks is broken
 */
public class BallSpeedBrick extends Brick{
    public static final double DEFAULT_SPEED_FACTOR = 3;
    public static final Color COLOR = Color.AQUA;
    private double speedFactor;

    public BallSpeedBrick(double x, double y, int width, int height) {
        super(x, y, width, height);
        speedFactor = DEFAULT_SPEED_FACTOR;
        this.setFill(COLOR);
    }

    public BallSpeedBrick(double x, double y) {
        super(x, y);
        speedFactor = DEFAULT_SPEED_FACTOR;
        this.setFill(COLOR);
    }

    public BallSpeedBrick(double x, double y, double factor) {
        super(x, y);
        speedFactor = factor;
        this.setFill(COLOR);
    }

    /**
     * Changes the speed of the balls in play when this brick is broken
     */
    @Override
    public void handleDestruction(List<InteractiveRectangle> objectList, List<Ball> ballList, List<Brick> brickList, Group root) {
        super.handleDestruction(objectList, ballList, brickList, root);
        for(int i = 0; i < ballList.size(); i++){
            ballList.get(i).multiplySpeed(speedFactor);
        }
    }
}
