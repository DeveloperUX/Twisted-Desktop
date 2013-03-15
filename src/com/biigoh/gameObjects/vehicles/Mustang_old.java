package com.biigoh.gameObjects.vehicles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.biigoh.controls.Controller;
import com.biigoh.controls.Joystick;
import com.biigoh.gameObjects.GameObject;
import com.biigoh.gameObjects.weaponry.MachineGun;
import com.biigoh.gameObjects.weaponry.Weapon;
import com.biigoh.utils.Consts;
import com.biigoh.utils.Physics;
import com.biigoh.utils.Vector2Pool;

public class Mustang extends GameObject {	

	private static final String LOG = "@ " + Mustang.class.getSimpleName();
	private final boolean DEBUG = true;
	
	// In case of multiplayer each Player will have his own Joystick
	private Joystick mJoystick;
	
	// Will hold the list of weapons the user has, can be as large as the total
	// number of weapons that exist in the game
	private Weapon[] weaponsDepo = new Weapon[3];
	private float curWeaponRateOfFire; 
	private int numWeapons;

	// Use the differnce between the system times to keep track of button timeouts
	private float lastUpdateTime;
	private float timeSinceLastFired = 0;	
	private float lastTimeSwitched = 0;

	private float deltaTime;

	private final int WEAPON_MACHINEGUN = 0;
	private final int WEAPON_POWER_MISSILE = 1;
	private final int WEAPON_SEEKER_MISSILE = 2;
	private final int WEAPON_ROCKET = 3;

	private int curWeapon = WEAPON_MACHINEGUN;

	//private int controllerState;

	// Car Specs
	protected static int MAX_SPEED = 3;
	protected static int ACCELERATION;
	protected static int ARMOR;
	protected static int HANDLING;
	
	// Car width and height in meters
	private final float WIDTH = 1.8f;
	private final float HEIGHT = 3.5f;	
	
	private float x = 0;
	private float y = 0;

	//======================
	//  Constructors
	//======================
	
	/**
	 * Create a new Car object at the given WORLD coordinates, in meters.
	 * @param texture The {@link Texture} image of the car
	 * @param posX World X coordinates of where to put the car, in Meters
	 * @param posY World Y coordinates of where to put the car, in Meters
	 * @param world The Physics world that will be doing calculations on all objects
	 */
	public Mustang( Texture texture, float posX, float posY ) {

		super( "Mustang" );	

		mSprite = new Sprite( texture );
		mSprite.setSize( WIDTH * Consts.PIXEL_METER_RATIO, HEIGHT * Consts.PIXEL_METER_RATIO );
		//mSprite.setSize( WIDTH, HEIGHT );

		mSprite.setOrigin( mSprite.getWidth()/2, mSprite.getHeight()/2);	// set the origin to be at the center of the body
		//mSprite.setOrigin( 0, 0);	// set the origin to be at the center of the body
		//mSprite.setPosition( posX * Consts.PIXEL_METER_RATIO, posY * Consts.PIXEL_METER_RATIO );	// place the car in the center of the game map

		FixtureDef carFixtureDef = new FixtureDef();

		// Set the Fixture's properties, like friction, using the car's shape
		carFixtureDef.density = 1.5f;	// needed to rotate body using applyTorque		
		//carFixtureDef.filter.groupIndex = -1;

		mBody = Physics.createBoxBody( BodyType.DynamicBody, carFixtureDef, mSprite );
		
		mBody.setTransform( posX, posY, 0 );

		// initialize the default Machine Gun
		Weapon defaultWeapon = new MachineGun();
		// Add the default Machine Gun to the list of weapons the user has
		weaponsDepo[curWeapon] = defaultWeapon;
		
		if(DEBUG) Gdx.app.log( LOG, "Constructor() :: Sprite Size = " + mSprite.getWidth() + " x " + mSprite.getHeight() );
		
		if(DEBUG) Gdx.app.log( LOG, "Constructor() :: Sprite Position = [" + mSprite.getX() + ":" + mSprite.getY() + "]" );
		if(DEBUG) Gdx.app.log( LOG, "Constructor() :: Body Position = " + mBody.getPosition() );
		
		
	}


	//======================
	//  Class Methods  
	//======================
	
	public void old_move( float dirX, float dirY ) {

		final Body carBody = mBody;

		final float rotationInRad = (float) Math.atan2( -dirX, dirY );

		// Make sure car does not rotate because controller is at rest, zero
		if( rotationInRad != 0 ) {

			// The car's speed depends on how much the user pushes the control
			final Vector2 velocity = new Vector2( dirX * 10, dirY * 10 );
			carBody.setLinearVelocity( velocity );			

			carBody.setTransform( carBody.getWorldCenter(), rotationInRad );	// rotate the car

			Gdx.app.log( "@ Car", "Rotate Car to: (RAD)" + rotationInRad );
			Gdx.app.log( "@ Car", "Move Car in direction: " + velocity );
		}
	}

