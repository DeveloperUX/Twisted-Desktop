package com.biigoh.sound;

import java.util.HashMap;

import com.badlogic.gdx.audio.Sound;
import com.biigoh.gameObjects.GameObject;
import com.biigoh.gameObjects.vehicles.Vehicle;

/**
 * Each {@link GameObject} has a specific Sound Component with a different implementation.
 * This class defines what a sound component must have or be.
 * 
 * @author BiiGo Games
 */
public abstract class SoundSystem {
	
	public static int DRIFT = 1;
	public static int DRIVE = 2;
	public static int SHOT = 3;
	public static int MISSILE = 4;
	
	HashMap<Integer, Sound> sound_map;
	
	
	/**
	 * Checks what sounds are currently playing and what sounds should be playing
	 * @param gameObject The {@link GameObject} that is initiated the sounds
	 */
	public abstract void update( Vehicle carSoundToPlay );
}
