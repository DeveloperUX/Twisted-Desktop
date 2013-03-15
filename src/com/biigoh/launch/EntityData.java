package com.biigoh.launch;

/**
 * Holds data for an object so that we can check collision on two bodies.
 * Also has some static object type fields
 */
public class EntityData {
	
	private int ownerID = -1; 	// ID of Vehicle that owns this Body
	private Type objectType;	// type of this body, whether bullet or vehicle
	private int uniqueID = -1;	// This object's universal and unique id
	private State state = State.ALIVE;
	
	// --------------------------
	//  User Data Constructor
	// --------------------------
	
	/**
	 * Create a simple object to be passed around during collision and 
	 * is attached to a body so we can get some information about the Body during collision
	 * @param uniqueID The unique and universal ID of this Object.
	 * @param ownerID The ID of the Vehicle that owns this Object or that created it
	 * @param bodyType The type of object this is, whether BULLET or VEHICLE
	 */
	public EntityData( int uniqueID, int ownerID, Type bodyType ) {
		this.ownerID = ownerID;
		this.objectType = bodyType;
		this.uniqueID = uniqueID;
	}

	// ---------------------
	//  Getters / Setters ||
	// ---------------------
	
	public Type getType() {	return objectType;	}
	public State getState() {	return state;	}	
	public int getID() {	return uniqueID;	}
	public int getOwnerID() {	return ownerID;	}

	public void setType( Type type ) {	type = type;	}
	public void setState( State objectState ) {		state = objectState;	}
	public void setID( int id ) {	uniqueID = id;	}
	public void setOwnerID( int id ) {	ownerID = id;	}

	// ---------------------
	//  Physics Body States
	// ---------------------
	public enum State {
		ALIVE,
		RECYCLE,
		DEAD
	}
	// ---------------------------------------------
	//  Static Projectile Types ||||||||||||||||||||
	// ---------------------------------------------	
	public enum Type {
		// The Projectile type objects
		MACHINE_GUN_ROUND,		SHRAPNEL,
		MISSILE,				FIRE,
		SEEKER_MISSILE,			SNIPER_ROUND,	
//		PROJECTILE_TYPE,
		// The Vehicle type objects
		VEHICLE,			ROADSTER,
		MUSTANG,			TRUCK,
		// The different types of Ground tiles
		WALL, 			MACHINEGUN, 
		GROUND_HEAL, 		SNIPER,
		
		BOSS1, STINGER
	}
	
//	public static final int BULLET_TYPE = 0;
//	public static final int MISSILE_TYPE = 1;
//	public static final int INTERCEPTOR_TYPE = 2;
//	public static final int SHRAPNEL_TYPE = 3;
//	public static final int FIRE_TYPE = 4;
//	public static final int PROJECTILE_TYPE = 5;
//
//	// ----------------------
//	//  Static Vehicle Types
//	// ----------------------
//	public static final int VEHICLE_TYPE = 10;
//	public static final int MUSTANG_TYPE = 11;
//	public static final int ROADSTER_TYPE = 12;
//	public static final int TRUCK_TYPE = 13;
//	
//	// ---------------------
//	// Static Ground Types
//	// ---------------------
//	public static final int GROUND = 20;
	
}


