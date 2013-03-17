package twisted.rubber.ai.simplebehavior.library;

public abstract class Node {
	
	public abstract Task create();
	public abstract void destroy(Task task);
}
