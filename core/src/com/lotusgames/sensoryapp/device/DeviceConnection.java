package com.lotusgames.sensoryapp.device;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface DeviceConnection {
    class Port {
        public String name;
        public String description;

        public Port(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Override
        public String toString() {
            if (name == null) {
                return "--";
            } else {
                return name + " (" + description + ")";
            }
        }
    }

    byte[] read(int length, int timeout) throws IOException, TimeoutException;

    void write(byte[] buf) throws IOException;
    void write(byte[] buf, int timeout) throws IOException, TimeoutException;

    void open(String name) throws IOException;

    void close();

    void cancelWrite() throws IOException;

    Boolean useProtocol();

    Port[] availablePorts();
}
