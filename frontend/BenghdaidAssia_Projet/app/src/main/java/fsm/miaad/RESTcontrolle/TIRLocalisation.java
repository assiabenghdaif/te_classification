package fsm.miaad.RESTcontrolle;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TIRLocalisation extends AsyncTask<List<String>, Void, Void> {
    private Context context;
    private int similarity;
    private String emailConnect;
    String message;

    private TaskListener taskListener;

    public TIRLocalisation(Context context, TaskListener taskListener, int similarity, String emailConnect) {
        this.context = context;
        this.taskListener = taskListener;
        this.similarity = similarity;
        this.emailConnect=emailConnect;
    }



    @Override
    protected Void doInBackground(List<String>... sequenceLists) {
        try {
            if (sequenceLists.length > 0) {
                List<String> sequences = sequenceLists[0];

                // Specify the URL of your Flask server
                URL url = new URL("http://192.168.1.88:5000/TIRLocalisation");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set up the connection properties
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Convert the list of sequences to a JSON string
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("sequences", new JSONArray(sequences));
                jsonRequest.put("similarity", similarity);
                jsonRequest.put("emailConnect", emailConnect);
//                String jsonInputString = new Gson().toJson(sequences);
//                jsonRequest.put("similarity", similarity);

                // Convert the JSONObject to a JSON string
                String jsonInputString = jsonRequest.toString();

                // Write the JSON string to the output stream
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response from the server
                int responseCode = connection.getResponseCode();
                Log.d("SendDataTask", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Print response
                    System.out.println("Response: " + response.toString());
                    JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                    message = jsonResponse.getString("message");

                } else {
                    // Handle the error
                    Log.e("SendDataTask", "Error sending data. Response Code: " + responseCode);
                }

                // Close the connection
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SendDataTask", "Exception: " + e.getMessage());
        }

        return null;
    }



    public String getMessage() {
        return message;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (taskListener != null) {
            taskListener.onTaskFinished(message);
        }
    }

    public interface TaskListener {
        void onTaskFinished(String seq);
    }


}
