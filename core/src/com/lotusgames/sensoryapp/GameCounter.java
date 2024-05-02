package com.lotusgames.sensoryapp;

import com.lotusgames.sensoryapp.widgets.SensoryGrid;

import java.util.Random;

public class GameCounter {
    private boolean left = true;
    public int nCorrect = 0;
    public int nWrong = 0;

    private SensoryGrid leftGrid;
    private SensoryGrid rightGrid;
    private Settings settings;

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
    }

    public void makeChoice(boolean left) {
        if (left == this.left) {
            nCorrect++;
            System.out.println("Correct!");
        } else {
            nWrong++;
            System.out.println("Wrong!");
        }
        resetRound();

        System.out.println("Correct percent: " + (int)(getCorrectPart() * 100));
    }

    public void resetGame() {
        nCorrect = 0;
        nWrong = 0;
        resetRound();
    }

    public float getCorrectPart() {
        return (float) nCorrect / (float) (nCorrect + nWrong);
    }
}
