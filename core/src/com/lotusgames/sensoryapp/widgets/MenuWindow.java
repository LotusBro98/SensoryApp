package com.lotusgames.sensoryapp.widgets;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.lotusgames.sensoryapp.GameCounter;
import com.lotusgames.sensoryapp.Settings;
import com.lotusgames.sensoryapp.device.DeviceConnection;
import com.lotusgames.sensoryapp.device.DeviceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuWindow extends Table {
    Settings settings;
    GameCounter gameCounter;
    DeviceManager deviceManager;
    Skin skin;
    SelectBox<DeviceConnection.Port> devicePort;
    DeviceConnection.Port nullport = new DeviceConnection.Port(null, null);

    private void alert(String text) {
        getStage().addActor(new Notification(text, Notification.NotificationType.FAIL));
    }

    public void reloadDevicePorts() {
        ArrayList<DeviceConnection.Port> options = new ArrayList<>(Arrays.asList(settings.devicePortOptions));
        options.add(0, nullport);
        DeviceConnection.Port[] options_arr = new DeviceConnection.Port[options.size()];
        options.toArray(options_arr);
        devicePort.setItems(options_arr);
        if (options.contains(settings.devicePort)) {
            devicePort.setSelected(settings.devicePort);
        } else {
            devicePort.setSelected(nullport);
        }
    }

    public boolean open() {
        addAction(Actions.moveBy(0, -getHeight(), 0.5f, Interpolation.exp5));
        return true;
    }

    public boolean close() {
        if (settings.devicePort == null) {
            alert("Please select Device Port and press Init Device");
            return false;
        }
        if (!deviceManager.isInitialized()) {
            alert("Please press Init Device");
            return false;
        }
        addAction(Actions.moveBy(0, getHeight(), 0.5f, Interpolation.exp5));
        return true;
    }

    private void configDevicePort() {
        devicePort = new SelectBox<>(skin);
        reloadDevicePorts();
        devicePort.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (devicePort.getSelected() == nullport) {
                    settings.devicePort = null;
                } else {
                    settings.devicePort = devicePort.getSelected();
                }
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
                if (settings.devicePort == null) {
                    alert("Please select Device Port");
                } else {
                    boolean success = deviceManager.initDevice(settings.devicePort.name);
                    if (!success) {
                        alert("Init Device failed!");
                    }
                }
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
        gameCounter.addCallback(() -> correctPart.setText((int)(gameCounter.getCorrectPart() * 100) + " %"));
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

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor sup = super.hit(x, y, touchable);
        if (sup != null) {
            return sup;
        }
        if (touchable && x > 0 && x < getWidth() && y > 0 && y < getHeight()) {
            return this;
        }
        return null;
    }
}
