package twisted.rubber.ai.complexbehavior.controller;

import com.biigoh.utils.MathMan;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.LeafAction;

public class BackAwayFromObstacleAction extends LeafAction {

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
	public boolean CheckConditions() {
		LogTask("Back up :: Checking conditions");
		return true;
	}

	@Override
	public void Start() {
		LogTask("Back up :: Starting");
	}

	@Override
	public void End() {
		LogTask("Back up :: Ending");
	}

	@Override
	public void DoAction() {
		LogTask("Back up :: Doing Action");
		bb.controls.joystickAngle += MathMan.radiansToDegrees * 10;
		bb.controls.joystickStrength -= 0.1;
	}

}
