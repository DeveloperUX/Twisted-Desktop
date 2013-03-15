package com.biigoh.controls;

public class HumanController extends Controller {
	
	private Joystick joystick;
	private FireButton fireButton;
	private SwitchButton switchButton;
	
	/**
	 * Create normal controls for a Human player.
	 * @param pJoystick The Joystick
	 * @param pFireButton The Attack HUD button
	 * @param pSwitchButton The Switch Weapon HUD button
	 */
	public HumanController( Joystick pJoystick, FireButton pFireButton, SwitchButton pSwitchButton ) {
		joystick = pJoystick;
		fireButton = pFireButton;
		switchButton = pSwitchButton;
	}
	
	// Update the controls in a Human Controller fashion
	@Override
	public void update() {
		joystickStrength = joystick.getStrength();
		joystickAngle = joystick.getAngle();
		
		if( fireButton.pressed )
			fireButtonState = FIRE_BUTTON_DOWN;
		else
			fireButtonState = FIRE_BUTTON_UP;
		
		if( switchButton.pressed )
			switchButtonState = SWITCH_BUTTON_DOWN;
		else
			switchButtonState = SWITCH_BUTTON_UP;
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

}
