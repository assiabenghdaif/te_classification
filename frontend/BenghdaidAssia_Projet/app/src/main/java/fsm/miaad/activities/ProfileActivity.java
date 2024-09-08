package fsm.miaad.activities;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import fsm.miaad.DAO.DBManager;
import fsm.miaad.R;
import fsm.miaad.models.User;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout personalinfo;//, experience, review;
    TextView personalinfobtn, email, lastname,firstname,fullname,address,phone,savepi,editpi,saveci,editci;
    EditText inputfirstname,inputlastname,inputaddress,inputphone;
    ImageView profileImage,changeImageProfilBTN;
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALERIE_REQUEST = 0;
    private DBManager dbManager;
    private String emailConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        getSupportActionBar().hide();

        Intent intent = getIntent();
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        emailConnect= intent.getStringExtra("emailConnect");
        dbManager = new DBManager(this);
        dbManager.open();

        User userConnect=dbManager.getUserByEmail(emailConnect);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
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
                    return true;
                case R.id.bottom_adn:
                    Intent intent2=new Intent(getApplicationContext(),DNAClassificationActivity.class);
                    intent2.putExtra("emailConnect",emailConnect);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        byte[] image=dbManager.getImageByEmail(emailConnect);

        personalinfo = findViewById(R.id.personalinfo);
        profileImage = findViewById(R.id.profileImage);
        personalinfobtn = findViewById(R.id.personalinfobtn);
        email= findViewById(R.id.email);
        lastname= findViewById(R.id.lastname);
        firstname= findViewById(R.id.firstname);
        fullname= findViewById(R.id.fullname);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.phone);
        changeImageProfilBTN=findViewById(R.id.changeImageProfilBTN);

        inputfirstname=findViewById(R.id.inputfirstname);
        inputlastname=findViewById(R.id.inputlastname);
        inputaddress=findViewById(R.id.inputaddress);
        inputphone=findViewById(R.id.inputphone);

        editpi=findViewById(R.id.editpi);
        savepi=findViewById(R.id.savepi);
        saveci=findViewById(R.id.saveci);
        editci=findViewById(R.id.editci);

        inputlastname.setVisibility(View.GONE);
        lastname.setVisibility(View.VISIBLE);
        inputfirstname.setVisibility(View.GONE);
        firstname.setVisibility(View.VISIBLE);
        inputaddress.setVisibility(View.GONE);
        address.setVisibility(View.VISIBLE);
        inputphone.setVisibility(View.GONE);
        phone.setVisibility(View.VISIBLE);
