package com.example.whatsapp.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.entidades.Usuario;
import com.example.whatsapp.ferramentas.firebase;
import com.example.whatsapp.recyclers.AdapterContatos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContatoActivity extends AppCompatActivity {
    private RecyclerView recyclerViewContatos;
    private AdapterContatos adapterContatos;
    private final List<Usuario> listContatos = new ArrayList<>();
    private ValueEventListener eventListener;
    private DatabaseReference usuarioRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);
        getSupportActionBar().setTitle("Contatos");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerViewContatos = findViewById(R.id.recyclerContatos);

        adapterContatos = new AdapterContatos(listContatos, ContatoActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewContatos.setLayoutManager(linearLayoutManager);
        recyclerViewContatos.setHasFixedSize(true);
        recyclerViewContatos.setAdapter(adapterContatos);


    }

    public void ListContatos() {
        DatabaseReference databaseReference = firebase.databaseInstance();
        usuarioRef = databaseReference.child("Usuarios");

        eventListener = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listContatos.clear();
                Usuario usuario = new Usuario();
                usuario.setNome("Novo grupo");
                usuario.setEmail("");
                usuario.setFoto("");
                listContatos.add(usuario);
                for (DataSnapshot dados : snapshot.getChildren()) {
                    usuario = dados.getValue(Usuario.class);

                    if (!firebase.recuperar_emailUsuario().equals(usuario.getEmail())) {

                        listContatos.add(usuario);
                    }

                }
                adapterContatos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        ListContatos();
    }

    @Override
    protected void onStop() {
        super.onStop();

        usuarioRef.removeEventListener(eventListener);
    }
}