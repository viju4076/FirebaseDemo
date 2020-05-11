package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    TextView loginOrSign;
    boolean login=true;
    Button go;
    private FirebaseAuth mAuth;
    public void change(View view)
    {
        if(login)
        {
            login = false;
            loginOrSign.setText("Or LogIn");
            go.setText("SignUp");

        }
        else
        {
            login = true;
            loginOrSign.setText("Or SignUp");
            go.setText("LogIn");
        }

    }
    public void start()
    {

      // mAuth.getCurrentUser().sendEmailVerification();
        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
        startActivity(intent);
        finish();

    }
    public void login(View view)
    {
        String user=email.getText().toString();
        String pass=password.getText().toString();
        if(user.equals("")||pass.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Fields cannot be left blank",Toast.LENGTH_SHORT).show();
            return;
        }
        if(login)
        {
         mAuth.signInWithEmailAndPassword(user,pass)
         .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful())
                 {
                     Toast.makeText(getApplicationContext(),"Successfully logged in",Toast.LENGTH_SHORT).show();
                     start();
                 }
                 else
                 {
                     Toast.makeText(getApplicationContext(),"Sign in failed ",Toast.LENGTH_SHORT).show();
                   Log.i("sign in failure",task.getException().toString());
                 }

             }
         })   ;


        }
        else
        {
          mAuth.createUserWithEmailAndPassword(user,pass)
                  .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful())
                          {
                              Toast.makeText(getApplicationContext(),"Signup Successful...Now login",Toast.LENGTH_SHORT).show();

                          }
                          else
                          {
                              Toast.makeText(getApplicationContext(),"Could not signup",Toast.LENGTH_SHORT).show();
                              Log.i("signup failure",task.getException().toString());

                          }
                      }
                  });


        }
    }




            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                email=findViewById(R.id.username);
                password=findViewById(R.id.password);
                loginOrSign=findViewById(R.id.loginOrSign);
                go=findViewById(R.id.button);
                getSupportActionBar().setTitle("Email Login");
                mAuth=FirebaseAuth.getInstance();
                if(mAuth.getCurrentUser()!=null)
                {
                    start();
                }

            }
}
