package com.lotusgames.sensoryapp.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.lotusgames.sensoryapp.Settings;
import com.lotusgames.sensoryapp.device.Device;

import java.util.ArrayList;
import java.util.Random;

public class SensoryGrid extends Actor {
    private class MyInputListener extends InputListener {
        float y_prev = 0;
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
                return;

            if (Gdx.input.justTouched()) {
                y_prev = y;
                return;
            }
            for (float y_line : lines) {
                if (
                    y_prev < y_line && y >= y_line ||
                    y_prev > y_line && y <= y_line
                ) {
                    device.generate_impulse(0, settings.tau_us, settings.frequency_Hz, settings.duration_ms);
                    break;
                }
            }
            y_prev = y;
        }
    }

    Device device;
    Settings settings;
    ArrayList<Float> lines = new ArrayList<>();

    public SensoryGrid(float x, float y, float width, float height, Device device, Settings settings) {
        this.device = device;
        this.settings = settings;

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

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.rect(0, 0, getWidth(), getHeight());

        if (settings.drawLines) {
            renderer.setColor(Color.GRAY);
            for (float y : lines) {
                renderer.line(0, y, getWidth(), y);
            }
        }

        renderer.end();
        batch.begin();
    }

    public void makeGrid(int n_lines) {
        lines.clear();
        float y_cum = 0.0f;
        for (int i = 0; i < n_lines; i++) {
            float dy = 1.0f;
            lines.add(y_cum + dy);
            y_cum += dy;
        }
        y_cum += 1.0f;

        for (int i = 0; i < n_lines; i++) {
            lines.set(i, lines.get(i) * getHeight() / y_cum);
        }
    }
}
