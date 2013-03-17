package twisted.rubber.ai.simplebehavior.library;

import java.util.Vector;


public abstract class Composite extends Behavior {
	
	// list of behaviors
	public BehaviorList mChildren;		
//	public NodeList mChildren;

	protected class BehaviorList extends Vector<Behavior> {
		/** Auto generated Serial ID */
		private static final long serialVersionUID = 4899175769692494217L;
	}

}
