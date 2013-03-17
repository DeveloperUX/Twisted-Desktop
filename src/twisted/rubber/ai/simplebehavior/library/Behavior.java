package twisted.rubber.ai.simplebehavior.library;

/**
 * Base class for Behaviors,
 * ie an Action, Condition, or branch in the tree
 */
public abstract class Behavior {
	
	// Possible return states for each behavior
	public static enum Status {
		INVALID,
		SUCCESS,
		FAILURE,
		RUNNING
	}
	
	private Status mStatus;
	
	// Called every frame
	protected abstract Status update();
	
	public Behavior() {
		mStatus = Status.INVALID;
	}
	
	public Status tick() {
		if( mStatus == Status.INVALID )
			onInitialize();
		
		mStatus = update();
		
		if( mStatus != Status.RUNNING )
			onTerminate(mStatus);
		
		return mStatus;
	}

	// Called only the first time right before the update is run
	protected void onInitialize() { };
	// Called once the update is finished
	protected void onTerminate(Status status) { };
	
}
