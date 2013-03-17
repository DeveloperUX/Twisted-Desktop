package twisted.rubber.ai.simplebehavior.library;

import java.util.Iterator;

public class Selector extends Composite {

	Iterator<Behavior> mCurrentChild;
	
	@Override
	protected Status update() {
		// Loop until a child behavior is Running
		while(true) {
			// Get the current behavior's status on update
			Status status = mCurrentChild.next().tick();
			// if the child fails, or keeps running, continue
			if( status != Status.FAILURE )
				return status;
			// If we hit the last child then the job is done
			if( mCurrentChild.next().equals( mChildren.get( mChildren.size()-1 ) ) )
				return Status.FAILURE;
		}
	}

	@Override
	protected void onInitialize() {
		// TODO Auto-generated method stub
		mCurrentChild = mChildren.iterator();		
	}

}
