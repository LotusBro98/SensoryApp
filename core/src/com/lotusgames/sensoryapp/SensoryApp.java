package com.lotusgames.sensoryapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.lotusgames.sensoryapp.device.Device;
import com.lotusgames.sensoryapp.device.DeviceConnection;
import com.lotusgames.sensoryapp.device.Segment;
import com.lotusgames.sensoryapp.widgets.SelectButton;
import com.lotusgames.sensoryapp.widgets.SensoryGrid;

import java.io.IOException;

public class SensoryApp extends ApplicationAdapter {
	private Stage stage;

	private DeviceConnection deviceConnection;
	private Device device;
	private Settings settings;
	private GameCounter gameCounter;
	private SensoryGrid leftGrid;
	private SensoryGrid rightGrid;
	private SelectButton leftButton;
	private SelectButton rightButton;

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
		initDevice(settings.devicePort);

		stage = new Stage(new StretchViewport(640, 480));
		Gdx.input.setInputProcessor(stage);

		leftGrid = new SensoryGrid(10, 170, 300, 300, device, settings);
		rightGrid = new SensoryGrid(330, 170, 300, 300, device, settings);

		gameCounter = new GameCounter(settings, leftGrid, rightGrid);

		leftButton = new SelectButton(10, 10, 300, 150, settings, gameCounter, true);
		rightButton = new SelectButton(330, 10, 300, 150, settings, gameCounter, false);

		stage.addActor(leftGrid);
		stage.addActor(rightGrid);
		stage.addActor(leftButton);
		stage.addActor(rightButton);

		gameCounter.resetGame();
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
		stage.dispose();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
