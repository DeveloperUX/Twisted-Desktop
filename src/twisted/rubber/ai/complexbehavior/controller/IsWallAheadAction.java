package twisted.rubber.ai.complexbehavior.controller;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.Blackboard.Rays;
import twisted.rubber.ai.complexbehavior.library.Behavior;

import com.badlogic.gdx.math.Vector2;
import com.biigoh.controls.AIRaycastCallback;
import com.biigoh.launch.EntityData;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.MathMan;

public class IsWallAheadAction extends Behavior {


	public IsWallAheadAction(Blackboard blackboard, String name) {
		super(blackboard, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean CheckConditions() {
		// TODO Auto-generated method stub
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
		LogTask("Doing Action");
		DebugAction();
		// Get the distance to look ahead of us depending on how fast we're moving
		float distanceToLookAhead = MathMan.aScaleValue( bb.carToControl.currentSpeed, 0, 80, 10, 50 );
		// Get a position vector from that distance
		Vector2 pointAhead = MathMan.aPointFromDirection( bb.carToControl.getPosition(), bb.carToControl.getAngle(), distanceToLookAhead );
		// A left and right wing vector 
		Vector2 rayLeft = MathMan.aPointFromDirection( bb.carToControl.getPosition(), bb.carToControl.getAngle() + (8/distanceToLookAhead), distanceToLookAhead * 0.5f );
		Vector2 rayRight = MathMan.aPointFromDirection( bb.carToControl.getPosition(), bb.carToControl.getAngle() - (8/distanceToLookAhead), distanceToLookAhead * 0.5f );
		
		// Forward Raycast callback
		AIRaycastCallback forwardRayCallback = new AIRaycastCallback();
		// Left Raycast callback
		AIRaycastCallback leftRayCallback = new AIRaycastCallback();
		// Right Raycast callback
		AIRaycastCallback rightRayCallback = new AIRaycastCallback();
		
		// Create and run raycasts
		BattleScreen.getPhysicsWorld().rayCast( forwardRayCallback, bb.carToControl.getPosition(), pointAhead );
		BattleScreen.getPhysicsWorld().rayCast( leftRayCallback, bb.carToControl.getPosition(), rayLeft );
		BattleScreen.getPhysicsWorld().rayCast( rightRayCallback, bb.carToControl.getPosition(), rayRight );
		// Check which raycasts were hit
		bb.forwardRayHit = obstacleAhead(forwardRayCallback);
		bb.leftRayHit = obstacleAhead(leftRayCallback);
		bb.rightRayHit = obstacleAhead(rightRayCallback);
		
		// log which raycast was hit
//		if( isForwardRayHit )
//			bb.rayhit = Rays.FORWARD;
//		else if( isLeftRayHit )
//			bb.rayhit = Rays.LEFT;
//		else if( isRightRayHit )
//			bb.rayhit = Rays.RIGHT;
		
		// If any raycast was hit finish with success
		if( bb.forwardRayHit || bb.leftRayHit || bb.rightRayHit )
			GetControl().FinishWithSuccess();		
		else
			GetControl().FinishWithFailure();
	}

	private boolean obstacleAhead( AIRaycastCallback callback ) {		
		if( callback.fixture != null ) {
			EntityData ed = (EntityData) callback.fixture.getBody().getUserData();						
			if( ed.getType() == EntityData.Type.WALL )
				return true;
		}			
		return false;
	}

}
