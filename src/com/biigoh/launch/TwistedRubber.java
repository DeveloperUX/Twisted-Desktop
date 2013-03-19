package com.biigoh.launch;

/*******************************************************************************
 *
 ******************************************************************************/

import java.net.BindException;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.RemoteInput;
import com.biigoh.screens.BattleScreen;
import com.biigoh.screens.MenuScreen;
import com.biigoh.screens.SplashScreen;

/**
 * This class is only in charge of initiating the settings and starting
 * the first screen which is the Splash Screen.
 * 
 * @author BiiGo Games
 */
public class TwistedRubber extends Game {
	
	public static final String LOG = TwistedRubber.class.getSimpleName();
	public static final boolean ON_PHONE = false;
			
	private FPSLogger fpsLogger;
	
	private InputMultiplexer inputMultiplexer; 
	
	// All of our game screens
	private MenuScreen menuScreen;
	private BattleScreen battleScreen;
	private SplashScreen splashScreen;
//	private SplitScreen splitScreen;
	
	public static RemoteInput remote_1;
	public static RemoteInput remote_2;


	/**
	 * Called when the application is first created.
	 */
	@SuppressWarnings("unused")
	@Override
	public void create() {
		
        Gdx.app.log( LOG, "Creating game" );
        
        
        
        fpsLogger = new FPSLogger();
                
		// Ignore the whole powers of two warning
		Texture.setEnforcePotImages( false );
		
		// Create an object that will process user input but can also
		// hold many user input processors, like buttons and such
		inputMultiplexer = new InputMultiplexer();
		
		// by default the Mouse and Keyboard input will always be processed
		Gdx.input.setInputProcessor( inputMultiplexer );
		
		if( Gdx.app.getType() == ApplicationType.Desktop ) {
			for( int attempt = 0; attempt < 5; attempt++ ) {
				try {
					remote_1 = new RemoteInput( 9997 + attempt );
					remote_2 = new RemoteInput( 9988 + attempt );		
					break;
				} catch( Exception bindException ) {
					Gdx.app.log( LOG, "Exception Binding Remote at port: " + (9997 + attempt) );
				}
			}
					
		}	
		
		// load our tiled map
//		/TiledMap map = TiledLoader.createMap( Gdx.files.internal("data/output") );
		
		
		// initialize all of our game screens
		menuScreen = new MenuScreen( this );
		battleScreen = new BattleScreen( this );
		splashScreen = new SplashScreen( this );
//		splitScreen = new SplitScreen( this );
		
	}
	
	/**
	 * Get a reference to the MenuScreen, mainly used for 
	 * switching screens.
	 * @return a Reference to the Game Menu Screen
	 */
	public MenuScreen getMenuScreen() {
		return menuScreen;
	}

	/**
	 * Reference to the BattleScreen for screen switching
	 * @return a Reference to the Game's Battle Screen
	 */
	public BattleScreen getBattleScreen() {
		return battleScreen;
	}

	/**
	 * Reference to the SplashScreen for screen switching
	 * Call this when you want to switch to the Splash Screen
	 * @return a Reference to the Game's Splash Screen
	 */
	public SplashScreen getSplashScreen() {
		return splashScreen;
	}	
	
	public void setInputProcessor( InputProcessor input ) {
		Gdx.input.setInputProcessor( input );
	}
	
	public void disposeInputProcessors() {
		
		if(Gdx.input.getInputProcessor() != null )
			Gdx.input.setInputProcessor(null);
		
		if( remote_1 != null && remote_1.getInputProcessor() != null )
			remote_1.setInputProcessor(null);

		if( remote_2 != null && remote_2.getInputProcessor() != null )
			remote_2.setInputProcessor(null);
		
	}
	
	public void setRemoteProcessor( InputProcessor processor ) {
							
		if( remote_1.getInputProcessor() == null ) {
//			System.out.println( "SETTING REMOTE 1" );		
			remote_1.setInputProcessor(processor);
		}
		
		else {
//			System.out.println( "SETTING REMOTE 2" );
			remote_2.setInputProcessor(processor);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#render()
	 */
	@Override
	public void render() {        
        super.render();           
//		fpsLogger.log();	// Log the current frame rate
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#pause()
	 */
	@Override
	public void pause() {		
		Gdx.app.log( LOG, "Pausing game" );
		super.pause();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#resume()
	 */
	@Override
	public void resume() {
		Gdx.app.log( LOG, "Resuming game" );
		super.resume();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		super.resize( width, height );
		Gdx.app.log( LOG, "Resizing game to: " + width + " x " + height );

        // show the splash screen when the game is resized for the first time
        if( getScreen() == null ) {
            super.setScreen( new SplashScreen( this ) );
        }

	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#dispose()
	 */
	@Override
	public void dispose() {
		Gdx.app.log( LOG, "Disposing game" );
		super.dispose();
		disposeInputProcessors();		
	}


}
