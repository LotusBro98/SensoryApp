package com.lotusgames.sensoryapp;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.lotusgames.sensoryapp.SensoryApp;
import com.lotusgames.sensoryapp.device.DeviceConnection;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("SensoryApp");

		DeviceConnection deviceConnection = new WindowsCOMPort();
		new Lwjgl3Application(new SensoryApp(deviceConnection), config);
	}
}
