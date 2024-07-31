package com.lotusgames.sensoryapp;

import com.lotusgames.sensoryapp.device.DeviceConnection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Settings {
    public DeviceConnection.Port devicePort = null;
    public DeviceConnection.Port[] devicePortOptions;
    public Set<Integer> pinsA = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
    public Set<Integer> pinsB =  new HashSet<>(Arrays.asList(8, 9, 10, 11, 12, 13, 14, 15));
    public float tau_us = 15;

    // TODO select frequency
    public float frequency_Hz = 100;

    // TODO select duration
    public float duration_ms = 10;
    public int linesMin = 10;
    public int linesMax = 20;
    public boolean drawLines = false;
}
