package com.lotusgames.sensoryapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class WinRegistry {

    /**
     *
     * @param location path in the registry
     * @return registry values or null if not found
     */
    public static Map<String, String> readRegistry(String location) {
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "chcp 1251 & reg query \"" + location + "\"");
            Process process = pb.start();

            StreamReader reader = new StreamReader(process.inputReader(Charset.forName("cp1251")));
            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult();

            String sep = output.contains("\t") ? "\t" : "    ";
            String[] lines = output.replace("\r", "").split("\n\n")[1].split("\n");
            HashMap<String, String> reg = new HashMap<>();
            for (int i = 1; i < lines.length; i++) {
                String key = lines[i].strip().split(sep)[0];
                String value = lines[i].strip().split(sep)[2];
                reg.put(key, value);
            }
            return reg;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static class StreamReader extends Thread {
        private BufferedReader is;
        private StringWriter sw= new StringWriter();

        public StreamReader(BufferedReader is) {
            this.is = is;
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            }
            catch (IOException e) {
            }
        }

        public String getResult() {
            return sw.toString();
        }
    }
}