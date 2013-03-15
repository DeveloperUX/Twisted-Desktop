package twisted.rubber.ai;


import java.util.Vector;

import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.screens.BattleScreen;

/**
 * Data class for the player AI in the game.
 * Has static members for shared data for all players,
 * and normal members for player-specific data.
 *  
 * @author Ying
 *
 */
public class Blackboard 
{
	/**
	 * Reference to the vector of players in the game
	 */
	public static Vector<Vehicle> players;
	
	/**
	 * Reference to the game map
	 */
	public static BattleScreen map;
	
	/**
	 * Closest enemy cursor
	 */
//	public Cursor closestEnemyCursor;
	
	/**
	 * Direction vector to move in
	 */
//	public Vector2 moveDirection;
	
	/**
	 * Destination point to arrive at
	 */
//	public Vec2 destination;
	
	/**
	 * Path of positions to move to
	 */
//	public Vector<Tile> path;
	
	/**
	 * Reference to the owner player
	 */
	public Vehicle player;
	
	/**
	 * Creates a new instance of the Blackboard class
	 */
	public Blackboard()
	{
//		this.moveDirection = new Vec2();
//		this.destination = new Vec2();
//		this.path = new Vector<Tile>();
	}
}
