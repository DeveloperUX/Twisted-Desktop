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
public class FireButton extends Button {


	// ===========================================================
	// Constants
	// ===========================================================

	public final boolean DEBUG = false;
	
	// ===========================================================
	// Fields
	// ===========================================================

	public boolean pressed = false;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Create a new Button to handle user pressing fire
	 * @param region The image of the Fire Button
	 */
	public FireButton(TextureRegion region) {
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
		
		// TODO: Took out static state for more concrete HumanController state
		
		//Controller.buttonState = Controller.FIRE_BUTTON_DOWN;
		pressed = true;
		
		if(DEBUG) Gdx.app.log( "@"+this.getClass().getSimpleName(), "touchDown():: FireButton pressed" );
		return true;
	}

	/** Called when the user releases the button */
	@Override
	public void touchUp(float x, float y, int pointer) {
		super.touchUp(x, y, pointer);
		
		//Controller.buttonState = Controller.FIRE_BUTTON_UP;
		pressed = false;
		
		if(DEBUG) Gdx.app.log( "@"+this.getClass().getSimpleName(), "touchDown():: FireButton released" );
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
