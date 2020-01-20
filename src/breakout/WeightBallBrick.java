package breakout;

import javafx.scene.Group;
import javafx.scene.shape.Shape;

import java.util.List;

public class WeightBallBrick extends Brick {
    private int ballWeightMultiplier;


    public WeightBallBrick(double x, double y, int width, int height) {
        super(x, y, width, height);

    }

    public WeightBallBrick(double x, double y, int weightFactor) {
        super(x, y);
        ballWeightMultiplier = weightFactor;
    }

    public int getBallWeightMultiplier(){
        return ballWeightMultiplier;
    }

    @Override
    public void handleDestruction(List<InteractiveRectangle> objectList, List<Ball> ballList, List<Brick> brickList, Group root){
        super.handleDestruction(objectList, ballList, brickList, root);
        for(int i = 0; i < ballList.size(); i++){
            ballList.get(i).setMyWeight(ballList.get(i).getWeight()*ballWeightMultiplier);
        }
    }
}
