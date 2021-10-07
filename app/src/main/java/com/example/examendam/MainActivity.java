package com.example.examendam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.examendam.Adaptadores.ListViewMascotaAdapter;
import com.example.examendam.Modelo.Mascota;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Mascota> listMascota = new ArrayList<Mascota>();
    ArrayAdapter<Mascota> arrayAdapterMascota;
    ListViewMascotaAdapter listViewMascotaAdapter;
    LinearLayout linearLayoutEditar;

    EditText inputNombreMascota, inputRaza, inputNombreDueño, inputTelefono;
    Button btnCancelar;
    ListView listViewMascotas;

    Mascota mascotaSeleccionada;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNombreMascota = findViewById(R.id.inputNombreMascota);
        inputRaza = findViewById(R.id.inputRaza);
        inputNombreDueño = findViewById(R.id.inputNombreDueño);
        inputTelefono = findViewById(R.id.inputTelefono);
        btnCancelar = findViewById(R.id.btnCancelar);
        listViewMascotas = findViewById(R.id.listViewMascotas);
        linearLayoutEditar = findViewById(R.id.CajaEditar);

        listViewMascotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                mascotaSeleccionada = (Mascota) parent.getItemAtPosition(position);
                /// Imprimo los datos en los inputs
                inputNombreMascota.setText(mascotaSeleccionada.getMascota());
                inputRaza.setText(mascotaSeleccionada.getRaza());
                inputNombreDueño.setText(mascotaSeleccionada.getDueño());
                inputTelefono.setText(mascotaSeleccionada.getTelefono());
                //Hacemos visible la caja de editar
                linearLayoutEditar.setVisibility(View.VISIBLE);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutEditar.setVisibility(View.GONE);
                mascotaSeleccionada = null;
            }
        });

        inicializarFirebase();
        listarMacota();
    }

    private void listarMacota() {
        databaseReference.child("Mascota").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMascota.clear();
                // Agregamos los datos de la base de datos a la lista
                for (DataSnapshot objSnaptshot: dataSnapshot.getChildren()){
                    Mascota m = objSnaptshot.getValue(Mascota.class);
                    listMascota.add(m);
                }
                // Iniciar nuestro adaptador
                listViewMascotaAdapter = new ListViewMascotaAdapter(MainActivity.this, listMascota);
                //arrayAdapterMascota = new ArrayAdapter<Mascota>(
                   //     MainActivity.this,
                    //    android.R.layout.simple_list_item_1,
                   //     listMascota
               // );
                listViewMascotas.setAdapter(listViewMascotaAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crud_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String mascota = inputNombreMascota.getText().toString();
        String raza = inputRaza.getText().toString();
        String dueño = inputNombreDueño.getText().toString();
        String telefono = inputTelefono.getText().toString();

        switch (item.getItemId()){
            case R.id.menu_agregar:
                insertar();
                break;
            case R.id.menu_guardar:
                if (mascotaSeleccionada != null){
                    if (validarInputs() == false){
                        Mascota m = new Mascota();
                        m.setIdmascota(mascotaSeleccionada.getIdmascota());
                        m.setMascota(mascota);
                        m.setRaza(raza);
                        m.setDueño(dueño);
                        m.setTelefono(telefono);
                        databaseReference.child("Mascota").child(m.getIdmascota()).setValue(m);
                        Toast.makeText(MainActivity.this, "ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        linearLayoutEditar.setVisibility(View.GONE);
                        mascotaSeleccionada = null;
                    }
                }else{
                    Toast.makeText(this, "SELECCIONE UNA MASCOTA", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_eliminar:
                    if (mascotaSeleccionada != null){
                        Mascota m2 = new Mascota();
                        m2.setIdmascota(mascotaSeleccionada.getIdmascota());
                        databaseReference.child("Mascota").child(m2.getIdmascota()).removeValue();
                        linearLayoutEditar.setVisibility(View.GONE);
                        mascotaSeleccionada = null;
                        Toast.makeText(this, "MASCOTA ELIMINADA CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "SELECCIONE UNA MASCOTA PARA ELIMINAR", Toast.LENGTH_SHORT).show();
                    }
                break;
            case R.id.closeSesion:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "SESIÓN CERRADA!!", Toast.LENGTH_SHORT).show();
                login();
        }
        return super.onOptionsItemSelected(item);
    }
    private void login() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    private boolean validarInputs() {
        String mascota = inputNombreMascota.getText().toString();
        String raza = inputRaza.getText().toString();
        String dueño = inputNombreDueño.getText().toString();
        String telefono = inputTelefono.getText().toString();
        if (mascota.isEmpty() || mascota.length() < 3){
            showError(inputNombreMascota, "Nombre Inválido - 3 caracteres como mínimo");
            return true;
        }else if (raza.isEmpty() || raza.length() < 3){
            showError(inputRaza, "Nombre de Raza Inválido - 3 caracteres como mínimo");
            return true;
        }else if (dueño.isEmpty() || dueño.length() < 3){
            showError(inputNombreDueño, "Nombre de Dueño Inválido - 3 caracteres como mínimo");
            return true;
        }else if (telefono.isEmpty() || telefono.length() < 9){
            showError(inputRaza, "Número de Teléfono Inválido - 9 caracteres como mínimo");
            return true;
        }else{
            return false;
        }
    }

    private void insertar() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                MainActivity.this
        );
        View mView = getLayoutInflater().inflate(R.layout.insertar, null);
        Button btnIngresar = (Button) mView.findViewById(R.id.btnIngresar);
        final EditText mInputMascota = (EditText) mView.findViewById(R.id.inputMascotaIns);
        final EditText mInputRaza = (EditText) mView.findViewById(R.id.inputRazaIns);
        final EditText mInputDueño = (EditText) mView.findViewById(R.id.inputDueñoIns);
        final EditText mTelefono = (EditText) mView.findViewById(R.id.inputTelefonoIns);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mascota = mInputMascota.getText().toString();
                String raza = mInputRaza.getText().toString();
                String dueño = mInputDueño.getText().toString();
                String telefono = mTelefono.getText().toString();
                if (mascota.isEmpty() || mascota.length()< 3){
                    showError(mInputMascota, "Nombre Invalido mínimo 3 letras");
                }else if (raza.isEmpty() || raza.length()< 3){
                    showError(mInputRaza, "Raza Invalido mínimo 3 letras");
                }else if (dueño.isEmpty() || dueño.length()< 3){
                    showError(mInputDueño, "Nombre de Dueño Invalido mínimo 3 letras");
                }else if (telefono.isEmpty() || telefono.length()< 9){
                    showError(mInputMascota, "Telefono de 9 digitos");
                }else{
                    Mascota m = new Mascota();
                    m.setIdmascota(UUID.randomUUID().toString());
                    m.setMascota(mascota);
                    m.setRaza(raza);
                    m.setDueño(dueño);
                    m.setTelefono(telefono);
                    databaseReference.child("Mascota").child(m.getIdmascota()).setValue(m);
                    Toast.makeText(MainActivity.this, "REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
    }

    public void showError(EditText mInputMascota, String s) {
           inputNombreMascota.requestFocus();
           inputNombreMascota.setError(s);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}