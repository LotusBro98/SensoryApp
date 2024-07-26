package com.lotusgames.sensoryapp.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class Notification extends Dialog {

    public enum NotificationType{
        SUCCESS, FAIL, INFO;
    }

    static Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    Label label;
    public Notification(String text, NotificationType type) {
        super("", skin);
        setWidth(Gdx.graphics.getWidth() * 0.2f);
        setPosition(Gdx.graphics.getWidth() * 0.4f, Gdx.graphics.getHeight() * 0.7f);
        setColor(getColor().mul(1, 1, 1, 0));
        label = new Label(text, skin);
        label.setWrap(true);
        label.setAlignment(Align.center);
        getContentTable().add(label).expand().fill();

        SequenceAction action = new SequenceAction(
                Actions.fadeIn(0.5f),
                Actions.delay(1.0f),
                Actions.fadeOut(0.5f),
                Actions.removeActor()
        );
        addAction(action);
    }
}
