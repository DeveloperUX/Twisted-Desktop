/**
 * 
 */
package com.biigoh.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

/**
 * @author masry
 *
 */
public class SwitchButton extends Button {


	// ===========================================================
	// Constants
	// ===========================================================

	public final boolean DEBUG = true;
	public boolean pressed = false;
	
	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Create a new Button to handle user pressing fire
	 * @param region The image of the Fire Button
	 */
	public SwitchButton(TextureRegion region) {
		super(region);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


	/** Called when the user presses down on the button */
	@Override
	public boolean touchDown(float x, float y, int pointer) {
		super.touchDown(x, y, pointer);
		
		//Controller.fireButtonState = Controller.SWITCH_BUTTON_DOWN;
		pressed = true;
		
		if(DEBUG) Gdx.app.log( "@"+this.getClass().getSimpleName(), "touchDown():: Switch Button pressed" );
		return true;
	}

	/** Called when the user releases the button */
	@Override
	public void touchUp(float x, float y, int pointer) {
		super.touchUp(x, y, pointer);
		
		//Controller.fireButtonState = Controller.SWITCH_BUTTON_UP;
		pressed = false;
		
		if(DEBUG) Gdx.app.log( "@"+this.getClass().getSimpleName(), "touchDown():: Switch Button released" );
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
