package twisted.rubber.ai.simplebehavior.library;

import java.util.Iterator;

/**
 * A type of Composite object,
 * simply a list of behaviors done in order
 */
public class Sequence extends Composite {
		
	Iterator<Behavior> mCurrentChild;
	
	@Override
	protected Status update() {
		// Loop until a child behavior is Running
		while(true) {
			
			Status status = mCurrentChild.next().tick();
			// if the child fails, or keeps running, continue
			if( status != Status.SUCCESS )
				return status;
			// If we hit the last child then the job is done
			if( mCurrentChild.next().equals( mChildren.get( mChildren.size()-1 ) ) )
				return Status.SUCCESS;
		}
	}

	@Override
	protected void onInitialize() {
		// beginning of the array of behaviors
		mCurrentChild = mChildren.iterator();
	}
	
}
