package twisted.rubber.ai.complexbehavior.controller;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.Behavior;

public class RamCarAction extends Behavior {

	private long timeBeforeUpdate;
	private long timeDiff;
	private long elapsedTime;
	
	private float oldJoystickStrength;
	
	public RamCarAction(Blackboard blackboard, String name) {
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
		oldJoystickStrength = bb.getAiControls().joystickStrength;
	}

	@Override
	public void End() {
		// TODO Auto-generated method stub
		bb.getAiControls().joystickStrength = oldJoystickStrength;
	}

	@Override
	public void DoAction() {
		DebugAction();
		LogTask("Doing Action \n");
		// time before we update
//		timeBeforeUpdate = System.currentTimeMillis();		
		// TODO Auto-generated method stub
		bb.carToControl.getController().joystickStrength = 6;
		// time difference between before and after the update and render cycle
//		timeDiff = System.currentTimeMillis() - timeBeforeUpdate;
		elapsedTime += 1;
		
		if( elapsedTime > 5 ) {
			elapsedTime = 0;
			GetControl().FinishWithSuccess();
		}
	}


}
