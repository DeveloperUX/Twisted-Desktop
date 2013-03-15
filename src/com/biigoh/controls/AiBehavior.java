package com.biigoh.controls;

import twisted.rubber.ai.controller.AvoidObstacleAction;
import twisted.rubber.ai.controller.BackAwayFromObstacleAction;
import twisted.rubber.ai.library.Action;
import twisted.rubber.ai.library.Blackboard;
import twisted.rubber.ai.library.ParentActionController;
import twisted.rubber.ai.library.RegulatorDecorator;
import twisted.rubber.ai.library.ResetDecorator;
import twisted.rubber.ai.library.Selector;
import twisted.rubber.ai.library.Sequence;

import com.biigoh.gameObjects.vehicles.Vehicle;

public class AiBehavior extends Controller {

	/** Root task of the behavior tree for the AI */
	private Action planner;
	
	/** Information blackboard for the AI */
	private Blackboard blackboard;
	
	/**
	 * Creates a new instance of the AIInputDevice
	 * @param playScene Reference to the playScene
	 */
	public AiBehavior() {		
		// Set AI the blackboard data.
		blackboard = new Blackboard(this);	
		CreateBehaviourTree();
	}

	/**
	 * Sets the parent of the InputDevice.
	 */
	public void SetParent(Vehicle parent) {
		super.SetParent(parent);
		blackboard.player = parent;	
	}

	/**
	 * Start logic.
	 */
	@Override
	public void Start() {
		this.planner.GetControl().SafeStart();
	}

	/**
	 * Creates the behavior tree and populates the node hierarchy
	 */
	private void CreateBehaviourTree() {
		// Planner
		this.planner = new Selector(blackboard, "Planner");
		this.planner = new ResetDecorator(blackboard, this.planner, "Planner");
		this.planner = new RegulatorDecorator(blackboard, this.planner, "Planner", 0.1f);

		// Maneuvering between obstacles
		Action maneuver = new Selector(blackboard, "Maneuver");
		
		// Avoid Obstacle
		Action avoidObstacle = new Sequence(blackboard, "Avoid Obstacle Sequence");
		((ParentActionController) avoidObstacle.GetControl()).Add(new AvoidObstacleAction(blackboard, "AvoidObstacle"));
		
		// Back away from Obstacle
		Action backAway = new Sequence(blackboard, "Circle chase sequence");
		((ParentActionController) backAway.GetControl()).Add(new BackAwayFromObstacleAction(blackboard, "BackAwayFromObstacle"));
		
		// Add maneuvering behaviors to Maneuver
		((ParentActionController) maneuver.GetControl()).Add(avoidObstacle);
		((ParentActionController) maneuver.GetControl()).Add(backAway);
		
		// Add to planner
		((ParentActionController) planner.GetControl()).Add(maneuver);
		
		// Chase enemy vehicle
//		Action chaseEnemy = new Sequence(blackboard, "Circle chase sequence");
//		((ParentActionController) chaseEnemy.GetControl()).Add(new ChaseEnemyAction(blackboard, "BackAwayFromObstacle"));
		
		
		
		
		/*
		// Attack
		Action attack = new Selector(blackboard, "Attack");
				
		/// Circle Chase Attack
		Action circleChase = new Sequence(blackboard, "Circle chase sequence");
		circleChase = new ChanceDecorator(blackboard, circleChase, "Circle chase sequence", 60);
		((ParentActionController)circleChase.GetControl()).Add(new GetClosestEnemyCursorTask(blackboard, "GetClosestEnemyCursor"));
		((ParentActionController)circleChase.GetControl()).Add(new CalculateCirclePathTask(blackboard, "CalculateCirclePathTask"));
		Action circleChasePathSequence = new Sequence(blackboard, "Follow next tile sequence");
		circleChasePathSequence = new IteratePathDecorator(blackboard, circleChasePathSequence, "Follow next tile sequence");
		((ParentActionController)circleChasePathSequence.GetControl()).Add(new SetPathTileAsDestination(blackboard, "SetPathTileAsDestination"));
		((ParentActionController)circleChasePathSequence.GetControl()).Add(new MoveToDestinationTask(blackboard, "MoveToDestination"));
		((ParentActionController)circleChasePathSequence.GetControl()).Add(new WaitTillNearDestinationTask(blackboard, "WaitTillNearDestination"));
		((ParentActionController)circleChase.GetControl()).Add(circleChasePathSequence);
		
		/// Straight Chase Attack
		Action straightChase = new Sequence(blackboard, "Chase sequence");
		((ParentActionController)straightChase.GetControl()).Add(new GetClosestEnemyCursorTask(blackboard, "GetClosestEnemyCursor"));
		((ParentActionController)straightChase.GetControl()).Add(new SetEnemyCursorAsDestinationTask(blackboard, "SetEnemyCursorAsDestination"));
		((ParentActionController)straightChase.GetControl()).Add(new MoveToDestinationTask(blackboard, "MoveToDestination"));
		((ParentActionController)straightChase.GetControl()).Add(new WaitTillNearDestinationTask(blackboard, "WaitTillNearDestination"));
		
		// Add to attack
		((ParentActionController)attack.GetControl()).Add(circleChase);
		((ParentActionController)attack.GetControl()).Add(straightChase);
		
		// Defend
		Action defend = new Selector(blackboard, "Defend");
		defend = new DefendDecorator(blackboard, defend, "Defend");
		
		/// Circle Flee Defend
		Action circleFlee = new Sequence(blackboard, "Circle flee sequence");
		((ParentActionController)circleFlee.GetControl()).Add(new CalculateFleePathTask(blackboard, "CalculateFleePath"));
		Action circleFleePathSequence = new Sequence(blackboard, "Flee chase sequence");
		circleFleePathSequence = new IteratePathDecorator(blackboard, circleFleePathSequence, "Flee chase sequence");
		((ParentActionController)circleFleePathSequence.GetControl()).Add(new SetPathTileAsDestination(blackboard, "SetPathTileAsDestination"));
		((ParentActionController)circleFleePathSequence.GetControl()).Add(new MoveToDestinationTask(blackboard, "MoveToDestination"));
		((ParentActionController)circleFleePathSequence.GetControl()).Add(new WaitTillNearDestinationTask(blackboard, "WaitTillNearDestination"));
		((ParentActionController)circleFlee.GetControl()).Add(circleFleePathSequence);
		
		/// Straight Flee Defend
		Action straightFlee = new Sequence(blackboard, "Straight flee sequence");		
		((ParentActionController)straightFlee.GetControl()).Add(new CalculateFleeDestinationTask(blackboard, "CalculateFleeDestination"));
		((ParentActionController)straightFlee.GetControl()).Add(new MoveToDestinationTask(blackboard, "MoveToDestination"));
		((ParentActionController)straightFlee.GetControl()).Add(new WaitTillNearDestinationTask(blackboard, "WaitTillNearDestination"));
		
		// Add to defend
		((ParentActionController)defend.GetControl()).Add(circleFlee);
		((ParentActionController)defend.GetControl()).Add(straightFlee);
		
		// Add to planner
		((ParentActionController)this.planner.GetControl()).Add(defend);
		((ParentActionController)this.planner.GetControl()).Add(attack);
		*/
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		this.planner.DoAction();
	}

}