//        image=findViewById(R.id.image);

        email.setText(emailConnect);
        lastname.setText(userConnect.getLastname());
        firstname.setText(userConnect.getFirstname());
        fullname.setText(userConnect.getFirstname()+" "+userConnect.getLastname());
        address.setText(userConnect.getAddress());
        phone.setText(userConnect.getPhone());
        if(image!=null) {
            Bitmap bitmapimage = BitmapFactory.decodeByteArray(image, 0, image.length);
            profileImage.setImageBitmap(bitmapimage);
        }


        personalinfo.setVisibility(View.VISIBLE);


        personalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.VISIBLE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.blue));

            }
        });
    }
    public void changeImageProfil(View v){
//        launchCamera();

        // Initializing the popup menu and giving the reference as current context
        PopupMenu popupMenu = new PopupMenu(ProfileActivity.this, changeImageProfilBTN);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.camera_or_galerie, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.camera) {
                    launchCamera();
//                    Toast.makeText(getApplicationContext(), "Email clicked", Toast.LENGTH_SHORT).show();
                } else if (menuItem.getItemId() == R.id.galerie) {
                    launchGalerie();
//                    Toast.makeText(getApplicationContext(), "Call clicked", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(ProfileActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // Showing the popup menu
        popupMenu.show();
    }



    private void launchGalerie(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALERIE_REQUEST);

    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            profileImage.setImageBitmap(imageBitmap);

            // Assuming you have already captured the image and stored it in a Bitmap object called "imageBitmap"
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Cursor cursor = dbManager.getAllImage();
            boolean result=false;

            boolean empty=false;
            if (cursor.moveToFirst()) {
                do{
                    if(cursor.getString(1).equals(emailConnect)) {
                        result = true;
                        break;
                    }
                }while (cursor.moveToNext());
            }
            if(result==true){
                //update
                dbManager.updateImage(emailConnect,byteArray);

            }
            else
                dbManager.insertImage(emailConnect,byteArray);
        }
        else if(requestCode == GALERIE_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

            profileImage.setImageBitmap(imageBitmap);

            // Assuming you have already captured the image and stored it in a Bitmap object called "imageBitmap"
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Cursor cursor = dbManager.getAllImage();
            boolean result=false;
            boolean empty=false;
            if (cursor.moveToFirst()) {
                do{
                    if(cursor.getString(1).equals(emailConnect)) {
                        result=true;
                        break;

                    }
                }while (cursor.moveToNext());
            }
            if(result==true){
                //update
                dbManager.updateImage(emailConnect,byteArray);

            }
            else
                dbManager.insertImage(emailConnect,byteArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  void Disconnect(View v){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void EditContactInfo(View v){
        editci.setVisibility(View.GONE);
        saveci.setVisibility(View.VISIBLE);
        saveci.setText("Save");

        String oldpho=phone.getText().toString();
        phone.setVisibility(View.GONE);
        inputphone.setVisibility(View.VISIBLE);
        inputphone.setText(oldpho);

    }

    public void SaveContactInfo(View v){
        String phoneString=inputphone.getText().toString();
        String pattern = "(06|07)\\d{8}";
        if(phoneString.matches(pattern)) {
            saveci.setVisibility(View.GONE);
            editci.setVisibility(View.VISIBLE);
            editci.setText("Edit");

            dbManager.updateContactInfo(emailConnect, inputphone.getText().toString());
            inputphone.setVisibility(View.GONE);
            phone.setVisibility(View.VISIBLE);
            User userConnect = dbManager.getUserByEmail(emailConnect);
            phone.setText(userConnect.getPhone());
            Toast.makeText(ProfileActivity.this, "Your changes have been successfully saved!!", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(ProfileActivity.this, "Your phone number is invalide!!", Toast.LENGTH_SHORT).show();


    }

    public void EditPersonnalInfo(View v){


        editpi.setVisibility(View.GONE);
        savepi.setVisibility(View.VISIBLE);
        savepi.setText("Save");

        String oldf=firstname.getText().toString();
        firstname.setVisibility(View.GONE);
        inputfirstname.setVisibility(View.VISIBLE);
        inputfirstname.setText(oldf);
        String oldl=lastname.getText().toString();
        lastname.setVisibility(View.GONE);
        inputlastname.setVisibility(View.VISIBLE);
        inputlastname.setText(oldl);
        String olda=address.getText().toString();
        address.setVisibility(View.GONE);
        inputaddress.setVisibility(View.VISIBLE);
        inputaddress.setText(olda);






    }
    public  void SavePersonnalInfo(View v){
        savepi.setVisibility(View.GONE);
        editpi.setVisibility(View.VISIBLE);
        editpi.setText("Edit");

        dbManager.updatePersonnalInfo(emailConnect,inputfirstname.getText().toString(),inputlastname.getText().toString(),inputaddress.getText().toString());
        inputlastname.setVisibility(View.GONE);
        lastname.setVisibility(View.VISIBLE);
        inputfirstname.setVisibility(View.GONE);
        firstname.setVisibility(View.VISIBLE);
        inputaddress.setVisibility(View.GONE);
        address.setVisibility(View.VISIBLE);
        User userConnect=dbManager.getUserByEmail(emailConnect);
        lastname.setText(userConnect.getLastname() );
        firstname.setText(userConnect.getFirstname());
        fullname.setText(userConnect.getFirstname()+" "+userConnect.getLastname());
        address.setText(userConnect.getAddress());
        Toast.makeText(ProfileActivity.this, "Your changes have been successfully saved!!", Toast.LENGTH_SHORT).show();
    }

//    public void ReturnClick(View v){
//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//
//        intent.putExtra("emailConnect",emailConnect);
//        startActivity(intent);
//    }


}

