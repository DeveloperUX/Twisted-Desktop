package twisted.rubber.ai.complexbehavior.library;


/**
 * Decorator that resets to "Started" the task it is applied to, each time said
 * task finishes.
 * 
 * @author Moose
 *
 */
public class RepeatDecorator extends ActionDecorator 
{
	/**
	 * Creates a new instance of the ResetDecorator class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param task Task to decorate
	 * @param name Name of the class, used for debugging
	 */
	public RepeatDecorator(Blackboard blackboard, Task task, String name)	{
		super(blackboard, task, name);
	}
	
	/**
	 * Creates a new instance of the ResetDecorator class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param task Task to decorate
	 */
	public RepeatDecorator(Blackboard blackboard, Task task) {
		super(blackboard, task);
	}

	/**
	 * Does the decorated task's action, and if it's done, resets it.
	 */
	@Override
	public void DoAction() {
		task.DoAction();
		if(task.GetControl().isFailed())
			task.GetControl().Reset();
		
	}
}
