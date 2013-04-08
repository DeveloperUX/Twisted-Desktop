package twisted.rubber.ai.complexbehavior.controller;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.Blackboard.Rays;
import twisted.rubber.ai.complexbehavior.library.Behavior;

import com.badlogic.gdx.math.Vector2;
import com.biigoh.controls.AIRaycastCallback;
import com.biigoh.launch.EntityData;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.MathMan;

public class IsEnemyInFieldOfView extends Behavior {


	public IsEnemyInFieldOfView(Blackboard blackboard, String name) {
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
		// -- Our weapon deteremines our field of view -- //
		// -- So a Sniper has a longer FOV than a Shotgun -- //
		float angleToTarget = MathMan.aAngleBetweenPoints( bb.carToControl.getPosition(), bb.closestEnemy.getPosition() );
		float thisCarAngle = MathMan.aConvertToUsableAngle( bb.carToControl.getAngle() );
		
		// first check if the other car is in range of our weapon
		float range = bb.carToControl.getWeapon().getRange();		
		float distance = bb.carToControl.getPosition().cpy().sub( bb.closestEnemy.getPosition() ).len();
		
		if( Math.abs(thisCarAngle - angleToTarget) < MathMan.PI/1.5 && distance < range )
			GetControl().FinishWithSuccess();
		
		else {
//			lockOnTimer = 0;	// reset lock on
			GetControl().FinishWithFailure();
		}
	}

}
