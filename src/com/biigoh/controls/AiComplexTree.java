package com.biigoh.controls;

import twisted.rubber.ai.complexbehavior.controller.DodgeWallAction;
import twisted.rubber.ai.complexbehavior.controller.IsCarAheadAction;
import twisted.rubber.ai.complexbehavior.controller.IsWallAheadAction;
import twisted.rubber.ai.complexbehavior.controller.MoveInDirection;
import twisted.rubber.ai.complexbehavior.controller.RamCarAction;
import twisted.rubber.ai.complexbehavior.library.Task;
import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.BranchController;
import twisted.rubber.ai.complexbehavior.library.RegulatorDecorator;
import twisted.rubber.ai.complexbehavior.library.RepeatDecorator;
import twisted.rubber.ai.complexbehavior.library.ResetDecorator;
import twisted.rubber.ai.complexbehavior.library.Selector;
import twisted.rubber.ai.complexbehavior.library.Sequence;

import com.biigoh.gameObjects.vehicles.Vehicle;

public class AiComplexTree extends Controller {

	/** Root task of the behavior tree for the AI */
	private Task rootPlanner;
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
//		this.rootPlanner = new RegulatorDecorator(blackboard, this.rootPlanner, "Planner", 0.1f);

		// Maneuvering between obstacles
		Task maneuver = new Selector(blackboard, "Maneuver");
		
		// Avoid Wall actions
		Task avoidanceSequence = new Sequence(blackboard, "Avoidance");
//		avoidanceSequence = new RepeatDecorator(blackboard, avoidanceSequence, "Avoid Wall Reset");
		((BranchController) avoidanceSequence.GetControl()).add(new IsWallAheadAction(blackboard, "Wall Ahead?"));
		((BranchController) avoidanceSequence.GetControl()).add(new DodgeWallAction(blackboard, "Dodge Wall"));
		
		// Ram Car actions
		Task meleeSequence = new Sequence(blackboard, "Melee");		
		((BranchController) meleeSequence.GetControl()).add(new IsCarAheadAction(blackboard, "Car Ahead?"));
		((BranchController) meleeSequence.GetControl()).add(new RamCarAction(blackboard, "Ram Car"));
		
		// Add Maneuvering sequences to Selector
		((BranchController) maneuver.GetControl()).add( avoidanceSequence );
		((BranchController) maneuver.GetControl()).add( meleeSequence );
//		((ParentActionController) maneuver.GetControl()).Add(new BackAwayFromObstacleAction(blackboard, "Back Up"));
		
		// Chase sequence
		Task combatSequence = new Sequence(blackboard, "Combat");
		((BranchController) combatSequence.GetControl()).add(new MoveInDirection(blackboard, "Move In Direction"));
		
		// Add to planner
		((BranchController) rootPlanner.GetControl()).add(maneuver);
		((BranchController) rootPlanner.GetControl()).add(combatSequence);
		
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
