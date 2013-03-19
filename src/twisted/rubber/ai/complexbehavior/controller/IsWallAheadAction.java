package twisted.rubber.ai.complexbehavior.controller;

import com.badlogic.gdx.math.Vector2;
import com.biigoh.controls.AIRaycastCallback;
import com.biigoh.launch.EntityData;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.MathMan;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.LeafAction;

public class IsWallAheadAction extends LeafAction {


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
		float distanceToLookAhead = MathMan.aScaleValue( bb.carToControl.currentSpeed, 0, 200, 40, 60 );
		// Get a position vector from that distance
		Vector2 pointAhead = MathMan.aPointFromDirection( bb.carToControl.getPosition(), bb.carToControl.getAngle(), distanceToLookAhead );
		// Forward Raycast callback
		AIRaycastCallback forwardRayCallback = new AIRaycastCallback();
		// Left Raycast callback
		AIRaycastCallback leftRayCallback = new AIRaycastCallback();
		// Right Raycast callback
		AIRaycastCallback rightRayCallback = new AIRaycastCallback();
		
		BattleScreen.getPhysicsWorld().rayCast( forwardRayCallback, bb.carToControl.getPosition(), pointAhead );
		BattleScreen.getPhysicsWorld().rayCast( leftRayCallback, bb.carToControl.getPosition(), pointAhead );
		BattleScreen.getPhysicsWorld().rayCast( rightRayCallback, bb.carToControl.getPosition(), pointAhead );
		
		boolean isForwardRayHit = obstacleAhead(forwardRayCallback);
		boolean isLeftRayHit = obstacleAhead(leftRayCallback);
		boolean isRightRayHit = obstacleAhead(rightRayCallback);
		
		if( isForwardRayHit || isLeftRayHit || isRightRayHit )
			GetControl().FinishWithSuccess();
		else
			GetControl().FinishWithFailure();
	}


	private boolean obstacleAhead( AIRaycastCallback callback ) {
		
//		if(DEBUG) Gdx.app.log( LOG, "CallBack Fixture: " + callback.fixture + " SPEED: " + carToControl.currentSpeed );
		
		if( callback.fixture != null ) {
			EntityData ed = (EntityData) callback.fixture.getBody().getUserData();
						
			if( ed.getType() == EntityData.Type.WALL )
				return true;
		}
			
		return false;
	}

}
