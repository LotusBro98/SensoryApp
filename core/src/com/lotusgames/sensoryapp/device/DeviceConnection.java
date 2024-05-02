package com.lotusgames.sensoryapp.device;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface DeviceConnection {
    byte[] read(int length, int timeout) throws IOException, TimeoutException;

    void write(byte[] buf) throws IOException;

    void open(String name) throws IOException;

    void close();

    Boolean useProtocol();
}
