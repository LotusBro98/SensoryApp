package com.lotusgames.sensoryapp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class SensoryGridActor extends Actor {
    private class MyInputListener extends InputListener {
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("down " + Float.toString(y));
            return true;
        }

        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("up");
        }
    }

    Texture img;

    public SensoryGridActor() {
        img = new Texture("badlogic.jpg");
        setBounds(10, 10, img.getWidth(), img.getHeight());

        addListener(new MyInputListener());
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(img, getX(), getY(), getWidth(), getHeight());
    }
}
