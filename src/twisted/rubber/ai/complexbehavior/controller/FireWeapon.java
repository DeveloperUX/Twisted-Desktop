package twisted.rubber.ai.complexbehavior.controller;

import com.biigoh.controls.Controller;
import com.biigoh.gameObjects.weaponry.Weapon.WeaponState;

import twisted.rubber.ai.complexbehavior.library.Behavior;
import twisted.rubber.ai.complexbehavior.library.Blackboard;

public class FireWeapon extends Behavior {

	
	public FireWeapon(Blackboard blackboard) {
		super(blackboard);
	}
	
	public FireWeapon(Blackboard blackboard, String name) {
		super(blackboard, name);	
	}

	@Override
	public boolean checkPreConditions() {
		return true;
	}

	@Override
	public void Start() {
		bb.carToControl.getWeapon().setWeaponState( WeaponState.FIRE );
		
	}

	@Override
	public void End() {
		bb.carToControl.getWeapon().setWeaponState( WeaponState.STEADY );			
	}

	@Override
	public void DoAction() {
		// add ammo check
		LogTask("");
		DebugAction();
//		bb.carToControl.getController().fireButtonState = Controller.FIRE_BUTTON_DOWN;
		GetControl().FinishWithSuccess();			
	}
	

}
