package com.biigoh.controls;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.gameObjects.weaponry.MachineGunRound;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.MathMan;
import com.biigoh.utils.Physics;
import com.biigoh.utils.Vector2Pool;

public class AIController extends Controller {

	private static final String LOG = "@ " + AIController.class.getSimpleName();
	private static final Boolean DEBUG_AI = false;
		
	private Vehicle mCarToControl;
	private Vehicle mTarget;	// Closest car
	private AIState mState;
	
	/**
	 * Create Computer controlled controls for a Computer player.
	 * @param carToControl The AI Vehicle to this controller will control
	 */
	public AIController( Vehicle carToControl ) {
		mCarToControl = carToControl;
		mState = new AIState( carToControl );
	}
	
	public void setTarget( Vehicle targetCar ) {
		mTarget = targetCar;
	}
	
	// Update the controls in a Computer Controller fashion
	@Override
	public void update() {
		
		mState.update();
		
		switch( mState.getDriveState() ) {
			
			case CRUISE:
				cruise();
				break;
			case CHASE:
				chaseTarget( mState.getTarget() );
				break;
			case EVADE:
				evadeTarget( mState.getTarget() );
				break;
			case STOP:
				stopCar();
				break;	
			case SCOUT:
				searchForLostTarget( mState.getTarget() );
				break;
			case DODGE:
				dodgeObstable();
				break;
			case BACK_UP:
				backUp();
				break;
		}
		
		switch( mCarToControl.getWeapon().getWeaponState() ) {				
			case FIRE:
				fireWeapon();
				break;
			case STEADY:
				stopFiring();
				break;	
			case AIM:
				break;	
			case LOCKED_ON:
				fireAtTarget( mState.getTarget() );
				break;
		}		
	}
	
	public void backUp() {
		joystickAngle += MathMan.radiansToDegrees * 5;
		joystickStrength = -1;
	}
	
	public void dodgeObstable() {
		joystickAngle += MathUtils.degreesToRadians * 10;
		joystickStrength -= 0.1;
	}
	

	// Added Obstacle Avoidance algorithm
	/*
	void ObstacleAvoidance(Vector3 dir, Vector3 steering, Boolean checkObstacles) {
		
		Vector2 trans = mCarToControl.getPosition();
		
		List<Vector3> steeringRays = new ArrayList<Vector3>();
		float _holdTheJump = dir.y;

		Boolean left = false;
		Boolean right = false;
		Boolean front = false;
		Vector3 adjDirection = dir;

		steeringRays.Add(trans.TransformDirection(-steering.x, steering.y, steering.z)); //ray pointed slightly left 
		steeringRays.Add(trans.TransformDirection(steering.x, steering.y, steering.z)); //ray pointed slightly right 
		steeringRays.Add(trans.forward); //ray 1 is pointed straight ahead

		AIRaycastCallback hit;

		if (checkObstacles ) {
			Debug.DrawRay(trans.localPosition, steeringRays[0].normalized * rayLength, Color.cyan);
			Debug.DrawRay(trans.localPosition, steeringRays[1].normalized * rayLength, Color.cyan);
			Debug.DrawRay(trans.localPosition, steeringRays[2].normalized * rayLength, Color.cyan);


			if (Physics.Raycast(trans.position, steeringRays[0], out hit, rayLength)) {
				if (hit.collider.gameObject.layer != 13 && (!front && !left)) {
					isSteering = true;
					front=false; right=false; left=true;
					Debug.DrawLine(trans.position, hit.point, Color.red);
					trans.forward = new Vector3(dir.x,0,dir.z) + (hit.normal).normalized * Time.smoothDeltaTime;
					Debug.Log("Steer Left");
				}
			}
			else          
				if (Physics.Raycast(trans.position, steeringRays[1], out hit, rayLength))
				{
					if (hit.collider.gameObject.layer != 13 && (!front && !left)) //Character layer
					{
						Debug.DrawLine(trans.position, hit.point, Color.red);
						front=false; right=true; left=false;
						isSteering = true;
						trans.forward = new Vector3(dir.x,0,dir.z) + (hit.normal).normalized * Time.smoothDeltaTime;
						Debug.Log("Steer Right");
					}
				}
				else 
				{
					isSteering = false;
					left = false; right = false; front = false;
				}
		}
		//if (isSteering)
		//trans.forward = new Vector3(adjDirection.x, 0, adjDirection.z)  * Time.smoothDeltaTime;
		//Quaternion rot = Quaternion.LookRotation(new Vector3(direction.x, 0, direction.z));
		//trans.rotation = Quaternion.Lerp(trans.rotation, rot, 15f * Time.smoothDeltaTime);
	}
	*/
	

