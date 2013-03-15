package com.biigoh.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;
import com.biigoh.gameObjects.weaponry.MachineGunRound;
import com.biigoh.gameObjects.weaponry.SeekerMissile;
import com.biigoh.gameObjects.weaponry.SniperRound;

/**
 * Use this Bullet Pool to recycle and obtain new or recycled 
 * Bullets that have made contact or flown off screen.
 * 
 * @author Lone Wolf
 */
public class ProjectilePool {
	
	// ===========================================================
	// Constants
	// ===========================================================

	public final static boolean DEBUG = false;
	
	// ===========================================================
	// Fields
	// ===========================================================

	private static final Pool<MachineGunRound> MachineGunRound_Pool = new Pool<MachineGunRound>() {
		@Override
		protected MachineGunRound newObject() {
			MachineGunRound newBullet = new MachineGunRound();
			newBullet.enable();

			if(DEBUG) Gdx.app.log( "@ BulletPool", "newObject() :: Creating new Bullet: " + newBullet.name );
			return newBullet;
		}
	};

	private static final Pool<SniperRound> SniperRound_Pool = new Pool<SniperRound>() {
		@Override
		protected SniperRound newObject() {
			SniperRound newBullet = new SniperRound();
			newBullet.enable();

			if(DEBUG) Gdx.app.log( "@ BulletPool", "newObject() :: Creating new Bullet: " + newBullet.name );
			return newBullet;
		}
	};

	private static final Pool<SeekerMissile> SeekerMissile_Pool = new Pool<SeekerMissile>() {
		@Override
		protected SeekerMissile newObject() {
			SeekerMissile newBullet = new SeekerMissile();
			newBullet.enable();

			if(DEBUG) Gdx.app.log( "@ BulletPool", "newObject() :: Creating new Bullet: " + newBullet.name );
			return newBullet;
		}
	};
	// ===========================================================
	// Constructors
	// ===========================================================
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Get a Bullet from the Pool, might be new or recycled
	 * @return A Bullet object that is new or recycled
	 */
	public static MachineGunRound obtainMachineGunRound() {		
		MachineGunRound recycled_bullet = MachineGunRound_Pool.obtain();
		recycled_bullet.enable();	// Make the bullet active and visible

		if(DEBUG) Gdx.app.log( "@ BulletPool", "obtain() :: Retrieving new or recycled Bullet: " + recycled_bullet.name );
		return recycled_bullet;
	}

	public static SniperRound obtainSniperRound() {		
		SniperRound recycled_bullet = SniperRound_Pool.obtain();
		recycled_bullet.enable();	// Make the bullet active and visible

		if(DEBUG) Gdx.app.log( "@ BulletPool", "obtain() :: Retrieving new or recycled Bullet: " + recycled_bullet.name );
		return recycled_bullet;
	}

	public static SeekerMissile obtainSeekerMissile() {		
		SeekerMissile recycled_bullet = SeekerMissile_Pool.obtain();
		recycled_bullet.enable();	// Make the bullet active and visible

		if(DEBUG) Gdx.app.log( "@ BulletPool", "obtain() :: Retrieving new or recycled Bullet: " + recycled_bullet.name );
		return recycled_bullet;
	}
	

	/**
	 * Recycle the given Bullet and put it back in the pool to be reused. 
	 * If the Pool already contains the max free items than this object is ignored.
	 * @param bullet The Bullet to be recycled
	 */
	public static void recycleMachineGunRound(final MachineGunRound bullet) {
		bullet.disable();	// Make it inactive and invisible

		if(DEBUG) Gdx.app.log( "@ BulletPool", "obtain() :: Recycling used Bullet: " + bullet.name );
		MachineGunRound_Pool.free( bullet );	// Add to the Pool
	}

	public static void recycleSniperRound(final SniperRound bullet) {
		bullet.disable();	// Make it inactive and invisible

		if(DEBUG) Gdx.app.log( "@ BulletPool", "obtain() :: Recycling used Bullet: " + bullet.name );
		SniperRound_Pool.free( bullet );	// Add to the Pool
	}

	public static void recycleSeekerMissile(final SeekerMissile bullet) {
		bullet.disable();	// Make it inactive and invisible

		if(DEBUG) Gdx.app.log( "@ BulletPool", "obtain() :: Recycling used Bullet: " + bullet.name );
		SeekerMissile_Pool.free( bullet );	// Add to the Pool
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}