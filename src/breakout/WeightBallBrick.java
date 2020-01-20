package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * This class is a brick that affects the weight of the balls in play when one of these bricks is broken
 */
public class WeightBallBrick extends Brick {
    private double ballWeightMultiplier;
    public static final Color COLOR = Color.SIENNA;


    public WeightBallBrick(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.setFill(COLOR);
    }

    public WeightBallBrick(double x, double y, double weightFactor) {
        super(x, y);
        ballWeightMultiplier = weightFactor;
        this.setFill(COLOR);
    }


    /**
     * Increases the weight of the balls in play when this brick is broken
     */
    @Override
    public void handleDestruction(List<InteractiveRectangle> objectList, List<Ball> ballList, List<Brick> brickList, Group root){
        super.handleDestruction(objectList, ballList, brickList, root);
        for(int i = 0; i < ballList.size(); i++){
            ballList.get(i).setMyWeight(ballList.get(i).getWeight()*ballWeightMultiplier);
        }
    }
}
