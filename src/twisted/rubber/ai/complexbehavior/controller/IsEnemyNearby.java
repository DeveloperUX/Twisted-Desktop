package twisted.rubber.ai.complexbehavior.controller;

import java.awt.Event;
import java.util.Collection;

import twisted.rubber.ai.complexbehavior.library.Behavior;
import twisted.rubber.ai.complexbehavior.library.Blackboard;

import com.biigoh.gameObjects.vehicles.Vehicle;

public class IsEnemyNearby extends Behavior {
	
	private int RANGE = 20;

	public IsEnemyNearby(Blackboard blackboard) {
		super(blackboard);
	}
	
	public IsEnemyNearby(Blackboard blackboard, String name) {
		super(blackboard, name);	
	}

	@Override
	public boolean checkPreConditions() {
		return true;
	}

	@Override
	public void Start() {
//		currentLocation = 0;
	}

	@Override
	public void End() {
//		currentLocation = 0;
	}

	@Override
	public void DoAction() {
		LogTask("");
		DebugAction();
		
		float distance = bb.carToControl.getPosition().cpy().sub( bb.closestEnemy.getPosition() ).len();
		
		if( distance < RANGE )
			GetControl().FinishWithSuccess();
		else 
			GetControl().FinishWithFailure();
	}
	

}
