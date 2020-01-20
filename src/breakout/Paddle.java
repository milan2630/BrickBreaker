package breakout;

import javafx.scene.shape.Shape;

/**
 * This class represents the user-controlled paddle that is used to play the game
 */
public class Paddle extends InteractiveRectangle {
    public static final int BASE_PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 20;
    public static final double BASE_PADDLE_SPEED = 1000;

    private double paddleSpeed;

    public Paddle(double startX, double startY){
        super(startX, startY, BASE_PADDLE_WIDTH, PADDLE_HEIGHT);
        paddleSpeed = BASE_PADDLE_SPEED;
    }

    public double getPaddleSpeed() {
        return paddleSpeed;
    }

    public void setPaddleSpeed(double paddleSpeed) {
        this.paddleSpeed = paddleSpeed;
    }

    public void moveRight(double elapsedTime){
        if(this.getX() + (paddleSpeed)*elapsedTime + this.getWidth() < Main.SCREEN_WIDTH) {
            this.setX(this.getX() + (paddleSpeed)*elapsedTime);
        }
        else{
            this.setX(Main.SCREEN_WIDTH - this.getWidth());
        }
    }

    public void moveLeft(double elapsedTime){
        if(this.getX() - (paddleSpeed)*elapsedTime > 0) {
            this.setX(this.getX() - (paddleSpeed) * elapsedTime);
        }
        else{
            this.setX(0);
        }
    }

    @Override
    public void onIntersect(Shape shape) {
        return;
    }


    @Override
    public void onWallContact(int screenWidth, int screenHeight) {
        return;
    }


    @Override
    public void updatePosition(double elapsedTime) {
        return;
    }
}
