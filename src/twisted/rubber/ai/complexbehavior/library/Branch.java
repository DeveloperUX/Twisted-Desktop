package twisted.rubber.ai.complexbehavior.library;


import com.badlogic.gdx.Gdx;

/**
 * Inner node of the behavior tree, a flow director node, 
 * that selects a child to be executed next.
 * 
 * Sets a specific kind of TaskController for these kinds of tasks.
 * 
 * @author Moose
 *
 */
public abstract class Branch extends Task 
{
	/**
	 * TaskControler for the parent task
	 */
	BranchController control;
	
	public Branch(Blackboard blackboard)
	{
		super(blackboard);
		CreateController();
	}
	
	/**
	 * Creates a new instance of the ParentTask class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param name Name of the class for debugging
	 */
	public Branch(Blackboard blackboard, String name)
	{
		super(blackboard, name);
		CreateController();
	}
	
	/**
	 * Creates the TaskController.
	 */
	private void CreateController()
	{
		this.control = new BranchController(this);
	}
	
	/**
	 * Gets the control reference
	 */
	@Override
	public BehaviorController GetControl() 
	{
		return control;
	}

	/**
	 * Checks for the appropiate pre-state of the data
	 */
	@Override
	public boolean checkPreConditions() 
	{
//		LogTask("Checking conditions");
		return control.subtasks.size() > 0;
	}
	
	/**
	 * Abstract to be overridden in child classes. Called when a child finishes with success.
	 */
	public abstract void ChildSucceeded();
	
	/**
	 * Abstract to be overridden in child classes. Called when a child finishes with failure.
	 */
	public abstract void ChildFailed();

	/**
	 * Checks whether the child has started, finished or needs updating, and takes the needed
	 * measures in each case
	 */
	@Override
	public void DoAction() {
//		LogTask("Doing action");
		if(control.isFinished())
		{
			return;
		}
		if(control.curTask == null)
		{
			// If there is a null task, we've done something wrong
			Gdx.app.debug("Selector", "Current task has a null action");
			return;
		}
		
		// If we do have a curTask...
		if( !control.curTask.GetControl().isStarted())
		{
			// ... and it's not started yet, start it.
			control.curTask.GetControl().SafeStart();
		}		
		else if(control.curTask.GetControl().isFinished())
		{
			// ... and it's finished, end it properly.
			control.curTask.GetControl().SafeEnd();
			
			if(control.curTask.GetControl().isSucceeded())
			{
				this.ChildSucceeded();
			}
			if(control.curTask.GetControl().isFailed())
			{
				this.ChildFailed();
			}
		}
		else
		{		
			// ... and it's ready, update it.		
			control.curTask.DoAction();
		}	
	}

	/**
	 * Ends the task
	 */
	@Override
	public void End() 
	{
//		LogTask("Ending");
	}

	/**
	 * Starts the task, and points the current task to the first 
	 * one of the available child tasks.
	 */
	@Override
	public void Start() 
	{
//		LogTask("Starting");
		control.curTask = control.subtasks.firstElement();
		if(control.curTask == null)
		{
			Gdx.app.debug("Selector", "Current task has a null action");
		}
	}
}
