package twisted.rubber.ai.controller;

import com.badlogic.gdx.math.MathUtils;

import twisted.rubber.ai.library.Blackboard;
import twisted.rubber.ai.library.LeafAction;

public class AvoidObstacleAction extends LeafAction {

	/**
	 * Creates a new instance of the AvoidObstacleAction class
	 * @param blackboard Reference to the AI Blackboard data
	 */
	public AvoidObstacleAction(Blackboard blackboard) {
		super(blackboard);
	}

	/**
	 * Creates a new instance of the AvoidObstacleAction class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param name Name of the class, for debug purposes
	 */
	public AvoidObstacleAction(Blackboard blackboard, String name) {
		super(blackboard, name);
	}

	@Override
	public boolean CheckConditions() {
		LogTask("Avoid Obstacle :: Checking conditions");
		return true;
	}

	@Override
	public void Start() {
		LogTask("Avoid Obstacle :: Starting");
	}

	@Override
	public void End() {
		LogTask("Avoid Obstacle :: Ending");
	}

	@Override
	public void DoAction() {
		LogTask("Avoid Obstacle :: Doing Action");
		bb.controls.joystickAngle += MathUtils.degreesToRadians * 10;
		bb.controls.joystickStrength -= 0.1;
	}

}
