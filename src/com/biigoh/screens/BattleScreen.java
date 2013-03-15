package com.biigoh.screens;


import java.io.OutputStream;
import java.util.ArrayList;

//import aurelienribon.tweenengine.BaseTween;
//import aurelienribon.tweenengine.Tween;
//import aurelienribon.tweenengine.TweenCallback;
//import aurelienribon.tweenengine.TweenEquations;
//import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.biigoh.controls.AIController;
import com.biigoh.controls.AiBehavior;
import com.biigoh.controls.FireButton;
import com.biigoh.controls.HumanController;
import com.biigoh.controls.Joystick;
import com.biigoh.controls.SwitchButton;
import com.biigoh.gameObjects.GameObject;
import com.biigoh.gameObjects.vehicles.Boss1;
import com.biigoh.gameObjects.vehicles.Mustang;
import com.biigoh.gameObjects.vehicles.Roadster;
import com.biigoh.gameObjects.vehicles.Truck;
import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.gameObjects.weaponry.MachineGun;
import com.biigoh.gameObjects.weaponry.Sniper;
import com.biigoh.gameObjects.weaponry.Stinger;
import com.biigoh.gameObjects.weaponry.Weapon;
import com.biigoh.launch.CollisionListener;
import com.biigoh.launch.EntityData;
import com.biigoh.launch.FixedFrameRate;
import com.biigoh.launch.TwistedRubber;
import com.biigoh.resources.Assets;
import com.biigoh.tilemap.MapLoader;
import com.biigoh.utils.Consts;
import com.biigoh.utils.MathMan;
import com.biigoh.utils.TileMapLoader;
import com.biigoh.view.BattleArena;
import com.biigoh.view.ChaseCamera;
//import com.biigoh.view.ChaseCameraAccessor;

public class BattleScreen extends AbstractScreen {
	
	// TODO: Fix physics time steps and stuff
	private final float PHYSICS_SPEED;
	
	// FOR DEBUGGING THIS CLASS
	private static final boolean DEBUG_POSITIONS = false;
	public static final boolean DEBUG = false;

	public static final int ENEMY_NUM = 3;
	public final String LOG = "@ " + BattleScreen.class.getSimpleName();
		
	private static World world;	// Physics World representation of game

	private Vehicle heroMustang;	// our Hero! ie, main player
//	private Vehicle enemyMustang;

	private static BattleArena battleArena;	// Where the objects are and the objects themselves

	private static Stage battleStage; // This stage will hold and update all of our game objects
	private Stage hudStage;	// Game HUD for buttons and controls
//	private Stage hudStage2;	// Game HUD for buttons and controls
	
	// Physics World Width and Height, use this to get the Pixel Width and Height
	public final int CAM_METER_WIDTH = 24;	// 1280px Screen Width	(24 ORIGINAL)
	public final int CAM_METER_HEIGHT = 16;	// 768px Screen Height	(16 ORIGINAL)
	
	
//	private GameMap gameMap;

	private OrthographicCamera cameraMiniMap;

	private SpriteBatch batchMiniMap;
	
	public BattleScreen(TwistedRubber game) {
		super(game);
		// adjust the physics world update speed depending on
		// whether we're running on Mobile or Desktop
		if( Gdx.app.getType() == ApplicationType.Desktop )
			PHYSICS_SPEED = 70;
		else
			PHYSICS_SPEED = 50;
	}	

	public static World getPhysicsWorld() {
		return world;
	}

	public static Stage getBattleStage() {
		return battleStage;
	}

