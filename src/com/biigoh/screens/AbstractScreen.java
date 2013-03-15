package com.biigoh.screens;

import java.io.IOException;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.biigoh.launch.TwistedRubber;

public abstract class AbstractScreen implements Screen {
	
	public static final String LOG = AbstractScreen.class.getSimpleName();

    public int ScreenType;	// What type of screen this is
    public static final int SPLASH_SCREEN = 0;
    public static final int MENU_SCREEN = 1;
    public static final int BATTLE_SCREEN = 2;
    public static final int HIGHSCORE_SCREEN = 3;
    public static final int OPTIONS_SCREEN = 4;

	private static final boolean DEBUG = false;
    
	public TwistedRubber mGame;	// Reference to the main game application for switching screens
	public SpriteBatch mGameSpriteBatch;	// Sprite batch for rendering objects in the game
	private Stage mainStage;	// A heirarichal scene graph for managing actors, or game objects
	
	private Skin mGameSkin;	// A reference to the Game's look and feel, the Skin singleton
	private BitmapFont mGameFont;	// A reference to the Game's font used
	private TextureAtlas gameTextureAtlas;	// The main texture atlas where we get all of the textures & sprites
	
	public AbstractScreen( TwistedRubber game ) {
		mGame = game;
		mGameSpriteBatch = new SpriteBatch();
		setMainStage( Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false );
		// instantiate all objects used in the screen here, like fonts images, audio, etc.
		mGameSkin = getSkin();
		mGameFont = getBitmapFont();
		//this.gameTextureAtlas = getTextureAtlas();
	}
	
	/**
	 * Get the Stage currently present on the viewport
	 * @return The stage currently being used.
	 */
	public final Stage getMainStage() {
		return mainStage;
	}
	
	/**
	 * Set the game Scene, or Stage.  This will hold all the objects and update them.
	 * Set the width and height of the scene and whether you want the scene to stretch
	 * if it is too small.
	 * @param width The width of the scene
	 * @param height The height of the scene
	 * @param stretch Whether or not to stretch the Scene if it is smaller than viewport
	 */
	public final void setMainStage( float width, float height, boolean stretch ) {
		mainStage = new Stage( width, height, stretch );
	}

	/**
	 * Set the game Scene, or Stage.  This will hold all the objects and update them.
	 * Set the width and height of the scene and whether you want the scene to stretch
	 * if it is too small.
	 * @param width The width of the scene
	 * @param height The height of the scene
	 * @param stretch Whether or not to stretch the Scene if it is smaller than viewport
	 */
	public final void setMainStage( Stage customStage ) {
		mainStage = customStage;
	}
	

	@Override
	public void render(float delta) {
        // Clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 0.1f, 0.1f, 0.1f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		// update all Actors movements in the game Stage
        mainStage.act( delta );        
		// Draw each Actor onto the Scene at their new positions
        mainStage.draw();
	}
	

	@Override
	public void resize(int width, int height){
		
       if(DEBUG) Gdx.app.log( LOG, "Resizing screen: " + 
        		getClass().getSimpleName() + " to: " + width + " x " + height );
        
        // resize our stage
//        mainStage.setViewport( width, height, false );
	}

	@Override
	public void show(){
		if(DEBUG) Gdx.app.log( LOG, "Showing Screen: " + getClass().getSimpleName() );
		// redirect all of our input handling to the Stage Scene Graph object
		//Gdx.input.setInputProcessor( mainStage );
		//TwistedRubber.remote1.setInputProcessor( mainStage );
		
		mGame.setInputProcessor( mainStage );
		if( Gdx.app.getType() == ApplicationType.Desktop )
			mGame.setRemoteProcessor( mainStage );
	}

	@Override
	public void hide(){
        if(DEBUG) Gdx.app.log( LOG, "Hiding screen: " + getClass().getSimpleName() );
//        Gdx.input.setInputProcessor( null );	// Stop processing input and touch events
//        TwistedRubber.remote1.setInputProcessor( null );        
		dispose();	// Call dispose here or dispose will never be called
	}

	@Override
	public void pause(){
		if(DEBUG) Gdx.app.log( LOG, "Pausing screen: " + getClass().getSimpleName() );
	}

	@Override
	public void resume(){
		if(DEBUG) Gdx.app.log( LOG, "Resuming screen: " + getClass().getSimpleName() );
	}

	@Override
	public void dispose(){
		if(DEBUG) Gdx.app.log( LOG, "Disposing screen: " + getClass().getSimpleName() );
		// make sure to get rid of all our resources we created here
		
//		if( mainStage != null ) mainStage.dispose();
//		if( mGameSpriteBatch != null ) mGameSpriteBatch.dispose();
	}	

	/**
	 * Create a Bitmap Font if one does not exist.  This allows us to load
	 * fonts that render quickly but are more barebones, I believe.
	 * @return A reference to the Bitmap Font singleton
	 */
    public BitmapFont getBitmapFont() {
        if( mGameFont == null ) {
            mGameFont = new BitmapFont();
        }
        return mGameFont;
    }

    /**
     * Create a new Sprite Batch if there isn't one.  If there is return
     * it.  The Sprite Batch can be used to render our Sprites quickly.
     * @return A reference to the Sprite Batch singleton
     */
    public SpriteBatch getBatch() {
        if( mGameSpriteBatch == null ) {
            mGameSpriteBatch = new SpriteBatch();
        }
        return mGameSpriteBatch;
    }

    /**
     * The Texture Atlas is the large image containing all of our sprites.
     * This is quicker than loading each individual sprite when needed.
     * @return A reference to the single Texture Atlas for the whole game.
     */
    public TextureAtlas getTextureAtlas() {
        if( gameTextureAtlas == null ) {
            gameTextureAtlas = new TextureAtlas( Gdx.files.internal( "image-atlases/pages-info" ) );
        }
        return gameTextureAtlas;
    }

    /**
     * This will be used for the overall look and feel of UI elements like
     * buttons, text, and widgets.
     * @return A reference to the Game's theme Skin
     */
    protected Skin getSkin() {
    	
        if( mGameSkin == null ) {
        	//FileHandle skinFile = Gdx.files.internal("font/skin.json")
            FileHandle skinFile = Gdx.files.internal( "skin/uiskin.json" );
            FileHandle textureFile = Gdx.files.internal( "skin/uiskin.png" );
            mGameSkin = new Skin( skinFile, textureFile );
        }
        
        return mGameSkin;
    }


}
