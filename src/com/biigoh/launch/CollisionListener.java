package com.biigoh.launch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.biigoh.view.BattleArena;

/**
 * @author Lone Wolf
 *
 */
public class CollisionListener implements ContactListener {

	// ===========================================================
	// Constants
	// ===========================================================
	
	public static final boolean DEBUG = false;
	public final String LOG = "@ " + CollisionListener.class.getSimpleName();
	
	private static final int OTHER = 0;
	private static final int VEHICLE = 1;
	private static final int GROUND = 1;

	// ===========================================================
	// Fields
	// ===========================================================
	
	private BattleArena arena;

	// ===========================================================
	// Constructors
	// ===========================================================
	/**
	 * Listens for Box2D collisions between bodies and calls the appropriate
	 * methods to process them before, during, and after the collision.
	 * @param arena The Battle Arena where the collisions will happen
	 */
	public CollisionListener( BattleArena arena ) {
		this.arena = arena;
	}

	// ===========================================================`
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/** Called at the moment of collision between 2 objects. */
	@Override
	public void beginContact(Contact contact) {
						
		// TODO: Add the rest of the collision types, other than bullets
		
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
		
		EntityData userDataA = (EntityData) bodyA.getUserData();
		EntityData userDataB = (EntityData) bodyB.getUserData();

//		if(DEBUG) Gdx.app.log( LOG, "beginContact() :: Collision happened betweeen: " + arena.getObject(userDataA.getID()).name + " & " + arena.getObject(userDataB.getID()).name );
		
		if( arena.getObject( userDataA.getID() ) != null ) 
			arena.getObject( userDataA.getID() ).collideWith( userDataB.getType() );
	
		if( arena.getObject( userDataB.getID() ) != null ) 
			arena.getObject( userDataB.getID() ).collideWith( userDataA.getType() );
		
		
		return;
		
		
//		EntityData[] objectIDs;
//		
//		objectIDs = checkWeaponPickupCollision(userDataA, userDataB);
//		if( objectIDs != null ) {	
//			if( objectIDs[OTHER].getOwnerID() == objectIDs[OTHER].getID() ) {
//				Vehicle car = arena.getCar( objectIDs[VEHICLE].getID() );				
//				car.addWeapon = true;;
//				objectIDs[OTHER].setState( EntityData.State.DEAD );
//				return;
//			}
//		}
//		
//		objectIDs = checkProjectile2CarCollision(userDataA, userDataB);		
//		if( objectIDs != null ) {			
//			
//			Vehicle carThatGotHit = arena.getCar( objectIDs[VEHICLE].getOwnerID() );
//			Vehicle carThatDidTheHitting = arena.getCar( objectIDs[OTHER].getOwnerID() );
//			Projectile projectile = arena.getProjectile( objectIDs[OTHER].getID() );
//			
//			
//			if(DEBUG) Gdx.app.log( LOG, "beginContact() :: " + carThatGotHit.name + " Got hit by " + carThatDidTheHitting.name + "'s " + projectile.name );
//
//			
//			// second owner, who owns the Car that got hit, detuct health points
//			carThatGotHit.gotHit( projectile.getDamage() ); 	// replace 10 with projectile Damage value
//			
//			// first owner, who owns the projectile, gets 1 point for a hit! NAH! we'll just get 1 point for 
//			// a kill.. actually to make it REAL interesting, we'll get 1 point for a kill, but we'll 
//			// ALSO take the other Car's score and add it to ours! PLUS the point for the kill! WOOT WOOT!
//			if( carThatGotHit.getArmor() < 0 ) {
//				carThatDidTheHitting.addToScore( carThatGotHit.getScore() + 1 );	// one for the destroyed car		
//			}
//						
////			for( Actor a : BattleScreen.getBattleStage().getActors() )
////				if(DEBUG) Gdx.app.log( LOG, "beginContact() :: All actors: " + a.name );
//
//			// Remove the projectile from the screen
//			if(DEBUG) Gdx.app.log( LOG, "Projectile to be recycled: " + projectile.name );
//			
//			projectile.setState( EntityData.State.RECYCLE );  
////			BattleScreen.getBattleStage().findActor( "Object:" + objectIDs[0].objectID ).markToRemove(true);
//			
//		}
//		
//		objectIDs = checkCar2GroundCollision(userDataA, userDataB);
//		if( objectIDs != null ) {					
//			Vehicle carThatGotHit = arena.getCar( objectIDs[VEHICLE].getOwnerID() );
//			carThatGotHit.setArmor( carThatGotHit.getArmor() + 1 );		
//		}
////		objectIDs = checkCar2CarCollision(userDataA, userDataB);
			
		
			
			
	}


	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#endContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#preSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.Manifold)
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		if( ((EntityData) contact.getFixtureA().getBody().getUserData()).getType() == EntityData.Type.MACHINE_GUN_ROUND &&
				((EntityData) contact.getFixtureB().getBody().getUserData()).getType() == EntityData.Type.MACHINE_GUN_ROUND )
			contact.setEnabled(false);