	/** Called when the Screen is first displayed */
	@Override
	public void show() {
		super.show();
		// ------------------ Setup the Battle Stage and its Camera -------------------- //
		// get rid of the old stage
//		getMainStage().dispose();
		// Like they say, set the Stage. Width and Height are in Pixels
		setMainStage( new Stage( CAM_METER_WIDTH * Consts.P2M_RATIO, CAM_METER_HEIGHT * Consts.P2M_RATIO, false ) );
		battleStage = getMainStage();

		// --------------------------- Physics World -------------------------------- //
		// create the physics world with no gravity
		world = new World( new Vector2(0, 0), false ); // change doSleep to true
		

		// ---------------- LOAD TILE MAP ---------------------------------- //
//		gameMap = new GameMap( CAM_METER_WIDTH, CAM_METER_HEIGHT );

		levelLoader = new MapLoader( battleStage.getCamera() );
		levelLoader.loadMapLevel( 2 );
		
		
		// ------------- Create a new Game --------------------- //
		// Set the Games current state, which will hold things like
		// player positions and items on the field, points won, and such				
		battleArena = new BattleArena( levelLoader.getWidth(), levelLoader.getHeight(), world );
		
		
		
		////////////////////////////////////////////////////////////
		// create our super hero, and place him somewhere using meters and give him a name
		heroMustang = new Mustang( 24, 18, 0 );

		//////////////////////////////////////////////////////////
		// create the joystick and add it to the screen at the given position
		Joystick joyStick = new Joystick( 110, 110 );
		
		// create the a button to fire our weapons
		TextureRegion textureRegion = new TextureRegion( Assets.FireButton );
		FireButton fireButton = new FireButton( textureRegion );

		// create a button to switch between weapons, or toggle weapons* Consts.PIXEL_METER_RATIO
		textureRegion = new TextureRegion( Assets.SwitchButton );
		SwitchButton switchButton = new SwitchButton( textureRegion );
		
		// fix the button locations and sizes
		fireButton.x = (CAM_METER_WIDTH - (CAM_METER_WIDTH * 0.3f)) * Consts.P2M_RATIO;
		fireButton.y = (CAM_METER_HEIGHT - (CAM_METER_HEIGHT * 0.92f)) * Consts.P2M_RATIO;
		fireButton.width = 104 * 1.1f;
		fireButton.height = 104 * 1.1f;
		switchButton.x = (CAM_METER_WIDTH - (CAM_METER_WIDTH * 0.16f)) * Consts.P2M_RATIO;
		switchButton.y = (CAM_METER_HEIGHT - (CAM_METER_HEIGHT * 0.82f)) * Consts.P2M_RATIO;
		switchButton.width = 104 * 1.1f;
		switchButton.height = 104 * 1.1f;
		/*
		fireButton.x = 580;
		fireButton.y = 16;
		fireButton.width = 104;
		fireButton.height = 104;
		switchButton.x = 680;
		switchButton.y = 64;
		switchButton.width = 104;
		switchButton.height = 104;
		*/
		/////////////////////////////////////////////////////////////
		
		
		// ---------- Add the controls to the cars --------------- //		
		heroMustang.setController( new HumanController(joyStick, fireButton, switchButton) );
		battleArena.addVehicle(heroMustang);
		// ----------- Add all the Cars to the Stage ------------- //		

		// add our little guys to the Scene, or Stage
		battleStage.addActor( heroMustang );
		
		addEnemies( ENEMY_NUM );
		addWeapons( 10 );

		// add the rest of the game objects here
		// ...


		// Initialize the a Chase Camera to chase our Hero
		ChaseCamera chaseCam = new ChaseCamera(CAM_METER_WIDTH, CAM_METER_HEIGHT, battleArena);
		chaseCam.setCarToChase( heroMustang );	// TAKE THIS OUT FOR SPLITSCREEN	
		
		battleStage.setCamera( chaseCam );
		
//		gameMap.setCamera( (OrthographicCamera) battleStage.getCamera() );
		levelLoader.setCamera( chaseCam );
		
		
		
		/////////////////////////////////////////////////////////
		// Create a new Stage for the HUD so that the
		// HUD does not move when the camera moves
		hudStage = new Stage( Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false );

		// add the JoyStick and the other buttons to the HUD
		hudStage.addActor( joyStick );
		hudStage.addActor( fireButton );
		hudStage.addActor( switchButton );

		
		////////////////////////////////////////////////////////////
				
		///////////////////////////////
		// set up our contact listener to handle all collisions
		ContactListener contactListener = new CollisionListener( battleArena );
		// Change the Physics Handler from the default to our spanking new Contact Listener
		world.setContactListener( contactListener );
		///////////////////////////////
		
		
		// ------------- Font for debugging ---------------------- //
		font = new BitmapFont();
		font.setScale( 0.8f );
		
		debugBox = new Sprite( new Texture( Gdx.files.internal("debug/DebugBox.png") ) );
		debugBox.setScale( 1f, 1.4f);
		debugBox.setPosition( 0, hudStage.getCamera().viewportHeight - 110 );
		
		// ------------- FONT FOR GAME --------------- //
		gameFont = new BitmapFont();
		gameFont.setScale( 5 );
		gameFont.setColor( 1, 0.2f, 0.2f, 1 );
		// --------------- Set up our MiniMap ------------------ //
	    cameraMiniMap = new OrthographicCamera( CAM_METER_WIDTH, CAM_METER_HEIGHT );
	    cameraMiniMap.zoom = 8;
	    batchMiniMap = new SpriteBatch();
	    
	    gameStarted = false;
	    gameEnded = false;

		// Setup our Tween Engine
//		Tween.registerAccessor(ChaseCamera.class, new ChaseCameraAccessor());		
		// Create a new Tween Manager to manage animations
//		manager = new TweenManager();
		
	}
	
