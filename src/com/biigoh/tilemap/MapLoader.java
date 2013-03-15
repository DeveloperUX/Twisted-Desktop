package com.biigoh.tilemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.biigoh.gameObjects.GameObject;
import com.biigoh.launch.EntityData;
import com.biigoh.launch.EntityData.Type;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.Consts;
import com.biigoh.view.BattleArena;

public class MapLoader {

	private TileAtlas tileAtlas;
	private TiledMap tileMap;
	private TextureAtlas textureAtlas;
	private TileMapRenderer renderer;
	private OrthographicCamera mCamera;
	
	private long startTime;
	private long endTime;
	
    TileMapRenderer tileMapRenderer;
    TiledMap map;
    TileAtlas atlas;
    
    public static final String LEVEL1 = "map/world/level1/level.tmx";
    public static final String LEVEL2 = "map/world/level2/level.tmx";
    

    public MapLoader() {
    	this(null);
    }

	public MapLoader( Camera camera ) {
		
//		loadMapLevel( 2 );
		
		mCamera = (OrthographicCamera) camera;
	}
	
	public void loadMapLevel( int level ) {
		
		String levelFilename = "map/world/level" + level + "/level.tmx";
		
		FileHandle basedir = Gdx.files.internal("map/packer"); 
		tileMap = TiledLoader.createMap(Gdx.files.internal( levelFilename ));

		tileAtlas = new TileAtlas( tileMap, basedir ); 
		tileMapRenderer = new TileMapRenderer( tileMap, tileAtlas, 16, 8 );

		setWalls();		
	}
	
	public void setCamera( Camera cam ) {
		mCamera = (OrthographicCamera) cam;
	}

	public void render() {
		tileMapRenderer.render(mCamera);
	}
	
	public void setWalls() {
		
		Wall tempWall;

		
		int[][] layer = tileMap.layers.get(0).tiles;
				
		for( int i = 0; i < layer.length; i++ ) {			
			for( int j=0; j < layer[i].length; j++ ) {

				//Gdx.app.log( "TILE MAP", "TILE PROPERTY: " + tileMap.getTileProperty( layer[i][j], "type") );
				
//				if(1==1) continue;
			
				if( tileMap.getTileProperty( layer[i][j], "type" ) != null && tileMap.getTileProperty( layer[i][j], "type").equals( "wall" ) ) {
					
					tempWall = new Wall( "Wall" );
					
					float pixXpos = j * tileMap.tileWidth;					
					float pixYpos = (layer.length-1 - i) * tileMap.tileHeight;	// <= HACK
					
					float meterXpos = pixXpos / Consts.P2M_RATIO;
					float meterYpos = pixYpos / Consts.P2M_RATIO;
					
					tempWall.createBody( tileMap.tileWidth, tileMap.tileHeight, 0f, false );
					tempWall.setPosition( meterXpos + tileMap.tileHeight / Consts.P2M_RATIO/2, meterYpos + tileMap.tileHeight / Consts.P2M_RATIO/2 );
					
					tempWall.setType( EntityData.Type.WALL );
				}
			
			}
		}
				
	}
	
	public int getWidth() {		
		return (int) (tileMap.width * tileMap.tileWidth / Consts.P2M_RATIO);	
	}
	public int getHeight() {	
		return (int) (tileMap.height * tileMap.tileHeight / Consts.P2M_RATIO);
	}

	public TileMapRenderer loadLevel(FileHandle mapHandle, FileHandle packFile, FileHandle baseDir) {

		int blockWidth = 80;
		int blockHeight = 60;

		startTime = System.currentTimeMillis();
		tileMap = TiledLoader.createMap(mapHandle); //MAKE MAP
		endTime = System.currentTimeMillis();
		System.out.println("Loaded map in " + (endTime - startTime) + "mS");

		atlas = new TileAtlas(tileMap, baseDir);

		startTime = System.currentTimeMillis();
		renderer = new TileMapRenderer(tileMap, atlas, blockWidth, blockHeight);
		endTime = System.currentTimeMillis();
		System.out.println("Created cache in " + (endTime - startTime) + "mS");

		return renderer;
	}
	

	
	public class Wall extends GameObject {
		

		private float x_pos;
		private float y_pos;

		public PolygonShape shape;	// static body with sensor shape

		public Wall(String name) {
			super(name);
		}
		
		public void setPosition( float x, float y ) {
			x_pos = x;
			y_pos = y;
			mBody.setTransform( x, y, 0 );
		}
		
		public float getX() { return x_pos; }
		public float getY() { return y_pos; }
				
		public void createBody( float width, float height, float friction, boolean isSensor ) {
			shape = new PolygonShape();
			shape.setAsBox( width / 2 / Consts.P2M_RATIO, height / 2 / Consts.P2M_RATIO );

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.isSensor = isSensor;
			fixtureDef.friction = 0;
			fixtureDef.density = 0f;
			fixtureDef.restitution = 0f;
			fixtureDef.shape = shape;

			mBody = BattleScreen.getPhysicsWorld().createBody( new BodyDef() );
			mBody.setType( BodyType.StaticBody );
			mBody.createFixture( fixtureDef );
			
			int uniqueID = GameObject.createUniqueID();
			mBody.setUserData(new EntityData( uniqueID, uniqueID, EntityData.Type.WALL ));
		}

		@Override
		public void collideWith(GameObject gameObj) {
			// Do nothing
		}

		/* (non-Javadoc)
		 * @see com.biigoh.gameObjects.GameObject#collideWith(com.biigoh.launch.EntityData.Type)
		 */
		@Override
		public void collideWith(Type type) {
			// Do nothing
		}
	}

}
