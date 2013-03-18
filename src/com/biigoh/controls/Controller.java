package com.biigoh.controls;

import com.biigoh.gameObjects.vehicles.Vehicle;

/**
 * 
 * @author Lone Wolf
 *
 */
public abstract class Controller {

	// ===========================================================
	// Constants
	// ===========================================================

	public static final int NONE_PRESSED = 0;
	
	public static final int FIRE_BUTTON_UP = 1;
	public static final int FIRE_BUTTON_DOWN = 2;
	
	public static final int SWITCH_BUTTON_UP = 1;
	public static final int SWITCH_BUTTON_DOWN = 2;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	public int fireButtonState = NONE_PRESSED;
	public int switchButtonState = NONE_PRESSED;
	public float joystickStrength;
	public float joystickAngle;

	/** Parent Vehicle for this Input Controller */
	protected Vehicle parent;	

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Sets the parent player
	 * @param parent player we assign this input to
	 */
	public void setParentOwner(Vehicle parent) {
		this.parent = parent;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/** For the base class to override. Called after the creating of the InputDevice, before the Update. */
	public abstract void Start();
	
	/** For the base class to overrride. Called every update loop */	
	public abstract void update();
	
//	public abstract float getJoystickStrength();	
//	public abstract float getJoystickAngle();	
//	public abstract int getFireButtonState();
//	public abstract int getSwitchButtonState();
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
