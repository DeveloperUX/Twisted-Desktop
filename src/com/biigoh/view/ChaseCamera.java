package com.biigoh.view;

//import aurelienribon.tweenengine.BaseTween;
//import aurelienribon.tweenengine.Tween;
//import aurelienribon.tweenengine.TweenCallback;
//import aurelienribon.tweenengine.TweenEquations;
//import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.launch.EntityData;
import com.biigoh.screens.BattleScreen;
import com.biigoh.utils.Consts;
import com.biigoh.utils.MathMan;

public class ChaseCamera extends OrthographicCamera {
	
	private static final boolean DEBUG_RAYS = true;
	private static final boolean DEBUG_FORCES = false;
	
	private final String LOG = ChaseCamera.class.getSimpleName();
	private final boolean DEBUG = false;	
	private boolean debugPhysics = true;
	private boolean debugRaycasts = false;
	
	private BattleArena arena;
	private Vehicle carToChase;
	
	// FOR DEBUGGING BOX2D BODIES
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera debugCam;
	
	Rectangle camBoundsPix;
	
	private final float MAX_ZOOM = 160;
	
	
	public ChaseCamera( float cameraWidth, float cameraHeight, BattleArena pGameWorld ) {
		this( cameraWidth, cameraHeight, pGameWorld, null );
	}
	
	public ChaseCamera( float meterWidth, float meterHeight, BattleArena pGameWorld, Vehicle carToChase ) {
		super( meterWidth * Consts.P2M_RATIO, meterHeight * Consts.P2M_RATIO );
		this.carToChase = carToChase;
		this.arena = pGameWorld;

		// The Box2D Debug Renderer will handle rendering all physics objects for debugging
		debugRenderer = new Box2DDebugRenderer( true, true, true, true );
		// Setup the Debug camera that handles drawing boxes around all Physics Bodies. 
		// In Box2D we operate on a meter scale, pixels won't do it. So we use
        // an Orthographic camera with a Viewport width and height given in meters.
		debugCam = new OrthographicCamera( meterWidth, meterHeight );
		
		camBoundsPix = new Rectangle( viewportWidth / 2, viewportHeight / 2, arena.width * Consts.P2M_RATIO - viewportWidth / 2, arena.height * Consts.P2M_RATIO - viewportHeight / 2 );
		
	}
	
	/**
	 * Whether or not to make the camera also draw physics lines
	 * @param debugging True for Physics Debugging
	 */
	public void setDebug( boolean debugging ) {
		debugPhysics = debugging;
	}
	
	
	/**
	 * 
	 * @param carToChase
	 */
	public void setCarToChase( Vehicle carToChase ) {
		this.carToChase = carToChase;
	}

	ShapeRenderer renderer = new ShapeRenderer();
	
	/**
	 * Recalculate the camera's new position
	 * @param debugPhysics Whether or not to turn on Physics World Debug boxes
	 */
	@Override
	public void update() {
		
		if( arena == null )
			return;

		try {
			if( carToChase.getArmor() <= 0 ) {			
				carToChase = arena.getHighestScoreCar();
				if(DEBUG) Gdx.app.log( LOG, "Car Removed, switched to: " + carToChase.name );
			}
	
			if( carToChase == null )
				return;
			
			if( debugPhysics ) 			
				renderDebugging();		
	
			
		} catch( Exception e ) {
			return;
		}
		
		camBoundsPix.set( viewportWidth * zoom * 0.5f, viewportHeight * zoom * 0.5f,
				arena.width * Consts.P2M_RATIO - viewportWidth * zoom * 0.5f, arena.height * Consts.P2M_RATIO - viewportHeight * zoom * 0.5f);
		
		
		if(DEBUG) Gdx.app.log( LOG, "update() :: Chasing car: " + carToChase );
		
		// If this car is being removed than start chasing another car, possibly
		// the next car with the highest number of points
		if( carToChase.getBody() == null ) {
			carToChase = arena.getHighestScoreCar();
			if(DEBUG) Gdx.app.log( LOG, "update() :: No car to chase, chose another car: " + carToChase.getID() );
			// If there are no more cars left then stop moving the camera around
			if( carToChase == null ) {			
				if(DEBUG) Gdx.app.log( LOG, "update() :: Still no car to chase! Cancel" );
				return;
			}
			
		}
		
		// update the camera's zoom depending on the speed of the car
//		updateZoom(carToChase.currentSpeed);
		
		// move the camera to the player's new position.
		// ie: Make the camera chase the player around
//		updatePosition(carToChase);
		
		
		position.set( 0, 0, 0 );
		zoom = 5;

		if(DEBUG) Gdx.app.log( LOG, "update() :: Zoom level: " + zoom );
		
		// Constrain the camera's viewport to be within the game's bounds
		constrainWithinBounds( camBoundsPix, position.x, position.y );
		
		super.update();
	}
	
	public void constrainWithinBounds( Rectangle meterBounds, float meterPosX, float meterPosY ) {
		if( position.x < camBoundsPix.x ) {
			position.x = camBoundsPix.x;
			debugCam.position.x = camBoundsPix.x / Consts.P2M_RATIO;
		}
		if( position.y < camBoundsPix.y ) {
			position.y = camBoundsPix.y;
			debugCam.position.y = camBoundsPix.y / Consts.P2M_RATIO;
		}
		if( position.x > camBoundsPix.width ) {
			position.x = camBoundsPix.width;
			debugCam.position.x = camBoundsPix.width / Consts.P2M_RATIO;
		}
		if( position.y > camBoundsPix.height ) {
			position.y = camBoundsPix.height;
			debugCam.position.y = camBoundsPix.height / Consts.P2M_RATIO;
		}
	}
	
