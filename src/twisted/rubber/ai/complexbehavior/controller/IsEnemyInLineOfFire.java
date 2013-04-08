package twisted.rubber.ai.complexbehavior.controller;

import java.awt.Event;

import twisted.rubber.ai.complexbehavior.library.Behavior;
import twisted.rubber.ai.complexbehavior.library.Blackboard;

import com.badlogic.gdx.math.Vector2;
import com.biigoh.gameObjects.weaponry.MachineGunRound;
import com.biigoh.utils.MathMan;
import com.biigoh.utils.Vector2Pool;

public class IsEnemyInLineOfFire extends Behavior {
	
	private int currentLocation;

	public IsEnemyInLineOfFire(Blackboard blackboard) {
		super(blackboard);
	}
	
	public IsEnemyInLineOfFire(Blackboard blackboard, String name) {
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
		// INTERCEPT ALGORITHM
//		Vector2 vR = target.curLinear.cpy().sub( carToControl.curLinear ); 
		Vector2 vR = bb.closestEnemy.curLinear.cpy().sub( Vector2Pool.obtain(MachineGunRound.speed,0 ) );
		Vector2 sR = bb.closestEnemy.getPosition().sub( bb.carToControl.getPosition() );
		float tC = sR.len() / vR.len();
		Vector2 sT = bb.closestEnemy.getPosition().add( bb.closestEnemy.curLinear.cpy().mul( tC ) );
		
		float angleToTarget = MathMan.aAngleBetweenPoints( bb.carToControl.getPosition(), sT );		
			
		float thisCarAngle = MathMan.aConvertToUsableAngle( bb.carToControl.getAngle() );
		
		// 14 is the View width
		if( Math.abs(thisCarAngle - angleToTarget) < MathMan.PI/16 )
			GetControl().FinishWithSuccess();
		else
			GetControl().FinishWithFailure();
	}
	

}
