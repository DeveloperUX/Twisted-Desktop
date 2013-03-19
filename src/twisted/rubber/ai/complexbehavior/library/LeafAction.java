package twisted.rubber.ai.complexbehavior.library;

import com.biigoh.screens.BattleScreen;


/**
 * Leaf task (or node) in the behavior tree.
 *  
 * Specifies a TaskController, by composition, 
 * to take care of all the control logic, 
 * without burdening the Task class with 
 * complications.
 * 
 * @author Moose
 *
 */
public abstract class LeafAction extends Action {
	
	/** Task controller to keep track of the Task state */
	protected ActionController control;

	/**
	 * Creates a new instance of the LeafTask class
	 * @param blackboard Reference to the AI Blackboard data
	 */
	public LeafAction(Blackboard blackboard) {
		super(blackboard);
		CreateController();
	}
	
	/**
	 * Creates a new instance of the LeafTask class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param name Name of the class for debugging
	 */
	public LeafAction(Blackboard blackboard, String name) {
		super(blackboard, name);
		CreateController();
	}
	
	/** Creates the controller for the class */
	private void CreateController() {
		this.control = new ActionController(this);
	}
	
	/** Gets the controller reference */
	@Override
	public ActionController GetControl() {
		return this.control;
	}

	/**
	 * Logs the current Action to the Screen.
	 * @param text Message to display
	 */
	public void DebugAction() {
//		BattleScreen.debugStr += "AI State :: " + name + "\n";
		BattleScreen.debugCurAiState = name;		
	}
}
