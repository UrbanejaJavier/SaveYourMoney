package com.example.saveyourmoney1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saveyourmoney1.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText usuarioRegistro, correoRegistro, contraseñaRegistro, confirmacionContraseñaRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        iniciarComponentes();
    }

    //Relación de variables con las vistas
    private void iniciarComponentes(){
        usuarioRegistro = findViewById(R.id.usuarioRegistro);
        correoRegistro = findViewById(R.id.correoRegistro);
        contraseñaRegistro = findViewById(R.id.contraseñaRegistro);
        confirmacionContraseñaRegistro = findViewById(R.id.confirmacionContraseñaRegistro);
    }

    //Método de codificación de la contraseña para un uso más seguro
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /*Método que se ejecuta al pulsar botón de registro
        En este metodo se comprueba que no queden campos vacios,
        se comprueba que la contraseña y la contraseña de confirmación sean iguales,
        se crea usuario en FirebaseAuthentication con correo y contraseña
        y se realiza un salto a la pantalla inicio de sesión.
     */
    public void registrarUsuario(View view) {
        if (usuarioRegistro.getText().toString().isEmpty() || correoRegistro.getText().toString().isEmpty() || contraseñaRegistro.getText().toString().isEmpty() || confirmacionContraseñaRegistro.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe rellenar todos los campos.", Toast.LENGTH_LONG).show();
        } else {
            if (contraseñaRegistro.getText().toString().equals(confirmacionContraseñaRegistro.getText().toString())) {
                mAuth.createUserWithEmailAndPassword(correoRegistro.getText().toString(), getMD5(contraseñaRegistro.getText().toString()))
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Sing in success, update UI with the signed in user's information
                                    mUser = mAuth.getCurrentUser();
                                    guardarRealDatabase();
                                    Toast.makeText(getApplicationContext(), "Usuario creado.", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), InicioSesion.class);
                                    startActivity(i);
                                    Registro.this.finish();
                                } else {
                                    //If sing in falls, display a message to the user.
                                    //Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                    obtenerError(errorCode);
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Método para la creación de la base de datos en Firebase Realtime Database
    private void guardarRealDatabase() {
        assert mUser != null;
        String uid = mUser.getUid();
        String usuario = usuarioRegistro.getText().toString();
        String correo = correoRegistro.getText().toString();
        String contraseña = getMD5(contraseñaRegistro.getText().toString());

        Usuario user = new Usuario();
        user.setUid(uid);
        user.setUsuario(usuario);
        user.setCorreo(correo);
        user.setContraseña(contraseña);
        user.setCantIngresos(0.0);
        user.setCantGastos(0.0);
        user.setCantAhorros(0.0);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("USUARIO");

        reference.child(uid).setValue(user);
    }

    //Método para mostrar al usuario el motivo por el que no se ha podido crear usuario
    private void obtenerError(String error) {

        switch (error) {
            case "ERROR_INVALID_EMAIL":
                correoRegistro.setError("La dirección de correo electrónico no tiene el formato deseado.");
                correoRegistro.requestFocus();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                correoRegistro.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                correoRegistro.requestFocus();
                break;

            case "ERROR_WEAK_PASSWORD":
                contraseñaRegistro.setError("La contraseña no es válida.");
                contraseñaRegistro.requestFocus();
                confirmacionContraseñaRegistro.setError("La contraseña no es válida.");
                confirmacionContraseñaRegistro.requestFocus();
                break;
        }
    }
}