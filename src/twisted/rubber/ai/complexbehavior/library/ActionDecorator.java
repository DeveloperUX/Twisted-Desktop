package twisted.rubber.ai.complexbehavior.library;


/**
 * Base class for the specific decorators.
 * Decorates all the task methods except for the DoAction, for commodity. 
 * (Tough any method can be decorated in the base classes with no problem, 
 * they are decorated by default so the programmer does not forget)
 * 
 * @author Moose
 *
 */
public abstract class ActionDecorator extends Task 
{
	/**
	 * Reference to the task to decorate
	 */
	Task task;

	/**
	 * Creates a new instance of the Decorator class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param task Task to decorate
	 */
	public ActionDecorator(Blackboard blackboard, Task task) 
	{
		super(blackboard);
		InitTask(task);
	}
	
	/**
	 * Creates a new instance of the Decorator class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param task Task to decorate
	 * @param name Name of the class, for debugging
	 */
	public ActionDecorator(Blackboard blackboard, Task task, String name) 
	{
		super(blackboard, name);
		InitTask(task);
	}
	
	/**
	 * Initializes the task reference
	 * @param task Task to decorate
	 */
	private void InitTask(Task task)
	{
		this.task = task;
		this.task.GetControl().SetTask(this);
	}

	/**
	 * Decorate the CheckConditions
	 */
	@Override
	public boolean checkPreConditions() 
	{
		return this.task.checkPreConditions();
	}

	/**
	 * Decorate the end
	 */
	@Override
	public void End() 
	{
		this.task.End();
	}

	/**
	 * Decorate the request for the Control reference
	 */
	@Override
	public BehaviorController GetControl() 
	{
		return this.task.GetControl();
	}

	/**
	 * Decorate the start
	 */
	@Override
	public void Start() 
	{
		this.task.Start();

	}
}
