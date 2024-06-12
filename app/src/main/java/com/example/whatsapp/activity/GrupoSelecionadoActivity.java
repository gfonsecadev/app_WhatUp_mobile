package com.example.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.databinding.ActivityGrupoSelecionadoBinding;
import com.example.whatsapp.entidades.Grupo;
import com.example.whatsapp.entidades.Usuario;
import com.example.whatsapp.ferramentas.firebase;
import com.example.whatsapp.recyclers.AdapterGrupoSelecionado;
import com.example.whatsapp.recyclers.RecyclerItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrupoSelecionadoActivity extends AppCompatActivity {
    private ActivityGrupoSelecionadoBinding binding;
    private List<Usuario> listaCadastrarGrupo = new ArrayList<>();
    private Grupo grupo;
    ActivityResultLauncher galeria = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Bitmap bitmap = null;
            Uri uri = null;
            try {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent imagem = result.getData();
                    uri = imagem.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (bitmap != null) {
                binding.imageGrupoCadastrar.setImageBitmap(bitmap);
                StorageReference storageReference = firebase.storageReference().child("Imagens")
                        .child("Perfil").child("Grupo").child(grupo.getIdGrupo()).child("perfilGrupo.jpg");

                UploadTask uploadTask = storageReference.putFile(uri);

                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(GrupoSelecionadoActivity.this, "Imagem salva!", Toast.LENGTH_LONG).show();
                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri uri = task.getResult();
                                    grupo.setFotoGrupo(String.valueOf(uri));
                                }
                            });
                        }
                    }
                });
            }


        }


    });
    private final DatabaseReference databaseReference = firebase.databaseInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //para a variavel passe o binding do tipo da activity inflando um getLayoutInflater
        binding = ActivityGrupoSelecionadoBinding.inflate(getLayoutInflater());
        //no setContentView invez de passar o layout da activity passe a variavel chamamdo o getRoot
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        grupo = new Grupo();
        String idGrupo = databaseReference.push().getKey();
        grupo.setIdGrupo(idGrupo);

        if (getIntent().getExtras() != null) {
            listaCadastrarGrupo = (List<Usuario>) getIntent().getSerializableExtra("usuario");

        }
        binding.textParticipantes.setText("Participantes: " + listaCadastrarGrupo.size());

        AdapterGrupoSelecionado adapterGrupo = new AdapterGrupoSelecionado(listaCadastrarGrupo, getApplication());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerCadastrarGrupo.setLayoutManager(layoutManager);
        binding.recyclerCadastrarGrupo.setHasFixedSize(true);
        binding.recyclerCadastrarGrupo.setAdapter(adapterGrupo);

        binding.fabCadastrarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeGrupo = binding.editNomeGrupo.getText().toString();
                Usuario usuario = new Usuario();
                usuario.setNome(firebase.firebaseUser().getDisplayName());
                usuario.setFoto(firebase.firebaseUser().getPhotoUrl().toString());
                usuario.setEmail(firebase.recuperar_emailUsuario());
                listaCadastrarGrupo.add(usuario);
                grupo.setNomeGrupo(nomeGrupo);
                grupo.setMembrosGrupo(listaCadastrarGrupo);
                grupo.salvarGrupo(GrupoSelecionadoActivity.this);


            }
        });

        binding.recyclerCadastrarGrupo.addOnItemTouchListener(new RecyclerItemClickListener(this, binding.recyclerCadastrarGrupo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelecionado = listaCadastrarGrupo.get(position);
                listaCadastrarGrupo.remove(usuarioSelecionado);
                adapterGrupo.notifyDataSetChanged();
                atualizarParticipantes();


                Toast.makeText(getApplicationContext(), "Contato removido para grupo", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }

    public void atualizarParticipantes() {
        binding.textParticipantes.setText("Participantes: " + listaCadastrarGrupo.size());

    }

    public void selecionarImagemGrupo(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            galeria.launch(intent);
        }
    }


}