package breakout;

import javafx.scene.Group;

import java.util.List;

public class PaddleSizeBrick extends Brick {
    public static final double DEFAULT_SIZE_FACTOR = 2;

    private double paddleSizeFactor;

    public PaddleSizeBrick(double x, double y, int width, int height) {
        super(x, y, width, height);
        paddleSizeFactor = DEFAULT_SIZE_FACTOR;
    }

    public PaddleSizeBrick(double x, double y) {
        super(x, y);
        paddleSizeFactor = DEFAULT_SIZE_FACTOR;
    }

    public PaddleSizeBrick(double x, double y, int factor) {
        super(x, y);
        paddleSizeFactor = factor;
    }

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
