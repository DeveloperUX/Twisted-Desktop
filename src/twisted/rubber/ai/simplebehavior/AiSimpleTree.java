package twisted.rubber.ai.simplebehavior;

import twisted.rubber.ai.complexbehavior.library.Blackboard;
import twisted.rubber.ai.simplebehavior.library.Behavior;
import twisted.rubber.ai.simplebehavior.library.Selector;
import twisted.rubber.ai.simplebehavior.library.Sequence;

import com.biigoh.controls.Controller;
import com.biigoh.gameObjects.vehicles.Vehicle;

public class AiSimpleTree extends Controller {

	/** Root task of the behavior tree for the AI */
	private Behavior rootPlanner;
	/** Shared information blackboard for all AI objects */
	private Blackboard blackboard;

	/**
	 * Creates a new instance of the AIInputDevice
	 * @param playScene Reference to the playScene
	 */
	public AiSimpleTree(Vehicle aiCarToControl) {		
		// Set AI the blackboard data.
		blackboard = new Blackboard( aiCarToControl );	
		CreateBehaviourTree();
	}

	private void CreateBehaviourTree() {
		// Planner
		rootPlanner = new Selector();
//		this.rootPlanner = new ResetDecorator(blackboard, this.rootPlanner, "Planner");
//		this.rootPlanner = new RegulatorDecorator(blackboard, this.rootPlanner, "Planner", 0.1f);

		// Maneuvering between obstacles
		Selector maneuver = new Selector();
		
		// Avoid Obstacle
		Sequence avoidObstacle = new Sequence();
//		avoidObstacle.mChildren.add(new AvoidObstacleAction());
//		((ParentActionController) avoidObstacle.GetControl()).Add(new AvoidObstacleAction(blackboard, "AvoidObstacle"));
		
		// Back away from Obstacle
//		Action backAway = new Sequence(blackboard, "Circle chase sequence");
//		((ParentActionController) backAway.GetControl()).Add(new BackAwayFromObstacleAction(blackboard, "BackAwayFromObstacle"));
		
		// Add maneuvering behaviors to Maneuver
//		((ParentActionController) maneuver.GetControl()).Add(avoidObstacle);
//		((ParentActionController) maneuver.GetControl()).Add(backAway);
		
		// Add to planner
//		((ParentActionController) rootPlanner.GetControl()).Add(maneuver);		
	}

	/**
	 * Sets the parent of the InputDevice.
	 */
	public void setParentOwner(Vehicle parent) {
		super.setParentOwner(parent);
		blackboard.carToControl = parent;	
	}
	
	@Override
	public void Start() {
		// TODO Auto-generated method stub
		rootPlanner = new Selector();
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		rootPlanner.tick();
	}
	

}
