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
import androidx.annotation.ColorRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import fsm.miaad.DAO.DBManager;
import fsm.miaad.R;
import fsm.miaad.RESTcontrolle.SendDataTask;
import fsm.miaad.RESTcontrolle.SuperFamilies;
import fsm.miaad.RESTcontrolle.TIRLocalisation;
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

public class DNAClassificationActivity extends AppCompatActivity {
    private String classDetectedValue;
    TextView stat,superfamily,count,withbiggy,withouttc1,hat,casta,merlin,mudr,p,sequence;
    ImageView menu_botton,upload,predict_im;
    SeekBar percentage_chooser;
    TextView percentage_value;
    View withbiggyV,withouttc1V,hatV,castaV,merlinV,mudrV,pV;
    PieChart pieChart;
    ProgressBar pb;
    int progress=0;
    User userConnect;
    private DBManager dbManager;
    private String emailConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dnaclassification);
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
        predict_im = findViewById(R.id.predict_im);
        sequence = findViewById(R.id.sequence);
        stat = findViewById(R.id.stat);
        superfamily = findViewById(R.id.superfamily);
        count = findViewById(R.id.count);
        percentage_chooser = findViewById(R.id.percentage_chooser);
        percentage_value =findViewById(R.id.percentage_value);
        percentage_chooser.setVisibility(View.GONE);
        percentage_value.setVisibility(View.GONE);

        withbiggyV = findViewById(R.id.withbiggyV);
        withouttc1 = findViewById(R.id.withouttc1);
        withouttc1V = findViewById(R.id.withouttc1V);
        withbiggy = findViewById(R.id.withbiggy);

        hat = findViewById(R.id.hat);
        hatV = findViewById(R.id.hatV);

        pV = findViewById(R.id.pV);
        p = findViewById(R.id.p);
        mudr = findViewById(R.id.mudr);
        mudrV = findViewById(R.id.mudrV);
        merlin = findViewById(R.id.merlin);
        merlinV = findViewById(R.id.merlinV);
        casta = findViewById(R.id.casta);
        castaV = findViewById(R.id.castaV);
        menu_botton = findViewById(R.id.menu_botton);

        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);
        predict_im.setVisibility(View.GONE);
        count.setVisibility(View.GONE);
//        detecteFamilybtn.setVisibility(View.GONE);
        p.setVisibility(View.GONE);
        pV.setVisibility(View.GONE);
        withbiggy.setVisibility(View.GONE);
        withbiggyV.setVisibility(View.GONE);
        withouttc1V.setVisibility(View.GONE);
        withouttc1.setVisibility(View.GONE);
        hat.setVisibility(View.GONE);
        hatV.setVisibility(View.GONE);
        merlin.setVisibility(View.GONE);
        merlinV.setVisibility(View.GONE);
        mudr.setVisibility(View.GONE);
        mudrV.setVisibility(View.GONE);
        castaV.setVisibility(View.GONE);
        casta.setVisibility(View.GONE);

        stat.setVisibility(View.GONE);
        superfamily.setVisibility(View.GONE);
        sequence.setVisibility(View.GONE);

// Find the pie chart view in the layout
        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setVisibility(View.GONE);


// Add some data to the pie chart



    }

    private static final int PICK_TXT_FILE_REQUEST_CODE = 1;
    Uri selectedFileUri;
    List<String> sequences=new ArrayList<>();
    String truncatedSequence;

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
                    String fullSequence = entry.getSequence();


                    // Check if the sequence is longer than 30 characters
                    if (fullSequence.length() > 40) {
                        truncatedSequence = fullSequence.substring(0, 40); // Get the first 30 characters
                    } else {
                        truncatedSequence = fullSequence; // If it's less than or equal to 30 characters, use the full sequence
                    }

                    // Set the truncated sequence text to your TextView
                      // Assuming 'sequence' is your TextView
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


        pb.setVisibility(View.GONE);
        count.setVisibility(View.GONE);
