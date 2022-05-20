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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saveyourmoney1.model.Usuario;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;;

public class Home extends AppCompatActivity {

    PieChart pieChart;
    ArrayList<String> valoresX = new ArrayList<>();
    ArrayList<Entry> valoresY = new ArrayList<>();
    ArrayList<Integer> colores = new ArrayList<>();

    private TextView usuarioHome, simulacion;
    private EditText ingreso, gasto;
    private ImageView imageHome;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usuarioHome = findViewById(R.id.usuarioHome);
        simulacion = findViewById(R.id.simulacion);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        ingreso = findViewById(R.id.ingresoET);
        gasto = findViewById(R.id.gastoET);
        imageHome = findViewById(R.id.imageHome);

        pieChart = findViewById(R.id.pcGrafica);
    }

    @Override
    protected void onStart() {
        verificarInicioSesion();
        super.onStart();
    }

    //Método para añadir una cantidad de ingreso a la base de datos Firebase Realtime Database
    public void añadirIngreso(View view) {
        if(ingreso.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe rellenar el campo ingreso.", Toast.LENGTH_LONG).show();
        }else{
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("USUARIO").child(mUser.getUid()).child("cantIngresos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Double ingresoStatic = Double.parseDouble(String.valueOf(task.getResult().getValue()));
                    Double ingresoExtra = Double.parseDouble(ingreso.getText().toString());
                    Double result = ingresoStatic + ingresoExtra;
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIO");

                    reference.child(mUser.getUid()).child("cantIngresos").setValue(result);
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                    Home.this.finish();
                }
            });
        }
    }

    //Método para quitar una cantidad de ingreso a la base de datos Firebase Realtime Database
    public void quitarIngreso(View view) {
        if(ingreso.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe rellenar el campo ingreso.", Toast.LENGTH_LONG).show();
        }else {
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("USUARIO").child(mUser.getUid()).child("cantIngresos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Double ingresoStatic = Double.parseDouble(String.valueOf(task.getResult().getValue()));
                    Double ingresoExtra = Double.parseDouble(ingreso.getText().toString());
                    Double result = ingresoStatic - ingresoExtra;
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIO");

                    reference.child(mUser.getUid()).child("cantIngresos").setValue(result);
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                    Home.this.finish();
                }
            });
        }
    }

    //Método para añadir una cantidad de gastos a la base de datos Firebase Realtime Database
    public void añadirGasto(View view) {
        if(gasto.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe rellenar el campo gasto.", Toast.LENGTH_LONG).show();
        }else {
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("USUARIO").child(mUser.getUid()).child("cantGastos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Double gastoStatic = Double.parseDouble(String.valueOf(task.getResult().getValue()));
                    Double gastoExtra = Double.parseDouble(gasto.getText().toString());
                    Double result = gastoStatic + gastoExtra;
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIO");

                    reference.child(mUser.getUid()).child("cantGastos").setValue(result);
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                    Home.this.finish();
                }
            });
        }
    }

    //Método para quitar una cantidad de gastos a la base de datos Firebase Realtime Database
    public void quitarGasto(View view) {
        if(gasto.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe rellenar el campo gasto.", Toast.LENGTH_LONG).show();
        }else {
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("USUARIO").child(mUser.getUid()).child("cantGastos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Double gastoStatic = Double.parseDouble(String.valueOf(task.getResult().getValue()));
                    Double gastoExtra = Double.parseDouble(gasto.getText().toString());
                    Double result = gastoStatic - gastoExtra;
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIO");

                    reference.child(mUser.getUid()).child("cantGastos").setValue(result);
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                    Home.this.finish();
                }
            });
        }
    }

    //Método para verificar que la App se encuentra con una sesión cargada
    private void verificarInicioSesion() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            crearGrafica();
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    //Método para la creación del menú superior
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    /*Método para darle funcionalidad el menú superior
        - Cerrar Sesión: se realiza un singOut sobre el FirebaseAuth
        - Deseo: permite un salto de pantalla directo a Deseo
        - Reset: se utiliza una ventana de dialogo para la confirmación
                 y se eliminan los datos de usuario sobre Firebase Realtime Database.
                 Se facilita un refresco de pantalla para ejecutar la gráfica.
        - Atras: Facilita el salto de pantalla a StartUser
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
                Home.this.finish();
                break;
            case (R.id.deseo):
                Intent e = new Intent(this, Deseo.class);
                startActivity(e);
                Home.this.finish();
                break;
            case (R.id.reset):
                AlertDialog.Builder alerta = new AlertDialog.Builder(Home.this);
                alerta.setMessage("¿Desea resetear los datos de la gráfica?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIO");
                                reference.child(mUser.getUid()).child("cantGastos").setValue(0.0);
                                reference.child(mUser.getUid()).child("cantIngresos").setValue(0.0);
                                reference.child(mUser.getUid()).child("cantAhorros").setValue(0.0);
                                Intent q = new Intent(getApplicationContext(), Home.class);
                                startActivity(q);
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
                break;
            case (R.id.atras):
                Intent x = new Intent(this, StartUser.class);
                startActivity(x);
                Home.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Método para realizar la creación de la gráfica de datos obtenidos de Firebase Realtime Database
    private void crearGrafica() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("USUARIO").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario user1 = snapshot.getValue(Usuario.class);
                    usuarioHome.setText("Usuario: " + user1.getUsuario());
                    if(user1.getCantGastos()==0.0&&user1.getCantIngresos()==0.0){
                        Toast.makeText(getApplicationContext(), "Su gráfica esta actualmente sin datos.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        imageHome.setVisibility(View.INVISIBLE);
                        simulacion.setVisibility(View.INVISIBLE);
                        Double dato0 = user1.getCantGastos();
                        Double dato1 = user1.getCantIngresos();
                        Double dato2 = (dato1 - dato0);

                        reference.child("USUARIO").child(mUser.getUid()).child("cantAhorros").setValue(dato2);

                        //pieChart.des;
                        pieChart.setHoleRadius(20f);
                        pieChart.setDrawXValues(true);
                        pieChart.setDrawYValues(true);
                        pieChart.setRotationEnabled(true);
                        pieChart.animateXY(1500, 1500);
                        pieChart.setNoDataText("");

                        //Datos lista X
                        valoresX.add("Gastos");
                        valoresX.add("Ingresos");
                        valoresX.add("Ahorro");

                        //Datos lista Y
                        valoresY.add(new Entry(dato0.floatValue(), 0));
                        valoresY.add(new Entry(dato1.floatValue(), 1));
                        valoresY.add(new Entry(dato2.floatValue(), 2));

                        //Datos colores
                        colores.add(getResources().getColor(R.color.red_flat));
                        colores.add(getResources().getColor(R.color.green_flat));
                        colores.add(getResources().getColor(R.color.blue_flat));

                        //Seteo de valores Y y colores
                        PieDataSet set = new PieDataSet(valoresY, "");
                        set.setSliceSpace(5f);
                        set.setColors(colores);

                        //Seteo valores X
                        PieData data = new PieData(valoresX, set);
                        pieChart.setData(data);
                        pieChart.highlightValues(null);
                        pieChart.invalidate();

                        //Ocultar descripcion
                        pieChart.setDescription("Ingresos y gastos");
                        //Ocultar leyenda
                        pieChart.setDrawLegend(true);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}