	// Added Obstacle Avoidance algorithm
	/*
	void ObstacleAvoidance(Vector3 dir, Vector3 steering, Boolean checkObstacles) {
		
		Vector2 trans = mCarToControl.getPosition();
		
		List<Vector3> steeringRays = new ArrayList<Vector3>();
		float _holdTheJump = dir.y;

		Boolean left = false;
		Boolean right = false;
		Boolean front = false;
		Vector3 adjDirection = dir;

		steeringRays.Add(trans.TransformDirection(-steering.x, steering.y, steering.z)); //ray pointed slightly left 
		steeringRays.Add(trans.TransformDirection(steering.x, steering.y, steering.z)); //ray pointed slightly right 
		steeringRays.Add(trans.forward); //ray 1 is pointed straight ahead

		AIRaycastCallback hit;

		if (checkObstacles ) {
			Debug.DrawRay(trans.localPosition, steeringRays[0].normalized * rayLength, Color.cyan);
			Debug.DrawRay(trans.localPosition, steeringRays[1].normalized * rayLength, Color.cyan);
			Debug.DrawRay(trans.localPosition, steeringRays[2].normalized * rayLength, Color.cyan);


			if (Physics.Raycast(trans.position, steeringRays[0], out hit, rayLength)) {
				if (hit.collider.gameObject.layer != 13 && (!front && !left)) {
					isSteering = true;
					front=false; right=false; left=true;
					Debug.DrawLine(trans.position, hit.point, Color.red);
					trans.forward = new Vector3(dir.x,0,dir.z) + (hit.normal).normalized * Time.smoothDeltaTime;
					Debug.Log("Steer Left");
				}
			}
			else          
				if (Physics.Raycast(trans.position, steeringRays[1], out hit, rayLength))
				{
					if (hit.collider.gameObject.layer != 13 && (!front && !left)) //Character layer
					{
						Debug.DrawLine(trans.position, hit.point, Color.red);
						front=false; right=true; left=false;
						isSteering = true;
						trans.forward = new Vector3(dir.x,0,dir.z) + (hit.normal).normalized * Time.smoothDeltaTime;
						Debug.Log("Steer Right");
					}
				}
				else 
				{
					isSteering = false;
					left = false; right = false; front = false;
				}
		}
		//if (isSteering)
		//trans.forward = new Vector3(adjDirection.x, 0, adjDirection.z)  * Time.smoothDeltaTime;
		//Quaternion rot = Quaternion.LookRotation(new Vector3(direction.x, 0, direction.z));
		//trans.rotation = Quaternion.Lerp(trans.rotation, rot, 15f * Time.smoothDeltaTime);
	}
	*/
	
	public void searchForLostTarget( Vehicle carToFind ) {
		chaseTarget(carToFind);
	}
	
	public void fireWeapon() {
		fireButtonState = FIRE_BUTTON_DOWN;
	}
	
	public void fireAtTarget( Vehicle target ) {
		mCarToControl.getWeapon().fireAtLockedOnTarget( target );
	}
	
	public void stopFiring() {
		fireButtonState = FIRE_BUTTON_UP;
	}
	
	
	public void stopCar() {
		joystickAngle = 0f;
		joystickStrength = 0;
		
	}
	
	private float timer = 0;
	
