package com.biigoh.gameObjects.vehicles;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.biigoh.controls.Controller;
import com.biigoh.controls.SwitchButton;
import com.biigoh.gameObjects.GameObject;
import com.biigoh.gameObjects.MovingObject;
import com.biigoh.gameObjects.weaponry.MachineGun;
import com.biigoh.gameObjects.weaponry.MachineGunRound;
import com.biigoh.gameObjects.weaponry.Sniper;
import com.biigoh.gameObjects.weaponry.SniperRound;
import com.biigoh.gameObjects.weaponry.Stinger;
import com.biigoh.gameObjects.weaponry.Weapon;
import com.biigoh.launch.EntityData;
import com.biigoh.sound.VehicleSound;
import com.biigoh.utils.Consts;
import com.biigoh.utils.MathMan;
import com.biigoh.utils.Physics;
import com.biigoh.utils.Vector2Pool;

/**
 * @author BiiGo Games
 *
 */
public class Vehicle extends MovingObject {

	private static final String LOG = "@ " + Vehicle.class.getSimpleName();
	private final boolean DEBUG_MOVEMENT = false;
	private final boolean DEBUG = false;
	private final boolean DEBUG_WEAPON = false;
	private final boolean DEBUG_SOUND = false;

	protected float meterWidth;
	protected float meterHeight;
	
	public float desiredSpeed;

	// Will hold the list of weapons the user has, can be as large as the total
	// number of weapons that exist in the game
	private LinkedList<Weapon> weaponsDepo = new LinkedList<Weapon>();
//	private int numWeapons = 0;

	// Use the differnce between the system times to keep track of button timeouts
	private float timeSinceLastFired = 0;	
	private float timeSinceLastSwitch = 0;	
	
	private Weapon curWeapon;

	// TODO: Fix Max Speed so that the car can actually reach it, fix in LinearDamping

	// Default Car Specs
	public int MAX_SPEED;	// Max Speed in Miles Per Hour!
	protected int ACCELERATION;	// Value between 10 and 40
	protected float HANDLING; // the lower the value the worse the handling

	protected int armor;	// health
//	protected int fieldOfViewRange = 40;
	
	private int score = 0;

	// These control this Vehicle, whether AI or Human
	private Controller controls;

	// Have a taste of Component Design sucka!!
	private VehicleSound soundComponent;
	
	// Sound //	
	public boolean drifting = false;
	public boolean stopped = false;
	public boolean breaking = false;
	public boolean movingForward = false;
	public boolean movingBackward = false;
	


	//=======================
	//  Constructors		|
	//=======================
	
	/**
	 * Create a new Car object at the given WORLD coordinates, in meters.
	 * @param texture The {@link Texture} image of the car
	 * @param posX World X coordinates of where to put the car, in Meters
	 * @param posY World Y coordinates of where to put the car, in Meters
	 * @param angle The new Car's initial angle or direction it should face
	 */
	public Vehicle( String name, Texture texture, float posX, float posY, float angle ) {

		super(name);	// Init a new Actor with a unique ID
		
		mSprite = new Sprite( texture );
		//mSprite.setSize( width * Consts.PIXEL_METER_RATIO, height * Consts.PIXEL_METER_RATIO );	 // setting the size rather than scaling
		mSprite.setOrigin( mSprite.getWidth()/2, mSprite.getHeight()/2);	// set the origin to be at the center of the sprite

		FixtureDef carFixtureDef = new FixtureDef();

		// Set the Fixture's properties, like friction, using the car's shape
		carFixtureDef.density = 1f;	// needed to rotate body using applyTorque
		carFixtureDef.friction = 1f;	// I believe this doesn't do anything til we add a Ground Body
		carFixtureDef.restitution = 0.6f;	// bouncyness


		mBody = Physics.createBoxBody( BodyType.DynamicBody, carFixtureDef, mSprite );
		mBody.setTransform( posX, posY, angle );

		mBody.setLinearDamping(0.4f);
		mBody.setAngularDamping(0.1f);
		
//		MassData md = mBody.getMassData();
//		md.mass = mBody.getMass() * 0.1f;
//		mBody.setMassData(md);

		//		setUniqueID( Consts.getUniqueID() );
		mBody.setUserData( new EntityData( getID(), getID(), EntityData.Type.VEHICLE ));


		// initialize the default Machine Gun
		Weapon defaultWeapon = new MachineGun( this );
		// Add the default Machine Gun to the list of weapons the user has
		weaponsDepo.add( defaultWeapon );
		curWeapon = weaponsDepo.pop();
		
		// initialize the Sound Component to take charge of updating sound effects
		soundComponent = new VehicleSound(this);
	}
	
