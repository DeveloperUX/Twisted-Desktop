/**
 * 
 */
package com.biigoh.controls;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

/**
 * @author masry
 *
 */
public class AIRaycastCallback implements RayCastCallback {


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

	public Fixture fixture;
	public Vector2 point;
	public Vector2 normal;
	public float fraction;
	
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

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.RayCastCallback#reportRayFixture(com.badlogic.gdx.physics.box2d.Fixture, com.badlogic.gdx.math.Vector2, com.badlogic.gdx.math.Vector2, float)
	 */
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		
		this.fixture = fixture;
		this.point = point;
		this.normal = normal;
		this.fraction = fraction;
		
		return fraction;
	}
	
	//--------------------------------------------
	//  Private Helper Methods |||||||||||||||||||
	//--------------------------------------------

	//--------------------------------------------
	//  Anonymous Classes ||||||||||||||||||||||||
	//--------------------------------------------
}
