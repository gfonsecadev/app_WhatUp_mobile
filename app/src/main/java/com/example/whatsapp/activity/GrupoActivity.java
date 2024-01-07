package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;

import com.example.whatsapp.R;
import com.example.whatsapp.entidades.Usuario;
import com.example.whatsapp.ferramentas.firebase;
import com.example.whatsapp.recyclers.AdapterGrupo;
import com.example.whatsapp.recyclers.AdapterGrupoSelecionado;
import com.example.whatsapp.recyclers.RecyclerItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GrupoActivity extends AppCompatActivity {
    private RecyclerView recyclerGrupo;
    private RecyclerView recyclerGrupoSelecionado;
    private List<Usuario> listGrupo=new ArrayList<>();
    private List<Usuario> listGrupoReserva=new ArrayList<>();
    private List<Usuario> listGrupoSelecionado=new ArrayList<>();
    private Toolbar toolbar;
    private ValueEventListener eventListener;
    private DatabaseReference usuarioRef;
    private AdapterGrupo adapterGrupo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        recyclerGrupo=findViewById(R.id.recyclerGrupoContatos);
        recyclerGrupoSelecionado=findViewById(R.id.recyclerGrupoSelecionados);
        toolbar=findViewById(R.id.toolbarGrupos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getExtras()!=null){
            listGrupo= (List<Usuario>) getIntent().getSerializableExtra("usuario");

        }else ListContatos();

            adapterGrupo=new AdapterGrupo(listGrupo,getApplicationContext());

            AdapterGrupoSelecionado adapterGrupoSelecionado=new AdapterGrupoSelecionado(listGrupoSelecionado,getApplicationContext());
            RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            recyclerGrupoSelecionado.setLayoutManager(layoutManager1);
            recyclerGrupoSelecionado.setHasFixedSize(true);
            recyclerGrupoSelecionado.setAdapter(adapterGrupoSelecionado);



        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerGrupo.setLayoutManager(layoutManager);
        recyclerGrupo.setHasFixedSize(true);
        recyclerGrupo.setAdapter(adapterGrupo);

        recyclerGrupoSelecionado.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerGrupo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelecionado=listGrupoSelecionado.get(position);
                listGrupoSelecionado.remove(usuarioSelecionado);
                listGrupo.add(usuarioSelecionado);
                adapterGrupoSelecionado.notifyDataSetChanged();
                adapterGrupo.notifyDataSetChanged();
                toolbar();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));


        recyclerGrupo.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerGrupo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelecionado=listGrupo.get(position);
                listGrupo.remove(usuarioSelecionado);
                listGrupoSelecionado.add(usuarioSelecionado);
                adapterGrupoSelecionado.notifyDataSetChanged();
                adapterGrupo.notifyDataSetChanged();
                toolbar();


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));



    }



    public void toolbar(){
        int selecionado=listGrupoSelecionado.size();
        int totalGrupo=listGrupo.size()+selecionado;

        toolbar.setSubtitle("Selecionado: " +selecionado+ " de "+totalGrupo);

    }

    public void SelecionarGrupo(View view){
        Intent intent =new Intent(this,GrupoSelecionadoActivity.class   );
        intent.putExtra("usuario", (Serializable) listGrupoSelecionado);
        startActivity(intent);
        finish();

    }

    public void ListContatos(){
        DatabaseReference databaseReference= firebase.databaseInstance();
        usuarioRef=databaseReference.child("Usuarios");

        eventListener= usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dados:snapshot.getChildren()) {
                    Usuario usuario=dados.getValue(Usuario.class);

                    if(!firebase.recuperar_emailUsuario().equals(usuario.getEmail())) {

                        listGrupo.add(usuario);
                    }

                }

                adapterGrupo.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @Override
    protected void onStop() {
        super.onStop();
        if(getIntent().getExtras()==null){
            usuarioRef.removeEventListener(eventListener);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}