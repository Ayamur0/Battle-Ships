package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.window.WindowManager;
import org.apache.commons.math3.linear.*;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

/**
 * Special {@link Entity} that is used to indicate shots made during game.
 *
 * @author Tim Staudenmaier
 */

public class Cannonball{

    private static final float SPEED = 180.0f;
    private static final float DRAG = 0.1f;
    private static final float ANGLE = (float)Math.toRadians(75);
    private static final float MAXHEIGHT = 100;

    /**
     * The Entity containing position, rotation,... for this cannonball.
     */
    private Entity ball;
    /**
     * Destination of the cannonball in world coordinates.
     */
    private Vector2f destination;
    /**
     * Destination of the cannonball as index on the opposite field.
     */
    private Vector2i destinationCell;
    /**
     * Destination field the cannonball.
     */
    private int destinationField;
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
     * Creates a new Cannonball Entity, that flies from the origin to the
     * destination in a parabola.
     * @param ball - the entity of this cannonball
     * @param destination - destination where the cannonball should land
     * @param origin - where the cannonball is fired from
     */
    public Cannonball(Entity ball, Vector2f destination, Vector2f origin, Vector2i destinationCell, int destinationField) {
        this.ball = ball;
        this.destination = destination;
        this.destinationCell = destinationCell;
        this.destinationField = destinationField;

        //position of the cannonball that gets updated every frame
        //start by setting position to origin
        position.x = origin.x;
        position.y = -2.5f;
        position.z = origin.y;
        ball.setPosition(position);

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
    }

    /**
     * Calculate with how much speed (strength) the cannonball needs to be shot, so it always reaches it's destination in the
     * same time.
     */
    private void calculateSpeed(){
        float averageDistance = PlayingField.getScale();
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
        y3 = MAXHEIGHT;

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
     * Update the position of the flying cannonball.
     * Needs to be called every frame while the cannonball is flying.
     * @return {@code true} if cannonball has reached it's destination and hit the playingField
     */
    public boolean update(){
        //update x,z positions of cannonball using horizontalVelocity
        position.x += horizontalVelocity.x * WindowManager.getDeltaTime();
        position.z += horizontalVelocity.y * WindowManager.getDeltaTime();

        //calculate y position of cannonball using parabola function
        x += (Math.abs(horizontalVelocity.x) + Math.abs(horizontalVelocity.y)) * WindowManager.getDeltaTime();
        position.y = a*x*x + b*x + c;

//        System.out.println(position.x + " " + position.y + " " + position.z);
        ball.setPosition(position);

        updateVelocities();

        //if ball is below this y value it has hit a ship or water
        if(position.y < -5) {
            return true;
        }
        return false;
    }

    /**
     * Update the velocity values, according to the drag and speed during last update.
     */
    public void updateVelocities(){
        currentSpeed -= WindowManager.getDeltaTime() * DRAG;
        horizontalVelocity.x *=  currentSpeed / startSpeed;
        horizontalVelocity.y *=  currentSpeed / startSpeed;
    }

    /**
     *
     * @return - The index of the cell the cannonball is flying towards.
     */
    public Vector2f getDestination() {
        return destination;
    }

    /**
     *
     * @return - Render the cannonball if one exists.
     */
    public void render(MasterRenderer renderer) {
        if(ball != null)
            renderer.processEntity(ball);
    }

    /**
     *
     * @return - The index of the destination cell.
     */
    public Vector2i getDestinationCell() {
        return destinationCell;
    }

    /**
     *
     * @return - The field the destination is on.
     */
    public int getDestinationField() {
        return destinationField;
    }
}
