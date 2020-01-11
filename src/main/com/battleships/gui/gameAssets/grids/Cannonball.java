package com.battleships.gui.gameAssets.grids;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import org.apache.commons.math3.linear.*;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

/**
 * Specific {@link Entity} that is used to indicate shots made during game.
 *
 * @author Tim Staudenmaier
 */

public class Cannonball extends Entity implements Runnable {

    /**
     * Standard speed the cannonball starts at.
     */
    private static final float SPEED = 180.0f;
    /**
     * How much the air slows the speed of the cannonball.
     */
    private static final float DRAG = 0.1f;
    /**
     * Angle at which the cannonball gets shot.
     */
    private static final float ANGLE = (float)Math.toRadians(75);

    /**
     * Maximum height the cannonball is allowed to reach during the flight (world coordinates).
     */
    private static final float MAXHEIGHT = 100;
    /**
     * Maximum height the cannonball reaches during the flight with this gridSize (world coordinates).
     */
    private float maxheight = 100;

    /**
     * Destination of the cannonball in world coordinates.
     */
    private Vector2f destination;
    /**
     * Destination of the cannonball as index on the opposite field.
     */
    private Vector2i destinationCell;
    /**
     * Destination grid of the cannonball.
     */
    private int destinationGrid;
    /**
     * Current horizontalVelocity of the cannonball.
     * Decreases faster with higher DRAG.
     */
    private Vector2f horizontalVelocity = new Vector2f();
    /**
     * Current position of the cannonball in the world.
     */
    private Vector3f position = new Vector3f();
    /**
     * How far the cannonball has to travel to reach it's destination.
     */
    private float sidewaysDistance;
    /**
     * The speed at which the cannonball starts (lower for smaller distances)
     */
    private float startSpeed;
    /**
     * Current speed of the cannonball.
     */
    private float currentSpeed;
    /**
     * Parameters of the parabola function, describing the curve the cannonball flies.
     */
    private float a,b,c,x = 0;
    /**
     * GridManager this cannonball belongs to.
     */
    private GridManager gridManager;
    /**
     * {@code true} if this cannonball is currently flying.
     */
    private boolean flying;


    /**
     * Creates a new cannonball entity.
     * @param loader Loader to load model.
     * @param gridManager GridManager this cannonball belongs to.
     */
    public Cannonball(Loader loader, GridManager gridManager) {
        super(loader.loadModelFromOBJ("cannonball", "cannonball.png", 10, 1), new Vector3f(), new Vector3f(), 1);
        this.gridManager = gridManager;
    }

    /**
     * Start the shooting animation of this cannonball.
     * Doesn't work if this cannonball is already flying.
     * If animations are disabled the animation is not played an the {@link #cannonballHit()} function is immediately called.
     * @param destination Destination the ball is heading in world coordinates (x,z).
     * @param origin Origin where the ball starts in world coordinates (x,z)
     * @param destinationCell Index of the cell the ball is heading to.
     * @param destinationField ID of the grid the ball is heading to.
     */
    public void start(Vector2f destination, Vector2f origin, Vector2i destinationCell, int destinationField){
        maxheight = (float)gridManager.getSize() / GridManager.getMAXSIZE() * MAXHEIGHT;
        flying = true;
        this.destination = destination;
        this.destinationCell = destinationCell;
        this.destinationGrid = destinationField;

        if(!gridManager.isAnimation()) {
            cannonballHit();
            return;
        }

        //position of the cannonball that gets updated every frame
        //start by setting position to origin
        position.x = origin.x;
        position.y = -2.5f;
        position.z = origin.y;
        super.setPosition(position);

        //calculate the distance the cannonball has to move horizontally
        sidewaysDistance = Math.abs(origin.x - destination.x) + Math.abs(origin.y - destination.y);
        //calculate how much of the distance the ball moves is in x direction and z direction
        float xPercentage = Math.abs(origin.x - destination.x) / sidewaysDistance;
        float zPercentage = Math.abs(origin.y - destination.y) / sidewaysDistance;

        calculateSpeed();

        //calculate the velocity for the ball in x and z direction depending on launch angle, ball speed
        //and distance to travel in x and z direction
        horizontalVelocity.x = (float)Math.sin(ANGLE) * startSpeed * xPercentage;
        horizontalVelocity.y = (float)Math.sin(ANGLE) * startSpeed * zPercentage;

        //invert velocity if ball has to move in negative x/z direction
        if(origin.x > destination.x)
            horizontalVelocity.x *= -1;
        if(origin.y > destination.y)
            horizontalVelocity.y *= -1;

        //calculate the parameters of a parabola function that describes the flight
        //of the cannonball. This function is used to calculate the y value of the ball
        calculateParabolaFunction();


        Thread t = new Thread(this);
        t.start();
    }
    /**
     * Calculate with how much speed (strength) the cannonball needs to be shot, so it always reaches it's destination in the
     * same time.
     */
    private void calculateSpeed(){
        float averageDistance = gridManager.getScale();
        float shotStrength = sidewaysDistance / averageDistance;
        startSpeed = shotStrength * SPEED;
        currentSpeed = startSpeed;
    }

