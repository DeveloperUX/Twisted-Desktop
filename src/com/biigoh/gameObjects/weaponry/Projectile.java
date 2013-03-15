package com.biigoh.gameObjects.weaponry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.biigoh.gameObjects.GameObject;
import com.biigoh.gameObjects.MovingObject;
import com.biigoh.launch.EntityData;
import com.biigoh.launch.EntityData.State;
import com.biigoh.launch.EntityData.Type;
import com.biigoh.utils.Vector2Pool;

public abstract class Projectile extends MovingObject {

	public static final boolean DEBUG = false;
	public final String LOG = "@ " + Projectile.class.getSimpleName();
	
	protected int ownerID;
	
	public static float speed;
	public static int damage;
	
	
	public Projectile( String name ) {
		super(name);	
	}
	
	public void setOwnerID( int ownerID ) {
		this.ownerID = ownerID;
		((EntityData) mBody.getUserData()).setOwnerID( ownerID );		
	}
	
	public abstract void shoot( Sprite sprite, Body body );	
	public abstract void explode();

	
	/**
	 * @return
	 */
	public int getDamage() {	
		return damage;
	}

	/* (non-Javadoc)
	 * @see com.biigoh.gameObjects.MovingObject#collideWidth(com.biigoh.gameObjects.GameObject)
	 */
	@Override
	public void collideWith(GameObject gameObj) {
		if(DEBUG) Gdx.app.log( LOG, "Projectile Collided" );
		setState( State.RECYCLE );
	}
	
	/* (non-Javadoc)
	 * @see com.biigoh.gameObjects.MovingObject#collideWidth(com.biigoh.gameObjects.GameObject)
	 */
	@Override
	public void collideWith(EntityData.Type type) {
		if(DEBUG) Gdx.app.log( LOG, "Projectile Collided" );
		setState( State.RECYCLE );
	}
	

    /**
     * Get the position of the Bullet.
     * @return A Vector object with the X and Y coordinates of the Bullet
     */
    public Vector2 getPosition() {
    	return mBody.getPosition();
    }

    /**
     * Set the Bullet's position manually. It is better to use Velocity and Physics
     * to automatically set the position.
     * @param pX The X coordinate
     * @param pY The Y coordinate
     */
	public void setPosition( float pX, float pY ) {
		// Set the initial Position of the bullet to the position of the car
		mBody.setTransform( pX, pY, 0 );
		if(DEBUG) Gdx.app.log( "@ Bullet", "setPosition() :: Manual Body Transform to: " + "<" + pX + ", " + pY + ">" );		
	}
	
	public void setTransform( float pX, float pY, float angle ) {
		// Set the initial Position of the bullet to the position of the car
		mBody.setTransform( pX, pY, angle );
		if(DEBUG) Gdx.app.log( "@ Bullet", "setPosition() :: Manual Body Transform to: " + "<" + pX + ", " + pY + ">" );		
	}
	
	/**
	 * Get the Bullet's velocity.
	 * @return The velocity as a Vector with X and Y coords.
	 */
	public Vector2 getVelocity() {
		return mBody.getLinearVelocity();
	}
    
	/**
	 * Set the Bullet's Linear velocity
	 * @param velocity Desired velocity vector
	 */
    public void setVelocity( Vector2 velocity ) {
		if(DEBUG) Gdx.app.log( "@ Bullet", "setVelocity() :: Setting Velocity to: " + velocity.cpy().mul(speed) );
    	mBody.setLinearVelocity( velocity.mul(speed) );
    }

	/**
	 * Set the Bullet's Linear velocity. This will be magnified by the Bullet constant Speed.
	 * @param xComp X Component of the desired velocity
	 * @param yComp Y Component of the desired velocity
	 */
    public void setVelocity( float xComp, float yComp ) {		
    	setVelocity( Vector2Pool.obtain(xComp, yComp) );
    }
		
	/**
	 * Make the bullet invisible and not active so that it is not constantly 
	 * update by the game loop. 
	 * ie: It does not exist
	 */
	public void disable() {
		visible = false;
		mBody.setActive( false );	// disable physics
	}
	
	/**
	 * The Bullet is visible on the scene and can do damage.
	 * ie: The bullet is in flight.
	 */
	public void enable() {
		//mSprite.setVisible( true );
		visible = true;	// will allow stage to call draw on this
		mBody.setActive( true );
		
	}

	/**
	 * Get the physics body of this object.  In case you need to 
	 * run it through some physiques. ;)
	 * @return The physics body attached to this bullet sprite
	 */
	public Body getBody(){	
		return mBody;	
	}
	
	/**
	 * Change the physics body of this bullet
	 * @param body The physics body object
	 */
	public void setBody( Body body ) {
		mBody = body;	
	}
	
}
