package fsm.miaad.RESTcontrolle;

import android.os.AsyncTask;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUploadTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://192.168.1.88:5000/upload");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create JSON object with the file URI
            String fileUri = params[0];
            String jsonInputString = "{\"file_uri\": \"" + fileUri + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the server response
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Handle success
                // You can read the response if needed
            } else {
                // Handle error
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
