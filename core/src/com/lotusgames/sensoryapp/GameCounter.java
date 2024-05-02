package com.lotusgames.sensoryapp;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.lotusgames.sensoryapp.widgets.SensoryGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameCounter {
    private boolean left = true;
    public int nCorrect = 0;
    public int nWrong = 0;
    public boolean lastCorrect = false;
    public boolean firstRound = true;

    private SensoryGrid leftGrid;
    private SensoryGrid rightGrid;
    private Settings settings;

    private List<Runnable> callbacks = new ArrayList<>();

    public GameCounter(Settings settings, SensoryGrid leftGrid, SensoryGrid rightGrid) {
        this.settings = settings;
        this.leftGrid = leftGrid;
        this.rightGrid = rightGrid;
    }

    public void resetRound() {
        Random ran = new Random();
        left = ran.nextBoolean();

        if (left) {
            leftGrid.makeGrid(settings.linesMax);
            rightGrid.makeGrid(settings.linesMin);
        } else {
            leftGrid.makeGrid(settings.linesMin);
            rightGrid.makeGrid(settings.linesMax);
        }

        for (Runnable cb: callbacks) {
            cb.run();
        }
    }

    public void makeChoice(boolean left) {
        if (left == this.left) {
            lastCorrect = true;
            nCorrect++;
            System.out.println("Correct!");
        } else {
            lastCorrect = false;
            nWrong++;
            System.out.println("Wrong!");
        }
        firstRound = false;
        resetRound();
    }

    public void resetGame() {
        nCorrect = 0;
        nWrong = 0;
        firstRound = true;
        resetRound();
    }

    public float getCorrectPart() {
        return (float) nCorrect / (float) (nCorrect + nWrong);
    }

    public void addCallback(Runnable callback) {
        callbacks.add(callback);
    }
}
