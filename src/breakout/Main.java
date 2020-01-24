//@author Milan Bhat
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Main class is used to run the game and keeps track of the stage and scene.
 */
public class Main extends Application {
    public static final int FRAMES_PER_SECOND = 100;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 500;
    private static final int START_LIVES = 3;
    private static final int NUM_LEVELS = 4;
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

    /**
     * First function to run in the program, simply launches the game
     */
    public static void main (String[] args) {
        launch(args);
    }

    /**
     * Sets up the splash screen and assigns all variables
     * @param stage is the stage showing the scenes
     * @throws Exception if the setup files have an issue
     */
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

    /**
     * @return a scene with the splash screen
     */
    private Scene splashScreen(){
        Button startGame = new Button("PLAY");
        startGame.setOnAction(e -> startGame());

        VBox vbox = new VBox(header(), startGame);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(20);
        return new Scene(vbox, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * @return a VBox containing the game name and the instructions
     */
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

    /**
     * Enters levels when player clicks play
     */
    private void startGame(){
        try{
            mainStage.setScene(setupLevel(1));
        }
        catch(IOException e){
            e.printStackTrace();
        }
        isLevel = true;
    }

    /**
     * Initializes variables and objects for the levels and puts the bricks where they are supposed to be
     * @param level which the player is on
     * @return a scene with everything for a level set up
     * @throws IOException if there is an issue with the level setup file
     */
    private Scene setupLevel(int level) throws IOException {
        currentLevel = level;
        initializeLevelVariables();
        initializeDisplay();
        initializePaddle();
        initializeBall();
        initializeBricks(level);


        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }


    /**
     * Puts the bricks in the desired formation for the level
     * @param level is the current level
     * @throws IOException if there is an issue with the level setup file
     */
    private void initializeBricks(int level) throws IOException {
        File levelFile = new File("doc/LevelFiles/SetupLevel"+level+".txt");
        BufferedReader reader = new BufferedReader(new FileReader(levelFile));
        String line = "";
        while((line = reader.readLine()) != null){
            String[] lineComponents = line.split("/");
            try{
                createBrick(lineComponents);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates an individual brick based on a line in the setup file
     * @param components the information on one line of the setup file regarding what brick to place where
     * Throws and exception if there is an issue with the level setup file
     */
    private void createBrick(String[] components) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class brickClass = Class.forName("breakout."+components[2]);
        Class[] parameterTypes = {Double.TYPE, Double.TYPE, Double.TYPE};
        Constructor constructor = brickClass.getConstructor(parameterTypes);
        double brickY = Double.parseDouble(components[0]);
        double brickX = Double.parseDouble(components[1]);
        Object newBrick = constructor.newInstance(brickX, brickY, Double.parseDouble(components[3]));
        bricksInPlay.add((Brick) newBrick);
        objectsInPlay.add((Brick) newBrick);
        root.getChildren().add((Brick) newBrick);
    }

    /**
     * Creates a ball and sets it on the paddle in the start of the game
     */
    private void initializeBall() {
        Ball resetBall = new Ball(paddle);
        resetBall.setFill(Color.RED);
        addBallToScene(resetBall);
        isBallActive = false;
    }

    /**
     * Initializes lists which keep track of objects in the game
     */
    private void initializeLevelVariables(){
        root = new Group();
        ballsInPlay = new ArrayList<>();
        bricksInPlay = new ArrayList<>();
        objectsInPlay = new ArrayList<>();
    }

    /**
     * Initializes paddle in scene
     */
    private void initializePaddle() {
        paddle = new Paddle((SCREEN_WIDTH / 2) - (Paddle.BASE_PADDLE_WIDTH / 2), PADDLE_Y_POS);
        paddle.setFill(Color.BLACK);
        root.getChildren().add(paddle);
        objectsInPlay.add(paddle);
    }

    /**
     * Initializes the display for the score and the player's lives
     */
    private void initializeDisplay() {
        display.setX(0);
        display.setY(15);
        display.setFill(Color.RED);
        display.setOpacity(.3);
        display.setFont(Font.font("Arial", 15));
        display.setText("Score: " + score + " Lives: "+ lives);
        root.getChildren().add(display);
    }

    /**
     * Controls what is done when a key is pressed
     * @param code the code related to which key is pressed
     */
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
        else if(code == KeyCode.Y){
            gameWin();
        }
        setLevelOnClick(code);

    }

    /**
     * Resets the ball to be in the middle of the paddle and both to be in the middle of the scene
     */
    private void resetBallPaddle() {
        paddle.setX(SCREEN_WIDTH/2);
        objectsInPlay.removeAll(ballsInPlay);
        root.getChildren().removeAll(ballsInPlay);
        ballsInPlay = new ArrayList<>();

        initializeBall();

    }

    /**
     * Controls changing the level to the desired one when the number is clicked.
     * @param code is the code related to the key which is pressed
     */
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

    /**
     * Pauses the game
     * @param paused is the scene when the game is paused so that it can be continued from the same point
     */
    private void pauseGame(Scene paused) {
        isLevel = false;
        mainStage.setScene(pauseScreen(paused));
    }

    /**
     * @param paused is the scene when the game is paused so that it can be continued from the same point
     * @return the scene for the pause screen
     */
    private Scene pauseScreen(Scene paused) {
        Button resumeGame = new Button("Resume");
        resumeGame.setOnAction(e -> resumeGame(paused));

        VBox vbox = new VBox(header(), resumeGame, cheatKeySet(), restartButton());

        vbox.setSpacing(20);
        vbox.setAlignment(Pos.TOP_CENTER);

        return new Scene(vbox, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * @return a VBox explaining how to use the cheat keys
     */
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

    /**
     * @param key to perform the cheat
     * @param function of the cheat key
     * @return a cheat key instruction in the proper format
     */
    private String cheatCodeInstruction(String key, String function){
        return "Press " + key + " to " + function;
    }

    /**
     * Restarts the game
     */
    private void restartGame() {
        mainStage.setScene(splashScreen());
        currentLevel = 1;
        lives = START_LIVES;
    }

    /**
     * Resumes the game to the scene from before it was paused
     * @param paused is the scene from before the game was paused
     */
    private void resumeGame(Scene paused){
        mainStage.setScene(paused);
        isLevel = true;
    }

    /**
     * Runs continuously as the game is played.
     * This is the function doing repeated checks for any changes in the game.
     * @param elapsedTime time between each call to this function
     */
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

    /**
     * Updates the displayed score
     */
    private void updateScore() {
        display.setText("Score: " + score + " Lives: "+ lives);
    }

    /**
     * Moves the ball with the paddle if the ball has not been launched yet
     */
    private void moveBallWithPaddle(){
        if(!isBallActive){
            for(int i = 0; i < ballsInPlay.size(); i++){
                ballsInPlay.get(i).setPositionOnPaddle(paddle);
            }
        }
    }


    /**
     * Checks if the level has been beaten and moves to the next level or calls the win screen if the it has.
     */
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

    /**
     * Displays the winning screen
     */
    private void gameWin() {
        mainStage.setScene(winScreen());
        isLevel = false;
    }

    /**
     * @return The scene with the winning screen information
     */
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

    /**
     * @return a button to restart the game
     */
    private Button restartButton(){
        Button restartGame = new Button("Restart");
        restartGame.setOnAction(e -> restartGame());
        return restartGame;
    }

    /**
     * Check if the player has lost and if so, then display the game over scene
     */
    private void checkGameOver(){
        if(lives == 0){
            mainStage.setScene(gameOverScreen());
            isLevel = false;
        }
    }

    /**
     * @return the scene with the game over screen
     */
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

    /**
     * Check if the ball has gone out of the bottom of the screen to determine if the player has lost a life
     */
    private void checkIfLifeLoss(){
        if(ballsInPlay.size() == 0){
            lives--;
            Ball newBall = new Ball(paddle);
            newBall.setFill(Color.RED);
            addBallToScene(newBall);
            isBallActive = false;
        }
    }

    /**
     * Updates the position of all objects in play
     * @param elapsedTime is the time between each update which is used to calculate how far objects have moved
     */
    private void updateObjectPositions(double elapsedTime){
        for(int i = 0; i < objectsInPlay.size(); i++){
            objectsInPlay.get(i).updatePosition(elapsedTime);
        }
    }

    /**
     * Adds the given ball to the scene and the relevant object lists
     * @param ball is the ball to be added
     */
    private void addBallToScene(Ball ball){
        ballsInPlay.add(ball);
        objectsInPlay.add(ball);
        root.getChildren().add(ball);
    }

    /**
     * Removes any balls that went below the screen
     */
    private void removeLostBalls() {
        for(int i = 0; i < ballsInPlay.size(); i++){
            if(ballsInPlay.get(i).isOutOfPlay(SCREEN_HEIGHT)){
                objectsInPlay.remove(ballsInPlay.get(i));
                ballsInPlay.remove(i);
            }
        }
    }

    /**
     * Remove any bricks that have 0 or less required hits to break
     */
    private void removeDestroyedBricks() {
        for(int i = 0; i < bricksInPlay.size(); i++){
            if(bricksInPlay.get(i).getRequiredHits() <= 0) {
                bricksInPlay.get(i).handleDestruction(objectsInPlay, ballsInPlay, bricksInPlay, root);
                score+=SCORE_PER_BRICK;
            }
        }
    }

    /**
     * Checks if any objects in play intersect with any other objects in play or the wall and calls the appropriate function to handle the situation.
     */
    private void checkForIntersections() {
        for(int i = 0; i < objectsInPlay.size(); i++){
            for(int j = 0; j <objectsInPlay.size(); j++){
                if(i != j){
                    objectsInPlay.get(i).onIntersect(objectsInPlay.get(j));
                }
            }
            objectsInPlay.get(i).onWallContact(SCREEN_WIDTH, SCREEN_HEIGHT);
        }
    }
}
