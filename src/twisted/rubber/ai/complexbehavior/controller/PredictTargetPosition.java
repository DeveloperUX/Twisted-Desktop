package twisted.rubber.ai.complexbehavior.controller;

import java.awt.Event;
import java.util.Random;

import twisted.rubber.ai.complexbehavior.library.Behavior;
import twisted.rubber.ai.complexbehavior.library.Blackboard;

import com.badlogic.gdx.math.Vector2;
import com.biigoh.gameObjects.weaponry.MachineGunRound;
import com.biigoh.utils.Vector2Pool;

public class PredictTargetPosition extends Behavior {
	
	Random rand;
	int moveTo;

	public PredictTargetPosition(Blackboard blackboard) {
		super(blackboard);
		rand = new Random();
	}
	
	public PredictTargetPosition(Blackboard blackboard, String name) {
		super(blackboard, name);
		rand = new Random();	
	}

	@Override
	public boolean checkPreConditions() {
		return true;
	}

	@Override
	public void Start() {
//		moveTo = rand.nextInt(40);
	}

	@Override
	public void End() {
		
	}

	@Override
	public void DoAction() {	
		LogTask("");
		DebugAction();
		try {
			// Set our desired angle to the same as the other car's expected future position, that way we intercept the Target		
	//		Vector2 vR = bb.closestEnemy.curLinear.cpy().sub( bb.carToControl.curLinear );
			Vector2 vR = bb.closestEnemy.curLinear.cpy().sub( Vector2Pool.obtain(MachineGunRound.speed,0) );
			// Work out the distance to target
			Vector2 sR = bb.closestEnemy.getPosition().sub( bb.carToControl.getPosition() );
			float tC = sR.len() / vR.len();
			Vector2 sT = bb.closestEnemy.getPosition().add( bb.closestEnemy.curLinear.cpy().mul( tC ) );
			
			bb.targetLocation = sT;
			GetControl().FinishWithSuccess();
			
		} catch( Exception e ) {
			LogTask("Failed");
			GetControl().FinishWithFailure();
		}
	}
	

}