	BitmapFont gameFont;
	TileMapLoader tml;
	TileMapRenderer tileMapRenderer;
	static MapLoader levelLoader;
	
	public static void addEnemies( int numEnemiesToAdd ) {
		
		EntityData.Type[] carTypes = { EntityData.Type.ROADSTER,
										EntityData.Type.MUSTANG,
										EntityData.Type.TRUCK,
										EntityData.Type.BOSS1 };
		int randType;
		
		while( numEnemiesToAdd > 0 ) {
			float randPosX = MathUtils.random( 60, levelLoader.getWidth() / Consts.P2M_RATIO );
			float randPosY = MathUtils.random( 60, levelLoader.getWidth() / Consts.P2M_RATIO );		
			randType = MathMan.random(3);
			
			Vehicle enemy;
			
			switch( carTypes[randType] ) {
			case ROADSTER:
				enemy = new Roadster( randPosX, randPosY, 0 );
				break;
			case MUSTANG:
				enemy = new Mustang( randPosX, randPosY, 0 );
				break;
			case TRUCK:
				enemy = new Truck( randPosX, randPosY, 0 );
				break;
			case BOSS1:
				enemy = new Boss1( randPosX, randPosY, 0 );
				break;
			default:
				enemy = new Boss1( randPosX, randPosY, 0 );
			}
			 
//			enemy.setController( new AIController(enemy) );
			enemy.setController( new AiBehavior() );
			enemy.getController().Start();
						
			battleArena.addVehicle( enemy );	
			battleStage.addActor( enemy );
			
			numEnemiesToAdd--;
		}
	}
	
	public void addWeapons( int numWeapons ) {

		// TODO: Change these to Weapons, not projectiles!
		EntityData.Type[] weaponTypes = { EntityData.Type.SNIPER,
											EntityData.Type.STINGER,
											EntityData.Type.MACHINEGUN };

		Weapon weapon;		
		for( int i=0; i < numWeapons; i++ ) {
			
			int randPosX = MathUtils.random( 5, (int) levelLoader.getWidth() - 5 );
			int randPosY = MathUtils.random( 5, (int) levelLoader.getWidth() - 5 );
			int randType = MathMan.random(2);
			
			switch( weaponTypes[randType] ) {
			case SNIPER:
				weapon = new Sniper();
				break;
			case STINGER:
				weapon = new Stinger();
				break;
			case MACHINEGUN:
				weapon = new MachineGun();
				break;
			default:
				weapon = new MachineGun();
			}
				
			weapon.setTransform( randPosX, randPosY, 0 );
			
			battleArena.addWeapon( weapon );
			battleStage.addActor( weapon );
		}
		
	}
	
	BitmapFont font;
	
	// For keeping track of a constant frame rate
	private long timeBeforeUpdate;
	private long timeDiff;
	private FixedFrameRate fps = new FixedFrameRate();
	
	/**
	 * Called when the screen should render itself.
	 * @param delta The time difference since the last render
	 */
	@Override
	public void render(float delta) {
		debugStr = "FPS: " + Gdx.graphics.getFramesPerSecond() + "\n";
		
		//time before we update and render
		timeBeforeUpdate = System.currentTimeMillis();
				
        // Update elapsed time since zoom start
        if (_zoomTime > 0.0f)
            _zoomTime -= delta;
        
        // Update everything that needs tweening
        // manager.update(delta);

		update(delta);
		
		if( gameEnded ) return;
		
		draw(delta);
		
		//time difference between before and after the update and render cycle
		timeDiff = System.currentTimeMillis() - timeBeforeUpdate;
		//check if we need to put the thread to sleep, or catch up
//		fps.sleep(timeDiff, this, delta);		
	}
	

	float countDownTimer = 0;
	private boolean gameStarted;
	private boolean gameEnded;
	
	private int endCounter = 0;
	
