package fsm.miaad.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    private DBManager dbManager;
    private EditText email,password;
    private TextView btn,forgotPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        getActionBar().hide();
        setContentView(R.layout.activity_login);

        dbManager = new DBManager(this);
        dbManager.open();

        btn=findViewById(R.id.textViewSignUp);
        email=findViewById(R.id.inputEmail);
        password=findViewById(R.id.inputPassword);
        btnLogin=findViewById(R.id.btnlogin);
//        error=findViewById(R.id.error);
        forgotPassword = findViewById(R.id.forgotPassword);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = dbManager.fetch();
                boolean result=false;
                boolean empty=false;
                if (cursor.moveToFirst()) {
                    do{
                        if(cursor.getString(4).equals(password.getText().toString()) && cursor.getString(3).equals(email.getText().toString().trim())) {
                            result=true;
                            break;

                        }
                    }while (cursor.moveToNext());
                }
                if(result==true){
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    String emailConnect= email.getText().toString().trim();
                    intent.putExtra("emailConnect",emailConnect);
                    startActivity(intent);

                }
                else Toast.makeText(LoginActivity.this, "The Email or Password incorrect", Toast.LENGTH_SHORT).show();

//                error.setText();
            }
        });
    }
}

