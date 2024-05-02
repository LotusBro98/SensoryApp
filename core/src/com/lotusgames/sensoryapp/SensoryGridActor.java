package com.lotusgames.sensoryapp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.lotusgames.sensoryapp.device.Device;

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

    Texture img;
    Device device;
    Settings settings;

    public SensoryGridActor(float x, float y, float width, float height, Device device, Settings settings) {
        this.device = device;
        this.settings = settings;

        img = new Texture("badlogic.jpg");
        setBounds(x, y, width, height);

        addListener(new MyInputListener());
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(img, getX(), getY(), getWidth(), getHeight());
    }
}
