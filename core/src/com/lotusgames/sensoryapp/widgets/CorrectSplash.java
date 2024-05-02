package com.lotusgames.sensoryapp.widgets;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.lotusgames.sensoryapp.GameCounter;
import com.lotusgames.sensoryapp.Settings;
import com.lotusgames.sensoryapp.device.Device;

import java.util.ArrayList;

public class CorrectSplash extends Actor {

    Texture img;
    Texture img_indicator;
    Texture correctImg;
    Texture wrongImg;
    Texture[] imgsCorrect;
    Texture[] imgsWrong;
    GameCounter gameCounter;


    public CorrectSplash(float x, float y, float width, float height, GameCounter gameCounter) {
        this.gameCounter = gameCounter;

        gameCounter.addCallback(() -> showSplash(gameCounter.lastCorrect));

        setBounds(x, y, width, height);

        loadImages();
        img = imgsCorrect[0];
        img_indicator = correctImg;
        addAction(Actions.fadeOut(0));
    }

    private void loadImages() {
        imgsCorrect = new Texture[]{
            new Texture("correct/correct1.jpg"),
            new Texture("correct/correct2.jpg"),
            new Texture("correct/correct3.jpg"),
            new Texture("correct/correct4.jpg"),
            new Texture("correct/correct5.jpg"),
            new Texture("correct/correct6.jpg"),
            new Texture("correct/correct7.jpg"),
            new Texture("correct/correct8.jpg"),
        };
        imgsWrong = new Texture[]{
            new Texture("wrong/wrong1.jpg"),
            new Texture("wrong/wrong2.jpg"),
            new Texture("wrong/wrong3.jpg"),
            new Texture("wrong/wrong4.jpg"),
            new Texture("wrong/wrong5.jpg"),
            new Texture("wrong/wrong6.jpg"),
            new Texture("wrong/wrong7.jpg"),
        };

        correctImg = new Texture("correct_over.png");
        wrongImg = new Texture("wrong_over.png");
    }

    public void showSplash(boolean correct) {
        if (gameCounter.firstRound) {
            return;
        }

        if (correct) {
            img = imgsCorrect[random.nextInt(imgsCorrect.length - 1)];
            img_indicator = correctImg;
        } else {
            img = imgsWrong[random.nextInt(imgsCorrect.length - 1)];
            img_indicator = wrongImg;
        }

        addAction(Actions.sequence(
                Actions.fadeIn(0.5f),
                Actions.delay(1f),
                Actions.fadeOut(0.5f)
        ));
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return null;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(img, getX(), getY(), getWidth(), getHeight());

        float img_size = getHeight() * 0.4f;
        float pad = getHeight() * 0.05f;

        batch.draw(img_indicator, getX() + getWidth() - img_size - pad, getY() + pad, img_size, img_size);
    }
}
