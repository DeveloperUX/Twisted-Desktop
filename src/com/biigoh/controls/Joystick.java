package com.biigoh.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.biigoh.resources.Assets;
import com.biigoh.utils.Vector2Pool;

public class Joystick extends Actor {

	public final boolean DEBUG = true;
	public final String LOG = "@ " + Joystick.class.getSimpleName();
	
	private boolean dpadActive = true;

	private Vector2 dpadOrigin;
	private Vector2 dpadInput;
	private Vector2 dpadOutput;

	private int dpadPointerIndex = -1;
	private float dpadScale = 2;	// Scale the output of the Joystick distance from the base
	public float dpadRadius = 64;	// how far the stick can go from the center of the base

	public boolean drawControls = true;

	private Sprite dpadBodySprite;
	private Sprite dpadStickSprite;

	public Joystick(float originX, float originY) {
		
		super( "Analog Controls" );
		super.touchable = true;

		dpadOrigin = new Vector2(originX,  originY);

		dpadInput = new Vector2();
		dpadOutput = new Vector2();

		// NOTE: You'll need to load the texture somehow, I made a simple asset management class for this purpose.

		dpadBodySprite = new Sprite( Assets.ControlBase, 0, 0, 128, 128);
		dpadStickSprite = new Sprite( Assets.ControlKnob, 0, 0, 64, 64 );

		if(DEBUG) Gdx.app.log( LOG, "DPad Origin and Sprite Origin: " + dpadOrigin + " - " + "[" + dpadBodySprite.getOriginX() + ":" + dpadBodySprite.getOriginY() + "]");
		dpadBodySprite.setPosition( dpadOrigin.x - dpadBodySprite.getOriginX(), dpadOrigin.y -  dpadBodySprite.getOriginY() );
		
		// TODO: Fix Joystick so that it appears where ever the user clicks
		width = dpadBodySprite.getWidth();
		height = dpadBodySprite.getHeight();

//		width = Gdx.graphics.getWidth() / 2;
//		height = Gdx.graphics.getHeight() / 1.6f;
		
	}
	
	public Rectangle getBodyBounds() {
		return dpadBodySprite.getBoundingRectangle();
	}
	
	public Rectangle getStickBounds() {
		return dpadStickSprite.getBoundingRectangle();
	}

	public boolean isActive() {
		return dpadActive;
	}

	private void updateValue() {
		// Limit how far the stick can go from the base
		limit(dpadInput, dpadRadius);
		
		dpadOutput.x = dpadInput.x;
		dpadOutput.y = dpadInput.y; // made positive
		// How far the dpad stick is from the base
		float len = dpadOutput.len();
		dpadOutput.nor();
		// This gives us a vector output of the joystick 
		// in the form of <1, 0>. With a range from 0 to 1
		dpadOutput.mul(dpadScale * (len / dpadRadius));
	}

	public void setValue(Vector2 dir) {
		this.dpadInput.set(dir);
		this.updateValue();

		if(dir.len() > 0) {
			dpadActive = true;
		}
		else {
			dpadActive = false;
		}
	}

	public Vector2 getValue() {
		return dpadOutput;
	}
	
	/**
	 * The strength of the pull on the Joystick, ie: how far the stick is from the center of the joystick
	 * @return The distance from the center of the Joystick to the Stick itself
	 */
	public float getStrength() {
		//return dpadOutput.len();
		return dpadOutput.len() / 2;	// distance from center to stick from [0..1]
	}
	
	/**
	 * Get the angle from the Stick makes while the user is pulling it.
	 * @return The RADIAN angle between North (0) and the current Stick position
	 */
	public float getAngle() {
		return MathUtils.atan2( dpadOutput.x, dpadOutput.y  );
	}
	
	/**
	 * Get the polar coordinate value of the control
	 * @return A Vector containing the Distance and Angle
	 * from the center of the controller
	 */
	public Vector2 getPolarValue() {
		float length = dpadOutput.len();
		float radAngle = MathUtils.atan2( dpadOutput.x, dpadOutput.y );
		return Vector2Pool.obtain(length, radAngle);
	}

