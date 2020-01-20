package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * This class is a brick that affects the size of the paddle when one of these bricks is broken
 */
public class PaddleSizeBrick extends Brick {
    public static final double DEFAULT_SIZE_FACTOR = 2;
    public static final Color COLOR = Color.SIENNA;
    private double paddleSizeFactor;

    public PaddleSizeBrick(double x, double y, int width, int height) {
        super(x, y, width, height);
        paddleSizeFactor = DEFAULT_SIZE_FACTOR;
        this.setFill(COLOR);
    }

    public PaddleSizeBrick(double x, double y) {
        super(x, y);
        paddleSizeFactor = DEFAULT_SIZE_FACTOR;
        this.setFill(COLOR);
    }

    public PaddleSizeBrick(double x, double y, double factor) {
        super(x, y);
        paddleSizeFactor = factor;
        this.setFill(COLOR);
    }

    /**
     * Changes the size of the paddle when this brick is broken
     */
    @Override
    public void handleDestruction(List<InteractiveRectangle> objectList, List<Ball> ballList, List<Brick> brickList, Group root) {
        super.handleDestruction(objectList, ballList, brickList, root);
        for(int i = 0; i < objectList.size(); i++){
            if(objectList.get(i) instanceof Paddle){
                Paddle pad = ((Paddle) objectList.get(i));
                pad.setWidth(pad.getWidth()*paddleSizeFactor);
            }
        }
    }

}
