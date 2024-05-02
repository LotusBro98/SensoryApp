package com.lotusgames.sensoryapp;

import com.lotusgames.sensoryapp.device.Device;
import com.lotusgames.sensoryapp.device.DeviceConnection;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import jssc.SerialPortList;

public class WindowsCOMPort implements DeviceConnection {
    private SerialPort serialPort;

    @Override
    public void open(String name) throws IOException {
        try {
            serialPort = new SerialPort(name);
            serialPort.openPort();
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] read(int length, int timeout) throws IOException, TimeoutException {
        try {
            return serialPort.readBytes(length, timeout);
        } catch (SerialPortException e) {
            throw new IOException(e);
        } catch (SerialPortTimeoutException e) {
            throw  new TimeoutException(e.getMessage());
        }

    }

    @Override
    public void write(byte[] buf) throws IOException {
        try {
            serialPort.writeBytes(buf);
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Boolean useProtocol() {
        return false;
    }

    @Override
    public String[] availablePorts() {
        return SerialPortList.getPortNames();
    }
}
