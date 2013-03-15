/**
 * 
 */
package com.biigoh.gameObjects.vehicles;

import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.resources.Assets;
import com.biigoh.utils.Consts;

/**
 * @author masry
 *
 */
public class Mustang extends Vehicle {

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
	public Mustang( float posX, float posY, float angle ) {
		
		super( "Mustang", Assets.Mustang_1, posX, posY, angle );
		
		meterWidth = Assets.Mustang_1.getWidth() / Consts.P2M_RATIO;
		meterHeight = Assets.Mustang_1.getHeight() / Consts.P2M_RATIO;
		
		MAX_SPEED = 160;	// extremely fast
		ACCELERATION = 300;	// Acceleration (in Newtons since this is the Force we apply) 
		HANDLING = 10f;	// horrible handling
		
		armor = 120;		// weak armor
		
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
			mSprite.setTexture( Assets.Mustang_2 );
		if( armor <= 10 )
			mSprite.setTexture( Assets.Mustang_3 );
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
