package com.biigoh.sound;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.biigoh.gameObjects.vehicles.Vehicle;
import com.biigoh.launch.EntityData;
import com.biigoh.resources.Assets;
import com.biigoh.utils.Consts;
import com.biigoh.utils.MathMan;
import com.biigoh.utils.Vector2Pool;


public class VehicleSound extends SoundSystem {
	
	public static boolean DEBUG = true;
	public static String LOG = VehicleSound.class.getSimpleName();

	private Sound driftSound;	// sound made when drifting
	private Sound driveSound;	// sound made when casually driving
	private Sound shotSound;	// <-- put in Weapon class
	
	public boolean playDriftSound = false;
	public boolean playDriveSound = false;
	public boolean playFireSound = false;
	
	public boolean driftSoundPlaying = false;
	public boolean driveSoundPlaying = false;
	
	private long driftSoundID;
	private long driveSoundID;
	
	private Vehicle car;
	
	// Sound Keys
	public static final int DRIVE_SOUND = 0;
	public static final int DRIFT_SOUND = 1;
	public static final int SHOT_SOUND = 2;
	
	public VehicleSound( Vehicle car ) {
		
		this.car = car;
		
		// init our tuple of Sounds and their IDs
		sound_map = new HashMap<Integer, Sound>();

		driveSound = Assets.DriveSound_1;
		driftSound = Assets.DriftSound;
		shotSound = Assets.ShotSound;

		sound_map.put( DRIVE_SOUND, driveSound );
		sound_map.put( DRIFT_SOUND, driftSound );
		sound_map.put( SHOT_SOUND, shotSound );
	}
	
	public void playSound( int soundID ) {}
	
	float distantVolume;

	@Override
	public void update( Vehicle carSoundToPlay ) {
		
//		// loop through all sounds
//		for( Sound s : sound_map.values() ) {
//			// if that sound is playing check if we should stop playing it
//			if( )
//			// if that sound is not playing check if we should start playing it
//			// if that sound is not playing but it IS set to be playing, play it again
//		}
		try {
			if( car.getState() == EntityData.State.DEAD )
				return;
		} catch( Exception e ) {
			return;
		}
		
		Vector2 cam_pos = Vector2Pool.obtain( car.getStage().getCamera().position.x, car.getStage().getCamera().position.y );
		distantVolume = MathMan.aDistanceBetweenPoints( cam_pos.mul( 1/Consts.P2M_RATIO ), car.getPosition() );
		distantVolume = 1 - MathMan.aScaleValue( distantVolume, 0, car.getArena().width, 0, 1);
		
//		if(DEBUG) Gdx.app.log( LOG, "Distance from camera: " + distantVolume );
		
		driftSound.setVolume( driftSoundID, distantVolume );
		driveSound.setVolume( driftSoundID, distantVolume );
		
		
		// check if we are playing the drift sound
		if( driftSoundPlaying ) {
			// if so, check if we should stop playing
			if( playDriftSound == false ) {
				// stop playing the sound 
				driftSound.stop( driftSoundID );
				// update drift sound state
				driftSoundPlaying = false;	
			}
		}
		// if not playing the drifting sound
		else {
			// check if we should play
			if( playDriftSound ) {
				// if so, start playing
				//driftSound.play( Settings.effectsVolume ); TODO: Add Settings class
				driftSoundID = driftSound.play();				
				// update drift sound state
				driftSoundPlaying = true;				
			}
		}
		
		// check if we are playing the drift sound
		if( driveSoundPlaying ) {
			// if so, check if we should stop playing
			if( playDriveSound == false ) {
				// stop playing the sound 
				driveSound.stop( driveSoundID );
				// update drive sound state
				driveSoundPlaying = false;	
			}
		}
		// if not playing the driveing sound
		else {
			// check if we should play
			if( playDriveSound ) {
				// if so, start playing
				//driveSound.play( Settings.effectsVolume ); TODO: Add Settings class
				driveSoundID = driveSound.play();
				// update drive sound state
				driveSoundPlaying = true;	
			}
		}
		

	}

	public void playShotSound() {
		shotSound.play( distantVolume );
	}
	
	
}
