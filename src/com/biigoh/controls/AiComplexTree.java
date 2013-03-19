package com.biigoh.controls;

import twisted.rubber.ai.complexbehavior.controller.DodgeObstacleAction;
import twisted.rubber.ai.complexbehavior.controller.BackAwayFromObstacleAction;
import twisted.rubber.ai.complexbehavior.controller.MoveInDirection;
import twisted.rubber.ai.complexbehavior.controller.IsCarAheadAction;
import twisted.rubber.ai.complexbehavior.controller.IsWallAheadAction;
import twisted.rubber.ai.complexbehavior.controller.RamCarAction;
import twisted.rubber.ai.complexbehavior.library.Action;
import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.ParentActionController;
import twisted.rubber.ai.complexbehavior.library.RegulatorDecorator;
import twisted.rubber.ai.complexbehavior.library.ResetDecorator;
import twisted.rubber.ai.complexbehavior.library.Selector;
import twisted.rubber.ai.complexbehavior.library.Sequence;

import com.biigoh.gameObjects.vehicles.Vehicle;

public class AiComplexTree extends Controller {

	/** Root task of the behavior tree for the AI */
	private Action rootPlanner;
	/** Shared information blackboard for all AI objects */
	private Blackboard blackboard;
	
	// DIFFERENT IMPLEMENTATION OF AI
	
	/**
	 * Creates a new instance of the AIInputDevice
	 * @param playScene Reference to the playScene
	 */
	public AiComplexTree(Vehicle aiCarToControl) {		
		// Set AI the blackboard data.
		blackboard = new Blackboard(aiCarToControl);	
		createBehaviorTree();
	}

	/**
	 * Sets the parent of the InputDevice.
	 */
	public void setParentOwner(Vehicle parent) {
		super.setParentOwner(parent);
		blackboard.carToControl = parent;	
	}

	/**
	 * Start logic.
	 */
	@Override
	public void Start() {
		this.rootPlanner.GetControl().SafeStart();
	}

	/**
	 * Creates the behavior tree and populates the node hierarchy
	 * ORDER MATTERS!!!
	 */
	private void createBehaviorTree() {
		// Planner
		this.rootPlanner = new Selector(blackboard, "Planner");
		this.rootPlanner = new ResetDecorator(blackboard, this.rootPlanner, "Planner");
		this.rootPlanner = new RegulatorDecorator(blackboard, this.rootPlanner, "Planner", 0.1f);

		// Maneuvering between obstacles
		Action maneuver = new Selector(blackboard, "Maneuver");
		
		// Avoid Wall actions
		Action avoidWallSequence = new Sequence(blackboard, "Avoid Wall Sequence");
		((ParentActionController) avoidWallSequence.GetControl()).add(new IsWallAheadAction(blackboard, "Wall Ahead?"));
		((ParentActionController) avoidWallSequence.GetControl()).add(new DodgeObstacleAction(blackboard, "Dodge Wall"));
		
		// Ram Car actions
		Action ramCarSequence = new Sequence(blackboard, "Ram Car Sequence");		
		((ParentActionController) ramCarSequence.GetControl()).add(new IsCarAheadAction(blackboard, "Car Ahead?"));
		((ParentActionController) ramCarSequence.GetControl()).add(new RamCarAction(blackboard, "Ram Car"));
		
		// Add Maneuvering sequences to Selector
		((ParentActionController) maneuver.GetControl()).add( avoidWallSequence );
		((ParentActionController) maneuver.GetControl()).add( ramCarSequence );
//		((ParentActionController) maneuver.GetControl()).Add(new BackAwayFromObstacleAction(blackboard, "Back Up"));
		
		// Chase sequence
		Action chaseSequence = new Sequence(blackboard, "Chase");
		((ParentActionController) chaseSequence.GetControl()).add(new MoveInDirection(blackboard, "Drive To Position"));
		
		// Add to planner
		((ParentActionController) rootPlanner.GetControl()).add(maneuver);
		((ParentActionController) rootPlanner.GetControl()).add(chaseSequence);
		
		// Chase enemy vehicle
//		Action chaseEnemy = new Sequence(blackboard, "Circle chase sequence");
//		((ParentActionController) chaseEnemy.GetControl()).Add(new ChaseEnemyAction(blackboard, "BackAwayFromObstacle"));
				
	}
	
	
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		this.rootPlanner.DoAction();
	}

}
