/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.biigoh.resources;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	
	public static final String LOG = Assets.class.getSimpleName();
	public static final boolean DEBUG = true;
			
	public static Texture background;
	public static TextureRegion backgroundRegion;

	public static Texture items;
	public static Texture ControlBase;
	public static Texture ControlKnob;

	public static Animation coinAnim;
	public static Animation bobJump;
	public static Animation bobFall;
	public static Animation squirrelFly;
	public static Animation brakingPlatform;
	
	public static BitmapFont font;

	public static Music music;
	
	public static Sound DriveSound_1;
	public static Sound DriveSound_2;
	public static Sound DriveSound_3;
	public static Sound DriftSound;
	public static Sound CrashSound;
	public static Sound ShotSound;
	
	private static AssetManager manager;
		
	public static Texture FireButton;
	public static Texture SwitchButton;
	
	public static Texture Boss_1;
	public static Texture Mustang_1;
	public static Texture Mustang_2;
	public static Texture Mustang_3;
	public static Texture Roadster_1;
	public static Texture Roadster_2;
	public static Texture Roadster_3;
	public static Texture Truck_1;
	public static Texture Truck_2;
	public static Texture Truck_3;
	
	public static Texture MachineGunBullet;
	public static Texture SniperRound;
	public static Texture SeekerMissile;
	
	public static Texture DebugBox;
	
	public static Texture Cannon;
	public static Texture Sniper;
	public static Texture Stinger;
	public static Texture MachineGun;
	public static Texture RPG;
	public static Texture HeavyGun;

	private HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
	
	private enum AssetType {
		TEXTURE,
		TEXTURE_REGION,
		TEXTURE_ATLAS,
		SOUND,
		ANIMATION
	}
	
	
	public static boolean load() {		
		manager = new AssetManager();
		
		try {
			load_images();
			load_sounds();
			load_animations();
			load_music();
		} 
		
		catch( Exception e ) {	return false;	}
		
		return true;
	}
	
	
	private static void load_images() {
		// Tell the Asset Manager exactly which files to add to the queue for loading
		load( "hud/aFireButton.png", AssetType.TEXTURE );
		load( "hud/aSwitchButton.png", AssetType.TEXTURE );
		load( "hud/controlBase.png", AssetType.TEXTURE );
		load( "hud/controlKnob.png", AssetType.TEXTURE );
		
		load( "cars/Boss_1.png", AssetType.TEXTURE );
		load( "cars/Roadster_1.png", AssetType.TEXTURE );
		load( "cars/Roadster_2.png", AssetType.TEXTURE );
		load( "cars/Roadster_3.png", AssetType.TEXTURE );
		load( "cars/Mustang_1.png", AssetType.TEXTURE );
		load( "cars/Mustang_2.png", AssetType.TEXTURE );
		load( "cars/Mustang_3.png", AssetType.TEXTURE );
		load( "cars/Truck_1.png", AssetType.TEXTURE );
		load( "cars/Truck_2.png", AssetType.TEXTURE );
		load( "cars/Truck_3.png", AssetType.TEXTURE );
		
		load( "projectiles/MachineGunBullet.png", AssetType.TEXTURE );
		load( "projectiles/SniperRound.png", AssetType.TEXTURE );
		load( "projectiles/SeekerMissile.png", AssetType.TEXTURE );
		
		load( "weapons/Cannon.png", AssetType.TEXTURE );
		load( "weapons/Sniper.png", AssetType.TEXTURE );
		load( "weapons/Stinger.png", AssetType.TEXTURE );
		load( "weapons/MachineGun.png", AssetType.TEXTURE );
		load( "weapons/RPG.png", AssetType.TEXTURE );
		load( "weapons/HeavyGun.png", AssetType.TEXTURE );
		
		load( "debug/DebugBox.png", AssetType.TEXTURE );		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T get( String filename, AssetType type ) {
		T object = null;
		
		switch( type ) {
		case TEXTURE:
			object = (T) manager.get( filename, Texture.class );
			break;
		case TEXTURE_ATLAS:
			object = (T) manager.get( filename, TextureAtlas.class );
			break;
		case TEXTURE_REGION:
			object = (T) manager.get( filename, TextureRegion.class );
			break;
		case SOUND:
			object = (T) manager.get( filename, Sound.class );
			break;
		}
		
		return object;
	}
	
	private static void load( String filename, AssetType type ) {
		
		try {	
			switch( type ) {
			case TEXTURE:
				manager.load( filename, Texture.class );
				break;
			case TEXTURE_ATLAS:
				manager.load( filename, TextureAtlas.class );
				break;
			case TEXTURE_REGION:
				manager.load( filename, TextureRegion.class );
				break;
			case SOUND:
				manager.load( filename, Sound.class );
				break;
			case ANIMATION:
				manager.load( filename, Animation.class );
				break;
			}		
		}
		
		catch( Exception e ) {	if(DEBUG) Gdx.app.log( LOG, "loadTexture() :: Could not load texture!" );	}
		
	}
	
	private static void load_sounds() {
		load( "sound/drift_1.ogg", AssetType.SOUND );
		load( "sound/drive_1.ogg", AssetType.SOUND );
		load( "sound/speedup_b.ogg", AssetType.SOUND );
		load( "sound/speedup_m.ogg", AssetType.SOUND );
		load( "sound/shot_1.ogg", AssetType.SOUND );
	}
	
	private static void load_animations() {
//		Animation explosion = new Animation( 0.2f, new TextureR)
	}
	private static void load_music() {}

	//======================================
	// Public interface for using resources
	//=======================================
	public static void playSound( Sound sound ) {
		// TODO: Make a Map for Sound obj and name		
		if (Settings.soundEnabled) 
			sound.play( 1 );
	}
	

	//======================================
	//  Called Constantly for Updates
	//=======================================
	public static void update() {
		manager.update();
	}
	
	public static float getProgress() {
		if( manager.getProgress() == 1 )
			initAssets();
		return manager.getProgress();
	}
	
	

	public static void initAssets() {
		// Tell the Asset Manager exactly which files to add to the queue for loading
		FireButton = get( "hud/aFireButton.png", AssetType.TEXTURE );
		SwitchButton = get( "hud/aSwitchButton.png", AssetType.TEXTURE );
		ControlBase = get( "hud/controlBase.png", AssetType.TEXTURE );
		ControlKnob = get( "hud/controlKnob.png", AssetType.TEXTURE );
		
		Boss_1 = get( "cars/Boss_1.png", AssetType.TEXTURE );
		Roadster_1 = get( "cars/Roadster_1.png", AssetType.TEXTURE );
		Roadster_2 = get( "cars/Roadster_2.png", AssetType.TEXTURE );
		Roadster_3 = get( "cars/Roadster_3.png", AssetType.TEXTURE );
		Mustang_1 = get( "cars/Mustang_1.png", AssetType.TEXTURE );
		Mustang_2 = get( "cars/Mustang_2.png", AssetType.TEXTURE );
		Mustang_3 = get( "cars/Mustang_3.png", AssetType.TEXTURE );
		Truck_1 = get( "cars/Truck_1.png", AssetType.TEXTURE );
		Truck_2 = get( "cars/Truck_2.png", AssetType.TEXTURE );
		Truck_3 = get( "cars/Truck_3.png", AssetType.TEXTURE );
		
		MachineGunBullet = get( "projectiles/MachineGunBullet.png", AssetType.TEXTURE );
		SniperRound = get( "projectiles/SniperRound.png", AssetType.TEXTURE );
		SeekerMissile = get( "projectiles/SeekerMissile.png", AssetType.TEXTURE );
		
		// Init Weapons
		Cannon = get( "weapons/Cannon.png", AssetType.TEXTURE );
		Sniper = get( "weapons/Sniper.png", AssetType.TEXTURE );
		Stinger = get( "weapons/Stinger.png", AssetType.TEXTURE );
		MachineGun = get( "weapons/MachineGun.png", AssetType.TEXTURE );
		RPG = get( "weapons/RPG.png", AssetType.TEXTURE );
		HeavyGun = get( "weapons/HeavyGun.png", AssetType.TEXTURE );
		
		DebugBox = get( "debug/DebugBox.png", AssetType.TEXTURE );	
		
		DriftSound = get( "sound/drift_1.ogg", AssetType.SOUND );
		DriveSound_3 = get( "sound/drive_1.ogg", AssetType.SOUND );
		DriveSound_2 = get( "sound/speedup_b.ogg", AssetType.SOUND );
		DriveSound_1 = get( "sound/speedup_m.ogg", AssetType.SOUND );
		ShotSound = get( "sound/shot_1.ogg", AssetType.SOUND );
	}
	
		/*
		background = loadTexture("data/aHudInfo.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
		
		items = loadTexture("data/items.png");		
		mainMenu = new TextureRegion(items, 0, 224, 300, 110);
		return true;	// Splash screen loaded successfuly
		
		
		//background = loadTexture("data/background.png");
		background = loadTexture("data/aHudInfo.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
		
		items = loadTexture("data/items.png");		
		mainMenu = new TextureRegion(items, 0, 224, 300, 110);
		pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
		ready = new TextureRegion(items, 320, 224, 192, 32);
		gameOver = new TextureRegion(items, 352, 256, 160, 96);
		highScoresRegion = new TextureRegion(Assets.items, 0, 257, 300, 110 / 3);
		logo = new TextureRegion(items, 0, 352, 274, 142);
		soundOff = new TextureRegion(items, 0, 0, 64, 64);
		soundOn = new TextureRegion(items, 64, 0, 64, 64);
		arrow = new TextureRegion(items, 0, 64, 64, 64);
		pause = new TextureRegion(items, 64, 64, 64, 64);

		spring = new TextureRegion(items, 128, 0, 32, 32);
		castle = new TextureRegion(items, 128, 64, 64, 64);
		coinAnim = new Animation(0.2f, new TextureRegion(items, 128, 32, 32, 32), new TextureRegion(items, 160, 32, 32, 32),
			new TextureRegion(items, 192, 32, 32, 32), new TextureRegion(items, 160, 32, 32, 32));
		bobJump = new Animation(0.2f, new TextureRegion(items, 0, 128, 32, 32), new TextureRegion(items, 32, 128, 32, 32));
		bobFall = new Animation(0.2f, new TextureRegion(items, 64, 128, 32, 32), new TextureRegion(items, 96, 128, 32, 32));
		bobHit = new TextureRegion(items, 128, 128, 32, 32);
		squirrelFly = new Animation(0.2f, new TextureRegion(items, 0, 160, 32, 32), new TextureRegion(items, 32, 160, 32, 32));
		platform = new TextureRegion(items, 64, 160, 64, 16);
		brakingPlatform = new Animation(0.2f, new TextureRegion(items, 64, 160, 64, 16), new TextureRegion(items, 64, 176, 64, 16),
			new TextureRegion(items, 64, 192, 64, 16), new TextureRegion(items, 64, 208, 64, 16));

		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);

		music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
		music.setLooping(true);
		music.setVolume(0.5f);
		//if (Settings.soundEnabled) music.play();
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/jump.ogg"));
		highJumpSound = Gdx.audio.newSound(Gdx.files.internal("data/highjump.ogg"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.ogg"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.ogg"));
		clickSound = Gdx.audio.newSound(Gdx.files.internal("data/click.ogg"));
		*/
	

}