	private void limit(Vector2 v, float limit) {
		if (v.len() > limit) {
			v.nor();
			v.mul(limit);
		}
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Group#act(float)
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		
		// TODO: Took out static controller, this is now kept track of by the Controller object
		
		// Update the Controls for outside access
		//Controller.joystickStrength = getStrength();
		//Controller.joystickAngle = getAngle();
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		//super.draw(batch, parentAlpha);

		if(drawControls) {    	  
			if(dpadActive) {
				// When active make the joystick visible
				dpadBodySprite.setColor(1,1,1,1);
				dpadStickSprite.setColor(1,1,1,1);
			} 
			else {
				// Make them more transparent when they're inactive
				dpadBodySprite.setColor(1,1,1,0);
				dpadStickSprite.setColor(1,1,1,0);
			}

			// always draw the body after the Stick
			dpadBodySprite.draw(batch);

			if(dpadActive) 
				dpadStickSprite.setPosition(dpadOrigin.x + dpadInput.x - dpadStickSprite.getOriginX(), dpadOrigin.y + dpadInput.y - dpadStickSprite.getOriginY());
			else
				dpadStickSprite.setPosition(dpadOrigin.x - dpadStickSprite.getOriginX(), dpadOrigin.y - dpadStickSprite.getOriginY() );
						
			// Size properly
			//resize();
			
			// Always draw the stick whether active or inactive
			dpadStickSprite.draw(batch);
			
		}
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Actor#touchUp(float, float, int)
	 */
	@Override
	public void touchUp(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		super.touchUp(x, y, pointer);
		
		if(dpadActive && (pointer == dpadPointerIndex)) {
			dpadActive = false;
			dpadPointerIndex = -1;
			dpadInput.set(0,0);

			updateValue();
		}
		
		dpadActive = false;
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Actor#touchDragged(float, float, int)
	 */
	@Override
	public void touchDragged( float x, float y, int pointer ) {
		// TODO Auto-generated method stub
		super.touchDragged(x, y, pointer);
		
		if(dpadActive && (pointer == dpadPointerIndex)) {
			
			dpadInput.set(x, y);
			dpadInput.sub(dpadOrigin);

			updateValue();
		}
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Group#touchDown(float, float, int)
	 */
	@Override
	public boolean touchDown( float x, float y, int pointer ) {
		// TODO Auto-generated method stub
		super.touchDown(x, y, pointer);
		
		if(DEBUG) Gdx.app.log( LOG, "touchDown() :: TOUCH DOWN Active? " + dpadActive );
		
		if( !dpadActive ) {
			
//			if( dpadOrigin.dst(x, y) < dpadRadius )   {
			if( touchDownWithinBounds(x, y) ) {

				if(DEBUG) Gdx.app.log( LOG, "touchDown() :: dpadOrigin old value and new value = " + dpadOrigin + " [" + x + ", " + y + "]");			
				
				dpadOrigin.set(x, y);
				dpadBodySprite.setPosition( x - dpadBodySprite.getOriginX(), y -  dpadBodySprite.getOriginY() );
				
				if(DEBUG) Gdx.app.log( LOG, "touchDown() :: touchDownWithinBounds() = TRUE" );
				
				dpadActive = true;
				dpadPointerIndex = pointer;

				dpadInput.set(x, y);
				dpadInput.sub(dpadOrigin);

				updateValue();
			}
		}
		
		dpadActive = true;
		
		return true;
	}

	/**
	 * Check if we were touched, if yes return a pointer to this Actor
	 * @param x The x position of where the actual touch was on this Actor
	 * @param y The y position of where the actual touch was on this Actor
	 * @return The Actor that was touched (this)
	 */
	@Override
	public Actor hit(float x, float y) {
//		return getBodyBounds().contains( x, y ) ? this : null;
		// We will do this differently, Instead of checking if the Joystick sprite was touched
		// instead we're going to check if the user touched anywhere near the left hand side of
		// the screen.  So as long as the user touches near the Joystick it will register
		if( touchDownWithinBounds(x, y) )
			return this;
		else
			return null;
	}

	/**
	 * Checks whether or not the user touches down on a certain part of the screen
	 * @param x The x position of the user's touch
	 * @param y The y position of the user's touch
	 * @return Whether or not the user touched the lower left part of the screen
	 */
	private boolean touchDownWithinBounds( float x, float y ) {
		// Is the user's touch half way to the left of the screen
		// AND to the bottom two-thirds of the screen?
		if( (x < Gdx.graphics.getWidth() / 2) && 
				(y < Gdx.graphics.getHeight() / 1.5f) )
			return true;
		
		return false;
	}

	private void resize() {
		
		Vector2 position = new Vector2( dpadBodySprite.getX(), dpadBodySprite.getY() );
	    float x = Gdx.graphics.getWidth();
	    float y = Gdx.graphics.getHeight();
	
	    float changeX = x / 800; //being your screen size that you're developing with
	    float changeY = y / 480;
	
	    position = new Vector2(position.x * changeX, position.y * changeY);
	    float newWidth = width * changeX;
	    float newHeight = height * changeY;
	    Vector2 bounds = new Vector2 (position.x, (Gdx.graphics.getHeight() - position.y) - height);
	
	    dpadBodySprite.setPosition(bounds.x, bounds.y);
	}


	
	// --------------------------------
	// Methods from interface not implemented
	// -------------------------------
	
}
