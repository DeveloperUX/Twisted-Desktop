/**
 * 
 */
package com.biigoh.gameObjects.vehicles;

import com.biigoh.resources.Assets;
import com.biigoh.utils.Consts;

/**
 * @author masry
 *
 */
public class Truck extends Vehicle {

	//--------------------------------------------
	//  Constants ||||||||||||||||||||||||||||||||
	//--------------------------------------------

	//--------------------------------------------
	//  Static Fields ||||||||||||||||||||||||||||
	//--------------------------------------------

	//--------------------------------------------
	//  Public Fields ||||||||||||||||||||||||||||
	//--------------------------------------------

	//--------------------------------------------
	//  Private Fields |||||||||||||||||||||||||||
	//--------------------------------------------

	//--------------------------------------------
	//  Constructors |||||||||||||||||||||||||||||
	//--------------------------------------------
	public Truck( float posX, float posY, float angle ) {
		
		super( "Truck", Assets.Truck_1, posX, posY, angle );
		
		meterWidth = Assets.Truck_1.getWidth() / Consts.P2M_RATIO;
		meterHeight = Assets.Truck_1.getHeight() / Consts.P2M_RATIO;
		
		MAX_SPEED = 160;	// extremely fast
		ACCELERATION = 400;	// Acceleration (in Newtons since this is the Force we apply) 
		HANDLING = 10f;	// horrible handling
		MAX_TORQUE = 900;
		
		armor = 140;		// weak armor
		
	}

	//--------------------------------------------
	//  Getters ||||||||||||||||||||||||||||||||||
	//--------------------------------------------

	//--------------------------------------------
	//  Setters ||||||||||||||||||||||||||||||||||
	//--------------------------------------------

	//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
	//  Main Class Logic |||||||||||||||||||||||||
	//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-

	@Override
	public void act( float delta ) {
		super.act(delta);
		// Check how much damage we've taken and apply the appropriate logic
		if( armor < 60 ) 
			mSprite.setTexture( Assets.Truck_2 );
		if( armor <= 10 )
			mSprite.setTexture( Assets.Truck_3 );
	}
	//--------------------------------------------
	//  Inherited Methods ||||||||||||||||||||||||
	//--------------------------------------------

	//--------------------------------------------
	//  Private Helper Methods |||||||||||||||||||
	//--------------------------------------------

	//--------------------------------------------
	//  Anonymous Classes ||||||||||||||||||||||||
	//--------------------------------------------
}
