package twisted.rubber.ai.complexbehavior.library;


import java.util.Vector;

/**
 * This class extends the TaskController class to add support for 
 * child tasks and their logic. Used together with ParentTask.
 * @author Moose
 *
 */
public class BranchController extends BehaviorController 
{
	/**
	 * Vector of child Task
	 */
	public Vector<Task> subtasks;
	
	/**
	 * Current updating task
	 */
	public Task curTask;

	/**
	 * Creates a new instance of the ParentTaskController class
	 * @param task
	 */
	public BranchController(Task task) 
	{
		super(task);
		
		this.subtasks = new Vector<Task>();
		this.curTask = null;
	}
	
	/**
	 * Adds a new subtask to the end of the subtask list.
	 * @param task Task to add
	 */
	public void add(Task task)
	{
		subtasks.add(task);
	}
	
	/**
	 * Resets the task as if it had just started.
	 */
	public void Reset()
	{
		super.Reset();
		this.curTask = subtasks.firstElement();
	}
}