		if( ((EntityData) contact.getFixtureA().getBody().getUserData()).getType() == EntityData.Type.SNIPER_ROUND &&
				((EntityData) contact.getFixtureB().getBody().getUserData()).getType() == EntityData.Type.SNIPER_ROUND )
			contact.setEnabled(false);

		if( ((EntityData) contact.getFixtureA().getBody().getUserData()).getType() == EntityData.Type.MISSILE &&
				((EntityData) contact.getFixtureB().getBody().getUserData()).getType() == EntityData.Type.MISSILE )
			contact.setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#postSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.ContactImpulse)
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub	
	}	
	
	// ===========================================================
	// Methods
	// ===========================================================

	/** Check that collision happened between a projectile and a vehicle */
	private EntityData[] checkProjectile2CarCollision( EntityData userDataA, EntityData userDataB ) {
		
		// make sure we arent processing two Bodies that have the same owner
		if( userDataA.getOwnerID() == userDataB.getOwnerID() )
			return null;
		
		// TODO: Replace this method with Collision Filters on the objects themselves
		
		EntityData[] objectIDs = new EntityData[2];	// [ WHO_FIRED, WHO_GOT_HIT ]
		
		if( userDataA.getType() == EntityData.Type.MACHINE_GUN_ROUND && userDataB.getType() == EntityData.Type.VEHICLE ) {
			objectIDs[VEHICLE] = userDataB;
			objectIDs[OTHER] = userDataA;
			return objectIDs;	// contact between projectile and car
		}
		
		else if( userDataA.getType() == EntityData.Type.VEHICLE && userDataB.getType() == EntityData.Type.MACHINE_GUN_ROUND ) {
			objectIDs[VEHICLE] = userDataA;
			objectIDs[OTHER] = userDataB;
			return objectIDs;	// contact between a car and projectile
		}
		
		// TODO: Should we check collision and health for two Cars colliding?
		//else if( userDataA.mBodyType == UserData.VEHICLE && userDataB.mBodyType == UserData.VEHICLE )
			//return true;	// contact between two cars
		
		return null;	// improper contact, probably between 2 bullets
	}

	/** Check that collision happened between a projectile and a vehicle */
	private EntityData[] checkWeaponPickupCollision( EntityData userDataA, EntityData userDataB ) {
		// make sure we arent processing two Bodies that have the same owner
		if( userDataA.getOwnerID() == userDataB.getOwnerID() )
			return null;
		
		// TODO: Replace this method with Collision Filters on the objects themselves
		
		EntityData[] objectIDs = new EntityData[2];	// [ WHO_FIRED, WHO_GOT_HIT ]
		
		if( userDataA.getType() == EntityData.Type.MACHINEGUN && userDataB.getType() == EntityData.Type.VEHICLE ) {
			objectIDs[VEHICLE] = userDataB;
			objectIDs[OTHER] = userDataA;
			return objectIDs;	// contact between projectile and car
		}
		else if( userDataA.getType() == EntityData.Type.VEHICLE && userDataB.getType() == EntityData.Type.MACHINEGUN ) {
			objectIDs[VEHICLE] = userDataA;
			objectIDs[OTHER] = userDataB;
			return objectIDs;	// contact between a car and projectile
		}
		
		// TODO: Should we check collision and health for two Cars colliding?
		//else if( userDataA.mBodyType == UserData.VEHICLE && userDataB.mBodyType == UserData.VEHICLE )
			//return true;	// contact between two cars
		
		return null;	// improper contact, probably between 2 bullets
	}
	
	private EntityData[] checkCar2GroundCollision( EntityData userDataA, EntityData userDataB ) {
		// make sure we arent processing two Bodies that have the same owner
		if( userDataA.getOwnerID() == userDataB.getOwnerID() )
			return null;
		
		// TODO: Replace this method with Collision Filters on the objects themselves
		
		EntityData[] objectIDs = new EntityData[2];	// [ WHO_FIRED, WHO_GOT_HIT ]
		
		if( userDataA.getType() == EntityData.Type.GROUND_HEAL && userDataB.getType() == EntityData.Type.VEHICLE ) {
			objectIDs[VEHICLE] = userDataB;
			objectIDs[GROUND] = userDataA;
			return objectIDs;	// contact between projectile and car
		}
		else if( userDataA.getType() == EntityData.Type.VEHICLE && userDataB.getType() == EntityData.Type.GROUND_HEAL ) {
			objectIDs[VEHICLE] = userDataA;
			objectIDs[GROUND] = userDataB;
			return objectIDs;	// contact between a car and projectile
		}
		
		// TODO: Should we check collision and health for two Cars colliding?
		//else if( userDataA.mBodyType == UserData.VEHICLE && userDataB.mBodyType == UserData.VEHICLE )
			//return true;	// contact between two cars
		
		return null;	// improper contact, probably between 2 bullets		
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
