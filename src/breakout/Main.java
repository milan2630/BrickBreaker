package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {
    /**
     * Start of the program.
     */
    public static final int MOVER_SPEED = 5;
    public static final int FRAMES_PER_SECOND = 200;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 500;
    private static final int START_LIVES = 3;
    private static final int NUM_LEVELS = 2;
    private static final int SCORE_PER_BRICK = 10;
    private static final double PADDLE_Y_POS = 450;

    private List<Ball> ballsInPlay;
    private List<Brick> bricksInPlay;
    private List<InteractiveRectangle> objectsInPlay;
    private Paddle paddle;

    private boolean isBallActive;
    private boolean isLevel;
    private int currentLevel;
    private int lives;
    private int score;

    private Text display;
    private Group root;
    private Stage mainStage;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        lives = START_LIVES;
        score = 0;
        currentLevel = 1;
        display = new Text();
        mainStage = stage;
        isLevel = false;
        Scene startScene = splashScreen();
        mainStage.setScene(startScene);
        mainStage.show();

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene splashScreen(){
        Button startGame = new Button("PLAY");
        startGame.setOnAction(e -> startGame());

        VBox vbox = new VBox(header(), startGame);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(20);
        return new Scene(vbox, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private VBox header(){
        Label title = new Label("Brick Breaker Superstar");
        title.setFont(Font.font("Arial", 30));

        Label mainInstructions = new Label("Break all the bricks in each level to win the game.");
        Label moreInstructions = new Label("Use the arrow keys to move the paddle and space to launch the ball.");
        VBox v =  new VBox(title, mainInstructions, moreInstructions);
        v.setSpacing(20);
        v.setAlignment(Pos.TOP_CENTER);
        return v;

    }

    private void startGame(){
        try{
            mainStage.setScene(setupLevel(1));
        }
        catch(IOException e){
            e.printStackTrace();
        }
        isLevel = true;
    }

    private Scene setupLevel(int level) throws IOException {
        currentLevel = level;
        initializeLevelVariables();
        initializeDisplay();
        initializePaddle();
        initializeBall();
        File levelFile = new File("doc/LevelFiles/SetupLevel"+level+".txt");
        BufferedReader reader = new BufferedReader(new FileReader(levelFile));
        String line = "";
        while((line = reader.readLine()) != null){
            String[] lineComponents = line.split("/");
            double brickY = Double.parseDouble(lineComponents[0]);
            double brickX = Double.parseDouble(lineComponents[1]);
            try{
                Class brickClass = Class.forName("breakout."+lineComponents[2]);
                Class[] parameterTypes = {Double.TYPE, Double.TYPE, Double.TYPE};
                Constructor constructor = brickClass.getConstructor(parameterTypes);
                Object newBrick = constructor.newInstance(brickX, brickY, Double.parseDouble(lineComponents[3]));
                bricksInPlay.add((Brick) newBrick);
                objectsInPlay.add((Brick) newBrick);
                root.getChildren().add((Brick) newBrick);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private void intializeBricks(){

    }

    private void initializeBall() {
        Ball resetBall = new Ball(paddle);
        resetBall.setFill(Color.RED);
        addBallToScene(resetBall);
        isBallActive = false;
    }

    private void initializeLevelVariables(){
        root = new Group();
        ballsInPlay = new ArrayList<>();
        bricksInPlay = new ArrayList<>();
        objectsInPlay = new ArrayList<>();
    }

    private void initializePaddle() {
        paddle = new Paddle((SCREEN_WIDTH / 2) - (Paddle.BASE_PADDLE_WIDTH / 2), PADDLE_Y_POS);
        paddle.setFill(Color.BLACK);
        root.getChildren().add(paddle);
        objectsInPlay.add(paddle);
    }

    private void initializeDisplay() {
        display.setX(0);
        display.setY(15);
        display.setFill(Color.RED);
        display.setOpacity(.3);
        display.setFont(Font.font("Arial", 15));
        display.setText("Score: " + score + " Lives: "+ lives);
        root.getChildren().add(display);
    }

    private void handleKeyInput(KeyCode code) {
        if (code == KeyCode.RIGHT) {
            paddle.moveRight(SECOND_DELAY);
        }
        else if (code == KeyCode.LEFT) {
            paddle.moveLeft(SECOND_DELAY);
        }
        else if (code == KeyCode.SPACE && !isBallActive){
            for(Ball b: ballsInPlay){
                b.fireBall();
                isBallActive = true;
            }
        }
        else if (code == KeyCode.P){
            pauseGame(mainStage.getScene());
        }
        else if (code == KeyCode.DIGIT5){
            resetBallPaddle();
        }
        else if (code == KeyCode.DIGIT6){
            lives++;
        }
        setLevelOnClick(code);

    }

    private void resetBallPaddle() {
        paddle.setX(SCREEN_WIDTH/2);
        objectsInPlay.removeAll(ballsInPlay);
        root.getChildren().removeAll(ballsInPlay);
        ballsInPlay = new ArrayList<>();

        initializeBall();

    }

    private void setLevelOnClick(KeyCode code){
        if(code.getCode() >= 49 && code.getCode() <= 52){
            try{
                mainStage.setScene(setupLevel(code.getCode() - 48));
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void pauseGame(Scene paused) {
        isLevel = false;
        mainStage.setScene(pauseScreen(paused));
    }

    private Scene pauseScreen(Scene paused) {
        Button resumeGame = new Button("Resume");
        resumeGame.setOnAction(e -> resumeGame(paused));

        VBox vbox = new VBox(header(), resumeGame, cheatKeySet(), restartButton());

        vbox.setSpacing(20);
        vbox.setAlignment(Pos.TOP_CENTER);

        return new Scene(vbox, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public VBox cheatKeySet(){
        VBox cheatKeySet = new VBox();
        cheatKeySet.setAlignment(Pos.TOP_CENTER);
        for(int i = 1; i < 5; i++){
            Label cheatKey = new Label(cheatCodeInstruction("" + i, "go to level " + i));
            cheatKeySet.getChildren().add(cheatKey);
        }

        Label cheatInstruction5 = new Label(cheatCodeInstruction("5", "reset the ball and paddle"));
        Label cheatInstruction6 = new Label(cheatCodeInstruction("6", "add a life"));
        cheatKeySet.getChildren().add(cheatInstruction5);
        cheatKeySet.getChildren().add(cheatInstruction6);
        return cheatKeySet;
    }

    private String cheatCodeInstruction(String key, String function){
        return "Press " + key + " to " + function;
    }

    private void restartGame() {
        mainStage.setScene(splashScreen());
        currentLevel = 1;
        lives = START_LIVES;
    }

    private void resumeGame(Scene paused){
        mainStage.setScene(paused);
        isLevel = true;
    }

    private void step(double elapsedTime){
        if(isLevel) {
            checkForIntersections();
            removeDestroyedBricks();
            updateObjectPositions(elapsedTime);
            removeLostBalls();
            checkIfLifeLoss();
            checkGameOver();
            checkLevelBeat();
            moveBallWithPaddle();
            updateScore();
        }
    }

    private void updateScore() {
        display.setText("Score: " + score + " Lives: "+ lives);
    }

    private void moveBallWithPaddle(){
        if(!isBallActive){
            for(int i = 0; i < ballsInPlay.size(); i++){
                ballsInPlay.get(i).setPositionOnPaddle(paddle);
            }
        }
    }


    private void checkLevelBeat() {
        if(bricksInPlay.size() == 0){
            if(currentLevel <= NUM_LEVELS){
                try {
                    mainStage.setScene(setupLevel(currentLevel + 1));
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            else{
                gameWin();
            }

        }
    }

    private void gameWin() {
        mainStage.setScene(winScreen());
        isLevel = false;
    }

    private Scene winScreen(){
        Label congrats = new Label("CONGRATULATIONS!");
        congrats.setFont(Font.font("Arial", 40));
        Label youWin = new Label("YOU WIN!");
        youWin.setFont(Font.font("Arial", 30));



        VBox v = new VBox(congrats, youWin, restartButton());
        v.setAlignment(Pos.TOP_CENTER);
        v.setSpacing(20);
        return new Scene(v, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private Button restartButton(){
        Button restartGame = new Button("Restart");
        restartGame.setOnAction(e -> restartGame());
        return restartGame;
    }

    private void checkGameOver(){
        if(lives == 0){
            mainStage.setScene(gameOverScreen());
            isLevel = false;
        }
    }

    private Scene gameOverScreen(){
        Label lose = new Label("Game Over!");
        lose.setFont(Font.font("Arial", 40));
        Button restartGame = new Button("Restart");
        restartGame.setOnAction(e -> restartGame());
        VBox v = new VBox(lose, restartGame);
        v.setAlignment(Pos.TOP_CENTER);
        v.setSpacing(20);
        return new Scene(v, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void checkIfLifeLoss(){
        if(ballsInPlay.size() == 0){
            lives--;
            Ball newBall = new Ball(paddle);
            newBall.setFill(Color.RED);
            addBallToScene(newBall);
            isBallActive = false;
        }
    }

    private void updateObjectPositions(double elapsedTime){
        for(int i = 0; i < objectsInPlay.size(); i++){
            objectsInPlay.get(i).updatePosition(elapsedTime);
        }
    }

    private void addBallToScene(Ball ball){
        ballsInPlay.add(ball);
        objectsInPlay.add(ball);
        root.getChildren().add(ball);
    }

    private void removeLostBalls() {
        for(int i = 0; i < ballsInPlay.size(); i++){
            if(ballsInPlay.get(i).isOutOfPlay(SCREEN_HEIGHT)){
                objectsInPlay.remove(ballsInPlay.get(i));
                ballsInPlay.remove(i);
            }
        }
    }

    private void removeDestroyedBricks() {
        for(int i = 0; i < bricksInPlay.size(); i++){
            if(bricksInPlay.get(i).getRequiredHits() <= 0) {
                bricksInPlay.get(i).handleDestruction(objectsInPlay, ballsInPlay, bricksInPlay, root);
                score+=SCORE_PER_BRICK;
            }
        }
    }


    private void checkForIntersections() {
        for(int i = 0; i < objectsInPlay.size(); i++){
            for(int j = 0; j <objectsInPlay.size(); j++){
                if(i != j){
                    objectsInPlay.get(i).onIntersect(objectsInPlay.get(j), objectsInPlay, ballsInPlay);
                }
            }
            objectsInPlay.get(i).onWallContact(SCREEN_WIDTH, SCREEN_HEIGHT);
        }
    }
}
