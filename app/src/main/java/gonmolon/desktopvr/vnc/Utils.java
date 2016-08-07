package gonmolon.desktopvr.vnc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Utils {

    public static String ipAddress = "";
    private static String tcpPort = "8080";

    public static String GET(String endpoint) throws Exception {
        URL url = new URL("http://" + ipAddress + ":" + tcpPort + "/" + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream in = connection.getInputStream();
        InputStreamReader isw = new InputStreamReader(in);

        String output = "";
        int data = isw.read();
        while(data != -1) {
            char current = (char) data;
            data = isw.read();
            output += current;
        }

        connection.disconnect();

        return output;
    }

    public static void POST(final String endpoint) {
        new Thread(new Runnable() {
            @Override
            public void run () {
                try {
                    GET(endpoint);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
