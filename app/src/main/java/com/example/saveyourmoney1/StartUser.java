package com.example.saveyourmoney1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_user);
    }

    @Override
    protected void onStart() {
        verificarInicioSesion();
        super.onStart();
    }

    //Método para la creación del menú superior
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start_user, menu);
        return true;
    }

    /*Método para darle funcionalidad el menú superior
        - Cerrar Sesión: se realiza un singOut sobre el FirebaseAuth
        - Eliminar Usuario: se utiliza una ventana de dialogo para la confirmación
                            y se elimina usuario sobre FirebaseUser y Database.
                            Se facilita salto de pantalla a MainActivity para un nuevo login.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case (R.id.cerrarSesion):
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Toast.makeText(this, "Se ha cerrado la sesión", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                StartUser.this.finish();
                break;
            case (R.id.eliminarUsuario):
                AlertDialog.Builder alerta = new AlertDialog.Builder(StartUser.this);
                alerta.setMessage("¿Desea eliminar su cuenta?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIO");
                                reference.child(mUser.getUid()).removeValue();
                                mUser.delete();
                                Intent q = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(q);
                                StartUser.this.finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Confirmación");
                titulo.show();
        }
        return super.onOptionsItemSelected(item);
    }

    //Método para verificar que la App se encuentra con una sesión cargada
    private void verificarInicioSesion() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    //Método para ejecutar salto de pantalla a Home
    public void irGrafica(View view){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        StartUser.this.finish();
    }

    //Método para ejecutar salto de pantalla a Deseo
    public void irDeseo(View view){
        Intent i = new Intent(this, Deseo.class);
        startActivity(i);
        StartUser.this.finish();
    }
}