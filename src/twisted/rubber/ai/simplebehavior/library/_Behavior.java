package twisted.rubber.ai.simplebehavior.library;

import java.util.ArrayList;

/**
 * Base class for Behaviors,
 * ie an Action, Condition, or branch in the tree
 */
public abstract class _Behavior {
	
	public ArrayList<Node> nodes;
	
	// Possible return states for each behavior
	public static enum Status {
		INVALID,
		SUCCESS,
		FAILURE,
		RUNNING
	}
	
	protected Task mTask;
	protected Node mNode;	
	protected Status mStatus;
	
	/**
	 * Default Constructor
	 */
	public _Behavior() {
		nodes = new ArrayList<Node>();
		mStatus = Status.INVALID;
	}
	
	/**
	 * Attach a Behavior to a particular node
	 * @param node Node to attach Behavior to
	 */
	public _Behavior(Node node) {
		nodes = new ArrayList<Node>();
		setup(node);
		mStatus = Status.INVALID;
	}
	
	/**
	 * Set up this Behavior given the following Node
	 * @param node Behavior will be attached to this Node
	 */
	public void setup(Node node) {
		// Make sure we do not duplicate this Behavior
		teardown();
		// Set our Node
		mNode = node;
		// Create a Task from this Node,
		// This Task will be destroyed once we tear down this behavior
		mTask = node.create();
	}
	
	public void teardown() {
		if( mTask == null )
			return;
		
		mNode.destroy(mTask);
		mTask = null;
	}
	/*
	// Called every frame
	protected abstract Status update();
	// Called only the first time right before the update is run
	protected abstract void onInitialize();
	// Called once the update is finished
	protected abstract void onTerminate(Status status);
	*/
	public Status tick() {
		if( mStatus == Status.INVALID )
			mTask.onInitialize();
		
		mStatus = mTask.update();
		
		if( mStatus != Status.RUNNING )
			mTask.onTerminate(mStatus);
		
		return mStatus;
	}
	
}
