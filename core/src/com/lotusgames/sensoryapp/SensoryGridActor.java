package com.lotusgames.sensoryapp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.lotusgames.sensoryapp.device.Device;

import java.util.ArrayList;
import java.util.Random;

public class SensoryGridActor extends Actor {
    private class MyInputListener extends InputListener {
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("down " + Float.toString(x));
            device.generate_impulse(0, settings.tau_us, settings.frequency_Hz, settings.duration_ms);
            return true;
        }

        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("up");
        }
    }

    Device device;
    Settings settings;
    ArrayList<Float> lines = new ArrayList<>();

    public SensoryGridActor(float x, float y, float width, float height, Device device, Settings settings) {
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


        for (float y: lines) {
            renderer.line(0, y, getWidth(), y);
        }

        renderer.end();
        batch.begin();
    }

    public void makeGrid(int n_lines) {
        Random randGen = new Random();

        lines.clear();
        float y_cum = randGen.nextFloat();
        for (int i = 0; i < n_lines; i++) {
            float dy = randGen.nextFloat() + 0.5f;
            lines.add(y_cum + dy);
            y_cum += dy;
        }

        for (int i = 0; i < n_lines; i++) {
            lines.set(i, lines.get(i) * getHeight() / y_cum);
        }
    }
}
