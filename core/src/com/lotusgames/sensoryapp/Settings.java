package com.lotusgames.sensoryapp;

import com.lotusgames.sensoryapp.device.DeviceConnection;

import java.util.List;

public class Settings {
    public DeviceConnection.Port devicePort = null;
    public DeviceConnection.Port[] devicePortOptions;
    public int[] pinsA = {0, 1, 2, 3, 4, 5, 6, 7};
    public int[] pinsB = {12, 13, 14, 15};
    public float tau_us = 15;
    public float frequency_Hz = 100;
    public float duration_ms = 10;
    public int linesMin = 10;
    public int linesMax = 20;
    public boolean drawLines = false;
}
