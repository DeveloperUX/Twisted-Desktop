package com.biigoh.screens;

import java.util.prefs.BackingStoreException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.badlogic.gdx.utils.Scaling;
import com.biigoh.launch.TwistedRubber;

public class MenuScreen extends AbstractScreen {
	
	public final boolean DEBUG = false;
	public final String LOG = "@ " + MenuScreen.class.getSimpleName();
	
	// STAGE = SCENE
	// ACTOR = ENTITY
	
	Image backgroundImage;

	public MenuScreen( TwistedRubber game ) {
		super(game);	
	}

	/**
	 * This method is called when the Menu Screen is created.  It
	 * initializes everything then loads the Menu for display.
	 */
	@Override
	public void show() { 
		
		super.show();
		 
	    // load the splash image and create the texture region
	    Texture splashTexture = new Texture( "splash/MenuScreen.png" );
	
	    // we set the linear texture filter to improve the stretching
	    splashTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );

	    //---------
        // in the image atlas, our splash image begins at (0,0) of the
        // upper-left corner and has a dimension of 512x301
        TextureRegion splashRegion = new TextureRegion( splashTexture, 0, 0, 800, 480);
		// here we create the splash image actor and set its size
        backgroundImage = new Image( splashRegion, Scaling.stretch, Align.BOTTOM | Align.LEFT );
        //---------
//	    backgroundImage = new Image( splashTexture );
	    
	    getMainStage().addActor( backgroundImage );
	}
	
	
	@Override
	public void render( float delta ) {
		super.render(delta);
		
//		mGameSpriteBatch.begin();
//			backgroundImage.draw( mGameSpriteBatch, 1 );
//		mGameSpriteBatch.end();
	}

	/**
	 * Called when the Menu Screen is created or when the window is 
	 * resized.  This can be used for initiating the window since it's
	 * called whenever the screen is redrawn.
	 * @param width The new width of the screen after being redrawn
	 * @param height The new height of the screen after being redrawn
	 */
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		if(DEBUG) Gdx.app.log( LOG, "resize() :: Creating a Table Layout" );

		//
		backgroundImage.width = width;
		backgroundImage.height = height;
        
        // retrieve the skin (created on the AbstractScreen class)
        Skin skin = super.getSkin();
 
        // create the table actor.  This will be used for the layout of
        // the menu buttons.  The table takes up the whole screen
        Table menuTable = new Table( getSkin() );
        menuTable.width = width;
        menuTable.height = height;
 
        // add the table to the stage and retrieve its layout
        getMainStage().addActor( menuTable );
        TableLayout layout = menuTable.getTableLayout();
 
        // register the button "Play"
        TextButton playButton = new TextButton( "PLAY", skin );
        playButton.setClickListener( new ClickListener() {
        	// On Play button click, Lock and Load Baby!
            @Override
            public void click( Actor actor, float x, float y ) {
            	if(DEBUG) Gdx.app.log( LOG, "Play button clicked. WHOO!" );
                mGame.setScreen( new BattleScreen( mGame ) );            	
            }
        } );
        
        layout.register( "playButton", playButton );
        
        // register the button "options"
        TextButton levelButton = new TextButton( "Pick Level", skin );
        levelButton.setClickListener( new ClickListener() {
            @Override
            public void click( Actor actor, float x, float y ) {
            	if(DEBUG) Gdx.app.log( LOG, "resize() :: About to load Options Screen" );
                //game.setScreen( game.getOptionsScreen() );
            }
        } );
        
        layout.register( "optionsButton", levelButton );
 
        // register the button "hall of fame"
        TextButton highScoreButton = new TextButton( "High Score", skin );
        highScoreButton.setClickListener( new ClickListener() {
            @Override
            public void click( Actor actor, float x, float y ) {
            	if(DEBUG) Gdx.app.log( LOG, "resize() :: About to load High Score Screen" );
                //game.setScreen( game.getHighScoreScreen() );
            }
        } );
        layout.register( "highScoreButton", highScoreButton );
 
        // finally, parse the layout descriptor
        layout.parse( Gdx.files.internal( "layout/menu-screen.txt" ).readString() );
	}

	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
