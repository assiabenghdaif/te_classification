package fsm.miaad.activities;

import android.content.Intent;
import android.database.Cursor;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import fsm.miaad.DAO.DBManager;
import fsm.miaad.R;
import fsm.miaad.models.User;

public class HistoryActivity extends AppCompatActivity {
    private DBManager dbManager;
    LinearLayout activitylinlay;
    private String emailConnect;
    User userConnect;
    ImageView menu_botton,activity_image;
    TextView stat,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
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
        bottomNavigationView.setSelectedItemId(R.id.bottom_history);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_history:
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

        menu_botton = findViewById(R.id.menu_botton);
//        activity_image = findViewById(R.id.activity_image);
//        date = findViewById(R.id.date);
//        stat = findViewById(R.id.stat);
        activitylinlay =findViewById(R.id.activitylinlay);
        Cursor histories =dbManager.HistoryByEmail(emailConnect);
        int count=0;
        if (histories.moveToFirst()) {
            do{
                //activity,dateDe,withPer,withoutPer
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setPadding(0, 0, 10, 0);

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        (60), // Convert dp to pixels if needed
                        (60)));
                imageView.setBackgroundResource(R.drawable.circular_grey_bordersolid);
                imageView.setPadding((10), (10), (10), (10));
                if(histories.getString(0).contains("True")) {
                    imageView.setImageResource(R.drawable.stat_1);
                    imageView.setTag("True");
                }
                else if (histories.getString(0).contains("False")) {
                    imageView.setImageResource(R.drawable.stat_2);
                    imageView.setTag("False");
                }
                imageView.setId(View.generateViewId());

                Space space1 = new Space(this);
                space1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1));

                TextView textView1 = new TextView(this);
                textView1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
//            textView1.setMargin(dpToPx(20));
                textView1.setText(histories.getString(1));
                textView1.setTextColor(ContextCompat.getColor(this, R.color.goodgrey));
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                textView1.setId(View.generateViewId());

                Space space2 = new Space(this);
                space2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1));

                TextView textView2 = new TextView(this);
                textView2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
//            textView2.setMarginLeft(dpToPx(20));
                textView2.setText(histories.getString(0));
                textView2.setTextColor(ContextCompat.getColor(this, R.color.goodgrey));
                textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                textView2.setId(View.generateViewId());

                linearLayout.addView(imageView);
                linearLayout.addView(space1);
                linearLayout.addView(textView1);
                linearLayout.addView(space2);
                linearLayout.addView(textView2);

                activitylinlay.addView(linearLayout);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        fin.setText(histories.getString(0));
//                        History history=dbManager.HistoryByAll(emailConnect,(String) imageView.getTag(),textView1.getText().toString(),textView2.getText().toString());
                        Maps(textView1.getText().toString(),textView2.getText().toString());//,history.getLatitudeStart(),history.getLongitudeStart(),history.getLatitudeFinish(),history.getLongitudeFinish());
                    }
                });
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        fin.setText(histories.getString(0));
//                        History history=dbManager.HistoryByAll(emailConnect,(String) imageView.getTag(),textView1.getText().toString(),textView2.getText().toString());
                        Maps(textView1.getText().toString(),textView2.getText().toString());//,history.getLatitudeStart(),history.getLongitudeStart(),history.getLatitudeFinish(),history.getLongitudeFinish());
                    }
                });
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        fin.setText(histories.getString(0));
                        //History history=dbManager.HistoryByAll(emailConnect,(String) imageView.getTag(),textView1.getText().toString(),textView2.getText().toString());
                        Maps(textView1.getText().toString(),textView2.getText().toString());//,history.getLatitudeStart(),history.getLongitudeStart(),history.getLatitudeFinish(),history.getLongitudeFinish());
                    }
                });



            }while (histories.moveToNext());
        }
    }

    public void Maps(String date,String stat){//,double LatitudeStart,double LongitudeStart,double LatitudeFinish,double LongitudeFinish){
        Intent intent = new Intent(getApplicationContext(), PiechartActivity.class);

        intent.putExtra("date",date);
        intent.putExtra("stat",stat);

        intent.putExtra("emailConnect",emailConnect);
        startActivity(intent);
    }

    public void menu(View v){
        // Initializing the popup menu and giving the reference as current context
        PopupMenu popupMenu = new PopupMenu(HistoryActivity.this, menu_botton);

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

    public void emptyHist(View v){
        dbManager.deleteHistory(emailConnect);
        Intent intent2=new Intent(getApplicationContext(),HistoryActivity.class);
        intent2.putExtra("emailConnect",emailConnect);
        startActivity(intent2);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }


}