package com.biigoh.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.biigoh.launch.TwistedRubber;
import com.biigoh.resources.Assets;

/**
 * This Screen handles everything related to our Splash Screen which 
 * is the first thing the user will see.
 * @author BiiGo
 */
public class SplashScreen extends AbstractScreen {
    
	public final boolean DEBUG = false;
	public final String LOG = "@ " + SplashScreen.class.getSimpleName();
	
    private Texture splashTexture;	
//    public AssetManager assetManager;	// Handles loading and unloading of all assets
    
    private TextureRegion progressTextureRegion;	
    private boolean doneLoading = false;

	Image splashImage;
	FadeOut fadeOut;
	
    /**
     * The constructor simply calls the {@link AbstractScreen} constructor which
     * saves a reference to the Game Application
     * @param game A reference to the main Game Application in case we need
     * to change Screens.
     */
	public SplashScreen( TwistedRubber game ) {		
		super( game );		
		ScreenType = SPLASH_SCREEN;	// this is a Splash Screen
	}

	/**
	 * This method is called when the Splash Screen is created.  It
	 * initializes everything then loads the Screen for display.
	 */
	@Override
	public void show() {		
		super.show();	

		// Load all the textures the game will be using asynchronously
		Assets.load();	

	    // load the splash image and create the texture region
	    splashTexture = new Texture( "splash/SplashScreen.png" );	    
	
	    // we set the linear texture filter to improve the stretching
	    splashTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
	    
        // in the image atlas, our splash image begins at (0,0) of the
        // upper-left corner and has a dimension of 512x301
        TextureRegion splashRegion = new TextureRegion( splashTexture, 0, 0, 800, 480);
		// here we create the splash image actor and set its size
        splashImage = new Image( splashRegion, Scaling.stretch, Align.BOTTOM | Align.LEFT );

		
	}
	
	@Override
	public void render(float delta) {        
		super.render( delta );
        
		// Continue to load more junk or assets that are in the queue
		Assets.update();
		
		// How much stuff have we loaded?
		float progress = Assets.getProgress();
		
		if(DEBUG) Gdx.app.log(  LOG, "render() :: Progress of Asset Loading: " + progress + "%" );	
		
		if( doneLoading ) {
			// Get rid of the Splash Image and its actions
			splashImage.clearActions();
			splashImage.remove();
            // when the image is faded out, move on to the next screen
            mGame.setScreen( new MenuScreen( mGame ) );
		}		
		
		// Is our stuff loaded yet?!
		if( progress == 1 ) {
			if(DEBUG) Gdx.app.log(  LOG, "render() :: Loading Menu Screen!" );
			// The fadeOut action has an associated callback function that will load the Menu Screen
	        splashImage.action( fadeOut );
		}
		
		
		
//	    // we use the SpriteBatch to draw 2D textures (it is defined in our base class: AbstractScreen)
//	    mGameSpriteBatch.begin();	
//		    // we tell the batch to draw the Splash Screen starting at (0,0) of the 
//	    	// lower-left corner with the size of the screen
//	    	splashImage.draw( mGameSpriteBatch, 1 );
//	    // the end method does the drawing
//	    mGameSpriteBatch.end();
	    
	}
	
	/**
	 * Called when the Splash Screen is created or when the window is 
	 * resized.  Here we can tell the Splash Screen to take up the whole Window
	 */
	@Override
	public void resize(int width, int height) {	
		super.resize( width, height );
		// Clear the Stage which manages our actors
		getMainStage().clear();
        

        splashImage.width = width;
        splashImage.height = height;

        // this is needed for the fade-in effect to work correctly; we're just
        // making the image completely transparent
        splashImage.color.a = 0f;

        // configure the fade-in/out effect on the splash image
        FadeIn fadeIn = FadeIn.$( 0.95f ); // 
        fadeOut = FadeOut.$( 0.25f );
        
        splashImage.action( fadeIn );
        
        if(DEBUG) Gdx.app.log(  LOG, "show() :: Starting sequence action" );
        
        // This method will be called once we finish loading everything
        fadeOut.setCompletionListener( new OnActionCompleted() {
            @Override
            public void completed( Action action ) {
            	if(DEBUG) Gdx.app.log(  LOG, "resize() :: finished sequence action, now loading Menu" );
    			doneLoading = true;       
            }
        } );        
        
        // Add the only actor which is our Splash Image
        getMainStage().addActor( splashImage );
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		splashTexture.dispose();	// remove the Texture
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		//super.dispose();
		splashTexture.dispose();	// remove the Texture
	}
	
	

}
