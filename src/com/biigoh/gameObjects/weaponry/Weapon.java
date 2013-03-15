package com.biigoh.gameObjects.weaponry;

import com.badlogic.gdx.Gdx;
import com.biigoh.gameObjects.GameObject;
import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.launch.EntityData;

public abstract class Weapon extends GameObject {

	public static String LOG = Weapon.class.getSimpleName();
	
	protected Vehicle carAttachedTo;
	// The weapons rate of fire per second when user holds down fire button
	protected float mRateOfFire;
	protected int mAmmoCount;	// Number of bullets left
	protected int range;	// no need to use a float for the range of this weapon
	
	public float lockOnSpeed;
	
	private Vehicle lockedOnTarget;
	
	// For every arrow there's an archer
	protected int ownerID;


	public WeaponState weaponState = WeaponState.STEADY;
	public enum WeaponState {
		STEADY,
		AIM,
		LOCKED_ON,
		FIRE
	}
		
	protected Weapon() {
		super( "Weapon" );
	}
	
	protected Weapon( String name, Vehicle carThatOwnsWeapon ) {
		super( name );
		carAttachedTo = carThatOwnsWeapon;
		
		if( carThatOwnsWeapon != null )
			ownerID = carAttachedTo.getID();
		else
			ownerID = getID();
	}
	
	public void setWeaponState( WeaponState state ) {		weaponState = state;	}
	public WeaponState getWeaponState() {		return weaponState;		}

	public void setTransform( float posX, float posY, float angle ) {
		mBody.setTransform( posX, posY, angle );
	}
	
	public void attachTo( Vehicle car ) {
		carAttachedTo = car;
	}
	/**
	 * How fast the weapon can shoot before having to cool down.
	 * @return The weapon's rate of fire
	 */
	public float getRateOfFire() {
		return mRateOfFire;
	}

	/**
	 * How much ammo is left in the weapon
	 * @return The number of projectiles available
	 */
	public int getAmmoCount() {
		return mAmmoCount;
	}
	
	/**
	 * Add ammo to the weapon, like if a car runs over an ammo box
	 * @param ammoCount The ammount of ammo to add to the weapon
	 */
	public void addAmmo( int ammoCount ) {
		mAmmoCount += ammoCount;
	}
	
	/** Call when a projectile has been fired */
	public void decrementAmmo() {
		if( mAmmoCount == 0 )
			Gdx.app.log( LOG, "Oops, no more ammo but we're decrementing" );
		
		mAmmoCount--;
	}
	
	/**
	 * Set the weapon's new rate of fire
	 * @param rate The new rate of fire, number of seconds in between shots
	 */
	public void setRateOfFire( float rate ) {
		mRateOfFire = rate;
	}

	/** How far this weapon can shoot in meters */
	public int getRange() {
		return range;
	}
	
	/**
	 * Who owns this weapon?
	 * @return The owner of this weapon, or who it's attached to
	 */
	public int getOwnerID() {
		return ownerID;
	}
	
	/**
	 * Set the owner of this weapon or who its attached to
	 * @param ownerID The id of the Vehicle that owns this weapon
	 */
	public void setOwner( int ownerID ) {
		this.ownerID = ownerID;
	}

	/**
	 * Fire this Weapon at the direction the car is facing. Movement calculations are done by the {@link Projectile}s.
	 * @param position The position of the Vehicle or Object that fired this weapon so that we can find where
	 * to create the bullet.
	 * @param angle The rotation of the Vehicle or the direction which the Bullet should head in.
	 * @param width The width of the scaled sprite to calculate starting position of Bullet, at the car's head
	 * @param height The height of the scaled sprite to calculate starting position of Bullet, at the car's head
	 * @return Whether or not firing was successful
	 */
//	public abstract boolean fire( Vector2 position, float angle, float width, float height );
	public abstract boolean fireNoLock();
	public abstract boolean fireAtLockedOnTarget( Vehicle target );
	
	public boolean lockedOnATarget() {
		return lockedOnTarget != null ? true : false; 
	}
	
	public void lockOnTarget( Vehicle targetToLockOn ) {
//		lockOnTimer += Gdx.graphics.getDeltaTime();	
//		if( lockOnTimer > lockOnSpeed )
			
	}
	
	public void update(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	}
	/*
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	*/

	/* (non-Javadoc)
	 * @see com.biigoh.gameObjects.GameObject#collideWidth(com.biigoh.gameObjects.GameObject)
	 */
	@Override
	public void collideWith(GameObject gameObj) {
		// TODO Auto-generated method stub
		setState( EntityData.State.DEAD );
	}

	@Override
	public void collideWith( EntityData.Type type ) {
		// TODO Auto-generated method stub
		if( type == EntityData.Type.MUSTANG ||
				type == EntityData.Type.ROADSTER ||
					type == EntityData.Type.TRUCK ||
						type == EntityData.Type.BOSS1 ||
							type == EntityData.Type.VEHICLE	)
			setState( EntityData.State.DEAD );
	}
	
}

