package com.lotusgames.sensoryapp.device;

import com.lotusgames.sensoryapp.Settings;

import java.io.IOException;

public class DeviceManager {
    DeviceConnection deviceConnection;
    public Device device;
    Settings settings;
    boolean initialized = false;

    public DeviceManager(DeviceConnection deviceConnection, Settings settings) {
        this.deviceConnection = deviceConnection;
        this.device = new Device(deviceConnection);
        this.settings = settings;
    }

    public boolean initDevice(String portName) {
        try {
            deviceConnection.close();
            deviceConnection.open(portName);
            int addr = device.self_test();
            device.clear_segments();
            device.add_segment(new Segment(
                    "finger",
                    addr, settings.pinsA,
                    addr, settings.pinsB,
                    500
            ));
            device.initialize();
            initialized = true;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isInitialized() {
        return initialized;
    }
}
