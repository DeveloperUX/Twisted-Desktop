package com.biigoh.tilemap;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.biigoh.gameObjects.GameObject;
import com.biigoh.launch.EntityData;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.Consts;
import com.biigoh.utils.Physics;
import com.biigoh.view.BattleArena;

public class GameMap {

	private OrthographicCamera camera;

	private TextureAtlas mTileAtlas;
	private int TILEWIDTH;
	private int TILEHEIGHT;

	// tiles
	final char D = 0;	// Grass
	final char G = 1;	// Dirt (Dirt with rocks)
	final char S = 2;	// stone
	final char N = 9;

	char[][] layout;
	Tile[][] tileMap;
	
	SpriteCache spriteCache;

	/**
	 * Create a TileMap with the given Width and Height of the game camera 
	 * so that we know how much of the TileMap to draw
	 * @param cameraWidth Camera width in meters
	 * @param cameraHeight Camera height in meters
	 */
	public GameMap( float cameraWidth, float cameraHeight ) {
		
		spriteCache = new SpriteCache();

		camera = new OrthographicCamera( cameraWidth, cameraHeight );
		
		mTileAtlas = new TextureAtlas( Gdx.files.internal("map/packed/Ground packfile"), Gdx.files.internal("map/packed") );

		// TODO: Increase the actual Tile sizes in Gimp to something around 128 x 128
		TILEWIDTH = mTileAtlas.getRegions().get(0).getRegionWidth();
		TILEHEIGHT = mTileAtlas.getRegions().get(0).getRegionHeight();

		char[][] tileType = // 30 x 18
			{
				{ S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,D,D,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,D,D,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,S },
				{ S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S }
			};

		tileMap = new Tile[tileType.length][tileType[0].length];
		
		System.out.println( "WIDTH x HEIGHT: " + tileMap[0].length + "x" + tileMap.length );

//		spriteCache.beginCache();
		for( int i = tileType.length-1; i > -1; i-- ) {			
			for( int j=0; j < tileType[i].length; j++ ) {	
										
				if( tileType[i][j] == N )
					continue;

				boolean isSensor = true;
				
//				if( tileType[i][j] == S )
//					isSensor = false;
				
				AtlasRegion atlasRegion = mTileAtlas.getRegions().get( tileType[i][j]);			
				Tile tile = new Tile( atlasRegion, tileType[i][j] );
				
				float pixXpos = j * tile.getWidth(); // + tile.getWidth() * BLOCKSIZE/2);				
				float pixYpos = i * tile.getHeight();	//tile.getHeight() * BLOCKSIZE/2);
				
				float meterXpos = pixXpos / Consts.P2M_RATIO ;
				float meterYpos = pixYpos / Consts.P2M_RATIO;

				tile.createBody( tile.getWidth(), tile.getHeight(), 0f, isSensor );
				tile.setPosition( meterXpos + tile.getWidth()/Consts.P2M_RATIO/2, meterYpos + tile.getHeight()/Consts.P2M_RATIO/2 );
				
				spriteCache.beginCache();
					spriteCache.add( tile.getAtlasRegion(), pixXpos, pixYpos, tile.getWidth(), tile.getHeight() );
				spriteCache.endCache();
				
				if( tileType[i][j] == D )
					((EntityData) tile.body.getUserData()).setType( EntityData.Type.GROUND_HEAL );
				
				tileMap[i][j] = tile;

			}
		}
		

		layout = tileType;

	}
	
	public void setCamera( OrthographicCamera camera ) {
		this.camera = camera;
	}


	boolean moveTileBodies = true;

//	public void render( SpriteBatch spriteBatch, Vector3 cam_pos ) {
	public void render() {

		float x_pos, y_pos;
		float x_max, y_max;
		float x_min, y_min;
		float zoomFactor = camera.zoom * 1.4f;

		Tile tile; // temp tile
		
		spriteCache.setProjectionMatrix( camera.combined );
		
		int cacheID = 0;

		spriteCache.begin();
		// this will draw the tile layout but upside down since J and I start at the lower left corner
		for( int i = layout.length-1; i > -1; i-- ) {			
			for( int j=0; j < layout[i].length; j++ ) {				

				tile = tileMap[i][j];
				
				if( tile == null )	// empty space
					continue;

				// only draw tiles that are within the Camera bounds
				x_pos = TILEWIDTH + (j * TILEWIDTH);
				y_pos = TILEHEIGHT + (i * TILEHEIGHT);

				x_max = camera.position.x + (camera.viewportWidth/2) * Consts.P2M_RATIO + (TILEWIDTH * zoomFactor);
				y_max = camera.position.y + (camera.viewportHeight/2) * Consts.P2M_RATIO + (TILEHEIGHT * zoomFactor);

				x_min = camera.position.x - (camera.viewportWidth/2) * Consts.P2M_RATIO - (TILEWIDTH * zoomFactor);
				y_min = camera.position.y - (camera.viewportHeight/2) * Consts.P2M_RATIO - (TILEHEIGHT * zoomFactor);

				if( x_pos > x_min && y_pos > y_min && x_pos < x_max && y_pos < y_max )
					spriteCache.draw(cacheID);	
			
				cacheID++;
			}			
		}	
		spriteCache.end();
			
	}
	
	public float getWidth() {
		return TILEWIDTH * tileMap[0].length / Consts.P2M_RATIO;
	}

	public float getHeight() {
		return TILEHEIGHT * tileMap.length / Consts.P2M_RATIO;
	}
	
	public float getPixWidth() {
		return TILEWIDTH * tileMap[0].length;
	}
	
	public float getPixHeight() {
		return TILEHEIGHT * tileMap.length;
	}
	
	public Tile getTile( int index ) {
		int curIndex = 0;
		for( int i=0; i < tileMap.length; i++ ) 
			for( int j=0; j < tileMap.length; j++ ) {
				if( curIndex == index )
					return tileMap[i][j];		
			}
		return null;
	}
	
	public Tile getTileByPos( float x_pos, float y_pos ) {
		return null;
	}
	
	
	
	
	private class Tile {
		
		private float x_pos;
		private float y_pos;

		public AtlasRegion atlasRegion;
		public PolygonShape shape;	// static body with sensor shapexx
		public Body body;
		
		public int type;

		public Tile( AtlasRegion atlasRegion, int type ) {
			this.atlasRegion = atlasRegion;
			this.type = type;
		}		
		
		public void setPosition( float x_pos, float y_pos ) {
			this.x_pos = x_pos;
			this.y_pos = y_pos;
			body.setTransform( x_pos, y_pos, 0 );
		}
		
		public float getX() { return x_pos; }
		public float getY() { return y_pos; }
		
		public float getWidth() { return atlasRegion.getRegionWidth();	}
		public float getHeight() { return atlasRegion.getRegionHeight();	}

		public AtlasRegion getAtlasRegion() {
			return atlasRegion;
		}

		
		public void createBody( float width, float height, float friction, boolean isSensor ) {
			shape = new PolygonShape();
			shape.setAsBox( width/2/Consts.P2M_RATIO, height/2/Consts.P2M_RATIO );

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.isSensor = isSensor;
			fixtureDef.friction = 0.9f;
			fixtureDef.density = 0f;
			fixtureDef.restitution = 0f;
			fixtureDef.shape = shape;

			body = BattleScreen.getPhysicsWorld().createBody( new BodyDef() );
			body.setType( BodyType.StaticBody );
			body.createFixture( fixtureDef );
			
			int uniqueID = GameObject.createUniqueID();
			body.setUserData(new EntityData( uniqueID, uniqueID, EntityData.Type.WALL ));
		}
	}

}
