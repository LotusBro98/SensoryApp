package com.lotusgames.sensoryapp.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.lotusgames.sensoryapp.GameCounter;
import com.lotusgames.sensoryapp.Settings;

public class MenuButton extends Actor {
    private class MyInputListener extends InputListener {
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            if (closed) {
                menuWindow.addAction(Actions.moveBy(0, -menuWindow.getHeight(), 0.5f, Interpolation.exp5));
            } else {
                menuWindow.addAction(Actions.moveBy(0, menuWindow.getHeight(), 0.5f, Interpolation.exp5));
            }
            closed = !closed;
            return true;
        }
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        }
    }

    Settings settings;
    GameCounter gameCounter;
    MenuWindow menuWindow;
    boolean closed = false;

    public MenuButton(float x, float y, float width, float height, Settings settings, GameCounter gameCounter, MenuWindow menuWindow) {
        this.settings = settings;
        this.gameCounter = gameCounter;
        this.menuWindow = menuWindow;
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

        float st = getHeight() / 9;

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.WHITE);
        renderer.rect(st * 2, st * 2, st * 5.5f, st);
        renderer.rect(st * 2, st * 4, st * 5.5f, st);
        renderer.rect(st * 2, st * 6, st * 5.5f, st);

        renderer.end();
        batch.begin();
    }
}
