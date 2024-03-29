package twisted.rubber.ai.complexbehavior.controller;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.Behavior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.biigoh.gameObjects.weaponry.MachineGunRound;
import com.biigoh.utils.MathMan;
import com.biigoh.utils.Vector2Pool;

/**
 * Attempt to drive towards a set position,
 * specified in the {@link Blackboard} object
 */
public class MoveInOppositeDirection extends Behavior {

	public MoveInOppositeDirection(Blackboard blackboard) {
		super(blackboard);
		// TODO Auto-generated constructor stub
	}

	public MoveInOppositeDirection(Blackboard blackboard, String name) {
		super(blackboard, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkPreConditions() {
		return true;
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void End() {
		// TODO Auto-generated method stub

	}

	@Override
	public void DoAction() {
		LogTask("");
		DebugAction();

		// TODO: Instead of passing around Vehicle objects, change it to BTransform (Position & Angle)
		
		// get the car to evade's position for checking distance between the 2 cars
		
		// Set our desired angle to be exactly the opposite direction 
		// that the other car is coming at us from.
		bb.carToControl.getController().joystickAngle = MathUtils.atan2( 
				bb.targetLocation.y - bb.carToControl.getPosition().y, 
				bb.targetLocation.x - bb.carToControl.getPosition().x );
		
		bb.carToControl.getController().joystickStrength = 1f; // FULL_THROTTLE;
		
//		// if the Car to Evade is too close (20 meters) turn left or right hard and randomly
//		if( bb.carToControl.getPosition().sub( bb.targetLocation ).len() < 20 ) {
//			joystickAngle += MathUtils.degreesToRadians * MathUtils.random( -170, 170 ); 	// Random, just turn 90 degrees right for now
//		}
				
		GetControl().FinishWithSuccess();
	}
	
	/*
	@Override
	public void DoAction() {
		DebugAction();
		// TODO Auto-generated method stub
		// Set our desired angle to the same as the other car's expected future
		// position that way we intercept the Target 
		// ALSO: remember to invert the angle between the two points cause the Joystick works that way!
		
		// INTERCEPT ALGORITHM
//				Vector2 vR = bb.closestEnemy.curLinear.cpy().sub( bb.carToControl.curLinear );
		Vector2 vR = bb.closestEnemy.curLinear.cpy().sub( Vector2Pool.obtain(MachineGunRound.speed,0) );
		Vector2 sR = bb.closestEnemy.getPosition().sub( bb.carToControl.getPosition() );
		float tC = sR.len() / vR.len();
		Vector2 sT = bb.closestEnemy.getPosition().add( bb.closestEnemy.curLinear.cpy().mul( tC ) );
		
		
//				bb.getAiControls().joystickAngle = -MathMan.aAngleBetweenPoints( bb.carToControl.getPosition(), bb.closestEnemy.getPosition() );
		bb.getAiControls().joystickAngle = -MathMan.aAngleBetweenPoints( bb.carToControl.getPosition(), sT );
		
		// All of this is really not needed but just to over complicate things we're 
		// going to vary our speed depending on how far we are from our Target :)
		float distance = MathMan.aDistanceBetweenPoints( bb.carToControl.getPosition(), bb.closestEnemy.getPosition() );
		float throttle = distance / 60;
		
		// if we are way to far (60 meters) give it full throttle
		if( distance > 40 )
			throttle = 1;
						
		bb.getAiControls().joystickStrength = throttle; // FULL_THROTTLE;

		// if we are way to close (20 meters) turn left or right so we don't crash 
		if( distance < 10 )
			GetControl().FinishWithSuccess();
//			bb.getAiControls().joystickAngle += MathUtils.degreesToRadians * 90; 
		
		if(DEBUG) LogTask("Joystick Angle: " + bb.getAiControls().joystickAngle * MathUtils.radiansToDegrees);
		
		
	}
	*/

}
