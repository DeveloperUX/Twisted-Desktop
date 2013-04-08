package twisted.rubber.ai.complexbehavior.controller;

import com.biigoh.utils.MathMan;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.Behavior;

public class BackAwayFromObstacleAction extends Behavior {

	/**
	 * Creates a new instance of the AvoidObstacleAction class
	 * @param blackboard Reference to the AI Blackboard data
	 */
	public BackAwayFromObstacleAction(Blackboard blackboard) {
		super(blackboard);
	}

	/**
	 * Creates a new instance of the AvoidObstacleAction class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param name Name of the class, for debug purposes
	 */
	public BackAwayFromObstacleAction(Blackboard blackboard, String name) {
		super(blackboard, name);
	}

	@Override
	public boolean checkPreConditions() {
		LogTask("Checking conditions");
		return true;
	}

	@Override
	public void Start() {
		LogTask("Starting");
	}

	@Override
	public void End() {
		LogTask("Ending");
	}

	@Override
	public void DoAction() {
		LogTask("Back up :: Doing Action");
		bb.getAiControls().joystickAngle += MathMan.radiansToDegrees * 10;
		bb.getAiControls().joystickStrength -= 0.1;
	}

}
