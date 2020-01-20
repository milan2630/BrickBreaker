package breakout;

import javafx.scene.Group;
import javafx.scene.shape.Shape;

import java.util.List;

public class BallSpeedBrick extends Brick{
    public static final double DEFAULT_SPEED_FACTOR = 3;
    private double speedFactor;

    public BallSpeedBrick(double x, double y, int width, int height) {
        super(x, y, width, height);
        speedFactor = DEFAULT_SPEED_FACTOR;
    }

    public BallSpeedBrick(double x, double y) {
        super(x, y);
        speedFactor = DEFAULT_SPEED_FACTOR;
    }

    public BallSpeedBrick(double x, double y, int factor) {
        super(x, y);
        speedFactor = factor;
    }

    @Override
    public void handleDestruction(List<InteractiveRectangle> objectList, List<Ball> ballList, List<Brick> brickList, Group root) {
        super.handleDestruction(objectList, ballList, brickList, root);
        for(int i = 0; i < ballList.size(); i++){
            ballList.get(i).multiplySpeed(speedFactor);
        }
    }
}
