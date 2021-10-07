package com.example.examendam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText txtregName, txtregUser, txtregPass;
    Button btnRegister, buttonNext;

    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
                    getSupportActionBar().hide();
                firebaseAuth = FirebaseAuth.getInstance();
                awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
                awesomeValidation.addValidation(this, R.id.txtregUser, Patterns.EMAIL_ADDRESS,R.string.invalid_email);
                awesomeValidation.addValidation(this, R.id.txtregPass, ".{6,}", R.string.invalid_password);
        txtregName = findViewById(R.id.txtregName);
        txtregUser = findViewById(R.id.txtregUser);
        txtregPass = findViewById(R.id.txtregPass);

        btnRegister = findViewById(R.id.btnRegister);
        buttonNext = findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtregName.getText().toString();
                String email = txtregUser.getText().toString();
                String password = txtregPass.getText().toString();

                if (awesomeValidation.validate() || name.isEmpty()){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "USUARIO CREADO CORRECTAMENTE!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, "OCURRIO UN ERROR!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}