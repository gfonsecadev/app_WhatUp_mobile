package com.example.whatsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.R;
import com.example.whatsapp.ferramentas.EsconderTeclado;
import com.example.whatsapp.ferramentas.Metodos_Cadastro_Login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText email, senha;
    private FloatingActionButton fabLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Verifique sua conta");

        email = findViewById(R.id.textEmailLogin);
        senha = findViewById(R.id.textSenhaLogin);
        fabLogin = findViewById(R.id.floatingActionButton);

        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Metodos_Cadastro_Login.loginUsuario(email, senha, LoginActivity.this);
            }
        });

        EsconderTeclado.esconderTeclado(email, getApplicationContext());
        EsconderTeclado.esconderTeclado(senha, getApplicationContext());


    }

    public void cadastre_se(View view) {
        startActivity(new Intent(LoginActivity.this, CadastrarActivity.class));
    }
}