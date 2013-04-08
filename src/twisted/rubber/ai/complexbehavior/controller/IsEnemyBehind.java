package twisted.rubber.ai.complexbehavior.controller;

import twisted.rubber.ai.complexbehavior.library.Behavior;
import twisted.rubber.ai.complexbehavior.library.Blackboard;

import com.biigoh.utils.MathMan;

public class IsEnemyBehind extends Behavior {
	
	private int currentLocation;

	public IsEnemyBehind(Blackboard blackboard) {
		super(blackboard);
	}
	
	public IsEnemyBehind(Blackboard blackboard, String name) {
		super(blackboard, name);	
	}

	@Override
	public boolean checkPreConditions() {
		return true;
	}

	@Override
	public void Start() {
		currentLocation = 0;
	}

	@Override
	public void End() {
		currentLocation = 0;
	}

	@Override
	public void DoAction() {
		LogTask("");
		DebugAction();
		
		float angleToMe = MathMan.aAngleBetweenPoints( bb.closestEnemy.getPosition(), bb.carToControl.getPosition() );
		float thisCarAngle = MathMan.aConvertToUsableAngle( bb.carToControl.getAngle() );		
		
		// TODO: Should we know the enemy's selected weapon's range?
		float enemyRange = bb.closestEnemy.getWeapon().getRange();
		float distance = bb.carToControl.getPosition().cpy().sub( bb.closestEnemy.getPosition() ).len();
		
		
		if( Math.abs(thisCarAngle - angleToMe) < MathMan.PI/3 && distance < enemyRange )
			GetControl().FinishWithSuccess();
		else
			GetControl().FinishWithFailure();
	}
	

}
