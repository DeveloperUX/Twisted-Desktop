package twisted.rubber.unittests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import twisted.rubber.ai.simplebehavior.library.Behavior;
import twisted.rubber.ai.simplebehavior.library.Behavior.Status;
import junit.framework.TestCase;

public class TestBehavior extends Behavior {
	int funcIntializedCalled;
	int funcTerminteCalled;
	int funcUpdateCalled;
	Status returnStatus;
	Status terminateStatus;
	
	public void onInitialize() {
		funcIntializedCalled++;
	}
	
	@Test
	public void testInitializeFunction() {
		Behavior t = new TestBehavior();
		assertEquals(0, funcIntializedCalled);
		
		t.tick();
		assertEquals(1, funcIntializedCalled);
	}

	@Override
	protected Status update() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onTerminate(Status status) {
		// TODO Auto-generated method stub
		
	}
}
