package com.biigoh.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.biigoh.gameObjects.GameObject;
import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.gameObjects.weaponry.MachineGunRound;
import com.biigoh.gameObjects.weaponry.Projectile;
import com.biigoh.gameObjects.weaponry.SeekerMissile;
import com.biigoh.gameObjects.weaponry.SniperRound;
import com.biigoh.gameObjects.weaponry.Weapon;
import com.biigoh.launch.EntityData;
import com.biigoh.screens.BattleScreen;
import com.biigoh.tilemap.MapLoader.Wall;
import com.biigoh.utils.ProjectilePool;
import com.biigoh.utils.Consts;

/**
 * <p>
 * This object will hold the Battle's current state, like time left
 * number of points, what type of pickups are present, etc.
 * </p>
 * <p>
 * In charge of keeping track of all movable or interactable game objects
 * and recycling and removing them when needed.
 * </p>
 * @author BiiGo
 *
 */
public class BattleArena {
	
	//--------------
	//  Constants ||
	//--------------
	public static final boolean DEBUG = false;
	public final String LOG = "@ " + BattleArena.class.getSimpleName();
	
	//------------------
	//  Static Fields ||
	//------------------
	private static BattleArena instance;
	
	//------------------
	//  Public Fields ||
	//------------------
	private World world;
	
	// Game Size in meters, Width must be 1.5 times Height
	public float width; // = 12 * 2 * 8; //60;	12 tiles width x 2 meters per tile x 8 tiles per block	
	public float height; // = 8 * 2 * 8;	

	//-------------------
	//  Private Fields ||
	//-------------------
	

	
	// USE SIMPLETILEATLAS
			

	private int playerScore;
	
	//private Array<PowerUps> powerUps;
	
	/** Main List of Game Objects, Every single thing that can move on the screen will be added here, except HUD **/
//	private LinkedHashMap<Integer, GameObject> gameObjects;
	
	// Keep track of all of the projectiles on the screen with Unique ID as the key
	private LinkedHashMap<Integer, Projectile> projectileList;	
	// All Cars on the Arena with the key as their unique ID
	private LinkedHashMap<Integer, Vehicle> carList;
	// All Weapons currently on the Ground or attached to Cars
	private LinkedHashMap<Integer, Weapon> weaponList;
	
	private LinkedHashMap<Integer, Wall> groundList;
	
	private int numEnemies;	
	private int numWeaponsFree;
	// Our player controlled hero
	private Vehicle heroCar;
	private int heroID = 0;
	
	// The game points and stuff like who has weapons
	private int score;
	

	//-----------------
	//  Constructors ||
	//-----------------
	
	public BattleArena() {
		this( Gdx.graphics.getWidth() / Consts.P2M_RATIO, Gdx.graphics.getHeight() / Consts.P2M_RATIO, null );
	}
	
	public BattleArena( float width, float height, World physicsWorld ) {
//		gameObjects = new LinkedHashMap<Integer, GameObject>();
		
		carList = new LinkedHashMap<Integer, Vehicle>();
		projectileList = new LinkedHashMap<Integer, Projectile>();
		weaponList = new LinkedHashMap<Integer, Weapon>();
		
		numEnemies = 0;
		numWeaponsFree = 0;
		
		score = 0;
		
		this.width = width;
		this.height = height;
		
		world = physicsWorld;
		
		instance = this;
	}
	
	public static BattleArena getInstance() {
		return instance;
	}

	//======================
	//  Adders  ||||||||||||
	//======================
	
	public void attachArena( GameObject gameObject ) {
		// Attach the Game Object to this Arena
		gameObject.setArena( this );
	}
	
	public int addVehicle( Vehicle car ) {
				
		carList.put( car.getID(), car );
		
		attachArena( car );
		
		if( car.getID() == Consts.HERO_ID )
			heroCar = car;
		else
			numEnemies++;
		
		return numEnemies;
	}
	
	public void addProjectile( Projectile projectile ) {
		attachArena( projectile );
		projectileList.put( projectile.getID(), projectile );
	}

	public void addWeapon( Weapon weapon ) {
		attachArena( weapon );
		weaponList.put( weapon.getID(), weapon );
	}