	@Override
	public void setID( int pID ) {
		super.setID(pID);		
		((EntityData) mBody.getUserData()).setID( pID );
		((EntityData) mBody.getUserData()).setOwnerID( pID );		
//		for( int i=0; i < numWeapons; i++ )
//			weaponsDepo[i].setOwner( pID );
		for( Weapon weapon : weaponsDepo ) 
			weapon.setOwner( pID );
	}

	//======================
	//  Class Methods  
	//======================

	public Controller getController() {
		return controls;
	}
	
	public void setController( Controller controls ) {
		this.controls = controls;
	}

	public int getScore() { return score; }
	public void addToScore( int points ) { score += points; }

	public int getArmor() { return armor; }
	public void setArmor( int armor ) { this.armor = armor; }


	
	public void addWeapon( Weapon weapon ) {
		weaponsDepo.add( weapon );
	}

	/** Cycles to the next available weapon */
	public void switchWeapon( float pSecondsElapsed ) {

		if( timeSinceLastSwitch < 0.3f )
			return;
		
		// Cycle to the next weapon, make sure we loop
		if( curWeapon.getAmmoCount() > 0 || curWeapon.getType() == EntityData.Type.MACHINEGUN )
			weaponsDepo.add( curWeapon );
		
		curWeapon = weaponsDepo.pop();
		
		timeSinceLastSwitch = 0;
	}

	/**
	 * Make the player's vehicle fire the currently selected weapon
	 * in the direction the player is currently facing.
	 */
	public void fireWeapon( float pSecondsElapsed ) {	

//		if( weaponsDepo[ curWeapon ] == null )
//			return;
		
		if( curWeapon == null )
			return;
			

		// Check that the weapon has cooled down enough to fire again
//		if( timeSinceLastFired >  weaponsDepo.get(curWeapon).getRateOfFire() ) {
		if( timeSinceLastFired >  curWeapon.getRateOfFire() ) {

			// Fire the currently selected weapon
//			weaponsDepo.get( curWeapon ).fireNoLock();
			curWeapon.fireNoLock();
			timeSinceLastFired = 0;	//reset the timer			

			soundComponent.playShotSound();

		}

	}

	//======================
	//  Inherited Methods  
	//======================

	boolean alreadyHaveWeapon = false;
	
	public boolean markedForRemoval = false;
	int counter = 0;
	
