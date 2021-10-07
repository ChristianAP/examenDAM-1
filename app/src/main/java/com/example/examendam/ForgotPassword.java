package com.example.examendam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    Button btnRecuperar, buttonBack;
    EditText txtRecEmail;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
                getSupportActionBar().hide();
        btnRecuperar = findViewById(R.id.btnRecuperar);
        buttonBack = findViewById(R.id.buttonBack);
        txtRecEmail = findViewById(R.id.txtRecEmail);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.txtRecEmail, Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar();

            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void validar() {
        String email = txtRecEmail.getText().toString();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtRecEmail.setError("CORREO INVÁLIDO");
            return;
        }
        sendEmail(email);
    }

    private void sendEmail(String s) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = s;
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, "CORREO ENVIADO CON ÉXITO", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(ForgotPassword.this, "NO SE PUDO ENVIAR EL CORREO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}