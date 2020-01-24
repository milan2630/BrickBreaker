# DESIGN.md

## Milan Bhat (mb554), Developer

#### Design Goals:

The main design goal of this project was to make it easy to add different types of bricks 
in the future. Extensions on my brick breaker game would mainly be in the form of adding 
power up bricks. These power ups should be able to affect the bricks themselves, the balls in
play, and the paddle. 

#### High-level design 

* Main
    * Runs the game
    * Handles changing levels
    * Handles step function which runs once after every short time interval
    * Holds constants
    * Main Private Variables:
        * ``List<InteractiveRectangle> objectsInPlay`` holds all objects on the scene
        * ``List<Ball> ballsInPlay`` holds all balls on the scene
        * ``List<Brick> bricksInPlay`` holds all the bricks on the scene
    * Interactions with other classes:
        * Contains lists of the other classes and runs their public methods
* InteractiveRectangle
    * Abstract class that all other objects in the game extend
    * ``public int determineIntersectionType(Shape shape)`` is not abstract and is used to 
    determine if this InteractiveRectangle is intersecting with another shape. Returning 0 
    means no intersection, returning 1 means a vertical intersection, and returning -1 means 
    a horizontal intersection.
    * ``public abstract void onIntersect(Shape shape)`` is overridden to determine what to do 
    when this object intersects another.
    * ``public abstract void onWallContact(int screenWidth, int screenHeight)`` is overridden
    to determine what to do when this object hits the edge of the screen
    * ``public abstract void updatePosition(elapsedTime)`` is overridden to determine what 
    to do every time the step() function in main is called
* Brick
    * Represents the bricks in the game
    * Has method ``public void handleDestruction(List<InteractiveRectangle> objectList, List<Ball> ballList, List<Brick> brickList, Group root)`` 
    that can be overridden to create powerups
    * Interacts with the Ball class through the onIntersect method 
* Paddle 
    * Represents the user-controlled paddle in the game
    * When powerup bricks break they can change the Paddle's speed or size
    * Is not otherwise affected by other classes because it is controlled by the user


#### Assumptions
* There is only 1 paddle
* Each type of brick is a new class extending either Brick or MovingBrick
* The text file for the levels is in the proper format

#### Adding new features
To add new types of bricks, a new class must be made. If the brick is moving, it must extend
MovingBrick and if it is stationary it must extend Brick. The new power-up brick must then 
override the method handleDestruction to make the desired changes to the level based on the
power-up. To add new levels, new text files titled SetupLevelX.txt where X is the level must be added to the 
LevelFiles folder. Additionally, in the Main class, the static constant NUM_LEVELS must be 
updated to the total number of levels.
    
        