package breakout;

import javafx.scene.shape.Shape;

/**
 * This class represents the ball which moves around the screen and bounces of objects and breaks bricks
 */
public class Ball extends InteractiveRectangle {
    public static final int BALL_SIZE = 5;
    public static final int BASE_SPEED = 200;

    private double mySpeedX;
    private double mySpeedY;
    private double myWeight;


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

    public double getWeight(){
        return myWeight;
    }

    public void setMySpeedX(double newSpeedX){
        mySpeedX = newSpeedX;
    }

    public void setMySpeedY(double newSpeedY){
        mySpeedY = newSpeedY;
    }

    public void setMyWeight(double newWeight){
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

    /**
     * Moves the ball a displacement determined by the elapsed time and it's speed
     * @param elapsedTime is the time between each movement
     */
    @Override
    public void updatePosition(double elapsedTime){
        this.setX(this.getX() + (mySpeedX)*elapsedTime);
        this.setY(this.getY() + (mySpeedY)*elapsedTime);
    }

    /**
     * Set speeds so that the ball bounces at an angle relative to the position on the paddle it hits
     * @param paddle is the paddle it contacts
     */
    private void bounceOffPaddle(Paddle paddle){
        double distFromLeft = this.getX() - paddle.getX();
        setSpeedsFromAngle((distFromLeft / paddle.getWidth())*Math.PI);
        this.bounceVertical();
    }

    /**
     * Sets the x and y speeds from a given angle from the horizontal
     * @param angle from the left horizontal
     */
    public void setSpeedsFromAngle(double angle){
        double mag = this.getSpeedMagnitude();
        mySpeedY = Math.sin(angle) * mag;
        mySpeedX = -1*Math.cos(angle) * mag;
    }

    /**
     * @return magnitude of the speed
     */
    private double getSpeedMagnitude(){
        return Math.sqrt(mySpeedX*mySpeedX + mySpeedY*mySpeedY);
    }

    /**
     * Launches ball straight up
     */
    public void fireBall(){
        mySpeedY = -1*BASE_SPEED;
    }

    /**
     * When the ball makes contact with another object, it bounces off it
     * @param shape is the object that is being checked for intersection with this ball
     */
    @Override
    public void onIntersect(Shape shape) {
        if(this.determineIntersectionType(shape) == 1 && !(shape instanceof Ball)){
            if(shape instanceof Paddle){
                this.bounceOffPaddle((Paddle) shape);
            }
            else{
                this.bounceVertical();
            }
        }
        else if(this.determineIntersectionType(shape) == -1) {
            if(shape instanceof Paddle){
                this.bounceOffPaddle((Paddle) shape);
            }
            else{
                this.bounceHorizontal();
            }
        }
    }

    /**
     * When the ball makes contact with the wall, it should reverse direction
     * @param screenWidth
     * @param screenHeight
     */
    @Override
    public void onWallContact(int screenWidth, int screenHeight) {
        if(this.getX() < 0 || this.getX() > screenWidth - this.getBoundsInLocal().getWidth()){
            this.bounceHorizontal();
        }
        if(this.getY() < 0){
            this.bounceVertical();
        }
    }

    /**
     * @param screenHeight
     * @return whether the ball has gone out of the screen below the paddle
     */
    public boolean isOutOfPlay(int screenHeight){
        return this.getY() > screenHeight;
    }
}