	public static Vector2 colLine = new Vector2(0,0);
	
	public void renderDebugging() {		
//		debugCam.update();		
		debugCam.zoom = zoom;		
		// Loop through all Game Objects
		for( Vehicle car : carToChase.getArena().getCarList() ) {
						
			Vector2 pos = car.getPosition().cpy();
			
			renderer.setProjectionMatrix( debugCam.combined );					
			renderer.begin( ShapeType.FilledCircle );
				// Interesting or Special point
				renderer.setColor( Color.WHITE );	
				renderer.filledCircle(pos.x, pos.y, 0.3f, 10 );
			renderer.end();

			renderer.begin( ShapeType.Line );
				if( DEBUG_RAYS ) {
					// Get the distance to look ahead of us depending on how fast we're moving
					float distanceToLookAhead = MathMan.aScaleValue( car.currentSpeed, 0, 80, 10, 50 );
					// Get a position vector from that distance
					Vector2 pointAhead = MathMan.aPointFromDirection( car.getPosition(), car.getAngle(), distanceToLookAhead );
					Vector2 rayLeft = MathMan.aPointFromDirection( car.getPosition(), car.getAngle() + (8/distanceToLookAhead), distanceToLookAhead * 0.5f );
					Vector2 rayRight = MathMan.aPointFromDirection( car.getPosition(), car.getAngle() - (8/distanceToLookAhead), distanceToLookAhead * 0.5f );
					
					// Raycast Vector
					renderer.setColor( Color.BLUE );
					renderer.line( pos.x, pos.y, pointAhead.x, pointAhead.y );
					renderer.setColor( Color.GREEN );
					renderer.line( pos.x, pos.y, rayLeft.x, rayLeft.y );
					renderer.setColor( Color.GREEN );
					renderer.line( pos.x, pos.y, rayRight.x, rayRight.y );
//					renderer.line( colLine.x, colLine.y, col
				}
				if( DEBUG_FORCES ) {
					// Impulse Vector
					renderer.setColor( Color.ORANGE );	
					renderer.line( pos.x, pos.y, pos.x + car.curImpulse.x,  pos.y + car.curImpulse.y );
					// Forward Velocity
					renderer.setColor( Color.GRAY );
					renderer.line( pos.x, pos.y, pos.x + car.curForward.x, pos.y + car.curForward.y );
					// Lateral Velocity
					renderer.setColor( Color.DARK_GRAY );		
					renderer.line( pos.x, pos.y, pos.x + car.curLateral.x, pos.y + car.curLateral.y );
					// Linear Velocity
					renderer.setColor( Color.BLACK );	
					renderer.line( pos.x, pos.y, pos.x + car.curLinear.x, pos.y + car.curLinear.y );
				}
				// Special Debug Line for other temporary debug stuff
				renderer.setColor( Color.WHITE );	
				renderer.line( pos.x, pos.y, pos.x + car.debugLine.x, pos.y + car.debugLine.y );
			renderer.end();
			
		}
				
		// update the Camera matrices and call the debug renderer
		debugCam.update();		
		debugCam.position.set( carToChase.getPosition().x, carToChase.getPosition().y, 0 );
		debugRenderer.render( BattleScreen.getPhysicsWorld(), debugCam.combined );			
	}
	
	
	public void updatePosition( Vehicle carToChase ) {
		float distanceAhead = MathMan.aScaleValue( carToChase.currentSpeed, 0, 200, 1, 10 );
		Vector2 pointAhead = MathMan.aPointFromDirection( carToChase.getPosition(), carToChase.getAngle(), distanceAhead );
		
		Vector2 linearVelocity = carToChase.getBody().getLinearVelocity().cpy();
		Vector2 targetPosition = linearVelocity.mul( 1/linearVelocity.len() ).mul(distanceAhead * Consts.P2M_RATIO);
		targetPosition = carToChase.getPosition().cpy().add(targetPosition);
		
		position.set( carToChase.getPosition().x * Consts.P2M_RATIO * 1, carToChase.getPosition().y * Consts.P2M_RATIO * 1, 0 );
//		position.set( pointAhead.x * Consts.P2M_RATIO * 1.1f, pointAhead.y * Consts.P2M_RATIO * 1.1f, 0 );
		
		if(DEBUG) Gdx.app.log( LOG, "updatePosition() :: Inertia: " + carToChase.getBody().getInertia() );
		if(DEBUG) Gdx.app.log( LOG, "updatePosition() :: Distance Ahead: " + pointAhead );
		
	}
	
	public float lastCalculatedSpeed = 0;	
	
	public void updateZoom( float carSpeed ) {		
//		float curSpeed = MathMan.aRound( carToChase.currentSpeed, 0 );		
		// TODO: Use CarToChase instead of carVel, and add functionality for Sniper Zoom Out
		zoom = MathMan.aScaleValue( carSpeed, 0, MAX_ZOOM, 3, 5) * 1.2f;
		/*
		if( carSpeed > lastCalculatedSpeed ) {
			zoom = MathMan.aScaleValue( carSpeed, 0, MAX_ZOOM, 1.5f, 6) * 1.2f;
		}		
		else 
			zoom -= 0.01f;		
		*/		
		// LIMIT
//		if( zoom < 2 ) 
//			zoom = 2;
		
		if(DEBUG) Gdx.app.log( LOG, "Current Speed & Last Speed: " + MathMan.aRound( carSpeed, 2 ) + " & " + MathMan.aRound( lastCalculatedSpeed, 2 ));
		if(DEBUG) Gdx.app.log( LOG, "New Camera Zoom: " + MathMan.aRound( zoom, 2 ) );

		lastCalculatedSpeed = carSpeed;
	}
	
	
}
