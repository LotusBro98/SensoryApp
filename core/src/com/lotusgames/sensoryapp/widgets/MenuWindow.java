package com.lotusgames.sensoryapp.widgets;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.lotusgames.sensoryapp.GameCounter;
import com.lotusgames.sensoryapp.Settings;
import com.lotusgames.sensoryapp.device.DeviceManager;

import java.util.Arrays;

public class MenuWindow extends Table {
    Settings settings;
    GameCounter gameCounter;
    DeviceManager deviceManager;
    Skin skin;

    private void configDevicePort() {
        SelectBox<String> devicePort = new SelectBox<>(skin);
        devicePort.setItems(settings.devicePortOptions);
        if (Arrays.asList(settings.devicePortOptions).contains(settings.devicePort)) {
            devicePort.setSelected(settings.devicePort);
        }
        devicePort.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.devicePort = devicePort.getSelected();
            }
        });
        add(new Label("Device Port:", skin)).pad(4);
        add(devicePort).width(100).pad(4);
        row();
    }

    private void configTauSelect() {
        SelectBox<Double> tauSelect = new SelectBox<>(skin);
        tauSelect.setItems(10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0);
        tauSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.tau_us = tauSelect.getSelected().floatValue();
            }
        });
        add(new Label("Impulse strength (tau, us):", skin)).pad(4);
        add(tauSelect).width(100).pad(4);
        row();
    }

    private void configInitDevice() {
        TextButton initDevice = new TextButton("Init Device", skin);
        initDevice.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                deviceManager.initDevice(settings.devicePort);
                return true;
            }
        });
        add(initDevice).width(150).colspan(2).pad(4);
        row();
    }

    private void configResetGame() {
        TextButton resetGame = new TextButton("Reset Game", skin);
        resetGame.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameCounter.resetGame();
                return true;
            }
        });
        add(resetGame).width(150).colspan(2).pad(4);
        row();
    }

    private void configShowLines() {
        CheckBox showLines = new CheckBox("Show lines ", skin);
        showLines.setChecked(settings.drawLines);
        showLines.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.drawLines = showLines.isChecked();
            }
        });
        add(new Label("", skin)).pad(4);
        add(showLines).pad(4);
        row();
    }

    private void configCorrectPart() {
        Label correctPart = new Label("", skin);
        gameCounter.addCallback(() -> correctPart.setText("%d %%".formatted((int)(gameCounter.getCorrectPart() * 100))));
        add(new Label("Correct percent:", skin)).pad(4);
        add(correctPart).width(100).pad(4);
    }

    public MenuWindow(float x, float y, float width, float height, Settings settings, GameCounter gameCounter, DeviceManager deviceManager) {
        this.settings = settings;
        this.gameCounter = gameCounter;
        this.deviceManager = deviceManager;

        setBounds(x, y, width, height);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        setDebug(false);
        setFillParent(true);

        configDevicePort();
        configTauSelect();
        configInitDevice();
        configResetGame();
        configShowLines();
        configCorrectPart();
    }

    private ShapeRenderer renderer = new ShapeRenderer();
    @Override
    protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 0, 0, 0.7f);
        renderer.rect(0, 0, getWidth(), getHeight());

        renderer.end();
        batch.begin();
    }
}
