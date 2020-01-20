package breakout;

import javafx.scene.shape.Shape;

import java.util.List;

public class MovingBrick extends Brick {
    public static final double BASE_SPEED = 50;
    private double mySpeed;
    public MovingBrick(double x, double y, double speed){
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        mySpeed = speed;
    }

    public MovingBrick(double x, double y){
        super(x, y);
        mySpeed = BASE_SPEED;
    }

    @Override
    public void updatePosition(double elapsedTime){
        this.setX(this.getX() + (mySpeed)*elapsedTime);
    }

    public void bounceHorizontal(){
        mySpeed = -1*mySpeed;
    }


    @Override
    public void onIntersect(Shape shape, List<InteractiveRectangle> objectList, List<Ball> ballList) {
        super.onIntersect(shape, objectList, ballList);
        if(this.determineIntersectionType(shape) == -1 && shape instanceof Brick){
            bounceHorizontal();
        }
    }

    @Override
    public void onWallContact(int screenWidth, int screenHeight) {
        if(this.getX() < 0 || this.getX() > screenWidth - this.getBoundsInLocal().getWidth()){
            this.bounceHorizontal();
        }
    }
}
