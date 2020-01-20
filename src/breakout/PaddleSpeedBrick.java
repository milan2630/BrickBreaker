package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * This class is a brick that affects the speed of the paddle when one of these bricks is broken
 */
public class PaddleSpeedBrick extends Brick {
    public static final double DEFAULT_SPEED_FACTOR = 2;
    public static final Color COLOR = Color.BISQUE;

    private double paddleSpeedFactor;

    public PaddleSpeedBrick(double x, double y, int width, int height) {
        super(x, y, width, height);
        paddleSpeedFactor = DEFAULT_SPEED_FACTOR;
        this.setFill(COLOR);
    }

    public PaddleSpeedBrick(double x, double y) {
        super(x, y);
        paddleSpeedFactor = DEFAULT_SPEED_FACTOR;
        this.setFill(COLOR);
    }

    public PaddleSpeedBrick(double x, double y, double factor) {
        super(x, y);
        paddleSpeedFactor = factor;
        this.setFill(COLOR);
    }

    /**
     * Changes the speed of the paddle when this brick is broken
     */
    @Override
    public void handleDestruction(List<InteractiveRectangle> objectList, List<Ball> ballList, List<Brick> brickList, Group root) {
        super.handleDestruction(objectList, ballList, brickList, root);
        for(int i = 0; i < objectList.size(); i++){
            if(objectList.get(i) instanceof Paddle){
                Paddle pad = ((Paddle) objectList.get(i));
                pad.setPaddleSpeed(pad.getPaddleSpeed()*paddleSpeedFactor);
            }
        }
    }
}
