package fsm.miaad.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import fsm.miaad.DAO.DBManager;
import fsm.miaad.R;
import fsm.miaad.RESTcontrolle.SendDataTask;
import fsm.miaad.models.FastaEntry;
import fsm.miaad.models.History;
import fsm.miaad.models.User;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ETetEnTActivity extends AppCompatActivity {
    private String classDetectedValue;
    TextView upload,stat,count,with,without;
    ImageView menu_botton;
    View withV,withoutV;
    LinearLayout detectebtn;
    TextView detecteFamilybtn;
    ImageView stat_image;
    PieChart pieChart;
    ProgressBar pb;
    int progress=0;
    User userConnect;
    private DBManager dbManager;
    private String emailConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_et_et_ent);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        Intent intent = getIntent();
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        emailConnect= intent.getStringExtra("emailConnect");
        dbManager = new DBManager(this);
        dbManager.open();

        userConnect=dbManager.getUserByEmail(emailConnect);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_adn);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_history:
                    Intent intent1=new Intent(getApplicationContext(),HistoryActivity.class);
                    intent1.putExtra("emailConnect",emailConnect);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_profile:
                    Intent intent2=new Intent(getApplicationContext(),ProfileActivity.class);
                    intent2.putExtra("emailConnect",emailConnect);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_adn:
                    return true;
            }
            return false;
        });

        upload = findViewById(R.id.upload);
        stat = findViewById(R.id.stat);
        count = findViewById(R.id.count);
        stat_image =findViewById(R.id.stat_image);
        detectebtn = findViewById(R.id.detectebtn); detecteFamilybtn = findViewById(R.id.detecteFamilybtn);
        with = findViewById(R.id.with);
        without = findViewById(R.id.without);
        withV = findViewById(R.id.withV);
        withoutV = findViewById(R.id.withoutV);
        menu_botton = findViewById(R.id.menu_botton);

        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);
        count.setVisibility(View.GONE);
        detectebtn.setVisibility(View.GONE);detecteFamilybtn.setVisibility(View.GONE);
        with.setVisibility(View.GONE);
        without.setVisibility(View.GONE);
        withV.setVisibility(View.GONE);
        withoutV.setVisibility(View.GONE);

// Find the pie chart view in the layout
        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setVisibility(View.GONE);


// Add some data to the pie chart



    }

    private static final int PICK_TXT_FILE_REQUEST_CODE = 1;
    Uri selectedFileUri;
    List<String> sequences=new ArrayList<>();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_TXT_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Handle the selected text file URI
                selectedFileUri = data.getData();
                pb.setVisibility(View.VISIBLE);
                count.setVisibility(View.VISIBLE);
                progress();
                List<FastaEntry> fastaEntries = readFasta(selectedFileUri, this);

                // Do something with the extracted data, for example, print it
                sequences=new ArrayList<>();
                for (FastaEntry entry : fastaEntries) {
                    System.out.println("FastaReader" + "Sequence ID: " + entry.getSequenceID());
                    System.out.println("FastaReader" + "Sequence: " + entry.getSequence());
                    sequences.add(entry.getSequence());
//                    Log.d("FastaReader", "");

                    // Now, you can use this URI to perform operations with the selected text file


                }


            }
        }
    }


    public static List<FastaEntry> readFasta(Uri fileUri, Context context) {
        List<FastaEntry> fastaEntries = new ArrayList<>();

        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(fileUri);

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                String currentSequenceID = null;
                StringBuilder currentSequence = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    line = line.trim().toLowerCase();

                    if (line.startsWith(">")) {
                        if (currentSequenceID != null) {
                            fastaEntries.add(new FastaEntry(currentSequenceID, currentSequence.toString()));
                        }

                        currentSequenceID = line.substring(1);
                        currentSequence = new StringBuilder();
                    } else {
                        currentSequence.append(line);
                    }
                }

                // Add the last sequence to the list
                if (currentSequenceID != null) {
                    fastaEntries.add(new FastaEntry(currentSequenceID, currentSequence.toString()));
                } else if (!currentSequence.toString().isEmpty()) {
                    // Assign a default ID if the last sequence had no explicit identifier
                    fastaEntries.add(new FastaEntry("default_id", currentSequence.toString()));
                }

                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fastaEntries;
    }



    public void uploadFile(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Set to accept any file type

// Optionally, you can add specific MIME types for TXT and FASTA files
        String[] mimeTypes = {"text/plain", "application/fasta"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_TXT_FILE_REQUEST_CODE);
        pb.setProgress(0);
        count.setText("0%");
        detectebtn.setVisibility(View.GONE); detecteFamilybtn.setVisibility(View.GONE);
        stat.setText("with/without");
        stat_image.setImageResource(R.drawable.stat_0);
        with.setVisibility(View.GONE);
        without.setVisibility(View.GONE);
        withV.setVisibility(View.GONE);
        withoutV.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);
        pieChart.clearChart();
    }



    private void progress() {
        progress=0;
        pb.setProgress(0);
        count.setText(progress+"%");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {

            if (progress >= 0 && progress <= 100) {
                runOnUiThread(() -> {
                    upload.setText("In progress");
                    upload.setClickable(false);
                    pb.setProgress(progress);
                    count.setText(progress+"%");
                    if (progress == 100) {
                        upload.setText("Upload");
                        upload.setClickable(true);
                        detectebtn.setVisibility(View.VISIBLE);
                        stat.setText("with/without");
                        stat_image.setImageResource(R.drawable.stat_0);

                        executor.shutdown(); // Stop the scheduled task
                    }
                });
                progress+=5;
            }


        };

//        // Schedule the task to run every 1000 milliseconds (1 second)
        executor.scheduleAtFixedRate(task, 0, 100, TimeUnit.MILLISECONDS);

    }

    String seqClass="";
    List<String> sequences_pred_proba=new ArrayList<>();

    private void useClassDetectedValue() {
        if (classDetectedValue != null) {
            // Use classDetectedValue here
            Log.d("RESTcontroller", "Class Detected Value: " + classDetectedValue);
        } else {
            Log.d("RESTcontroller", "Class Detected Value is null");
        }
    }

    public void getsuperfamily(View v){
        Intent intent1=new Intent(getApplicationContext(),AdnActivity.class);
        intent1.putExtra("emailConnect",emailConnect);
        ArrayList<String> list = new ArrayList<String>();
        for(String i:sequences) list.add(i);
        intent1.putStringArrayListExtra("sequences", list);
        startActivity(intent1);
    }

    public void menu(View v){
        // Initializing the popup menu and giving the reference as current context
        PopupMenu popupMenu = new PopupMenu(ETetEnTActivity.this, menu_botton);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.profilehis_menu, popupMenu.getMenu());
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.user);

        String name=userConnect.getLastname()+" "+userConnect.getFirstname();
        menuItem.setTitle(name);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("emailConnect",emailConnect);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.disconnect) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
