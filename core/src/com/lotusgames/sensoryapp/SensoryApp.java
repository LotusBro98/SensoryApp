package com.lotusgames.sensoryapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.lotusgames.sensoryapp.device.Device;
import com.lotusgames.sensoryapp.device.DeviceConnection;
import com.lotusgames.sensoryapp.device.DeviceManager;
import com.lotusgames.sensoryapp.device.Segment;
import com.lotusgames.sensoryapp.widgets.MenuButton;
import com.lotusgames.sensoryapp.widgets.MenuWindow;
import com.lotusgames.sensoryapp.widgets.DecisionButton;
import com.lotusgames.sensoryapp.widgets.SensoryGrid;

import java.io.IOException;

public class SensoryApp extends ApplicationAdapter {
	private Stage stage;

	private DeviceManager deviceManager;
	private Settings settings;
	private GameCounter gameCounter;
	private SensoryGrid leftGrid;
	private SensoryGrid rightGrid;
	private DecisionButton leftButton;
	private DecisionButton rightButton;
	private MenuWindow menuWindow;

	public SensoryApp(DeviceConnection deviceConnection) {
		settings = new Settings();
		settings.devicePortOptions = deviceConnection.availablePorts();
		this.deviceManager = new DeviceManager(deviceConnection, settings);
    }



	@Override
	public void create () {
		stage = new Stage(new StretchViewport(640, 480));
		Gdx.input.setInputProcessor(stage);

		leftGrid = new SensoryGrid(10, 170, 300, 300, deviceManager.device, settings);
		rightGrid = new SensoryGrid(330, 170, 300, 300, deviceManager.device, settings);

		gameCounter = new GameCounter(settings, leftGrid, rightGrid);

		leftButton = new DecisionButton(10, 10, 300, 150, settings, gameCounter, true);
		rightButton = new DecisionButton(330, 10, 300, 150, settings, gameCounter, false);

		stage.addActor(leftGrid);
		stage.addActor(rightGrid);
		stage.addActor(leftButton);
		stage.addActor(rightButton);

		menuWindow = new MenuWindow(-320, 0, 320, 480, settings, gameCounter, deviceManager);

		stage.addActor(menuWindow);
		stage.addActor(new MenuButton(0, stage.getViewport().getScreenHeight() - 40, 40, 40, settings, gameCounter, menuWindow));

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
