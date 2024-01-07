package com.example.whatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsapp.R;
import com.example.whatsapp.ferramentas.Metodos_Cadastro_Login;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("");
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


    public void concordarTermos(View view){
        startActivity(new Intent(this,LoginActivity.class));
    }

    @Override
    protected void onStart() {
        Metodos_Cadastro_Login.verificar_logado(this);
        super.onStart();
    }
}