	/**
	 * @param mapLoader
	 */
	public void addGround( Wall ground ) {
		attachArena( ground );
		groundList.put( ground.getID(), ground );
	}	

	//=================================================================
	//  Getters  ||||||||||||||||||||||||||||||||||||||||||||||||||||||
	//=================================================================
		
	public Vehicle getHero() {	return heroCar;	}

	/** 
	 * Find and return a generic Game Object that could be of any type.
	 * @param id The unique ID of the Game Object
	 * @return The generic Game Object if found, NULL if it doesn't exist
	 */
	// TODO: Might need to change this so we check for instanceof and return appropriately
	public GameObject getObject( int id ) {
//		return gameObjects.get(id);
		
		if( carList.get( id ) != null )
			return carList.get( id );
		
		else if( projectileList.get( id ) != null ) 
			return projectileList.get( id );
		
		return weaponList.get( id );
	}

	/**
	 * Find and return the {@link Vehicle} with the given unique ID
	 * @param id The unique ID of the Vehicle
	 * @return The Vehicle if found, NULL if not
	 */
	public Vehicle getCar( int id ) {
		// This check may seem redundant since all items in the List
		// are Vehicles, but we'll keep it in case we want to refactor
		// and merge all Object lists into one.
		return carList.get( id );
		
//		if( gameObjects.get(id) instanceof Vehicle )
//			return (Vehicle) gameObjects.get(id);
//		
//		return null;
	}
	
	/**
	 * Return the Projectile with the given ID if it exists, else return Nil, nada, zip
	 * @param id The Object's unique ID
	 * @return A reference to the Projectile
	 */
	public Projectile getProjectile(int id) {
		return projectileList.get( id );
		
//		if( gameObjects.get(id) instanceof Projectile )
//			return (Projectile) gameObjects.get(id);		
//		return null;
	}

	/**
	 * Return the Weapon with the given ID if it exists, else returns Null
	 * @param id The Object's unique ID
	 * @return A reference to the Weapon
	 */
	public Weapon getWeapon(int id) {
		return weaponList.get( id );		
	}
	
	public Wall getGround( int id ) {
		return groundList.get( id );
	}

	public Collection<Projectile> getProjectileList() {
		
		return projectileList.values();

//		ArrayList<Projectile> tProjectiles = new ArrayList<Projectile>();
//		for( GameObject gameObj : gameObjects.values() )
//			if( gameObj instanceof Projectile )
//				tProjectiles.add( (Projectile) gameObj );		
//		return tProjectiles;
	}	
	
	public Collection<Vehicle> getCarList() {
		
		return carList.values();
		
//		ArrayList<Vehicle> tCars = new ArrayList<Vehicle>();
//		for( GameObject gameObj : gameObjects.values() )
//			if( gameObj instanceof Vehicle )
//				tCars.add( (Vehicle) gameObj );		
//		return tCars;
	}
	
	//=====================================================================
	//  Main Class Logic ||||||||||||||||||||||||||||||||||||||||||||||||||
	//=====================================================================
	
	ArrayList<Integer> carsToRemove = new ArrayList<Integer>();
	ArrayList<Integer> projectilesToRemove = new ArrayList<Integer>();
	ArrayList<Integer> weaponsToRemove = new ArrayList<Integer>();
	
