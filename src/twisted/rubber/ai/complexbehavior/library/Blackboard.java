package twisted.rubber.ai.complexbehavior.library;


import java.util.Collection;

import com.badlogic.gdx.math.Vector2;
import com.biigoh.controls.Controller;
import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.view.BattleArena;

/**
 * Data class for the player AI in the game.
 * Has static members for shared data for all players,
 * and normal members for player-specific data.
 *  
 * @author Moose
 *
 */
public class Blackboard {
	
	/** Reference to the vector of players in the game */
	public static Collection<Vehicle> cars;
	
	/** Reference to all game objects in the Arena */
	public static BattleArena arena;
	
	/** Closest enemy vehicle */
	public Vehicle closestEnemy;

	/** Enemy vehicle we have considered a target */
	public Vehicle targetEnemy;
	
	/** Position to move towards */
	public Vector2 moveLocation;
	
	/** Destination point to arrive at */
//	public Vec2 destination;
	
	/** Path of positions to move to */
//	public Vector<Tile> path;
	
	public static enum Rays {
		LEFT,
		RIGHT,
		FORWARD
	}
	
	public Rays rayhit;

	public boolean forwardRayHit;
	public boolean leftRayHit;
	public boolean rightRayHit;
	

	public int vehicleId;	
	public Vehicle carToControl;

	public Vector2 targetLocation;
	
	/**
	 * Create a new shared Blackboard for storing and sharing information
	 * @param aiCarToControl The AI Car that will be in charge of this Blackboard
	 */
	public Blackboard(Vehicle aiCarToControl) {
		carToControl = aiCarToControl;
		vehicleId = carToControl.getID();
		
		arena = BattleArena.getInstance();
		cars = arena.getCarList();
		// TODO :: Take this out
		closestEnemy = arena.getHero();
//		moveLocation = closestEnemy.getPosition();
		forwardRayHit = false;
		leftRayHit = false;
		rightRayHit = false;
	}
	
	// TODO Take this out, not used
	public Controller getAiControls() {
		return carToControl.getController();
	}

	/**
	 * Find the next nearest car to chase or evade
	 * @return The closest car in distance to this car
	 */
	public float findClosestEnemy() {
		
//		closestEnemy = null;
		
		float closestDistance = Float.MAX_VALUE;
		float distance = 0;
		
		// All the cars in the Arena
		Collection<Vehicle> carList = carToControl.getArena().getCarList();

		// either we find a new Target or keep the previous target if
		// no new targets were found, or if no target is closer
		for( Vehicle car : carList ) 
			// make sure we're not checking ourselves
			if( car.getID() != carToControl.getID() )	{ 
				// get the distance between the two cars
				distance = carToControl.getPosition().cpy().sub( car.getPosition() ).len();
				// if closer, set the car as the new target
				if( distance < closestDistance ) {
					closestDistance = distance;
					closestEnemy = car;
				}
			}				
		
		
		return distance;
	}
	
//	public void setAiController( Controller aiControls ) {
//		controls = aiControls;
//		if( carToControl.getController() == null )
//			carToControl.setController( aiControls );
//	}
}
