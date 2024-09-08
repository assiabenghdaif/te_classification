package fsm.miaad.activities;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fsm.miaad.DAO.DBManager;
import fsm.miaad.R;
import fsm.miaad.models.User;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private DBManager dbManager;
    private EditText email,password,passwordCon,fname,lname;
    private TextView btn,error;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        dbManager = new DBManager(this);
        dbManager.open();

        btn=findViewById(R.id.alreadyHaveAccount);
        btnRegister=findViewById(R.id.btnRegister);
        email=findViewById(R.id.inputEmail);
        password=findViewById(R.id.inputPassword);
        passwordCon=findViewById(R.id.inputConformPassword);
        fname=findViewById(R.id.inputFirstname);
        lname=findViewById(R.id.inputLastname);
        error=findViewById(R.id.error);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int len= password.length();
                if(password.getText().toString().equals(passwordCon.getText().toString())){
                    if(len>=8){
                        Cursor cursor = dbManager.fetch();
                        boolean result = false;
                        boolean empty = false;
                        if (cursor.moveToFirst()) {
                            do {
                                if (cursor.getString(3).equals(email.getText().toString().trim()) == true) {
                                    result = true;
                                    break;
                                }
                            } while (cursor.moveToNext());
                        }
                        if (result == true)
                            Toast.makeText(RegisterActivity.this, "This email is already in use!!!", Toast.LENGTH_SHORT).show();

//                    error.setText();
                        else {
                            User user = new User(fname.getText().toString(), lname.getText().toString(), email.getText().toString().trim(), password.getText().toString());
//                    error.setText(email.getText().toString().trim());
                            boolean bol = dbManager.insertUser(user);
                            if (bol == true) {

                                Toast.makeText(RegisterActivity.this, "You're have been successfully registry!!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    else Toast.makeText(RegisterActivity.this, "the password must be >=8!!!", Toast.LENGTH_SHORT).show();


                }
                else Toast.makeText(RegisterActivity.this, "the Two passwords aren't equals!!!", Toast.LENGTH_SHORT).show();

//                    error.setText("the Two passwords aren't equals!!!");


            }
        });
    }
}