	/** Check which Game Objects are outside the map area and move */
	public void update() {	
	    
		if( carList.size() < BattleScreen.ENEMY_NUM )
			BattleScreen.addEnemies(1);
		
		
		// Process Game Objects
		for( Vehicle car : carList.values() )
			processVehicle( car );
		
		for( Projectile projectile : projectileList.values() )
			processProjectile( projectile );
		
	    // Remove Destroyed items
	    for( Vehicle car : carList.values() ) 
	    	
	    	if( car.getState() == EntityData.State.DEAD ) {
	    		if(DEBUG) Gdx.app.log( LOG, "Destroying " + car.name );
	    		// Remove the about to be destroyed Entity from our master list
	    		carsToRemove.add( car.getID() );
	    		// Physically Destroy the Entity's Sprite and Body
	    		car.markedForRemoval = true;;  	    		
	    	}

	    for( Weapon weapon : weaponList.values() ) 
	    	
	    	if( weapon.getState() == EntityData.State.DEAD ) {
	    		if(DEBUG) Gdx.app.log( LOG, "Destroying " + weapon.name );
	    		// Remove the about to be destroyed Entity from our master list
	    		weaponsToRemove.add( weapon.getID() );
	    		// Physically Destroy the Entity's Sprite and Body
	    		weapon.destroy();  	    		
	    	}

	    // Clean up all the GameObjects, so recycle and destroy
	    for( Projectile projectile: projectileList.values() )  { 
	    		    	
	    	if( projectile.getState() == EntityData.State.RECYCLE ) {
	    		
	    		switch( projectile.getType() ) {
	    		
	    		case MACHINE_GUN_ROUND: 			
	    			ProjectilePool.recycleMachineGunRound( (MachineGunRound) projectile );
	    			projectilesToRemove.add( projectile.getID() );
	    			break;
	    			
	    		case SNIPER_ROUND:
	    			ProjectilePool.recycleSniperRound( (SniperRound) projectile );
	    			projectilesToRemove.add( projectile.getID() );
	    			break;
	    			
	    		case SEEKER_MISSILE:
	    			ProjectilePool.recycleSeekerMissile( (SeekerMissile) projectile );
	    			projectilesToRemove.add( projectile.getID() );
	    			break;	    		
	    			
	    		
	    		}
	    		
	    	}
	    		  	    	
	    }
	    
	    carList.keySet().removeAll( carsToRemove );
	    projectileList.keySet().removeAll( projectilesToRemove );
	    weaponList.keySet().removeAll( weaponsToRemove );
	    	
	    carsToRemove.clear();
	    projectilesToRemove.clear();
	    weaponsToRemove.clear();
	}

	public Vehicle getHighestScoreCar() {
		
		int highestScore = 0;
		Vehicle highestScoreCar = null;

		for( Vehicle potentialCar : carList.values() )
			
			if( potentialCar.getScore() > highestScore ) {
				highestScoreCar = potentialCar;			
				highestScore = highestScoreCar.getScore();
			}
			
//		for( GameObject potentialCar : gameObjects.values() )
//			if( potentialCar.getType() == EntityData.Type.VEHICLE_TYPE ){
//				if( ((Vehicle) potentialCar).getScore() > highestScore ) {
//					highestScoreCar = (Vehicle) potentialCar;			
//					highestScore = highestScoreCar.getScore();
//				}
//			}
		
		if(DEBUG) Gdx.app.log( LOG, "getHighestScoreCar() :: Highest Score Car" + highestScoreCar );
		
		return highestScoreCar;			
	}
	
	//-----------------------------------------------------------------
	//  Private Helper Methods ||||||||||||||||||||||||||||||||||||||||
	//-----------------------------------------------------------------
	
	private void processVehicle( Vehicle car ) {    	
    	// remove cars that have no more health left
    	if( car.getArmor() < 0 ) {
    		
    		// We have lost our Hero :(
    		if( car.getID() == Consts.HERO_ID )
    			heroCar = null;
    		
    		// mark this to be removed from the Game World
    		car.setState( EntityData.State.DEAD );
    		
//    		toRemoveSet.add(car.getID());
    		return;	// don't process the rest of the Car stuff, like off screen and such
    	}	
    	
    	// if the car has gone off screen move them back onto the game map
    	if( car.isOutOfArena() != null )
    		
    		switch (car.isOutOfArena()) {
			case RIGHT:
				car.getBody().setTransform( 0, car.getBody().getPosition().y, car.getBody().getAngle() );
				break;
			case LEFT:
				car.getBody().setTransform( width, car.getBody().getPosition().y, car.getBody().getAngle() );
				break;
			case BOTTOM:
				car.getBody().setTransform( car.getBody().getPosition().x, height, car.getBody().getAngle() );
				break;
			case TOP:
				car.getBody().setTransform( car.getBody().getPosition().x, 0, car.getBody().getAngle() );
				break;
			}		
	}
	
	private void processProjectile( Projectile projectile ) {
		if( projectile.isOutOfArena() != null ) {
			projectile.setState( EntityData.State.RECYCLE );			
		}
	}


	//-----------------------------------------------------------------
	//  Anonymous Classes |||||||||||||||||||||||||||||||||||||||||||||
	//-----------------------------------------------------------------



		
	// --------------------
	// add a method to save game state to a file
}
