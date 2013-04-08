package twisted.rubber.ai.complexbehavior.controller;

import java.awt.Event;
import java.util.Collection;

import twisted.rubber.ai.complexbehavior.library.Behavior;
import twisted.rubber.ai.complexbehavior.library.Blackboard;

import com.biigoh.gameObjects.vehicles.Vehicle;

public class FindNearestEnemy extends Behavior {
	
	private int currentLocation;

	public FindNearestEnemy(Blackboard blackboard) {
		super(blackboard);
	}
	
	public FindNearestEnemy(Blackboard blackboard, String name) {
		super(blackboard, name);	
	}

	@Override
	public boolean checkPreConditions() {
		return Blackboard.cars != null;
	}

	@Override
	public void Start() {
		currentLocation = 0;
	}

	@Override
	public void End() {
//		LogTask("Found: " + bb.closestEnemy.getID());
	}

	@Override
	public void DoAction() {

//		LogTask("");
		DebugAction();
		
		float closestDistance = Float.MAX_VALUE;
		float distance = 0;		

		// either we find a new Target or keep the previous target if
		// no new targets were found, or if no target is closer
		for( Vehicle car : Blackboard.cars ) 
			// make sure we're not checking ourselves
			if( car.getID() != bb.carToControl.getID() )	{ 
				// get the distance between the two cars
				distance = bb.carToControl.getPosition().cpy().sub( car.getPosition() ).len();
				// if closer, set the car as the new target
				if( distance < closestDistance ) {
					closestDistance = distance;
					bb.closestEnemy = car;
				}
			}				
		
		LogTask("Found: " + bb.closestEnemy.getID());
		
		if( bb.closestEnemy != null ) {
			bb.targetLocation = bb.closestEnemy.getPosition();
			GetControl().FinishWithSuccess();
		}
		else
			GetControl().FinishWithFailure();
	}
	

}
