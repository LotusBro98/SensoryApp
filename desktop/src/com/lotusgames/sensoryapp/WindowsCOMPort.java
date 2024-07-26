package com.lotusgames.sensoryapp;

import com.lotusgames.sensoryapp.device.DeviceConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
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
            if (serialPort != null && serialPort.isOpened()) {
                serialPort.closePort();
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelWrite() throws IOException {
        try {
            serialPort.purgePort(SerialPort.PURGE_TXABORT);
            serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
        } catch (SerialPortException e) {
            throw new IOException(e);
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
    public void write(byte[] buf, int timeout) throws IOException, TimeoutException {
        if (timeout <= 0) {
            this.write(buf);
        }
        else {
            new TimedOutByteWriting(this, buf, timeout).write();
        }
    }

    @Override
    public Boolean useProtocol() {
        return false;
    }

    @Override
    public Port[] availablePorts() {
        String[] com_ports = SerialPortList.getPortNames();

        String reg_root = "HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\";
        Map<String, String> reg = WinRegistry.readRegistry(reg_root + "Control\\COM Name Arbiter\\Devices");
        if (reg == null) {
            throw new RuntimeException("Unable to read Windows registry to check COM ports.");
        }

        ArrayList<Port> ports = new ArrayList<>();
        for (String com_name : com_ports) {
            String desc_reg = reg.get(com_name);
            String[] desc_path = desc_reg.split("\\\\\\\\\\?\\\\")[1].split("#");
            String reg_path = reg_root + "Enum\\" + desc_path[0] + "\\" + desc_path[1] + "\\" + desc_path[2];
            Map<String, String> dvc = WinRegistry.readRegistry(reg_path);
            String dvc_desc = dvc.get("DeviceDesc").split(";")[1];
            ports.add(new Port(com_name, dvc_desc));
        }

        Port[] ports_out = new Port[ports.size()];
        ports.toArray(ports_out);
        return ports_out;
    }
}
