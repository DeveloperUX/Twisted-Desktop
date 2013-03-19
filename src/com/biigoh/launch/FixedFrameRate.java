package com.biigoh.launch;

/*
 * Jason Roth (jmroth79@gmail.com) <-- Credit Must be given
 * 
 * A class for locking a steady 60 frames and updates per second. If in the main game loop, the update
 * and render take less time than our target, the thread is put to sleep for the difference. If the update
 * and render take longer than expected, then we need to catch up by continuing to update, without rendering.
 * 
 * Please use for educational purposes.
 */

import com.biigoh.screens.BattleScreen;

public class FixedFrameRate {

	private final int MAX_FPS = 30;	// this will actaully give us about 60 FPS
	private final int MAX_FRAME_SKIPS = 10;
	private final int FPS_PERIOD = 1000 / MAX_FPS;
	
	private long sleepTime;
	private int framesSkipped;

	public FixedFrameRate() {
		sleepTime = 0;		
		framesSkipped = 0;
	}

	public void sleep(long timeDiff, BattleScreen screen, float delta) {
		//find the amount of time the thread needs to sleep
		sleepTime = (int)(FPS_PERIOD - timeDiff);

		if (sleepTime > 0) {
			//case when we will put thread to sleep
			try {
				//set Thread to sleep for a short time
				Thread.sleep(sleepTime);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
			//case when the update and render took longer than our target
			//we need to catch up
			screen.update(delta);

			sleepTime += FPS_PERIOD;
			framesSkipped++;
		}
	}
}