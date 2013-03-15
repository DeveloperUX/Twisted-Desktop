package com.biigoh.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.biigoh.screens.BattleScreen;

public class Physics {
	
	// FOR DEBUGGING THIS CLASS
	public final static boolean DEBUG = true;
	public final static String LOG = "@ " + Physics.class.getSimpleName();

	public static Body createBoxBody( final BodyType pBodyType, final FixtureDef pFixtureDef, Sprite pSprite ) {
				
		float spriteWidth = pSprite.getWidth();
		float spriteHeight = pSprite.getHeight();

		final BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = pBodyType;
		
		// Temporary Box shape of the Body
		final PolygonShape boxPoly = new PolygonShape();
		final float halfWidth = spriteWidth * 0.5f / Consts.P2M_RATIO;
		final float halfHeight = spriteHeight * 0.5f / Consts.P2M_RATIO;
		boxPoly.setAsBox( halfWidth, halfHeight );	// set the anchor point to be the center of the sprite

		pFixtureDef.shape = boxPoly;
		
		final Body boxBody = BattleScreen.getPhysicsWorld().createBody(boxBodyDef);
		boxBody.createFixture(pFixtureDef);

		boxPoly.dispose();
		return boxBody;
	}
	
	public static Body createCircleBody( final BodyType pBodyType, final FixtureDef pFixtureDef, Sprite pSprite ) {

		float spriteWidth = pSprite.getWidth();
		float spriteHeight = pSprite.getHeight();

		final BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = pBodyType;
		
		// Temporary Box shape of the Body
		final CircleShape topCircle = new CircleShape();
		final CircleShape bottomCircle = new CircleShape();
		
		final float halfWidth = spriteWidth * 0.5f / Consts.P2M_RATIO;
		final float halfHeight = spriteHeight * 0.5f / Consts.P2M_RATIO;
		
		topCircle.setPosition(Vector2Pool.obtain( spriteWidth + spriteWidth/2, spriteHeight + spriteHeight/2) );
		topCircle.setRadius( spriteWidth/2 );
		
		bottomCircle.setPosition(Vector2Pool.obtain( spriteWidth - spriteWidth/2, spriteHeight - spriteHeight/2) );
		bottomCircle.setRadius( spriteWidth/2 );

		pFixtureDef.shape = topCircle;
		
		final Body boxBody = BattleScreen.getPhysicsWorld().createBody(boxBodyDef);
		boxBody.createFixture(pFixtureDef);

		topCircle.dispose();
		return boxBody;
	}


}