	public void update(float delta) {		

		
		if( gameEnded ) {
			gameStr = "Game Over :(";
//			endCounter += delta;
			while( endCounter < 10000 ) {
				endCounter += 1;
			}
			
			mGame.setScreen( new MenuScreen( mGame ) );				
		}
		
		// Count down before starting
		if( !gameStarted && !gameEnded) {
			
			countDownTimer += delta;
			if( countDownTimer > 1 && countDownTimer < 2 )
				// show number 3
				gameStr = "3";
				
			if( countDownTimer > 2 && countDownTimer < 3 )
				// show number 2
				gameStr = "2";
			
			if( countDownTimer > 3 && countDownTimer < 4 )
				// show number 1
				gameStr = "1";
			
			if( countDownTimer > 4 && countDownTimer < 5 )
				// start game
				gameStr = "GO";
				
			if( countDownTimer > 5 ) {
				gameStr = "";
				gameStarted = true;
				// Add our input processors, like joystick and buttons
				mGame.setInputProcessor( hudStage );		
				if( Gdx.app.getType() == ApplicationType.Desktop )
					mGame.setRemoteProcessor( hudStage );
			}
		}
		
		
		if( gameStarted && !gameEnded ) {
			// Update the Physics World, use 1/45 for something around 45 Frames/Second for mobile devices
			world.step( 1/PHYSICS_SPEED, 8, 3 ); 	// 1/45 for devices	but we make it 1/90 so its twice the frame rate
		
			// Update all Game Objects' positions and such
			battleStage.act( delta );
			// Update the Joystick and Buttons
			hudStage.act( delta );			
			// update the game world
			battleArena.update();

			score = heroMustang.getScore();
			
			if( heroMustang.getState() == EntityData.State.DEAD ) {
				gameEnded = true;			
				gameStarted = false;
			}
			
			/*
			// Tween camera so that it zooms and moves around smoothly
			float curSpeed = MathMan.aRound( heroMustang.currentSpeed, 0 );
			if( curSpeed > lastCalculatedSpeed )
				handleCameraZoom(2);			
			else if( curSpeed < lastCalculatedSpeed )
				handleCameraZoom(0);
			*/
		}
		
	}

	
	Sprite debugBox;
	String gameStr = "";
	public void draw(float delta) {

		// Clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 1f, 1f, 1f, 1f );
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// --------------------------------------------------- //
	    // update our camera
//	    cameraMiniMap.update();

	    // set the projection matrix for our batch so that it draws
	    // with the zoomed out perspective of the minimap camera
	    batchMiniMap.setProjectionMatrix(cameraMiniMap.combined);
//	    cameraMiniMap.position.set( Gdx.graphics.get, y, z)
	    
	    // draw the player
//	    batchMiniMap.begin();
//	    	batchMiniMap.draw( heroMustang.getSprite(), heroMustang.getPosition().x, heroMustang.getPosition().y );
//	    batchMiniMap.end();
	    // ---------------------------------------------------- //
	    
		// draw our tiles first so they're lower		
//		gameMap.render();
//	    tml.render();
//		tileMapRenderer.render();
	    
//	    tileMapRenderer.render( (OrthographicCamera) battleStage.getCamera() );
	    levelLoader.render();
	    
			
		// render the Battle Stage, ie all the game objects
		battleStage.draw();
		
		// render the player's HUD
		hudStage.draw();
		

		// update the camera, ie also moving it to the player's new position
		ChaseCamera chaseCam = (ChaseCamera) battleStage.getCamera();
		// TODO: Zooms out too quickly in the beggining.. FIX IT!
		chaseCam.update();
		
		calculateSpeed( heroMustang, delta );		
		debugStr += "MPH Speed: " + MathMan.aRound(mph, 1) + " mph \n";
//		debugStr += "Actual Speed: " + MathMan.aRound(heroMustang.currentSpeed, 1) + " SomeUnit \n";
		debugStr += "Hero Health: " + heroMustang.getArmor() + "\n";
		
		hudStage.getSpriteBatch().begin();
			debugBox.draw( hudStage.getSpriteBatch() );
			font.setColor( 0, 0, 0, 1 );
			font.drawMultiLine(hudStage.getSpriteBatch(), debugStr, 10, hudStage.getCamera().viewportHeight - 20);
			gameFont.draw( hudStage.getSpriteBatch(), gameStr, hudStage.getCamera().viewportWidth / 2 - 140, hudStage.getCamera().viewportHeight / 2 + 80 );
		hudStage.getSpriteBatch().end();
	}
	
	public static String debugStr = "";
	
	float lastClockedTime = 0;
	Vector2 changeInPosition;
	Vector2 lastPosition = new Vector2(0,0);
	float mph = 0;
	float mps = 0;

