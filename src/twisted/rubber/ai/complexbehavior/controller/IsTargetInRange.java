package twisted.rubber.ai.complexbehavior.controller;

import twisted.rubber.ai.complexbehavior.library.Behavior;
import twisted.rubber.ai.complexbehavior.library.Blackboard;

public class IsTargetInRange extends Behavior {

	int RANGE = 20;
	
	public IsTargetInRange(Blackboard blackboard) {
		super(blackboard);
	}
	
	public IsTargetInRange(Blackboard blackboard, String name) {
		super(blackboard, name);	
	}

	@Override
	public boolean checkPreConditions() {
		return true;
	}

	@Override
	public void Start() {
		
	}

	@Override
	public void End() {
			
	}

	@Override
	public void DoAction() {	
		LogTask("");
		DebugAction();	
		if( bb.targetLocation.cpy().sub( bb.carToControl.getPosition() ).len() < RANGE ) {
			LogTask("In Range");
			GetControl().FinishWithSuccess();
		}
		else {
			LogTask("Not In Range");
			GetControl().FinishWithFailure();
		}
		
	}
	

}
