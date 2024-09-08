package fsm.miaad.RESTcontrolle;

import android.content.Context;
import android.os.AsyncTask;
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

public class SuperFamilies extends AsyncTask<List<String>, Void, Void> {
    private Context context;
//    private int similarity;
//    private String emailConnect;
    String classdetected = "";
    List<String> sequences_pred_proba = new ArrayList<>();

    private TaskListener taskListener;

    public SuperFamilies(Context context, TaskListener taskListener) {
        this.context = context;
        this.taskListener = taskListener;

    }

    public String getClassdetected() {
        return classdetected;
    }

    public List<String> getSequences_pred_proba() {
        return sequences_pred_proba;
    }

    @Override
    protected Void doInBackground(List<String>... sequenceLists) {
        try {
            if (sequenceLists.length > 0) {
                List<String> sequences = sequenceLists[0];

                // Specify the URL of your Flask server
                URL url = new URL("http://192.168.1.88:5000/SuperFamilies");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set up the connection properties
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Convert the list of sequences to a JSON string
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("sequences", new JSONArray(sequences));
//                jsonRequest.put("similarity", similarity);
//                jsonRequest.put("emailConnect", emailConnect);
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
                    String message = jsonResponse.getString("message");
                    classdetected = jsonResponse.getString("sequences_family");
                    JSONArray sequences_pred_probaArray = jsonResponse.getJSONArray("sequences_pred_proba_family");


                    // Extract sequences classes from the JSON array

                    for (int i = 0; i < sequences_pred_probaArray.length(); i++) {
                        String sequenceClass = sequences_pred_probaArray.getString(i);
                        Log.d("SequenceClass", "Sequence Class: " + sequenceClass);
                        sequences_pred_proba.add(sequenceClass);

                        // Handle or use the sequence class as needed
                    }
                    for (String i : sequences_pred_proba) {
                        Log.e("sequences_pred_proba", i);
                    }

                    // Successfully sent data to the server
                    // You can read the response from the server if needed
                    // InputStream responseStream = connection.getInputStream();
                    // ... read the response ...
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


    public String getSequences() {
        return classdetected;
    }

    public List<String> getsequences_pred_proba() {
        return sequences_pred_proba;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (taskListener != null) {
            taskListener.onTaskFinished(classdetected,sequences_pred_proba);
        }
    }

    public interface TaskListener {
        void onTaskFinished(String seq,List<String> result);
    }


}