	public void cruise() {		
		joystickAngle = mCarToControl.getAngle();
		joystickStrength = 0.3f;	// cruise slowly
	}
	
	public void evadeTarget( Vehicle carToEvade ) {
		
		// TODO: Instead of passing around Vehicle objects, change it to BTransform (Position & Angle)
		
		// get the car to evade's position for checking distance between the 2 cars
		
		// Set our desired angle to be exactly the opposite direction 
		// that the other car is coming at us from.
		joystickAngle = MathUtils.atan2( carToEvade.getPosition().y - mCarToControl.getPosition().y, 
				carToEvade.getPosition().x - mCarToControl.getPosition().x );
		
		joystickStrength = 1f; // FULL_THROTTLE;
		
		// if the Car to Evade is too close (20 meters) turn left or right hard and randomly
		if( mCarToControl.getPosition().sub( carToEvade.getPosition() ).len() < 20 ) {
			if(DEBUG_AI) Gdx.app.log( LOG, "evadeTarget() :: Target Car is too close" );
			joystickAngle += MathUtils.degreesToRadians * MathUtils.random( -170, 170 ); 	// Random, just turn 90 degrees right for now
		}
		
	}
	
	float prediction = 0;
	float maxPrediction = 0.1f;
	
	public void chaseTarget_Diff( Vehicle carToChase ) {
		// 1. Calculate the target to delegate to seek 19
		
		// Work out the distance to target
		Vector2 direction = carToChase.getPosition().cpy().sub( mCarToControl.getPosition() );
		float distance = direction.len();
		
		// Work out our current speed
		float speed = mCarToControl.curLinear.len();
		
		// Check if speed is too small to give a reasonable
		// prediction time
		if (speed <= distance / maxPrediction)
			prediction = maxPrediction;
		
		// Otherwise calculate the prediction time
		else
			prediction = distance / speed;
		
		// Put the target together
//		Seek.target = explicitTarget
//		Seek.target.position += carToChase.curLinear.cpy().mul( prediction );
	}
	
	/**
	 * Set this AI Car to chase the given Car
	 * @param carToChase The car that this AI should chase
	 */
	public void chaseTarget( Vehicle carToChase ) {

		// Set our desired angle to the same as the other car's expected future
		// position that way we intercept the Target 
		// ALSO: remember to invert the angle between the two points cause the Joystick works that way!
		
		// INTERCEPT ALGORITHM
		
		Vector2 vR = carToChase.curLinear.cpy().sub( mCarToControl.curLinear );
//		Vector2 vR = carToChase.curLinear.cpy().sub( Vector2Pool.obtain(MachineGunRound.speed,0) );
		// Work out the distance to target
		Vector2 sR = carToChase.getPosition().sub( mCarToControl.getPosition() );
		float tC = sR.len() / vR.len();
		Vector2 sT = carToChase.getPosition().add( carToChase.curLinear.cpy().mul( tC ) );
		
		
//		joystickAngle = -MathMan.aAngleBetweenPoints( mCarToControl.getPosition(), carToChase.getPosition() );
		joystickAngle = -MathMan.aAngleBetweenPoints( mCarToControl.getPosition(), sT );
		
		// All of this is really not needed but just to over complicate things we're 
		// going to vary our speed depending on how far we are from our Target :)
		float distance = MathMan.aDistanceBetweenPoints( mCarToControl.getPosition(), carToChase.getPosition() );
		float throttle = distance / 60;
		
		// if we are way to far (60 meters) give it full throttle
		if( distance > 40 )
			throttle = 1;
		
		// if we are way to close (20 meters) turn left or right so we don't crash 
		if( distance < 10 ) 
			joystickAngle += MathUtils.degreesToRadians * 90; 
				
		joystickStrength = throttle; // FULL_THROTTLE;
		
		
		if(DEBUG_AI) Gdx.app.log( LOG, "chaseTarget() :: Joystick Angle: " + joystickAngle * MathUtils.radiansToDegrees);
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
	}
	
}
