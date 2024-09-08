package fsm.miaad.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import fsm.miaad.DAO.DBManager;
import fsm.miaad.R;
import fsm.miaad.models.History;
import fsm.miaad.models.User;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class PiechartActivity extends AppCompatActivity {
    private DBManager dbManager;
    LinearLayout activitylinlay;
    private String emailConnect;
    User userConnect;
    PieChart pieChart;
    TextView histwithbiggy,histwithouttc1,histhat,histcasta,histmerlin,histmudr,histp;
    View histwithbiggyV,histwithouttc1V,histhatV,histcastaV,histmerlinV,histmudrV,histpV;;

    public int getColor(@ColorRes int colorResId, Context context) {
        int color = ContextCompat.getColor(context, colorResId);
        return color ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piechart_history);
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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_history);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_history:
                    Intent intent3=new Intent(getApplicationContext(),HistoryActivity.class);
                    intent3.putExtra("emailConnect",emailConnect);
                    startActivity(intent3);
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
                    Intent intent1=new Intent(getApplicationContext(),DNAClassificationActivity.class);
                    intent1.putExtra("emailConnect",emailConnect);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        userConnect=dbManager.getUserByEmail(emailConnect);
        pieChart=(PieChart)findViewById(R.id.piechart);
        histwithouttc1=findViewById(R.id.histwithouttc1);
        histwithbiggy=findViewById(R.id.histwithbiggy);
        histhat=findViewById(R.id.histhat);
        histp=findViewById(R.id.histp);
        histcasta=findViewById(R.id.histcasta);
        histmerlin=findViewById(R.id.histmerlin);
        histmudr=findViewById(R.id.histmudr);

        histwithouttc1V=findViewById(R.id.histwithouttc1V);
        histwithbiggyV=findViewById(R.id.histwithbiggyV);
        histhatV=findViewById(R.id.histhatV);
        histpV=findViewById(R.id.histpV);
        histcastaV=findViewById(R.id.histcastaV);
        histmerlinV=findViewById(R.id.histmerlinV);
        histmudrV=findViewById(R.id.histmudrV);
        String stat =intent.getStringExtra("stat");
        Log.d("stat", "stat: " + stat);

        History history=dbManager.HistoryByAll(emailConnect,(String) intent.getStringExtra("stat"),intent.getStringExtra("date"));
        System.out.println(history.getPiggy());
        if(stat.equals("nTE")) {
            histhatV.setVisibility(View.GONE);
            histpV.setVisibility(View.GONE);
            histmerlinV.setVisibility(View.GONE);
            histmudrV.setVisibility(View.GONE);
            histcastaV.setVisibility(View.GONE);


            histhat.setVisibility(View.GONE);
            histp.setVisibility(View.GONE);
            histmerlin.setVisibility(View.GONE);
            histmudr.setVisibility(View.GONE);
            histcasta.setVisibility(View.GONE);
            float withPer = Float.parseFloat(history.getWithPer().split("%")[0]);
            float withoutPer = Float.parseFloat(history.getWithoutPer().split("%")[0]);
            pieChart.addPieSlice(new PieModel("with ET", withPer, getColor(  R.color.biggy, this)));
            pieChart.addPieSlice(new PieModel("without ET", withoutPer, getColor(  R.color.tc1, this)));
            // Start the animation
            pieChart.startAnimation();
            histwithouttc1.setText(withPer + "% TE");
            histwithbiggy.setText(withoutPer + "% nTE");
//            histwithouttc1V.setVisibility(View.GONE);
//            histwithbiggyV.setVisibility(View.GONE);

        }

        else {
            float tc1 = Float.parseFloat(history.getTc1().split("%")[0]);
            float hat = Float.parseFloat(history.getHat().split("%")[0]);
            float piggy = Float.parseFloat(history.getPiggy().split("%")[0]);
            float p = Float.parseFloat(history.getP().split("%")[0]);
            float mudr = Float.parseFloat(history.getMudr().split("%")[0]);
            float casta = Float.parseFloat(history.getCasta().split("%")[0]);
            float merlin = Float.parseFloat(history.getMerlin().split("%")[0]);

//            # ['0.005%', '0.009%', '0.000%', '0.010%', '0.974%', '0.001%', '0.000%']
//            # ['tc1ma', 'p ele', 'mudr%', 'hat %', 'biggy', 'merli', 'casta']
            Log.d("tc1", "tc1 Class: " + tc1);
            Log.d("hat", "hat Class: " + hat);
            Log.d("piggy", "piggy Class: " + piggy);
            Log.d("p", "p Class: " + p);
            pieChart.addPieSlice(new PieModel("PiggyBac", piggy, getColor(  R.color.biggy, this)));
            pieChart.addPieSlice(new PieModel("tc1mariner", tc1, getColor(  R.color.tc1, this)));
            pieChart.addPieSlice(new PieModel("P", p, getColor(  R.color.p, this)));
            pieChart.addPieSlice(new PieModel("HAT", hat, getColor(  R.color.hat, this)));
            pieChart.addPieSlice(new PieModel("merlin", merlin, getColor(  R.color.merlin, this)));
            pieChart.addPieSlice(new PieModel("mudr", mudr, getColor(  R.color.mudr, this)));
            pieChart.addPieSlice(new PieModel("casta", casta, getColor(  R.color.casta, this)));

            // Start the animation
            pieChart.startAnimation();
            histwithouttc1.setText(tc1 + "% TC1-Mariner");
            histwithbiggy.setText(piggy + "% PiggyBac");
//            histwithouttc1V.setVisibility(View.GONE);
//            histwithbiggyV.setVisibility(View.GONE);
            histhatV.setVisibility(View.VISIBLE);
            histpV.setVisibility(View.VISIBLE);
            histmerlinV.setVisibility(View.VISIBLE);
            histmudrV.setVisibility(View.VISIBLE);
            histcastaV.setVisibility(View.VISIBLE);


            histhat.setVisibility(View.VISIBLE);
            histp.setVisibility(View.VISIBLE);
            histmerlin.setVisibility(View.VISIBLE);
            histmudr.setVisibility(View.VISIBLE);
            histcasta.setVisibility(View.VISIBLE);
            histhat.setText(hat + "% hAT");
            histp.setText(p +"% P Element");
            histmerlin.setText(merlin+ "% Merlin");
            histcasta.setText( casta+"% CASTA");
            histmudr.setText(mudr+ "% muDR / Mutator");
        }
    }

    public void returnClick(View v){
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);

        intent.putExtra("emailConnect",emailConnect);
        startActivity(intent);
    }
}
