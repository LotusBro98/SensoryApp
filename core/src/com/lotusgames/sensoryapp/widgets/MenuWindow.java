package com.lotusgames.sensoryapp.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.lotusgames.sensoryapp.GameCounter;
import com.lotusgames.sensoryapp.Settings;

public class MenuWindow extends Actor {
    private class MyInputListener extends InputListener {
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        }
    }

    Settings settings;
    GameCounter gameCounter;

    public MenuWindow(float x, float y, float width, float height, Settings settings, GameCounter gameCounter) {
        this.settings = settings;
        this.gameCounter = gameCounter;
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
        renderer.setColor(Color.BLUE);
        renderer.rect(0, 0, getWidth(), getHeight());

        renderer.end();
        batch.begin();
    }
}