	@Override
	public void act( float delta ) {
		super.act(delta);

		if( markedForRemoval ) {
			counter++;
			if( counter == 3 )
				destroy();
		}
		// Have we picked up any weapons to add to our Depo?
		// Make sure we don't already have this weapon
		alreadyHaveWeapon = false;
		if( weaponTypeToAdd != null ) {
			
			for( Weapon weapon : weaponsDepo )
				if( weapon.getType() == weaponTypeToAdd )
					alreadyHaveWeapon = true;
				
			if( !alreadyHaveWeapon ) {
				
				switch( weaponTypeToAdd ) {
				case SNIPER:
					weaponsDepo.add( new Sniper( this ) );
					break;
				case MACHINEGUN:
					weaponsDepo.add( new MachineGun( this ) );
					break;
				case STINGER:
					weaponsDepo.add( new Stinger( this ) );
					break;
				}	
				
				switchWeapon( delta );
			}
			
		}
		
		weaponTypeToAdd = null;
			
		
		// TODO: Use a Component based game design
		//		ControllerComponent.updateVehicle();
		//		PhysicsComponent.updateVehicle( this, controlX, controlY );
		//		GraphicsComponent.updateVehicle( this, controlX, controlY );

		timeSinceLastFired += delta;
		timeSinceLastSwitch += delta;
		
		// ----------------- Update Controls ------------------//
		controls.update();

		// ----------------- Update Weapons ------------------//
		// This method runs on each frame
		if( controls.fireButtonState == Controller.FIRE_BUTTON_DOWN )							
			fireWeapon( delta );	// ... FIRE!		

		if( controls.switchButtonState == Controller.SWITCH_BUTTON_DOWN )				
			switchWeapon( delta );	// Cycle to the next weapon		
		
		// Make sure to check that if a weapon has no ammo so that we remove it from the list!
//		if( weaponsDepo.get( curWeapon ) != null && weaponsDepo.get( curWeapon ).getAmmoCount() <= 0 ) {
		if( curWeapon != null && curWeapon.getAmmoCount() <= 0 ) {
			switchWeapon( delta );
		}
		
		move( controls.joystickStrength, controls.joystickAngle );

		// ----------------- Update Sound ------------------//
		
		
		if( movingForward )
			soundComponent.playDriveSound = true;
		else 
			soundComponent.playDriveSound = false;

		if( breaking || drifting ) {
			soundComponent.playDriftSound = true;
			soundComponent.playDriveSound = false;
		}
		else
			soundComponent.playDriftSound = false;
		
		soundComponent.update( this );	// update the sound for this car
		
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		// update the sprite's position and rotation
		mSprite.setPosition( mBody.getPosition().x * Consts.P2M_RATIO - mSprite.getWidth()/2, 
				mBody.getPosition().y * Consts.P2M_RATIO - mSprite.getHeight()/2 );

		//mSprite.setPosition( mBody.getPosition().x * Consts.PIXEL_METER_RATIO, mBody.getPosition().y * Consts.PIXEL_METER_RATIO );
		mSprite.setRotation( MathUtils.radiansToDegrees * mBody.getAngle() );
		// draw the sprite
		mSprite.draw( batch );		
	}


	/**
	 * Set the Vehicle to drive towards the given angle with 
	 * the speed given by the strength of the JoyStick.
	 * @param strength How far the Joystick is pulled.  The farther the faster.
	 * @param angle At what angle the Joystick points to turn the car.
	 */
	public void move( float strength, float angle ) {
		// ----------------- Update Movement ------------------//
		// Update the car's movement depending on the input of the joystick
		float turnAngle = updateTurn(controls.joystickAngle);
		
		if( turnAngle > 2.9f || turnAngle < -2.9f )
			updateDrive(-controls.joystickStrength);
		else
			updateDrive(controls.joystickStrength);
		
		updateFriction();
	}
	
	//======================
	//  Getters / Setters
	//======================


	/** Get the currently toggled weapon **/
	public Weapon getWeapon() {
//		return weaponsDepo.get( curWeapon );
		return curWeapon;
	}
	
//	public int getFieldOfViewRange() {	return fieldOfViewRange;	}
	//--------------------------------------------
	//  Private Helper Methods For Car Movement
	//--------------------------------------------

	/**
	 * Turn the car depending on the angle the input controller is facing.
	 * This will turn the car until it faces the same direction as the Controller.
	 * @param radControlAngle The current angle the controller makes
	 */
	private float updateTurn( float radControlAngle ) {

		// don't turn if controller is stationary, since that would make the car turn to face North
		if( MathMan.aRound(radControlAngle, 2) == 0 )
			return 0;
		
		final Body carBody = mBody;

		// flip the control Angle because the controller works counterclockwise
		radControlAngle = -radControlAngle;	
		// get the difference between where the controller is pointing
		// and the car's current angle it is facing
		float deltaAngle = radControlAngle - mBody.getAngle();	
		
		deltaAngle = MathMan.aConvertToUsableAngle(deltaAngle);
				
		// scale down the torque to match our body, this way the car spins the right amount
		float torque = deltaAngle * MAX_TORQUE; // 10 IS TURN POWER, OR STEERING, OR WHATEVER YOU CALL IT - HANDLING?
		
		if( deltaAngle > 2.9f || deltaAngle < -2.9f )
			torque = -torque * 0.4f;	// scale down the turn power
						
		carBody.applyTorque( torque );	// Turn baby TURN!
		
		return deltaAngle;		
	}


