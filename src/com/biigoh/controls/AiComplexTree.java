package com.biigoh.controls;

import twisted.rubber.ai.complexbehavior.controller.DodgeWall;
import twisted.rubber.ai.complexbehavior.controller.FindNearestEnemy;
import twisted.rubber.ai.complexbehavior.controller.FireWeapon;
import twisted.rubber.ai.complexbehavior.controller.IsEnemyBehind;
import twisted.rubber.ai.complexbehavior.controller.IsEnemyInFieldOfView;
import twisted.rubber.ai.complexbehavior.controller.IsEnemyInLineOfFire;
import twisted.rubber.ai.complexbehavior.controller.IsEnemyNearby;
import twisted.rubber.ai.complexbehavior.controller.IsWallAhead;
import twisted.rubber.ai.complexbehavior.controller.MoveInDirection;
import twisted.rubber.ai.complexbehavior.controller.MoveInOppositeDirection;
import twisted.rubber.ai.complexbehavior.controller.PredictTargetPosition;
import twisted.rubber.ai.complexbehavior.controller.IsTargetInRange;
import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.complexbehavior.library.BranchController;
import twisted.rubber.ai.complexbehavior.library.InvertDecorator;
import twisted.rubber.ai.complexbehavior.library.RegulatorDecorator;
import twisted.rubber.ai.complexbehavior.library.ResetDecorator;
import twisted.rubber.ai.complexbehavior.library.Selector;
import twisted.rubber.ai.complexbehavior.library.Sequence;
import twisted.rubber.ai.complexbehavior.library.Task;

import com.biigoh.gameObjects.vehicles.Vehicle;

public class AiComplexTree extends Controller {

	/** Root task of the behavior tree for the AI */
	private Task root;
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
		this.root.GetControl().SafeStart();
	}

	/**
	 * Creates the behavior tree and populates the node hierarchy
	 * ORDER MATTERS!!!
	 */
	private void createBehaviorTree() {
		// Planner
		this.root = new Selector(blackboard, "Planner");
		this.root = new ResetDecorator(blackboard, this.root, "Planner");
//		this.root = new RegulatorDecorator(blackboard, this.root, "Planner", 0.1f);
		
//		Task wallAhead = new IsWallAhead(blackboard, "(C) Wall Ahead?");
//		Task noWallAhead = new InvertDecorator(blackboard, wallAhead, "(C) No Wall Ahead?");

		// Combat Sequence
		Task combat = new Sequence(blackboard, "Combat");
		((BranchController) combat.GetControl()).add(new IsEnemyInLineOfFire(blackboard, "(C) Enemy In Line of Fire?"));
		((BranchController) combat.GetControl()).add(new IsTargetInRange(blackboard, "(C) Target In Range?"));
//		((BranchController) combat.GetControl()).add(new HaveEnoughAmmo(blackboard, "(C) Have Adequate Ammo?"));
		((BranchController) combat.GetControl()).add(new FireWeapon(blackboard, "(A) Fire Weapon"));
		
		// Chase sequence
		Task chase = new Sequence(blackboard, "Chase");
		((BranchController) chase.GetControl()).add(new InvertDecorator(blackboard, new IsWallAhead(blackboard, "(C) No Wall Ahead?")));
		((BranchController) chase.GetControl()).add(new FindNearestEnemy(blackboard, "(A) Find Nearest Enemy"));
//		((BranchController) chase.GetControl()).add(new IsEnemyAhead(blackboard, "(C) Enemy Ahead?"));
		((BranchController) chase.GetControl()).add(new IsEnemyInFieldOfView(blackboard, "(C) Enemy In Field of View?"));
		// Predict Position
		((BranchController) chase.GetControl()).add(new PredictTargetPosition(blackboard, "(A) Predict Target Position"));
		// Move in direction of target
		((BranchController) chase.GetControl()).add(new MoveInDirection(blackboard, "(A) Move In Specified Direction"));
				
		((BranchController) chase.GetControl()).add( combat );

		// Create Evasion Sequence
		Task evade = new Sequence(blackboard, "Evade");
		((BranchController) evade.GetControl()).add(new InvertDecorator(blackboard, new IsWallAhead(blackboard, "(C) No Wall Ahead?")));
		((BranchController) evade.GetControl()).add(new FindNearestEnemy(blackboard, "(A) Find Nearest Enemy"));
		((BranchController) evade.GetControl()).add(new IsEnemyBehind(blackboard, "(C) Enemy Behind Us?"));
		((BranchController) evade.GetControl()).add(new IsEnemyNearby(blackboard, "(C) Enemy Nearby?"));
		((BranchController) evade.GetControl()).add(new PredictTargetPosition(blackboard, "(A) Predict Target Position"));
		((BranchController) evade.GetControl()).add(new MoveInOppositeDirection(blackboard, "(A) Move In Opposite Direction"));
		
			
		// Ram Car Sequence
//		Task ramEnemy = new Sequence(blackboarIsInvd, "Melee");		
//		((BranchController) ramEnemy.GetControl()).add(new IsEnemyClose(blackboard, "(C) Enemy Close?"));
//		((BranchController) ramEnemy.GetControl()).add(new IsEnemyAhead(blackboard, "(C) Enemy Ahead?"));
//		((BranchController) ramEnemy.GetControl()).add(new RamEnemy(blackboard, "(A) Ram Car"));
				
//		Task flee = new Sequence(blackboard, "Flee");
//		((BranchController) flee.GetControl()).add(maneuver);

		// Maneuvering between obstacles
		Task maneuver = new Selector(blackboard, "Maneuver");
//		maneuver = new RegulatorDecorator(blackboard, maneuver, "Planner", 0.1f);
		
		((BranchController) maneuver.GetControl()).add( evade );
		((BranchController) maneuver.GetControl()).add( chase );
//		((ParentActionController) maneuver.GetControl()).Add(new BackAwayFromObstacleAction(blackboard, "Back Up"));		

		// Avoid Wall actions
		Task dodge = new Sequence(blackboard, "Avoidance");
//		avoidanceSequence = new RepeatDecorator(blackboard, avoidanceSequence, "Avoid Wall Reset");
		((BranchController) dodge.GetControl()).add(new IsWallAhead(blackboard, "(C) Wall Ahead?"));
		((BranchController) dodge.GetControl()).add(new DodgeWall(blackboard, "(A) Dodge Wall"));
		
		// Add to planner
//		((BranchController) root.GetControl()).add(dodge);
//		((BranchController) root.GetControl()).add(new FindNearestEnemy(blackboard, "(A) Find Nearest Enemy"));
		((BranchController) root.GetControl()).add(maneuver);
		((BranchController) root.GetControl()).add(dodge);
						
	}
	
	
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		this.root.DoAction();
	}

}
