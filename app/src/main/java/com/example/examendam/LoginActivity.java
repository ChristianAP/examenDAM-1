package com.example.examendam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnScontraseña, buttonRegister;
    EditText txtuser, txtpass;
    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
                getSupportActionBar().hide();

        txtuser = findViewById(R.id.txtuser);
        txtpass = findViewById(R.id.txtpass);

        btnLogin = findViewById(R.id.btnLogin);
        btnScontraseña = findViewById(R.id.btnScontraseña);
        buttonRegister = findViewById(R.id.buttonRegister);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            home();
        }

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.txtregUser, Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.txtregPass, ".{6,}", R.string.invalid_password);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()){
                    String email = txtuser.getText().toString();
                    String password = txtpass.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                home();
                            }else{
                                Toast.makeText(LoginActivity.this, "OCURRIÓ UN ERROR!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        btnScontraseña.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void home() {
        Intent i = new Intent(this, WelcomeActivity.class);
        i.putExtra("email", txtuser.getText().toString());
        startActivity(i);
    }
}