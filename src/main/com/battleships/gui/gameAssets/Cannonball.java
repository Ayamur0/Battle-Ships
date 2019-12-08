package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.window.WindowManager;
import org.apache.commons.math3.linear.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Cannonball{

    private static final float SPEED = 100.0f;
    private static final float ANGLE = (float)Math.toRadians(75);
    private static final float MAXHEIGHT = 100;

    private Entity ball;
    private Vector2f destination;
    private Vector2f horizontalVelocity = new Vector2f();
    private Vector3f position = new Vector3f();
    private float sidewaysDistance;
    private float a,b,c,x = 0;

    private int debug;

    /**
     * Creates a new Cannonball Entity, that flies from the origin to the
     * destination in a parabola.
     * @param ball - the entity of this cannonball
     * @param destination - destination where the cannonball should land
     * @param origin - where the cannonball is fired from
     */
    public Cannonball(Entity ball, Vector2f destination, Vector2f origin) {
        this.ball = ball;
        this.destination = destination;

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

        //calculate the velocity for the ball in x and z direction depending on launch angle, ball speed
        //and distance to travel in x and z direction
        horizontalVelocity.x = (float)Math.sin(ANGLE) * SPEED * xPercentage;
        horizontalVelocity.y = (float)Math.sin(ANGLE) * SPEED * zPercentage;

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
        debug++;

//        System.out.println(position.x + " " + position.y + " " + position.z);
        ball.setPosition(position);

        //if ball is below this y value it has hit a ship or water
        //TODO remove debug value
        //debug is needed when cannonball is there before game start, because deltaTime is too long then
        if(debug < 10){
            x = 0;
            position.x = 650;
            position.z = -450;
            position.y = -2.5f;
        }
        if(position.y < -5) {
            return true;
        }
        return false;
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
}