	private int score = 0;;
	
	private float calculateSpeed( GameObject gameObject, float delta ) {		
		
		lastClockedTime += delta;
		
		if( lastClockedTime > 0.1f ) {	// if we passed one second
			
			Vector2 curPosition = gameObject.getPosition();
			changeInPosition = curPosition.sub( lastPosition );			
			lastPosition = gameObject.getPosition(); 	// update the last position of this car immediately			
			mps = changeInPosition.len();	// distance traveled in one second in meters				
			mph = mps * 2.237f; // convert to miles per hour
			mph = mph / lastClockedTime;	// This will keep the speed consistent since we might have passed more than 1 seconds
			
			lastClockedTime = 0;
		}
				
		return mph;	
	}	
	
	@Override
	public void hide() {

		// Save state
		
		// <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//		FileHandle fileHandle = Gdx.files.internal("data/score.txt");
//		
////		String content = fileHandle.readString();
////		String[] lines = content.split("\n");
////		ArrayList<String> lineList = new ArrayList<String>();
////		for( String line : lines )
////			Integer.parseInt( line.split(",")[1] );
//		
//		String scoreItem = "Level 2 : " + score; 
//		byte[] bArray = scoreItem.getBytes();
//		
//		Gdx.app.log("file content", fileHandle.readString());
//		
//		OutputStream os = null;
//		try {
//			os = fileHandle.write(true);
//			os.write(bArray);
//			os.close();
//		} catch (Exception e) {
//			Gdx.app.log("ERROR", e.getLocalizedMessage(), e);
//		}
		
		
		dispose();
	}

	/** Called when the screen is closed.  Dispose of everything.. leave no fingerprints */
	@Override
	public void dispose() {
		
//		Iterator<Body> bodyIter = world.getBodies();
//		while( bodyIter.hasNext() )
//			world.destroyBody( bodyIter.next() );
		
		world.dispose();
		battleStage.dispose();
		hudStage.dispose();	
		super.dispose();
	}
	

    // Fields needed
//    private OrthographicCamera _camera;
    private float _cameraInitZoom = 1.0f;
    private float _cameraMaxZoom = _cameraInitZoom / 8;    
    private float _newZoomAmount;
    private boolean _zooming = false;
    private float _zoomTime = 0.0f;

//	TweenManager manager;

	public float lastCalculatedSpeed = 0;	
	
//    /**
//     * Handle the zoom tweening
//     * @param amount Direction of the zoom ( < 1 || > 1 )
//     */
//    private void handleCameraZoom(int amount) {
//    	ChaseCamera chaseCam = (ChaseCamera) battleStage.getCamera();
//    	
//        float zoomTweenLength = 0.5f;
//        float zoomInitialStep = 0.2f;
//        float zoomIncrementalStep = 0.12f;
//
//        if (!_zooming && _zoomTime == 0.0f) {
//            // Starting new zoom
//            _newZoomAmount = chaseCam.zoom + (zoomInitialStep * Math.signum(amount));
//            _newZoomAmount = MathMan.clamp(_newZoomAmount, _cameraMaxZoom, _cameraInitZoom);
//            _zoomTime = zoomTweenLength;
//            _zooming = true;
//
//            Tween.to(chaseCam, ChaseCameraAccessor.ZOOM, _zoomTime)
//                    .target(_newZoomAmount)
//                    .setCallback(zoomingEnded)
//                    .ease(TweenEquations.easeInOutQuad)
//                    .start(manager);
//        } else {
//            // We're in the middle of a tween, and we received another zoom request, increment the target amount
//            _newZoomAmount += zoomIncrementalStep * Math.signum(amount);
//            _newZoomAmount = MathMan.clamp(_newZoomAmount, _cameraMaxZoom, _cameraInitZoom);
//
//            Tween.to(chaseCam, ChaseCameraAccessor.ZOOM, _zoomTime)
//                .target(_newZoomAmount)
//                .setCallback(zoomingEnded)
//                .ease(TweenEquations.easeOutQuad)
//                .start(manager);
//        }
//    }
//
//    /**
//     * Zoomimg end callback
//     */
//    private final TweenCallback zoomingEnded = new TweenCallback() {
//        @Override
//        public void onEvent(int eventType, BaseTween<?> source) {
//            _newZoomAmount = 0.0f;
//            _zooming = false;
//            _zoomTime = 0.0f;
//        }
//    };
	

}