    /**
     * Calculate a parabola function for the current cannonball depending
     * on the sidewaysDistance to be able to calculate the height of the ball with it.
     * Parabola function: y = Ax^2 + Bx + C
     */
    private void calculateParabolaFunction(){
        float x1,y1,x2,y2,x3,y3;

        //calculate 3 points (x1,y1), (x2,y2) and (x3,y3)
        //that are on the parabola to create a matrix from these points
        //to calculate coefficients abc of the parabola function
        x1 = 0;
        y1 = -2.5f;
        x2 = sidewaysDistance;
        y2 = -2.5f;
        x3 = 0.5f * sidewaysDistance;
        y3 = maxheight;

        //create matrix left side where one row contains the parabola function of one point
        //parabola function: y = ax^2 + bx + c
        //so one row is x^2,x,1 of that point
        double [][] values = {{x1 * x1, x1, 1}, {x2 * x2, x2, 1}, {x3 * x3, x3, 1}};
        RealMatrix linearSystem = new Array2DRowRealMatrix(values);
        //create array containing solution for the 3 parabola functions (the y values)
        //needed to solve matrix
        double[] solutions = {y1, y2, y3};

        //create solver for matrix
        DecompositionSolver solver = new LUDecomposition(linearSystem).getSolver();
        //convert solutions array to vector
        RealVector solution = new ArrayRealVector(solutions);
        //solve matrix and save solution into vector abc
        RealVector abc = solver.solve(solution);

        //write values from vector abc into a b and c
        a = (float)abc.getEntry(0);
        b = (float)abc.getEntry(1);
        c = (float)abc.getEntry(2);
    }

    /**
     * Updates the position of the flying cannonball.
     * Stops as soon as the cannonball has reached its destination and then calls the {@link #cannonballHit()} method.
     */
    public void run(){
        do{
            //update x,z positions of cannonball using horizontalVelocity
            position.x += horizontalVelocity.x * WindowManager.getDeltaTime();
            position.z += horizontalVelocity.y * WindowManager.getDeltaTime();

            //calculate y position of cannonball using parabola function
            x += (Math.abs(horizontalVelocity.x) + Math.abs(horizontalVelocity.y)) * WindowManager.getDeltaTime();
            position.y = a * x * x + b * x + c;

//        System.out.println(position.x + " " + position.y + " " + position.z);
            super.setPosition(position);

            updateVelocities();
            try {
                Thread.sleep((long)(WindowManager.getDeltaTime() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(position.y > -5);
        x = 0;
        this.cannonballHit();
    }

    /**
     * Method that gets called if the cannonball has hit its target.
     * Plays sound depending on what has been hit and places a correct marker.
     */
    private void cannonballHit(){
        boolean shipHit = GameManager.getLogic().shoot(destinationCell.x, destinationCell.y, destinationGrid);
        if(GameManager.getSettings().isOnline())
            return;
        cannonballHit2(shipHit);
    }

    /**
     * Method that executes the effects needed if a cannonball has hit.
     * @param shipHit {@code true} if the cannonball hit a ship, {@code false} else.
     */
    public void cannonballHit2(boolean shipHit){
        if(shipHit && destinationGrid == GridManager.OWNFIELD) {
            gridManager.playFireEffect(destination, destinationCell);
        }
        else {
            gridManager.placeMarker(shipHit, destinationCell, destinationGrid);
        }
        if (shipHit)
            gridManager.playSound(new Vector3f(destination.x, GridManager.getGRIDHEIGHT(), destination.y), CannonSounds.HITSOUND);
        else
            gridManager.playSound(new Vector3f(destination.x, GridManager.getGRIDHEIGHT(), destination.y), CannonSounds.WATERSPLASH);
        flying = false;
        remove();
        GameManager.cannonballHit(shipHit);
    }

    /**
     * Update the velocity values, according to the drag and speed during last update.
     */
    private void updateVelocities(){
        currentSpeed -= WindowManager.getDeltaTime() * DRAG;
        horizontalVelocity.x *=  currentSpeed / startSpeed;
        horizontalVelocity.y *=  currentSpeed / startSpeed;
    }

    /**
     * Removes the cannonball from the scene by moving under the terrain.
     */
    public void remove(){
        super.getPosition().y = -1000;
    }

    /**
     * @return {@code true} if this cannonball is currently flying.
     */
    public boolean isFlying() {
        return flying;
    }
}