	public float currentSpeed;
	
	private float updateDrive( float controlStrength ) {	

		desiredSpeed = controlStrength * MAX_SPEED;	// desired speed should always be larger than possible speed, consider it Max Speed		
		float m_maxDriveForce = ACCELERATION;	// How much force we apply to the car if we want to speed up, Acceleration
		
		if( controlStrength < 0 )
			m_maxDriveForce = ACCELERATION / 2;
		
		
		//find current speed in forward direction
		Vector2 currentForwardNormal = mBody.getWorldVector( Vector2Pool.obtain(0,1) ).cpy();
		currentSpeed = currentForwardNormal.dot( getForwardVelocity() );

		float driveForce = 0;		
		
		if ( desiredSpeed > currentSpeed ) 		
			driveForce = m_maxDriveForce;		
		
		else if ( desiredSpeed < currentSpeed )
			driveForce = -m_maxDriveForce; 	// Braking Power
		
		Vector2 forceVector = currentForwardNormal.mul( driveForce );		
		mBody.applyForce( forceVector, mBody.getWorldCenter() );

		
		/* SOUND */	
		if( currentSpeed > 1 && driveForce < 0 ||  currentSpeed < -1 && driveForce > 0 )
			breaking = true;
		else 
			breaking = false;
		
		if( currentSpeed > 1 ) {
			movingForward = true;
			movingBackward = false;
			stopped = false;
		}
		
		else if( currentSpeed < -1 ) {
			movingBackward = true;
			movingForward = false;
			stopped = false;
		}
		
		else {
			movingBackward = false;
			movingForward = false;		
			stopped = true;		 
		}
		
		return driveForce;
		
	}
	
	private void updateFriction() {
		Vector2 negLateralVelocity = getLateralVelocity().mul(-1);		
		Vector2 impulse = negLateralVelocity.mul( mBody.getMass() );
		
		// Allow the car to skid, or drift for you youngsters
		// the lower the value the greater the drift
		if ( impulse.len() > HANDLING ) {
			impulse.mul( HANDLING / impulse.len() );			
			drifting = true;
		}
		else 
			drifting = false;
		

		curImpulse = impulse.cpy();	// DEBUGGING
		
		// Cancel out the Lateral Velocity, keeping the car from skidding
		mBody.applyLinearImpulse( impulse, mBody.getWorldCenter());
		mBody.applyAngularImpulse( 0.1f * mBody.getInertia() * -mBody.getAngularVelocity() );

		// Update Drag force
		Vector2 currentForwardVelocity = getForwardVelocity();
		float currentForwardSpeed = currentForwardVelocity.len();
		float dragForceMagnitude = impulse.len() * -0.03f * currentForwardSpeed;
		// TODO: Apply a greater drag for harder turns
		mBody.applyForce( currentForwardVelocity.mul(dragForceMagnitude), mBody.getWorldCenter() );
	}
	
	/**
	 * Detucts the given damage from the Car's health
	 * @param damage The amount of damage to take.
	 * @return The new Health for chaining, or convenience
	 */
	public int gotHit(int damage) {
		armor -= damage;
		return armor;
	}

	
	EntityData.Type weaponTypeToAdd;
	/* (non-Javadoc)
	 * @see com.biigoh.gameObjects.MovingObject#collideWidth(com.biigoh.gameObjects.GameObject)
	 */
	@Override
	public void collideWith(GameObject gameObj) {}
	
	@Override
	public void collideWith( EntityData.Type type ) {
		switch( type ) {
		// Pick up a Weapon
		case MACHINEGUN:			
		case SNIPER:			
		case STINGER:
			weaponTypeToAdd = type;
			break;
			
		// Collision with Projectiles
		case MACHINE_GUN_ROUND:
			gotHit( MachineGunRound.damage );
			break;
			
		case SNIPER_ROUND:
			gotHit( SniperRound.damage );
			break;
			
		case SHRAPNEL:
			gotHit( 5 );
			break;
			
		// Interaction with the Ground
		case GROUND_HEAL:
			armor += 1;		
		}
	}

	
}
