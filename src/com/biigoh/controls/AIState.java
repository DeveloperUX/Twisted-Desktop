package com.biigoh.controls;

import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.gameObjects.weaponry.MachineGunRound;
import com.biigoh.gameObjects.weaponry.Weapon.WeaponState;
import com.biigoh.launch.EntityData;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.MathMan;
import com.biigoh.utils.Vector2Pool;

public class AIState {

	private static final boolean DEBUG = true;
	private static final String LOG = "@ " + AIState.class.getSimpleName();
	
	private Vehicle carToControl;
	
	private Vehicle mTarget;
	private Vector2 lastKnowTargetPosition;
		
	public enum DriveState {
		CRUISE,			EVADE,			CHASE,
		DODGE,			STOP,			FIND_FUEL,
		FIND_ARMOR,		FIND_WEAPON,	FIND_AMMO,
		SCOUT,			BACK_UP,			
	}

	public static final int STEADY = 10;
	public static final int AIM = 11;
	public static final int FIRE = 12;
	
	
//	private static final float VIEW_RANGE = 60;
	
	private DriveState driveState = DriveState.EVADE;
	
	public AIState( Vehicle carToControl ) {
		this.carToControl = carToControl;
	}
	
	/**
	 * Find the next nearest car to chase or evade
	 * @return The closest car in distance to this car
	 */
	public float findClosestCar() {
		
		mTarget = null;
		
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
					mTarget = car;
				}
			}				
		
		
		return distance;
	}
	
		
	private float stuckCounter = 0;
	
	/** Update the AI State, all AI algorithms should go here **/
	public void update() {
		
//		     (0)    
//		      |
//   (1.5) --   -- (-1.5)     
//		      |
//		     (PI)

		if(DEBUG) BattleScreen.debugStr += "AI DState: " + driveState + "\n";
//		if(DEBUG) BattleScreen.debugStr += "AI WState: " + carToControl.getWeapon().getWeaponState() + "\n";
	
		// find the closest car to us in distance
		float distance = findClosestCar();	
		
		if( mTarget == null ) {
			driveState = DriveState.CRUISE;
			carToControl.getWeapon().setWeaponState( WeaponState.STEADY );
			return;			
		}
		else {
			float angleToMe = MathMan.aAngleBetweenPoints( carToControl.getPosition(), mTarget.getPosition() );
			float enemyCarAngle = MathMan.aConvertToUsableAngle( mTarget.getAngle() );
		}
		
		// Get a point 20 meters ahead of us
		float distanceToLookAhead = MathMan.aScaleValue( carToControl.currentSpeed, 0, 200, 5, 10 );
		Vector2 pointAhead = MathMan.aPointFromDirection( carToControl.getPosition(), carToControl.getAngle(), distanceToLookAhead );

		AIRaycastCallback callback = new AIRaycastCallback();		
		BattleScreen.getPhysicsWorld().rayCast( callback, carToControl.getPosition(), pointAhead );
		
		// TODO: Check if we have ammo, if not then evade and search for weapons or ammo

		// add inLineOfSight
		// ORDERING MATTERS!
//		if( MathMan.aRound(carToControl.curLinear.len(), 0) < 2 && MathMan.aRound(carToControl.desiredSpeed, 0) > 0 ) {
//			if(DEBUG) BattleScreen.debugStr += "AI DState: STUCK - " + stuckCounter + "\n";
//			stuckCounter++;
//		} 
//		
//		if( stuckCounter > 34 )
//			stuckCounter = 0;
//
//		if( stuckCounter > 30 )
//			driveState = DriveState.BACK_UP;
		
		
		if( obstacleBlocking( callback ) )
			driveState = DriveState.BACK_UP;
		
		else if( obstacleAhead( callback ) && driveState != DriveState.BACK_UP )
			driveState = DriveState.DODGE;		
		
		// if the car is behind us, Runaway like a little girl SCREAMING! AAAAAAAAAAH!
		else if( isBehindUs(mTarget) ) {
			driveState = DriveState.EVADE;
			carToControl.getWeapon().setWeaponState( WeaponState.STEADY );	// Focus on running away and not on aiming
		}	
				
		else if( refueling() )
			driveState = DriveState.STOP;
		
		// if the car is in our field of view, KILL KILL KIlLL! No just Chase
		else if( inFieldOfView(mTarget) ) {
			
			driveState = DriveState.CHASE;
			
			lockOnTimer = Gdx.graphics.getDeltaTime();
				
			// if anyone happens to get in our line of fire, whether by chance while
			// we were running away or by luck as we chased him, Fire.
			
			if( isLockedOnTarget( mTarget ) )
				carToControl.getWeapon().setWeaponState( WeaponState.LOCKED_ON );
						
			else if( inLineOfFire(mTarget) )
				carToControl.getWeapon().setWeaponState( WeaponState.FIRE );
			
			else 
				carToControl.getWeapon().setWeaponState( WeaponState.AIM );			
		}
		
		else if( lostTarget(mTarget) )
			driveState = DriveState.SCOUT;
		
		else if( scoutingForTarget( mTarget ) )
			driveState = DriveState.SCOUT;
		
		// No one near by? Not even the cops? Time to cruise around
		else {
			driveState = DriveState.CRUISE;
			carToControl.getWeapon().setWeaponState( WeaponState.STEADY );
		}
		
	}
	
	public Vector2 lastPos = new Vector2();
	public float time = 0;
	
	public boolean obstacleBlocking( AIRaycastCallback callback ) {
		
		if( obstacleAhead(callback) )
			time += Gdx.graphics.getDeltaTime();
		else 
			time = 0;
				
		if( time > 2 )
			return true;
					
		return false;
	}
	/**
	 * @return
	 */
	private boolean obstacleAhead( AIRaycastCallback callback ) {

		
//		if(DEBUG) Gdx.app.log( LOG, "CallBack Fixture: " + callback.fixture + " SPEED: " + carToControl.currentSpeed );

		
		if( callback.fixture != null ) {
			EntityData ed = (EntityData) callback.fixture.getBody().getUserData();
						
			if( ed.getType() == EntityData.Type.WALL )
				return true;
		}
			
		return false;
	}

	float lockOnTimer = 0;
	public boolean isLockedOnTarget( Vehicle target ) {
		
		if( lockOnTimer > carToControl.getWeapon().lockOnSpeed )
			return true;
		
		return false;
	}
	
	private boolean lostTarget(Vehicle target) {
		
		if( driveState == DriveState.CHASE ) {
			float distance = carToControl.getPosition().cpy().sub( target.getPosition() ).len();
			if( distance > carToControl.getWeapon().getRange() )
				lastKnowTargetPosition = target.getPosition().cpy();
				return true;
		}
		
		return false;		
	}
	
	// Try to find the target if we have a hunch of where he is.
	private boolean scoutingForTarget(Vehicle target) {
		// Don't scout if we don't have any idea where he last was.
		if( lastKnowTargetPosition == null )
			return false;
		
		// keep searching til we get to 1 feet within where the target's last known position was.
		if( carToControl.getPosition().cpy().sub( lastKnowTargetPosition ).len() > 1 )
			return true;
		
		// If we reached his last known position then we've lost him
		lastKnowTargetPosition = null;
		return false;
	}
	
	private boolean refueling() {
		return false;
	}
	
	private boolean isBehindUs(Vehicle enemy) {
		float angleToMe = MathMan.aAngleBetweenPoints( enemy.getPosition(), carToControl.getPosition() );
		float thisCarAngle = MathMan.aConvertToUsableAngle( carToControl.getAngle() );		
		
		// TODO: Should we know the enemy's selected weapon's range?
		float enemyRange = enemy.getWeapon().getRange();
		float distance = carToControl.getPosition().cpy().sub( enemy.getPosition() ).len();
		
		
		if( Math.abs(thisCarAngle - angleToMe) < MathMan.PI/3 && distance < enemyRange )
			return true;
		
		return false;
	}

	private boolean inFieldOfView(Vehicle target) {	
		// -- Our weapon deteremines our field of view -- //
		// -- So a Sniper has a longer FOV than a Shotgun -- //
		float angleToTarget = MathMan.aAngleBetweenPoints( carToControl.getPosition(), target.getPosition() );
		float thisCarAngle = MathMan.aConvertToUsableAngle( carToControl.getAngle() );
		
		// first check if the other car is in range of our weapon
		float range = carToControl.getWeapon().getRange();		
		float distance = carToControl.getPosition().cpy().sub( target.getPosition() ).len();
		
		if( Math.abs(thisCarAngle - angleToTarget) < MathMan.PI/1.5 && distance < range )
			return true;
		

		lockOnTimer = 0;	// reset lock on
		return false;
	}
	
	private boolean inLineOfFire(Vehicle target) {
		

		// INTERCEPT ALGORITHM
//		Vector2 vR = target.curLinear.cpy().sub( carToControl.curLinear ); 
		Vector2 vR = target.curLinear.cpy().sub( Vector2Pool.obtain(MachineGunRound.speed,0 ) );
		Vector2 sR = target.getPosition().sub( carToControl.getPosition() );
		float tC = sR.len() / vR.len();
		Vector2 sT = target.getPosition().add( target.curLinear.cpy().mul( tC ) );
		
		float angleToTarget = MathMan.aAngleBetweenPoints( carToControl.getPosition(), sT );		
			
		float thisCarAngle = MathMan.aConvertToUsableAngle( carToControl.getAngle() );
		
		// 14 is the View width
		if( Math.abs(thisCarAngle - angleToTarget) < MathMan.PI/16 )
			return true;
		
		return false;
	}

	/** Get the current enemy Vehicle position we are targeting **/
	public Vehicle getTarget() {
		return mTarget;
	}
	
	/**
	 * Get the State of this car, ie what it's doing or should do
	 * @return The state of what we should do
	 */
	public DriveState getDriveState() {		
		return driveState;
	}

	public WeaponState getWeaponState() {
		return carToControl.getWeapon().getWeaponState();
	}
	
}

