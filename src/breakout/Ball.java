package breakout;

import javafx.scene.shape.Shape;

import java.util.List;

public class Ball extends InteractiveRectangle {

    public static final int BALL_SIZE = 5;
    public static final int BASE_SPEED = 400;

    private double mySpeedX;
    private double mySpeedY;
    private int myWeight;


    public Ball(double startX, double startY){
        super(startX, startY, BALL_SIZE, BALL_SIZE);
        mySpeedX = 0;
        mySpeedY = 0;
        myWeight = 1;
    }

    public Ball(double startX, double startY, int startXSpeed, int startYSpeed){
        super(startX, startY, BALL_SIZE, BALL_SIZE);
        mySpeedX = startXSpeed;
        mySpeedY = startYSpeed;
    }

    public Ball(Paddle paddle){
        this(paddle.getX() + (paddle.getWidth() / 2) - (BALL_SIZE / 2), paddle.getY() - BALL_SIZE);
    }

    public void setPositionOnPaddle(Paddle paddle){
        this.setX(paddle.getX() + (paddle.getWidth() / 2) - (BALL_SIZE / 2));
        this.setY(paddle.getY() - BALL_SIZE);
    }

    public int getWeight(){
        return myWeight;
    }

    public void setMySpeedX(double newSpeedX){
        mySpeedX = newSpeedX;
    }

    public void setMySpeedY(double newSpeedY){
        mySpeedY = newSpeedY;
    }

    public void setMyWeight(int newWeight){
        myWeight = newWeight;
    }

    public void bounceHorizontal(){
        setMySpeedX(mySpeedX * -1);
    }

    public void bounceVertical(){
        setMySpeedY((mySpeedY * -1));
    }

    public void multiplySpeed(double factor){
        mySpeedY *= factor;
        mySpeedX *= factor;
    }

    public void updatePosition(double elapsedTime){
        this.setX(this.getX() + (mySpeedX)*elapsedTime);
        this.setY(this.getY() + (mySpeedY)*elapsedTime);
    }

    private void bounceOffPaddle(Paddle paddle){
        double distFromLeft = this.getX() - paddle.getX();
        setSpeedsFromAngle((distFromLeft / paddle.getWidth())*Math.PI);
        this.bounceVertical();
    }

    public void setSpeedsFromAngle(double angle){
        double mag = this.getSpeedMagnitude();
        mySpeedY = Math.sin(angle) * mag;
        mySpeedX = -1*Math.cos(angle) * mag;
    }

    private double getSpeedMagnitude(){
        return Math.sqrt(mySpeedX*mySpeedX + mySpeedY*mySpeedY);
    }

    public void fireBall(){
        mySpeedY = -1*BASE_SPEED;
    }

    @Override
    public void onIntersect(Shape shape, List<InteractiveRectangle> objectList, List<Ball> ballList) {
        if(this.determineIntersectionType(shape) == 1){
            if(shape instanceof Paddle){
                this.bounceOffPaddle((Paddle) shape);
            }
            else{
                this.bounceVertical();
            }
        }
        else if(this.determineIntersectionType(shape) == -1) {
            this.bounceHorizontal();
        }
    }

    @Override
    public void onWallContact(int screenWidth, int screenHeight) {
        if(this.getX() < 0 || this.getX() > screenWidth - this.getBoundsInLocal().getWidth()){
            this.bounceHorizontal();
        }
        if(this.getY() < 0){
            this.bounceVertical();
        }
    }

    public boolean isOutOfPlay(int screenHeight){
        return this.getY() > screenHeight;
    }
}