	/**
	 * Set the Vehicle to drive towards the given angle with 
	 * the speed given by the strength of the JoyStick.
	 * @param strength How far the Joystick is pulled.  The farther the faster.
	 * @param angle At what angle the Joystick points to turn the car.
	 */
	public void move( float strength, float angle ) {

		updateFriction();
		updateDrive( strength );
		updateTurn( angle );
		
	}
	
	//================ START COPY ===========================================


	/** Cycles to the next available weapon */
	public void switchWeapon() {

		// Cycle to the next weapon, make sure we loop
		if( curWeapon >= numWeapons - 1 )
			curWeapon = WEAPON_MACHINEGUN;
		else
			curWeapon++;

		if(DEBUG) Gdx.app.log( LOG, "switchWeapon():: Weapon switched to: " + curWeapon );
	}
	
	/**
	 * Make the player's vehicle fire the currently selected weapon
	 * in the direction the player is currently facing.
	 */
	public void fireWeapon( float pSecondsElapsed ) {	

		// Check that the weapon has cooled down enough to fire again
		if( timeSinceLastFired >  weaponsDepo[ curWeapon ].getRateOfFire() ) {

			// Fire the currently selected weapon
			weaponsDepo[ curWeapon ].fire( mBody.getPosition(), mBody.getAngle(), mSprite.getWidth(), mSprite.getHeight());

			timeSinceLastFired = 0;	//reset the timer
			
			if(DEBUG) Gdx.app.log( "@"+getClass().getSimpleName(), "fireWeapon():: Weapon Fired" );

		}

	}
	//================== END COPY ============================================

	//======================
	//  Inherited Methods  
	//======================
	
	@Override
	public void act( float delta ) {
		
		
		/**
		 * Make sure to check that if a weapon has no ammo so that we remove it from the list!
		 */

		deltaTime += delta;
		timeSinceLastFired += delta;

		//printInfo(); 	// Debug Information

		// This method runs on each frame
		if( Controller.fireButtonState == Controller.FIRE_BUTTON_DOWN ) { 
			if(DEBUG) Gdx.app.log( "@ Car", "act() :: FireButton Down" );
			fireWeapon( delta );	// ... FIRE!
		}

		if( Controller.fireButtonState == Controller.SWITCH_BUTTON_DOWN ){
			if(DEBUG) Gdx.app.log( "@ Car", "act() :: SwitchButton Down" );
			switchWeapon();	// Cycle to the next weapon
		}	


		//		ControllerComponent.updateVehicle();
		//		PhysicsComponent.updateVehicle( this, controlX, controlY );
		//		GraphicsComponent.updateVehicle( this, controlX, controlY );
		
		// Update the car's movement depending on the input of the joystick
		move( Controller.joystickStrength, Controller.joystickAngle );//, deltaTime );
		
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// update the sprite's position and rotation
		mSprite.setPosition( mBody.getPosition().x * Consts.PIXEL_METER_RATIO - mSprite.getWidth()/2, 
				mBody.getPosition().y * Consts.PIXEL_METER_RATIO - mSprite.getHeight()/2 );
		
		//mSprite.setPosition( mBody.getPosition().x * Consts.PIXEL_METER_RATIO, mBody.getPosition().y * Consts.PIXEL_METER_RATIO );
		mSprite.setRotation( MathUtils.radiansToDegrees * mBody.getAngle() );
		// draw the sprite
		mSprite.draw( batch );
		
		//if(DEBUG) Gdx.app.log( LOG, "Drawing Sprite at Position: " + mSprite.getX() + ":" + mSprite.getY() );
	}

	@Override
	public Actor hit(float x, float y) {
		// what happens when this sprite is hit by another
		Gdx.app.log( "@ Car", "\n\n\n\n\n\n\n\n hit() :: Car was hit! at: " + new Vector2(x,y) + "\n\n\n\n\n\n\n\n");
		return null;
	}
	

	//======================
	//  Getters / Setters
	//======================

	/*
	public Vector2 getPosition() {
		return new Vector2( mSprite.getX() + mSprite.getOriginX(), mSprite.getY() + mSprite.getOriginY() );
	}
	*/
	//--------------------------------------------
	//  Private Helper Methods For Car Movement
	//--------------------------------------------

