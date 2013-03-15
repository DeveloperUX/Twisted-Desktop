package com.biigoh.gameObjects.vehicles;

import com.biigoh.resources.Assets;
import com.biigoh.utils.Consts;

public class Roadster extends Vehicle {	

	public Roadster( float posX, float posY, float angle ) {
		
		super( "Roadster", Assets.Roadster_1, posX, posY, angle );
		
		meterWidth = Assets.Roadster_1.getWidth() / Consts.P2M_RATIO;
		meterHeight = Assets.Roadster_1.getHeight() / Consts.P2M_RATIO;
		
		MAX_SPEED = 160;	// extremely fast
		ACCELERATION = 300;	// Acceleration (in Newtons since this is the Force we apply) 
		HANDLING = 8f;	// horrible handling
		
		armor = 80;		// weak armor
		
	}
	

	@Override
	public void act( float delta ) {
		super.act(delta);
		// Check how much damage we've taken and apply the appropriate logic
		if( armor < 60 ) 
			mSprite.setTexture( Assets.Roadster_2 );
		if( armor <= 10 )
			mSprite.setTexture( Assets.Roadster_3 );
	}

}
