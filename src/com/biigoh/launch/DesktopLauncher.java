package com.biigoh.launch;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;


public class DesktopLauncher {
	public static void main (String[] argv) {
		new LwjglApplication(new TwistedRubber(), "Twisted Rubber!", 800, 480, true);
	}
}
