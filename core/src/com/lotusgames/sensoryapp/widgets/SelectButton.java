package com.lotusgames.sensoryapp.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.lotusgames.sensoryapp.GameCounter;
import com.lotusgames.sensoryapp.Settings;
import com.lotusgames.sensoryapp.device.Device;

import java.util.ArrayList;
import java.util.Random;

public class SelectButton extends Actor {
    private class MyInputListener extends InputListener {
        float y_prev = 0;
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            gameCounter.makeChoice(left);
            return true;
        }
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        }
    }

    Settings settings;
    GameCounter gameCounter;
    boolean left;

    public SelectButton(float x, float y, float width, float height, Settings settings, GameCounter gameCounter, boolean left) {
        this.settings = settings;
        this.gameCounter = gameCounter;
        this.left = left;
        setBounds(x, y, width, height);
        addListener(new MyInputListener());
    }

    private ShapeRenderer renderer = new ShapeRenderer();
    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.YELLOW);
        renderer.rect(0, 0, getWidth(), getHeight());

        renderer.end();
        batch.begin();
    }
}
