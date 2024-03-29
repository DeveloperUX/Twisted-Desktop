package com.biigoh.gameObjects.weaponry;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.biigoh.gameObjects.GameObject;
import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.launch.EntityData;
import com.biigoh.resources.Assets;
import com.biigoh.screens.BattleScreen;
import com.biigoh.sound.SoundSystem;
import com.biigoh.utils.ProjectilePool;
import com.biigoh.utils.Consts;
import com.biigoh.utils.MathMan;
import com.biigoh.utils.Physics;
import com.biigoh.view.BattleArena;

/**
 * @author BiiGoh Apps 
 */
public class MachineGun extends Weapon {
	
	private static final boolean DEBUG = false;
	
	SoundSystem soundComponent;

	public MachineGun() {
		this( null );
	}
	
	/** Create a new Machine Gun */
	public MachineGun( Vehicle carAttachedTo ) {
		super( "MachineGun" , carAttachedTo );
		// set all the default weapon specs
		mAmmoCount = Integer.MAX_VALUE;
		mRateOfFire = 0.2f;
//		soundComponent = new VehicleSound(null);
		range = 40;		// we can see 20 meters out, bullet can live longer
		lockOnSpeed = 0.1f;
		
		
		
		// TODO: TEMPORARY!!!
		mSprite = new Sprite( Assets.MachineGun );
		mSprite.setOrigin( mSprite.getWidth()/2, mSprite.getHeight()/2);	// set the origin to be at the center of the sprite
		mSprite.setScale( 1.2f, 2f );
		
		FixtureDef fd = new FixtureDef();
		fd.isSensor = true;
		
		mBody = Physics.createBoxBody( BodyType.DynamicBody, fd, mSprite );
		
		mBody.setUserData( new EntityData( getID(), getOwnerID(), EntityData.Type.MACHINEGUN ) );
	}

	/**
	 * Fire the Machine Gun at the direction the car is facing. Movement calculations are done by the {@link MachineGunRound}s.
	 * @param position The position of the Vehicle or Object that fired this weapon so that we can find where
	 * to create the bullet.
	 * @param angle The rotation of the Vehicle or the direction which the Bullet should head in.
	 * @param width The width of the scaled sprite to calculate starting position of Bullet, at the car's head
	 * @param height The height of the scaled sprite to calculate starting position of Bullet, at the car's head
	 * @return Whether or not firing was successful
	 */
	public boolean fireNoLock() {
		
		if( mAmmoCount < 1 ) 
			return false;	// No more ammo :(
		
		MachineGunRound bullet = ProjectilePool.obtainMachineGunRound();
		bullet.getBody().setUserData( new EntityData( bullet.getID(), getOwnerID(), EntityData.Type.MACHINE_GUN_ROUND ) );
//		bullet.setOwnerID( getOwnerID() );
		bullet.setType( EntityData.Type.MACHINE_GUN_ROUND );
		
		float angle = MathMan.aConvertToUsableAngle( carAttachedTo.getAngle() );
		Vector2 startPoint = MathMan.aPointFromDirection( carAttachedTo.getPosition(), angle, 3 );
		
    	// Set the start position of the new Bullet, most like in front of the Car
		bullet.setTransform( startPoint.x, startPoint.y, angle );
    	
    	// Fire off the bullet in the direction pointed to by the radian angle    	
    	bullet.setVelocity( -MathUtils.sin( angle ), MathUtils.cos( angle ) );
			  	
    	
		BattleScreen.getBattleStage().addActor( bullet );	// Add the bullet to the stage
		BattleArena.getInstance().addProjectile( bullet );
		
		// One less bullet!
		decrementAmmo();
		
		//soundComponent.playShotSound();
		
		return true;	// We were able to fire the weapon, HURRAY!
	}

	/* (non-Javadoc)
	 * @see com.biigoh.gameObjects.weaponry.Weapon#fireAtLockedOnTarget(com.biigoh.gameObjects.vehicles.Vehicle)
	 */
	@Override
	public boolean fireAtLockedOnTarget(Vehicle target) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Group#act(float)
	 */
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		
		if( carAttachedTo != null ) 
			return;
		
		mBody.setAngularVelocity( 3f );
	}
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Actor#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch, float)
	 */
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		if( carAttachedTo != null )
			return;
		
		mSprite.setPosition( mBody.getPosition().x * Consts.P2M_RATIO - mSprite.getWidth()/2, 
				mBody.getPosition().y * Consts.P2M_RATIO - mSprite.getHeight()/2 );
		mSprite.setRotation( mBody.getAngle() * MathMan.radiansToDegrees );
		mSprite.draw( batch, parentAlpha );
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Actor#hit(float, float)
	 */
	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}


}

