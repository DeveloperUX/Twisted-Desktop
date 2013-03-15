package com.biigoh.gameObjects;

import com.badlogic.gdx.math.Vector2;
import com.biigoh.launch.EntityData.Type;
import com.biigoh.utils.Vector2Pool;

public abstract class MovingObject extends GameObject {

	//--------------
	//  Constants ||
	//--------------

		
	
	//------------------
	//  Static Fields ||
	//------------------
	
	//------------------
	//  Public Fields ||
	//------------------

	public Vector2 curImpulse = Vector2Pool.obtain();
	public Vector2 curLateral = Vector2Pool.obtain();
	public Vector2 curForward = Vector2Pool.obtain();
	public Vector2 curLinear = Vector2Pool.obtain();
	public Vector2 debugLine = Vector2Pool.obtain();
	
	//-------------------
	//  Private Fields ||
	//-------------------
	
	protected int MAX_TORQUE = 300;
	
	//-----------------
	//  Constructors ||
	//-----------------
	
	public MovingObject( String name ) {
		super(name);
	}
	
	public MovingObject( String name, int uniqueID ) {
		super(name, uniqueID);
	}
	
	public MovingObject( String name, Type type ) {
		super(name);
		
	}

	//------------
	//  Getters ||
	//------------

	//------------
	//  Setters ||
	//------------

	//=====================
	//  Main Class Logic ||
	//=====================
	
	public void setMaxTorque( int torque ) {	MAX_TORQUE = torque;	}
	public int getMaxTorque() {	return MAX_TORQUE;	}

	//----------------------
	//  Inherited Methods ||
	//----------------------
	
	@Override
	public void act(float delta) {
		curForward = getForwardVelocity();
		curLateral = getLateralVelocity();
		curLinear = getLinearVelocity();
	}

	//---------------------------
	//  Private Helper Methods ||
	//---------------------------

	protected Vector2 getForwardVelocity() {
		Vector2 currentForwardNormal = mBody.getWorldVector( Vector2Pool.obtain(0,1) ).cpy(); 
		float dotProduct = currentForwardNormal.dot( mBody.getLinearVelocity() );
		return currentForwardNormal.mul( dotProduct );
	}

	protected Vector2 getLateralVelocity() {		
		Vector2 currentRightNormal = mBody.getWorldVector( Vector2Pool.obtain(1,0) ).cpy();
		float dotProduct = currentRightNormal.dot( mBody.getLinearVelocity() );
		return currentRightNormal.mul( dotProduct );
	}
	
	protected Vector2 getLinearVelocity() {
		return mBody.getLinearVelocity();
	}

	/* (non-Javadoc)
	 * @see com.biigoh.gameObjects.GameObject#collideWidth(com.biigoh.gameObjects.GameObject)
	 */
	@Override
	public abstract void collideWith(GameObject gameObj);
	
	//----------------------
	//  Anonymous Classes ||
	//----------------------
	
}