// Optionally, you can add specific MIME types for TXT and FASTA files
        String[] mimeTypes = {"text/plain", "application/fasta"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_TXT_FILE_REQUEST_CODE);
        pb.setProgress(0);
        count.setText("0%");
//        detecteFamilybtn.setVisibility(View.GONE);
        sequence.setVisibility(View.GONE);
        predict_im.setVisibility(View.GONE);

        stat.setVisibility(View.GONE);
        percentage_chooser.setVisibility(View.GONE);
        percentage_value.setVisibility(View.GONE);
        superfamily.setVisibility(View.GONE);
        withbiggy.setVisibility(View.GONE);
        withbiggyV.setVisibility(View.GONE);
        withouttc1V.setVisibility(View.GONE);
        withouttc1.setVisibility(View.GONE);
        hat.setVisibility(View.GONE);
        hatV.setVisibility(View.GONE);
        merlin.setVisibility(View.GONE);
        merlinV.setVisibility(View.GONE);
        mudr.setVisibility(View.GONE);
        mudrV.setVisibility(View.GONE);
        castaV.setVisibility(View.GONE);
        casta.setVisibility(View.GONE);
        p.setVisibility(View.GONE);
        pV.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);
        pieChart.clearChart();
    }



    private void progress() {
        progress=0;

        pb.setVisibility(View.VISIBLE);
        count.setVisibility(View.VISIBLE);
        pb.setProgress(0);
        count.setText(progress+"%");

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {

            if (progress >= 0 && progress <= 100) {
                runOnUiThread(() -> {
                    upload.setImageDrawable(null);
//                    upload.setImageResource(R.drawable.check);
                    upload.setClickable(false);
                    pb.setProgress(progress);
                    count.setText(progress+"%");
                    if (progress == 100) {
                        upload.setImageDrawable(null);
                        upload.setImageResource(R.drawable.attachfile);
                        upload.setClickable(true);
                        predict_im.setVisibility(View.VISIBLE);



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
        PopupMenu popupMenu = new PopupMenu(DNAClassificationActivity.this, menu_botton);

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



    public int getColor(@ColorRes int colorResId, Context context) {
        int color = ContextCompat.getColor(context, colorResId);
        return color ;
    }
    int similarity=0;
    public void predicte(View v){

        if(sequences!=null){
            sequence.setVisibility(View.VISIBLE);
            sequence.setText(truncatedSequence+" ...");
            stat.setVisibility(View.VISIBLE);
            stat.setText(".........");
            SendDataTask sendDataTask = new SendDataTask(this, new SendDataTask.TaskListener() {
                @Override
                public void onTaskFinished(String seq,List<String> result) {
                    // Ici, result est sequences_pred_proba
                    // Vous pouvez l'utiliser comme vous le souhaitez
                    System.out.println("asss===="+result+"\n==="+result.get(0)+"\n==="+result.get(1));
                    String str=result.get(0)+","+result.get(1);
                    str = str.replace("[", "").replace("]", "").replace("\"", "");
                    String[] strArray = str.split(",");
                    float[] floatArray = new float[strArray.length];
                    for (int i = 0; i < strArray.length; i++) {
                        String numberStr = strArray[i].replace("%", "");
                        float number = Float.parseFloat(numberStr);
                        floatArray[i] = number;
                    }
                    float withPer = floatArray[1], withoutPer = floatArray[0];
                    System.out.println("===="+withPer+"nnn"+withoutPer);
                    if(seq.equals("ET")) {
                        percentage_chooser.setVisibility(View.VISIBLE);
                        percentage_value.setVisibility(View.VISIBLE);
                        stat.setText("This sequence contains a TE .... Please choose the percentage of similarity between TIRs.");
                        percentage_chooser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                similarity=progress;
                                percentage_value.setText(progress + "%");
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                // Optional: Do something when the user starts touching the SeekBar
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                // Optional: Do something when the user stops touching the SeekBar

                                superfamily.setVisibility(View.VISIBLE);
                                superfamily.setText(".........");
                                SuperFamilies sendDataTaskSuperFamilies = new SuperFamilies(v.getContext(), new SuperFamilies.TaskListener() {
                                    @Override
                                    public void onTaskFinished(String seq,List<String> result) {
                                        // Ici, result est sequences_pred_proba
                                        // Vous pouvez l'utiliser comme vous le souhaitez
                                        System.out.println("asss===="+result.get(0)+"\n");
                                        System.out.println("asss===="+result.get(1)+"\n");
                                        System.out.println("asss===="+result.get(2)+"\n");
                                        System.out.println("asss===="+result.get(3)+"\n");
                                        System.out.println("asss===="+result.get(4)+"\n");
                                        System.out.println("asss===="+result.get(5)+"\n");
                                        System.out.println("asss===="+result.get(6)+"\n");

//                                        # ['0.005%', '0.009%', '0.000%', '0.010%', '0.974%', '0.001%', '0.000%']
                                        //# ['tc1ma', 'p ele', 'mudr%', 'hat %', 'biggy', 'merli', 'casta']
                                        float tc1f = Float.parseFloat(result.get(0).replace("%", "")),
                                                pf = Float.parseFloat(result.get(1).replace("%", "")),
                                                mudrf=Float.parseFloat(result.get(2).replace("%", "")),
                                                hatf=Float.parseFloat(result.get(3).replace("%", "")),
                                                piggyf = Float.parseFloat(result.get(4).replace("%", "")),
                                                merlif=Float.parseFloat(result.get(5).replace("%", "")),
                                                castaf=Float.parseFloat(result.get(6).replace("%", ""));

//                    System.out.println("===="+withPer+"\nnnn"+withoutPer);
                                        if(seq.equals("TC1 MARINER")) superfamily.setText("This sequence has a TE of the TC1 MARINER superfamily.");
                                        else if (seq.equals("P Element")) superfamily.setText("This sequence has a TE of the P Element superfamily.");
                                        else if (seq.equals("HAT")) superfamily.setText("This sequence has a TE of the hAT superfamily.");
                                        else if (seq.equals("CACTA")) superfamily.setText("This sequence has a TE of the CASTA superfamily.");
                                        else if (seq.equals("Merlin")) superfamily.setText("This sequence has a TE of the MERLIN superfamily.");
                                        else if (seq.equals("MuDR/Mutator")) superfamily.setText("This sequence has a TE of the MuDR / MUTATOR superfamily.");
                                        else if (seq.equals("PiggyBac")) superfamily.setText("This sequence has a TE of the PIGGYBAC superfamily.");


                                        pieChart.setVisibility(View.VISIBLE);

                                        p.setVisibility(View.VISIBLE);
                                        pV.setVisibility(View.VISIBLE);
                                        withbiggy.setVisibility(View.VISIBLE);
                                        withbiggyV.setVisibility(View.VISIBLE);
                                        withouttc1V.setVisibility(View.VISIBLE);
                                        withouttc1.setVisibility(View.VISIBLE);
                                        hat.setVisibility(View.VISIBLE);
                                        hatV.setVisibility(View.VISIBLE);
                                        merlin.setVisibility(View.VISIBLE);
                                        merlinV.setVisibility(View.VISIBLE);
                                        mudr.setVisibility(View.VISIBLE);
                                        mudrV.setVisibility(View.VISIBLE);
                                        castaV.setVisibility(View.VISIBLE);
                                        casta.setVisibility(View.VISIBLE);

                                        withbiggy.setText("PIGGYBAC "+piggyf+" %");
                                        withouttc1.setText("TC1-MARINER "+tc1f+" %");
                                        p.setText("P Element "+pf+" %");
                                        hat.setText("hAT "+hatf+" %");
                                        merlin.setText("Merlin "+merlif+" %");
                                        mudr.setText("MuDR / Mutator "+mudrf+" %");
                                        casta.setText("CASTA "+castaf+" %");


                                        pieChart.clearChart();
                                        pieChart.addPieSlice(new PieModel("PiggyBac", piggyf, getColor(R.color.biggy,v.getContext())));
                                        pieChart.addPieSlice(new PieModel("tc1mariner", tc1f, getColor(R.color.tc1,v.getContext())));
                                        pieChart.addPieSlice(new PieModel("P", pf, getColor(R.color.p,v.getContext())));
                                        pieChart.addPieSlice(new PieModel("hAT", hatf, getColor(R.color.hat,v.getContext())));
                                        pieChart.addPieSlice(new PieModel("Merlin", merlif, getColor(R.color.merlin,v.getContext())));
                                        pieChart.addPieSlice(new PieModel("Casta", castaf, getColor(R.color.casta,v.getContext())));
                                        pieChart.addPieSlice(new PieModel("Mudr", mudrf, getColor(R.color.mudr,v.getContext())));

                                        // Start the animation
                                        pieChart.startAnimation();
                                        //String activity, String dateDe, String withPer, String withoutPer
                                        // Define the desired date and time format
                                        String pattern = "yyyy/MM/dd-HH:mm:ss";

                                        // Create a SimpleDateFormat object with the specified pattern
                                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                                        // Format the Date object using the SimpleDateFormat
                                        String formattedDate = sdf.format(new Date());

                                        dbManager.insertHistoryFamily(emailConnect, new History(seq, formattedDate, tc1f + "%",  pf+ "%",hatf+ "%",piggyf+ "%",merlif+"%",mudrf+"%",castaf+"%"));

                                        if(sequences!=null){
                                            TIRLocalisation sendDataTask = new TIRLocalisation(v.getContext(), new TIRLocalisation.TaskListener() {
                                                @Override
                                                public void onTaskFinished(String seq) {

                                                }
                                            },similarity,emailConnect);
                                            sendDataTask.execute(sequences);

                                        }
                                    }
                                });
                                sendDataTaskSuperFamilies.execute(sequences);
                            }
                        });

                    }
                    else
                    {
                        stat.setText("This sequence doesn't contains a TE");
                        pieChart.setVisibility(View.VISIBLE);


                        p.setVisibility(View.GONE);
                        pV.setVisibility(View.GONE);
                        withbiggy.setVisibility(View.VISIBLE);
                        withbiggyV.setVisibility(View.VISIBLE);
                        withouttc1V.setVisibility(View.VISIBLE);
                        withouttc1.setVisibility(View.VISIBLE);
                        hat.setVisibility(View.GONE);
                        hatV.setVisibility(View.GONE);
                        merlin.setVisibility(View.GONE);
                        merlinV.setVisibility(View.GONE);
                        mudr.setVisibility(View.GONE);
                        mudrV.setVisibility(View.GONE);
                        castaV.setVisibility(View.GONE);
                        casta.setVisibility(View.GONE);

                        withbiggy.setText("TE " + withPer + " %");
                        withouttc1.setText("nTE " + withoutPer + " %");

                        pieChart.clearChart();
                        pieChart.addPieSlice(new PieModel("with ET", withPer, getColor(  R.color.biggy, v.getContext())));
                        pieChart.addPieSlice(new PieModel("without ET", withoutPer, getColor(  R.color.tc1, v.getContext())));
                        // Start the animation
                        pieChart.startAnimation();
                        //String activity, String dateDe, String withPer, String withoutPer
                        // Define the desired date and time format
                        String pattern = "yyyy/MM/dd-HH:mm:ss";

                        // Create a SimpleDateFormat object with the specified pattern
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                        // Format the Date object using the SimpleDateFormat
                        String formattedDate = sdf.format(new Date());
                        dbManager.insertHistoryET(emailConnect, new History("nTE", formattedDate, withPer + "%", withoutPer + "%"));
                    }


                }
            });
            sendDataTask.execute(sequences);


        }

    }
}
