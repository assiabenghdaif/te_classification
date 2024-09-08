package fsm.miaad.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import fsm.miaad.DAO.DBManager;
import fsm.miaad.R;

public class ForgetActivity extends AppCompatActivity {

    private DBManager dbManager;
    private EditText email,password,confPassword;
    private TextView textViewSignUp;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        getActionBar().hide();
        setContentView(R.layout.activity_forget);

        dbManager = new DBManager(this);
        dbManager.open();

        confPassword=findViewById(R.id.confPassword);
        email=findViewById(R.id.inputEmail);
        password=findViewById(R.id.inputPassword);
        btnLogin=findViewById(R.id.btnlogin);
        textViewSignUp=findViewById(R.id.textViewSignUp);

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetActivity.this,LoginActivity.class));
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
                        if(cursor.getString(3).equals(email.getText().toString().trim())) {
                            result=true;
                            break;

                        }
                    }while (cursor.moveToNext());
                }
                if(result==true){
                    int len= password.length();
                    if(password.getText().toString().equals(confPassword.getText().toString())) {
                        if (len >= 8) {
                            boolean bol=dbManager.updatePassword(email.getText().toString(), password.getText().toString());
                            if (bol == true) {

                                Toast.makeText(ForgetActivity.this, "You're password have been successfully changed!!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                        else Toast.makeText(ForgetActivity.this, "The password must be >=8!!!", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(ForgetActivity.this, "The Two passwords aren't equals!!!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(ForgetActivity.this, "The Email is not existe", Toast.LENGTH_SHORT).show();

//                error.setText();
            }
        });
    }
}

