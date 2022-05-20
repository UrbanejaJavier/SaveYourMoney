package com.example.saveyourmoney1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InicioSesion extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText correoInicio, contraseñaInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        mAuth = FirebaseAuth.getInstance();
        iniciarComponentes();
    }

    //Relación de variables con las vistas
    private void iniciarComponentes() {
        correoInicio = findViewById(R.id.correoInicio);
        contraseñaInicio = findViewById(R.id.contraseñaInicio);
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

    /*Método que se ejecuta al pulsar botón de inicio de sesión
        En este metodo se comprueba que no queden campos vacios,
        se inicia sesión con correo y contraseña gracias a FirebaseAuthentication
        y se realiza un salto a la pantalla StartUser.
     */
    public void iniciarSesion(View view){
        if (correoInicio.getText().toString().isEmpty() || contraseñaInicio.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe rellenar todos los campos.", Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(correoInicio.getText().toString(), getMD5(contraseñaInicio.getText().toString()))
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Sing in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(getApplicationContext(), StartUser.class);
                                startActivity(i);
                                InicioSesion.this.finish();
                            } else {
                                // If sing in fails, display a message to the user.
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                obtenerError(errorCode);
                            }
                        }
                    });
        }
    }

    //Método para mostrar al usuario el motivo por el que no se ha podido iniciar sesión
    private void obtenerError(String error) {

        switch (error) {

            case "ERROR_INVALID_EMAIL":
                correoInicio.setError("La dirección de correo electrónico no tiene el formato deseado.");
                correoInicio.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                contraseñaInicio.setError("La contraseña es incorrecta.");
                contraseñaInicio.requestFocus();
                contraseñaInicio.setText("");
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(getApplicationContext(), "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(getApplicationContext(), "No hay ningún registro de usuario que corresponda a este correo. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                contraseñaInicio.setError("La contraseña no es válida.");
                contraseñaInicio.requestFocus();
                break;
        }

    }
}