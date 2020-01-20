package breakout;

import javafx.scene.Group;

import java.util.List;

public class PaddleSpeedBrick extends Brick {
    public static final double DEFAULT_SPEED_FACTOR = 2;

    private double paddleSpeedFactor;

    public PaddleSpeedBrick(double x, double y, int width, int height) {
        super(x, y, width, height);
        paddleSpeedFactor = DEFAULT_SPEED_FACTOR;
    }

    public PaddleSpeedBrick(double x, double y) {
        super(x, y);
        paddleSpeedFactor = DEFAULT_SPEED_FACTOR;
    }

    public PaddleSpeedBrick(double x, double y, int factor) {
        super(x, y);
        paddleSpeedFactor = factor;
    }

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
