package twisted.rubber.ai.complexbehavior.controller;

import com.badlogic.gdx.math.Vector2;
import com.biigoh.controls.AIRaycastCallback;
import com.biigoh.launch.EntityData;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.MathMan;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.Behavior;

public class IsEnemyClose extends Behavior {


	public IsEnemyClose(Blackboard blackboard, String name) {
		super(blackboard, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkPreConditions() {
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
		float distanceToLookAhead = MathMan.aScaleValue( bb.carToControl.currentSpeed, 0, 100, 10, 15 );
		// Get a position vector from that distance
		Vector2 pointAhead = MathMan.aPointFromDirection( bb.carToControl.getPosition(), bb.carToControl.getAngle(), distanceToLookAhead );
		// Forward Raycast callback
		AIRaycastCallback forwardRayCallback = new AIRaycastCallback();
		
		BattleScreen.getPhysicsWorld().rayCast( forwardRayCallback, bb.carToControl.getPosition(), pointAhead );
		
		boolean isForwardRayHit = obstacleAhead(forwardRayCallback);
		
		if( isForwardRayHit )
			GetControl().FinishWithSuccess();
		else
			GetControl().FinishWithFailure();
	}


	private boolean obstacleAhead( AIRaycastCallback callback ) {		
//		if(DEBUG) Gdx.app.log( LOG, "CallBack Fixture: " + callback.fixture + " SPEED: " + carToControl.currentSpeed );
		// Is there something ahead of us?
		if( callback.fixture != null ) {
			// What's in front?
			EntityData ed = (EntityData) callback.fixture.getBody().getUserData();
			// Is the object ahead of us a car?
			if( ed.getType() == EntityData.Type.VEHICLE )
				return true;
		}
		// Obstacle in front of us is not a car
		return false;
	}

}
