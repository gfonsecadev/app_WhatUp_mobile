package com.example.whatsapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.R;
import com.example.whatsapp.ferramentas.EsconderTeclado;
import com.example.whatsapp.ferramentas.Metodos_Cadastro_Login;
import com.google.android.material.textfield.TextInputEditText;

public class CadastrarActivity extends AppCompatActivity {
    private TextInputEditText nome, email, senha;
    private Button buttonCadastrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        getSupportActionBar().hide();
        nome = findViewById(R.id.textNomecadastrar);
        email = findViewById(R.id.textEmailCadastrar);
        senha = findViewById(R.id.textSenhaCadastrar);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        EsconderTeclado.esconderTeclado(nome, getApplicationContext());
        EsconderTeclado.esconderTeclado(email, getApplicationContext());
        EsconderTeclado.esconderTeclado(senha, getApplicationContext());


        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Metodos_Cadastro_Login.cadastrarUsuario(nome, email, senha, CadastrarActivity.this);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}