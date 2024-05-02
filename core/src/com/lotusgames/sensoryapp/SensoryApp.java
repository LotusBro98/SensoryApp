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
import com.lotusgames.sensoryapp.widgets.CorrectSplash;
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
		int width = 1280;
		int height = 720;
		int pad = 20;
		float cell_w = width / 2.0f;
		float cell_h = height * 0.75f;
		int menubtn = 40;

		stage = new Stage(new StretchViewport(width, height));
		Gdx.input.setInputProcessor(stage);

		leftGrid = new SensoryGrid(pad, height - cell_h + pad, cell_w - 1.5f * pad, cell_h - 2 * pad, deviceManager.device, settings);
		rightGrid = new SensoryGrid(cell_w + 0.5f * pad, height - cell_h + pad, cell_w - 1.5f * pad, cell_h - 2 * pad, deviceManager.device, settings);

		gameCounter = new GameCounter(settings, leftGrid, rightGrid);

		leftButton = new DecisionButton(pad, pad, cell_w - 1.5f * pad, height - cell_h - pad, settings, gameCounter, true);
		rightButton = new DecisionButton(cell_w + 0.5f * pad, pad, cell_w - 1.5f * pad, height - cell_h - pad, settings, gameCounter, false);

		stage.addActor(leftGrid);
		stage.addActor(rightGrid);
		stage.addActor(leftButton);
		stage.addActor(rightButton);

		menuWindow = new MenuWindow(0, 0, width, height, settings, gameCounter, deviceManager);

		stage.addActor(menuWindow);
		stage.addActor(new MenuButton(0, height - menubtn, menubtn, menubtn, settings, gameCounter, menuWindow));

		stage.addActor(new CorrectSplash(0, 0, width, height, gameCounter));

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
