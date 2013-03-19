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
	/**
	 * Reference to the vector of players in the game
	 */
	public static Collection<Vehicle> cars;
	
	/**
	 * Reference to all game objects in the Arena
	 */
	public static BattleArena arena;
	
	/**
	 * Closest enemy vehicle
	 */
	public Vehicle closestEnemy;
	
	/**
	 * Position to move towards
	 */
	public Vector2 moveLocation;
	
	/**
	 * Destination point to arrive at
	 */
//	public Vec2 destination;
	
	/**
	 * Path of positions to move to
	 */
//	public Vector<Tile> path;
	
	public static enum Rays {
		LEFT,
		RIGHT,
		FORWARD
	}
	
	public Rays rayhit;
	
	

	public int vehicleId;	
	public Vehicle carToControl;
	
	/**
	 * Creates a new instance of the Blackboard class
	 */
	public Blackboard(Vehicle aiCarToControl) {
		carToControl = aiCarToControl;
		vehicleId = carToControl.getID();
		
		arena = BattleArena.getInstance();
		cars = arena.getCarList();
		// TODO :: Take this out
		closestEnemy = arena.getHero();
//		moveLocation = closestEnemy.getPosition();
	}
	
	public Controller getAiControls() {
		return carToControl.getController();
	}
	
//	public void setAiController( Controller aiControls ) {
//		controls = aiControls;
//		if( carToControl.getController() == null )
//			carToControl.setController( aiControls );
//	}
}
