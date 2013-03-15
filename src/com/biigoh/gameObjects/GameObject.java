package com.biigoh.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import com.biigoh.launch.EntityData;
import com.biigoh.launch.EntityData.State;
import com.biigoh.launch.EntityData.Type;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.Consts;
import com.biigoh.utils.Vector2Pool;
import com.biigoh.view.BattleArena;


public abstract class GameObject extends Group {
  

	public final boolean DEBUG = true;
	private static final String LOG = "@ " + GameObject.class.getSimpleName();
	
	private BattleArena arena;
	
	protected Sprite mSprite;
	protected Body mBody;

	// Entity ID's 1 and 2 are reserved for
	// our Hero and a possible Rival
	private static int objectCount = 0;
	private int id;
	
	
	public enum ArenaBound {
		LEFT,
		RIGHT,
		BOTTOM,
		TOP
	}
	
	public GameObject( String name ) {
		this( name, createUniqueID() );
	}
	
	public GameObject( String name, int uniqueID ) {
		super( name + ":" + uniqueID );
		id = uniqueID;
		touchable = false;
	}
	
	/**
	 * Use an ID to keep track of different objects
	 * @return A unique ID number
	 */
	public static int createUniqueID() {
		return objectCount++;
	}
	
	public int getID() { return id; };	
	public void setID( int pID ) { id = pID; };
		
	public void setArena(BattleArena arena) { this.arena = arena; }
	public BattleArena getArena() { return arena; }
	
	public State getState() { 
		return ((EntityData) mBody.getUserData()).getState();
	}
	
	public void setState(State state) {
		((EntityData) mBody.getUserData()).setState( state );
	}
	
	public Type getType() { 
		return ((EntityData) mBody.getUserData()).getType();
	}
	
	public void setType(Type type) {
		((EntityData) mBody.getUserData()).setType(type);
	}
	
	public float getAngle() {
		return mBody.getAngle();
	}

	public Vector2 getPosition() {
		return mBody.getPosition().cpy();
	}
	
	public Vector2 getSpritePosition() {
		return Vector2Pool.obtain( mSprite.getX() + mSprite.getWidth() / 2, mSprite.getY() + mSprite.getHeight() / 2);
	}
	
	public Sprite getSprite() {
		return mSprite;
	}
	
	public Body getBody() {
		return mBody;
	}

	/**
	 * Get a rectangular box around this object's Sprite for debugging.
	 * @return A bounding {@link Rectangle} in Pixel Coordinates
	 */
	public Rectangle getSpriteBounds() {
		return mSprite.getBoundingRectangle();
	}
	
	/**
	 * Check if this object goes off the bounds of the Screen so that we could stop drawing it. 
	 * @return Whether the Object if out of the bounds of the screen or not
	 */
	public ArenaBound isOutOfArena() {

		float posX = getPosition().x;
		float posY = getPosition().y;
	
		// Check if this baby out of bounds or crossed the line
		if( posX < 0 )
			return ArenaBound.LEFT;
		
		else if( posX > arena.width )
			return ArenaBound.RIGHT;		
		
		else if( posY < 0 ) 
			return ArenaBound.BOTTOM;
		
		else if( posY > arena.height )
			return ArenaBound.TOP;
		
		else
			return null;
		
//		if( posX > arena.width || posX < 0 || posY > arena.height || posY < 0 ) {
//			// This Object crossed the line.. tsk tsk		
//			if(DEBUG) Gdx.app.log( "@ Bullet", "act() :: Object is out of the bag! (screen): " + mBody.getPosition() );
//			return true;
//		}
//
//		return false; //object within screen bounds
	}
	

	private int counter;
	@Override
	public void draw( SpriteBatch batch, float parentAlpha ) {
		
		mSprite.setPosition( mBody.getPosition().x * Consts.P2M_RATIO - mSprite.getWidth()/2, 
				mBody.getPosition().y * Consts.P2M_RATIO - mSprite.getHeight()/2 );
		
		//mSprite.setPosition( mBody.getPosition().x * Consts.PIXEL_METER_RATIO, mBody.getPosition().y * Consts.PIXEL_METER_RATIO );
		mSprite.setRotation( mBody.getAngle() * MathUtils.radiansToDegrees );
		
		// draw the Sprite only if it is visible, parent Actor class handles this
		mSprite.draw( batch, parentAlpha );		
		
	}
	
	private boolean destroy = false;
	/**
	 * <p>Remove this Actor from the Stage, but before removing, 
	 * destroy and completely get rid of the Physics Body.</p>
	 * 
	 * <p><b>CAUTION:</b> Removed items <i>cannot</i> be recycled.
	 */
	public void destroy() {
		if(DEBUG) Gdx.app.log( LOG, "REMOVING OBJECT: (" + name + ")" );
		// Destroy the Object's Body
		BattleScreen.getPhysicsWorld().destroyBody(mBody);
		// Make invisible
		visible = false;
		// destroy its spirit, Kidding, remove the Actor of this Car so it's not rendered anymore
		remove();
	}

	public abstract void collideWith( GameObject gameObj );

	/**
	 * @param id2
	 */
//	public abstract void collideWith(int id);

	/**
	 * @param type
	 */
	public abstract void collideWith(Type type);

}
