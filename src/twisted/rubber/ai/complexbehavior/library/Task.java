package twisted.rubber.ai.complexbehavior.library;

import com.badlogic.gdx.Gdx;
import com.biigoh.controls.Controller;

import com.badlogic.gdx.Gdx;




/**
 * Base abstract class for all the tasks in the behavior tree.
 * 
 * An Action can be either a specific Task or a Condition
 * 
 * DO NOT ADD UTILITY FUNCTIONS. This class must remain as much a 
 * interface as possible, otherwise the decorators have no security mechanism
 * to ensure they are properly acting as wrappers of their specific tasks.
 * 
 * @author Moose
 *
 */
public abstract class Task 
{	
	protected static final boolean DEBUG = true;

	/** Reference to the Blackboard data */
	protected Blackboard bb;
	
	/** Specific subclass name, for debugging */
	protected String name;
		
	/**
	 * Creates a new instance of the Task class
	 * @param blackboard Reference to the AI Blackboard data
	 */
	public Task(Blackboard blackboard) {
		this.bb = blackboard;
	}
	
	/**
	 * Creates a new instance of the Task class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param name Name of the class, used for debugging
	 */
	public Task(Blackboard blackboard, String name) {
		this.name = name;
		this.bb = blackboard;
	}
	
	/**
	 * Logs the Task reference plus a message.
	 * @param text Message to display
	 */
	public void LogTask(String text) {
		if(DEBUG) Gdx.app.log(name, "\t \t " + text);
	}
	
	/**
	 * Override to do a pre-conditions check to see if the task can be updated.
	 * @return True if it can, false if it can't
	 */
	public abstract boolean checkPreConditions();
	
	/** Override to add startup logic to the task */
	public abstract void Start();
	
	/** Override to add ending logic to the task */
	public abstract void End();
	
	/** Override to specify the logic the task must update each cycle */
	public abstract void DoAction();
	
	/**
	 * Override to specify the controller the task has
	 * @return The specific task controller.
	 */
	public abstract BehaviorController GetControl();
}
