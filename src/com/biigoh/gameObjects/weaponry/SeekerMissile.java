/**
 * 
 */
package com.biigoh.gameObjects.weaponry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.resources.Assets;
import com.biigoh.utils.MathMan;
import com.biigoh.utils.Physics;
import com.biigoh.utils.Vector2Pool;

	public class SeekerMissile extends Projectile {

		public boolean DEBUG = false; // for debugging, whether or not to print out debugging info
		
		public Vehicle carToIntercept;
		
		/**
		 * Create a new Bullet with the default position and angle (0) given an Image {@link Texture}
		 * @param pTexture The image texture of what the Bullet should look like
		 * @return The Bullet
		 */
		public SeekerMissile() {
			this( Assets.SeekerMissile );
		}
		
		/**
		 * Constructor for creating a new Bullet with a Sprite and a Physics Body.
		 * @param posX X Position of where to place the Bullet on the scene
		 * @param posY Y Position of where to place the Bullet on the scene
		 * @param pTextureRegion The actual object representation of the bullet image
		 * @param angle The angle Bullet should move towards given in RADIAN.
		 * @param pEngine The game engine.  Has to be passed around so we could use its context. 
		 */
		public SeekerMissile( Texture pTexture ) {
			
			super( "SeekerMissile" ); // make Static id class
					
			// The sprite or visual representation of the bullet
			mSprite = new Sprite( pTexture ); //( posX, posY, 6, 6, pTextureRegion );
			mSprite.setOrigin( mSprite.getWidth()/2, mSprite.getHeight()/2);	// set the origin to be at the center of the body
			
			// Set the properties of the bullet
			final FixtureDef bulletFixtureDef = new FixtureDef();
			bulletFixtureDef.friction = 0f;
			bulletFixtureDef.restitution = 0f;		
			//bulletFixtureDef.filter.groupIndex = -1;
			
			// Here we create a body for the sprite image to have all of the physics handled on it.
			mBody = Physics.createBoxBody( BodyType.DynamicBody, bulletFixtureDef, mSprite );
			
	 
	    	damage = 20;
	    	speed = 40;
		}
		
		public void setTarget( Vehicle target ) {
			carToIntercept = target;
		}

		@Override
		public void shoot(Sprite sprite, Body body) {}

		@Override
		public void explode() {}

		@Override
		public void act( final float deltaTime ) {
			
			
			// check nearby cars
			for( Vehicle car : getArena().getCarList() )
				if( car.getPosition().cpy().sub( getPosition()).len() < 20 )
					carToIntercept = car;
			
			if( carToIntercept == null )
				return;
			
			// INTERCEPT ALGORITHM
			Vector2 vR = carToIntercept.curLinear.cpy().sub( Vector2Pool.obtain( speed, 0 ) );
			Vector2 sR = carToIntercept.getPosition().sub( getPosition() );
			float tC = sR.len() / vR.len();
			Vector2 sT = carToIntercept.getPosition().add( carToIntercept.curLinear.cpy().mul( tC ) );
			
			float angleDiff = -MathMan.aAngleBetweenPoints( getPosition(), sT );
			mBody.applyTorque( angleDiff * 40 );
		}
		
		
		
	
	
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

	//--------------------------------------------
	//  Getters ||||||||||||||||||||||||||||||||||
	//--------------------------------------------

	//--------------------------------------------
	//  Setters ||||||||||||||||||||||||||||||||||
	//--------------------------------------------

	//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
	//  Main Class Logic |||||||||||||||||||||||||
	//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-

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