	/**
	 * Turn the car depending on the angle the input controller is facing.
	 * This will turn the car until it faces the same direction as the Controller.
	 * @param radControlAngle The current angle the controller makes
	 */
	private void updateTurn( float radControlAngle ) {

		final Body carBody = mBody;
		
		// flip the control Angle because the controller works counterclockwise
		radControlAngle = -radControlAngle;	
		// get the difference between where the controller is pointing
		// and the car's current angle it is facing
		float deltaAngle = radControlAngle - mBody.getAngle();	
		
		// correct the angle between the control and current angle of the car
		// since the physics body stores the angle by increments so we could have
		// an angle that is 300 because it the turned say 10 times.  Here we fix that
		// so we only get the smallest possible angle between control and car.
		while(deltaAngle <= Math.PI) deltaAngle += 2 * Math.PI;		
		while(deltaAngle > Math.PI) deltaAngle -= 2 * Math.PI;	// yes you read that correctly.. that's a while loop
		// scale down the torque to match our body, this way the car spins the right amount
		float torque = deltaAngle * 40; // 10 IS TURN POWER, OR STEERING, OR WHATEVER YOU CALL IT - HANDLING?	
		
		//if(DEBUG) Gdx.app.log( LOG, "updateTurn():: torque = " + torque );
		//Gdx.app.log( "@ Vehicle", "updateTurn :: Delta Angle = ControlAngle - CarAngle: " + 
			//R(deltaAngle,1) + " = " + R(radControlAngle,1) + " - " + R(carAngle,1) );

		// don't turn if controller is stationary, since that would make the car turn to face North
		if( Physics.round(radControlAngle,2) != 0 )		
			carBody.applyTorque( torque );	// Turn baby TURN!
	}
	
	private Vector2 getLateralVelocity() {
//		Vector2 currentRightNormal = mBody.getWorldVector( new Vector2(1,0) ); // needs recycling
//		float dotProduct = currentRightNormal.dot( mBody.getLinearVelocity() );
//		currentRightNormal = new Vector2( dotProduct * currentRightNormal.x, dotProduct * currentRightNormal.y);
//		return currentRightNormal;
		
		Vector2 currentRightNormal = mBody.getWorldVector( Vector2Pool.obtain(1,0) );
		float dotProduct = currentRightNormal.dot( mBody.getLinearVelocity() );
		return currentRightNormal.mul( dotProduct );
	}

	private void updateFriction() {
		Vector2 negLateralVelocity = getLateralVelocity().mul(-1);//new Vector2( -getLateralVelocity().x, -getLateralVelocity().y );
		Vector2 impulse = negLateralVelocity.mul( mBody.getMass() );
		mBody.applyLinearImpulse( impulse, mBody.getWorldCenter());
		mBody.applyAngularImpulse( 0.1f * mBody.getInertia() * -mBody.getAngularVelocity() );
	}

	private Vector2 getForwardVelocity() {
		Vector2 currentForwardNormal = mBody.getWorldVector( new Vector2(0,1) ); 
		float dotProduct = currentForwardNormal.dot( mBody.getLinearVelocity() );
		return new Vector2( dotProduct * currentForwardNormal.x, dotProduct * currentForwardNormal.y );
	}

	private void updateDrive( float controlStrength ) {		
		float desiredSpeed = controlStrength * MAX_SPEED;
		float m_maxDriveForce = 10;	// max speed
		//find current speed in forward direction
		Vector2 currentForwardNormal = mBody.getWorldVector( new Vector2(0,1) );
		float currentSpeed = currentForwardNormal.dot( getForwardVelocity() );

		
		
		//apply necessary force
		desiredSpeed = Physics.round( desiredSpeed, 1 );
		currentSpeed = Physics.round( currentSpeed, 1 );
		
		if(DEBUG) Gdx.app.log( LOG, "updateDrive() :: Current Speed = " + currentSpeed );
		if(DEBUG) Gdx.app.log( LOG, "updateDrive() :: Desired Speed = " + desiredSpeed );
		
		float force = 0;		
		if ( desiredSpeed > currentSpeed )
			force = m_maxDriveForce;
		else if ( desiredSpeed < currentSpeed )
			force = -m_maxDriveForce;
		else
			force = 0;
		
		if(DEBUG) Gdx.app.log( LOG, "updateDrive() :: Force Applied = " + force );
		
		Vector2 forceVector = new Vector2( force * currentForwardNormal.x, force * currentForwardNormal.y );		
		mBody.applyForceToCenter( forceVector );//( tempVector, mBody.getWorldCenter() );
		
		/* ACCELERAION *
		float velChange = desiredVel - vel.x;
	    float force = body->GetMass() * velChange / (1/60.0); //f = mv/t
	    body->ApplyForce( b2Vec2(force,0), body->GetWorldCenter() );
	    */
	}

}
