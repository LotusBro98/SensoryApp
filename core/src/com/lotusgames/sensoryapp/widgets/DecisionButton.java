package com.lotusgames.sensoryapp.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.lotusgames.sensoryapp.GameCounter;
import com.lotusgames.sensoryapp.Settings;

public class DecisionButton extends Actor {
    private class MyInputListener extends InputListener {
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

    public DecisionButton(float x, float y, float width, float height, Settings settings, GameCounter gameCounter, boolean left) {
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

        float pad = 16;
        float arw_width = getWidth() / 6;
        float arw_height = getHeight() / 3;

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1, 1, 0.1f, 1);
        renderer.rect(0, 0, getWidth(), getHeight());

        renderer.setColor(Color.BLACK);
        renderer.rect(getWidth() / 2 - arw_width / 2, pad, arw_width, getHeight() - arw_height - pad);
        renderer.triangle(
            getWidth() / 2 - arw_width, getHeight() - arw_height,
            getWidth() / 2, getHeight() - pad,
            getWidth() / 2 + arw_width, getHeight() - arw_height
        );

        renderer.end();
        batch.begin();
    }
}
