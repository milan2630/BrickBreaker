package breakout;

import javafx.scene.Group;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class MultiBallBrick extends Brick {
    public static final int BASE_NUM_BALLS = 2;

    private int numBalls;

    public MultiBallBrick(double x, double y, int width, int height) {
        super(x, y, width, height);
        numBalls = BASE_NUM_BALLS;
    }

    public MultiBallBrick(double x, double y) {
        super(x, y);
        numBalls = BASE_NUM_BALLS;
    }

    public MultiBallBrick(double x, double y, int balls) {
        super(x, y);
        numBalls = balls;
    }

    public MultiBallBrick(double x, double y, double balls){
        this(x, y, (int) balls);
    }

    public int getNumBalls() {
        return numBalls;
    }

    @Override
    public void handleDestruction(List<InteractiveRectangle> objectList, List<Ball> ballList, List<Brick> brickList, Group root){
        super.handleDestruction(objectList, ballList, brickList, root);
        root.getChildren().addAll(createMultipleBalls(objectList, ballList));
    }

    private List<Ball> createMultipleBalls(List<InteractiveRectangle> objectList, List<Ball> ballList) {
        List<Ball> newBalls = new ArrayList<>();
        double yPos = this.getY() + this.getHeight();
        for(int i = 0; i < this.getNumBalls(); i++){
            double xPos = this.getX() + (this.getWidth() / this.getNumBalls())*i;
            Ball newBall = new Ball(xPos, yPos, Ball.BASE_SPEED, 0);
            newBall.setSpeedsFromAngle((Math.PI / (this.getNumBalls()+1)*i+1));
            ballList.add(newBall);
            objectList.add(newBall);
            newBalls.add(newBall);
        }
        return newBalls;
    }
}
