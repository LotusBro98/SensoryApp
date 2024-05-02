package com.lotusgames.sensoryapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lotusgames.sensoryapp.device.Device;
import com.lotusgames.sensoryapp.device.DeviceConnection;
import com.lotusgames.sensoryapp.device.Segment;

import java.io.IOException;

public class SensoryApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	private DeviceConnection deviceConnection;
	private Device device;

	public SensoryApp(DeviceConnection deviceConnection) {
		this.deviceConnection = deviceConnection;
		this.device = new Device(deviceConnection);
    }

	private Boolean initDevice(String portName) {
		try {
			deviceConnection.open(portName);
			int addr = device.self_test();
			device.add_segment(new Segment(
					"finger",
					addr, new int[]{0, 1, 2, 3, 4, 5, 6, 7},
					addr, new int[]{12, 13, 14, 15},
					500
			));
			device.initialize();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		initDevice("COM5");

		MyInputProcessor inputProcessor = new MyInputProcessor(device);
		Gdx.input.setInputProcessor(inputProcessor);
    }

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
