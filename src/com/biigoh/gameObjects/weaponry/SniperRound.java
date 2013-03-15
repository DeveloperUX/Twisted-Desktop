/**
 * 
 */
package com.biigoh.gameObjects.weaponry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.biigoh.resources.Assets;
import com.biigoh.utils.ProjectilePool;
import com.biigoh.utils.Consts;
import com.biigoh.utils.Physics;
import com.biigoh.utils.Vector2Pool;

public class SniperRound extends Projectile {

	public boolean DEBUG = false; // for debugging, whether or not to print out debugging info
	
	/**
	 * Create a new Bullet with the default position and angle (0) given an Image {@link Texture}
	 * @param pTexture The image texture of what the Bullet should look like
	 * @return The Bullet
	 */
	public SniperRound() {
		this( Assets.SniperRound );
	}
	
	/**
	 * Constructor for creating a new Bullet with a Sprite and a Physics Body.
	 * @param posX X Position of where to place the Bullet on the scene
	 * @param posY Y Position of where to place the Bullet on the scene
	 * @param pTextureRegion The actual object representation of the bullet image
	 * @param angle The angle Bullet should move towards given in RADIAN.
	 * @param pEngine The game engine.  Has to be passed around so we could use its context. 
	 */
	public SniperRound( Texture pTexture ) {
		
		super( "Bullet" ); // make Static id class
				
		// The sprite or visual representation of the bullet
		mSprite = new Sprite( pTexture ); //( posX, posY, 6, 6, pTextureRegion );
		mSprite.setOrigin( mSprite.getWidth()/2, mSprite.getHeight()/2);	// set the origin to be at the center of the body
		
		// Set the properties of the bullet
		final FixtureDef bulletFixtureDef = new FixtureDef();
		//bulletFixtureDef.density = 1f;
		bulletFixtureDef.friction = 1f;
		bulletFixtureDef.restitution = 0f;		
		//bulletFixtureDef.filter.groupIndex = -1;
		
		// Here we create a body for the sprite image to have all of the physics handled on it.
		mBody = Physics.createBoxBody( BodyType.DynamicBody, bulletFixtureDef, mSprite );
		
		// Give this sprite bullet characteristics, meaning continuous collision detection
    	mBody.setBullet( true );
    	
    	damage = 30;
    	speed = 100;
    
	}

	@Override
	public void shoot(Sprite sprite, Body body) {}

	@Override
	public void explode() {}

	@Override
	public void act( final float deltaTime ) {}
	
	@Override
	public void draw( SpriteBatch batch, float parentAlpha ) {
		mSprite.setPosition( mBody.getPosition().x * Consts.P2M_RATIO - mSprite.getWidth()/2, 
				mBody.getPosition().y * Consts.P2M_RATIO - mSprite.getHeight()/2 );
		//mSprite.setPosition( mBody.getPosition().x * Consts.PIXEL_METER_RATIO, mBody.getPosition().y * Consts.PIXEL_METER_RATIO );
		mSprite.setRotation( mBody.getAngle() * MathUtils.radiansToDegrees );		
		// draw the Sprite only if it is visible, parent Actor class handles this
		mSprite.draw( batch, parentAlpha );
		
	}
		 
	/** Remove the Bullet from the Scene and disable it, so that it does not get updated */
    public void removeFromStage() {    	
//    	markToRemove( true );	// Stage handles actual removal
//    	BulletPool.recycle( this );	// takes car of disabling physics and graphics
    	destroy();
    }
    
}
