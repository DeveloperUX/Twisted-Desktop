package twisted.rubber.ai.simplebehavior.library;

import twisted.rubber.ai.simplebehavior.library._Behavior.Status;


public abstract class Task {

	protected Node mNode;
	
	public Task(Node node) {
		mNode = node;		
	}
	
	public abstract Status update();
	
	public abstract void onInitialize();
	public abstract void onTerminate(Status status);
	
}