//                Toast.makeText(ProfileActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // Showing the popup menu
        popupMenu.show();
    }




    public void predicte(View v){

        if(sequences!=null){
            SendDataTask sendDataTask = new SendDataTask(this, new SendDataTask.TaskListener() {
                @Override
                public void onTaskFinished(String seq,List<String> result) {
                    // Ici, result est sequences_pred_proba
                    // Vous pouvez l'utiliser comme vous le souhaitez
                     System.out.println("asss===="+result.get(0)+"\n");
                     String str=result.get(0);
                    str = str.replace("[", "").replace("]", "").replace("\"", "");
                    String[] strArray = str.split(",");
                    float[] floatArray = new float[strArray.length];
                    for (int i = 0; i < strArray.length; i++) {
                        String numberStr = strArray[i].replace("%", "");
                        float number = Float.parseFloat(numberStr);
                        floatArray[i] = number;
                    }
                    float withPer = floatArray[1], withoutPer = floatArray[0];
                    System.out.println("===="+withPer+"\nnnn"+withoutPer);
                    if(seq.equals("ET")) {
                        stat.setText("ET");
                        detecteFamilybtn.setVisibility(View.VISIBLE);
                    }
                    else stat.setText("EnT");
                    if (stat.getText().toString().equals("ET"))
                        stat_image.setImageResource(R.drawable.stat_1);
                    else if (stat.getText().toString().equals("EnT"))
                        stat_image.setImageResource(R.drawable.stat_2);
                    pieChart.setVisibility(View.VISIBLE);
                    with.setVisibility(View.VISIBLE);
                    without.setVisibility(View.VISIBLE);
                    with.setText("WITH "+withPer+" %"); without.setText("WITHOUT "+withoutPer+" %");
                    withV.setVisibility(View.VISIBLE);
                    withoutV.setVisibility(View.VISIBLE);
                    pieChart.clearChart();
                    pieChart.addPieSlice(new PieModel("with ET", withPer, Color.parseColor("#83ABC5")));
                    pieChart.addPieSlice(new PieModel("without ET", withoutPer, Color.parseColor("#223247")));
                    // Start the animation
                    pieChart.startAnimation();
                    //String activity, String dateDe, String withPer, String withoutPer
                    // Define the desired date and time format
                    String pattern = "yyyy/MM/dd-HH:mm:ss";

                    // Create a SimpleDateFormat object with the specified pattern
                    SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                    // Format the Date object using the SimpleDateFormat
                    String formattedDate = sdf.format(new Date());
                    dbManager.insertHistoryET(emailConnect, new History(stat.getText().toString(), formattedDate, withPer + "%", withoutPer + "%"));

                }
            });
            sendDataTask.execute(sequences);

//            for (String s: sequences_pred_proba) {
//            Log.e("hhh","hgsd"+sendDataTask.getSequences());



//                float withPer = extractPercentage(sequences_pred_proba.get(1)), withoutPer = extractPercentage(sequences_pred_proba.get(0));
//                if(seqClass.equals("ET"))
//                    stat.setText("ET");
//                else stat.setText("EnT");
//                if (stat.getText().toString().equals("ET"))
//                    stat_image.setImageResource(R.drawable.stat_1);
//                else if (stat.getText().toString().equals("EnT"))
//                    stat_image.setImageResource(R.drawable.stat_2);
//                pieChart.setVisibility(View.VISIBLE);
//                with.setVisibility(View.VISIBLE);
//                without.setVisibility(View.VISIBLE);
//                withV.setVisibility(View.VISIBLE);
//                withoutV.setVisibility(View.VISIBLE);
//                pieChart.clearChart();
//                pieChart.addPieSlice(new PieModel("with ET", withPer, Color.parseColor("#83ABC5")));
//                pieChart.addPieSlice(new PieModel("without ET", withoutPer, Color.parseColor("#223247")));
//                // Start the animation
//                pieChart.startAnimation();
//                //String activity, String dateDe, String withPer, String withoutPer
//                // Define the desired date and time format
//                String pattern = "yyyy/MM/dd-HH:mm:ss";
//
//                // Create a SimpleDateFormat object with the specified pattern
//                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//
//                // Format the Date object using the SimpleDateFormat
//                String formattedDate = sdf.format(new Date());
//                dbManager.insertHistory(emailConnect, new History(stat.getText().toString(), formattedDate, withPer + "%", withoutPer + "%"));
////            }
        }

    }
}
