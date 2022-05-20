package com.example.saveyourmoney1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saveyourmoney1.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Deseo extends AppCompatActivity {

    EditText objetivo, gastoDeseo;
    TextView resultadoDeseo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deseo);

        iniciarComponentes();
    }

    //Relación de variables con las vistas
    private void iniciarComponentes() {
        objetivo = findViewById(R.id.objetivo);
        gastoDeseo = findViewById(R.id.gastoDeseo);
        resultadoDeseo = findViewById(R.id.resultadoDeseo);
    }

    //Método para la creación del menú superior
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    /*Método para darle funcionalidad el menú superior
        - Atrás: Facilita el salto de pantalla a StartUser
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case (R.id.atras):
                Intent i = new Intent(this, StartUser.class);
                startActivity(i);
                Deseo.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*Método que se ejecuta al pulsar botón de calcular deseo
        Se comprueba que no queden campos vacios,
        se obtienen los datos necesarios de Firebase Realtime Database
        y se realiza el cálculo en meses comprobando que la cantidad de ahorros no sea 0.
        Se muestra en una ventana de dialogo el resultado.
     */
    public void calcularDeseo(View view){
        if(objetivo.getText().toString().isEmpty()||gastoDeseo.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe rellenar todos los campos.", Toast.LENGTH_LONG).show();
        }else{
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIO");
            reference.child(mUser.getUid()).child("objetivo").setValue(objetivo.getText().toString());
            reference.child(mUser.getUid()).child("gastoDeseo").setValue(Double.parseDouble(gastoDeseo.getText().toString()));
            reference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario user1 = snapshot.getValue(Usuario.class);
                    if (user1.getCantAhorros() == 0.0) {
                        Toast.makeText(getApplicationContext(), "Debe rellenar la gráfica para poder realizar el cálculo", Toast.LENGTH_LONG).show();
                    } else {
                        String objetivo = user1.getObjetivo();
                        Double cantAhorros = user1.getCantAhorros();
                        Double gastoDeseo = user1.getGastoDeseo();
                        int result = (int) (gastoDeseo / cantAhorros);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(Deseo.this);
                        alerta.setMessage("Se ha calculado con su ahorro actual que usted necesita " + result + " meses para poder invertir en: " + objetivo)
                                .setCancelable(false)
                                .setNegativeButton("Listo", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        ;
                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Resultado de su deseo");
                        titulo.show();
                        resultadoDeseo.setText("Se ha calculado con su ahorro actual que usted necesita " + result + " meses para poder invertir en: " + objetivo);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}