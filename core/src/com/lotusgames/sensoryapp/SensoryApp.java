package com.lotusgames.sensoryapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.lotusgames.sensoryapp.device.Device;
import com.lotusgames.sensoryapp.device.DeviceConnection;
import com.lotusgames.sensoryapp.device.Segment;

import java.io.IOException;

public class SensoryApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private Stage stage;

	private DeviceConnection deviceConnection;
	private Device device;
	private Settings settings;
	private SensoryGridActor leftGrid;
	private SensoryGridActor rightGrid;

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
					addr, settings.pinsA,
					addr, settings.pinsB,
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
		settings = new Settings();

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		initDevice(settings.devicePort);

		stage = new Stage(new StretchViewport(640, 480));
		Gdx.input.setInputProcessor(stage);

		leftGrid = new SensoryGridActor(10, 170, 300, 300, device, settings);
		rightGrid = new SensoryGridActor(330, 170, 300, 300, device, settings);

		stage.addActor(leftGrid);
		stage.addActor(rightGrid);

		leftGrid.makeGrid(settings.linesMin);
		rightGrid.makeGrid(settings.linesMax);
    }

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		stage.dispose();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
