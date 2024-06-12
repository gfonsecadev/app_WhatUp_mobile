package com.example.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.entidades.Conversas;
import com.example.whatsapp.entidades.Grupo;
import com.example.whatsapp.entidades.Mensagens;
import com.example.whatsapp.entidades.Usuario;
import com.example.whatsapp.ferramentas.Base64Custon;
import com.example.whatsapp.ferramentas.MostrarFoto;
import com.example.whatsapp.ferramentas.Permissoes;
import com.example.whatsapp.ferramentas.Upload_download;
import com.example.whatsapp.ferramentas.firebase;
import com.example.whatsapp.recyclers.AdapterChat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CircleImageView imageViewNome;
    private TextView textViewNome;
    private EditText editViewMensagemLayout;
    private EditText editViewMensagemPrincipal;
    private FloatingActionButton fabEnviarMensagem, fabEnviarImagem;
    private RecyclerView recyclerMensagem;
    private Usuario usuariorecebido;
    private AdapterChat adapterChat;
    private final List<Mensagens> list_mensagens = new ArrayList<>();
    private String usuarioRemetente;
    private String usuariodestinatario;
    private DatabaseReference databaseReference;
    private DatabaseReference usuarioRef;
    private Mensagens mensagens;
    private Grupo grupoRecebido;
    private final List<Usuario> listUsuariosComuns = new ArrayList<>();
    private LinearLayout linearEnviarImagem;
    private ImageView imagemEnviar;
    private Uri uriEnviarImagem;
    private Bitmap bitmap;
    ActivityResultLauncher resultadoGaleria = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            try {


                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Intent intent = result.getData();
                    Uri uri = intent.getData();

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    linearEnviarImagem.setVisibility(View.VISIBLE);
                    imagemEnviar.setImageBitmap(bitmap);
                    uriEnviarImagem = uri;

                }
            } catch (Exception e) {
                Toast.makeText(ChatActivity.this, e.toString(), Toast.LENGTH_LONG).show();

            }
        }
    });
    ActivityResultLauncher resultadoCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            try {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    bitmap = (Bitmap) result.getData().getExtras().get("data");
                    linearEnviarImagem.setVisibility(View.VISIBLE);
                    imagemEnviar.setImageBitmap(bitmap);


                }
            } catch (Exception e) {
                Toast.makeText(ChatActivity.this, e.toString(), Toast.LENGTH_LONG).show();

            }
        }
    });
    private ToggleButton btEmoji;
    private Conversas conversaRecebida;
    private int nVisualizado;
    private final HashMap<String, Integer> hashVisualizado = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = findViewById(R.id.toolbarChat);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btEmoji = findViewById(R.id.btEmoji);
        imageViewNome = findViewById(R.id.imagemChat);
        textViewNome = findViewById(R.id.nomeChat);
        editViewMensagemLayout = findViewById(R.id.text_send_layout_imagem);
        editViewMensagemPrincipal = findViewById(R.id.text_send_principal);
        fabEnviarMensagem = findViewById(R.id.fabsend);
        fabEnviarImagem = findViewById(R.id.fabSendImagem);

        linearEnviarImagem = findViewById(R.id.layoutEnviarImagem);
        imagemEnviar = findViewById(R.id.imagemEnviar);


        recyclerMensagem = findViewById(R.id.recyclerChat);
        intentRecebida();

        if (grupoRecebido != null) {
            usuariodestinatario = grupoRecebido.getIdGrupo();
        } else usuariodestinatario = Base64Custon.criptografar(usuariorecebido.getEmail());

        usuarioRemetente = firebase.recuperar_idUsuario();
        listarMensagens();

        adapterChat = new AdapterChat(list_mensagens, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMensagem.setLayoutManager(layoutManager);
        recyclerMensagem.setHasFixedSize(true);
        recyclerMensagem.setAdapter(adapterChat);

        EmojiManager.install(new GoogleEmojiProvider());
        EmojiPopup popup = EmojiPopup.Builder.fromRootView(findViewById(R.id.layoutChat)).build(editViewMensagemPrincipal);

        btEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.toggle();
            }
        });

        imageViewNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MostrarFoto.mostrar_foto(conversaRecebida, ChatActivity.this);
            }
        });


        fabEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagens();
            }
        });

        fabEnviarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagens();
            }
        });


    }

    public void enviarMensagens() {
        long hora = System.currentTimeMillis();
        SimpleDateFormat decimalFormat = new SimpleDateFormat("h:mm a");
        String horaAtual = decimalFormat.format(hora);
        String mensagem_enviar = editViewMensagemPrincipal.getText().toString();
        String mensagem_enviar_layout = editViewMensagemLayout.getText().toString();

        if (grupoRecebido != null) {
            for (Usuario usuario : grupoRecebido.getMembrosGrupo()) {
                mensagens = new Mensagens(usuariodestinatario);
                mensagens.setIdUsuario(firebase.recuperar_idUsuario());
                mensagens.setIsGrupo("true");
                mensagens.setHoraEnvio(horaAtual);
                mensagens.setNome(firebase.firebaseUser().getDisplayName());
                mensagens.setEmail(firebase.firebaseUser().getEmail());


                if (linearEnviarImagem.getVisibility() == View.VISIBLE) {
                    mensagens.setTexto(mensagem_enviar_layout);
                    Upload_download upload_download = new Upload_download(bitmap, this);
                    upload_download.upload_enviar(mensagens, Base64Custon.criptografar(usuario.getEmail()), usuariodestinatario);
                    salvar_para_conversas("IMAGEM");
                } else {
                    mensagens.setTexto(mensagem_enviar);
                    mensagens.salvar_mensagens_grupo(Base64Custon.criptografar(usuario.getEmail()));
                    salvar_para_conversas(mensagem_enviar);
                }

            }


        } else {

            for (Usuario usuario2 : listUsuariosComuns) {
                String usuarioParaInstancia;
                String idUsuario = Base64Custon.criptografar(usuario2.getEmail());
                if (usuario2.getEmail().equals(firebase.recuperar_emailUsuario())) {
                    usuarioParaInstancia = usuariodestinatario;
                } else {
                    usuarioParaInstancia = usuarioRemetente;
                }
                mensagens = new Mensagens(usuarioParaInstancia);

                mensagens.setIsGrupo("false");
                mensagens.setHoraEnvio(horaAtual);
                mensagens.setIdUsuario(firebase.recuperar_idUsuario());
                if (linearEnviarImagem.getVisibility() == View.VISIBLE) {
                    mensagens.setTexto(mensagem_enviar_layout);
                    Upload_download upload_download = new Upload_download(bitmap, this);
                    upload_download.upload_enviar(mensagens, idUsuario, usuarioParaInstancia);
                    salvar_para_conversas("IMAGEM ");
                } else {
                    mensagens.setTexto(mensagem_enviar);
                    mensagens.salvar_mensagens_grupo(idUsuario);
                    salvar_para_conversas(mensagem_enviar);
                }

            }

        }

        linearEnviarImagem.setVisibility(View.GONE);
        editViewMensagemPrincipal.setText("");
        editViewMensagemLayout.setText("");


    }

    private void listarMensagens() {
        databaseReference = firebase.databaseInstance();

        usuarioRef = databaseReference.child("Mensagens").child(usuarioRemetente).child(usuariodestinatario);
        usuarioRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Mensagens mensage = snapshot.getValue(Mensagens.class);
                list_mensagens.add(mensage);


                adapterChat.notifyDataSetChanged();
                recyclerMensagem.getLayoutManager().scrollToPosition(adapterChat.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void salvar_para_conversas(String ultima_messagem) {
        Long hora = System.currentTimeMillis();
        if (grupoRecebido != null) {


            for (Usuario usuario1 : grupoRecebido.getMembrosGrupo()) {
                Conversas conversas = new Conversas();
                conversas.setUsuarioConversa(usuario1);
                conversas.setIsGrupo("true");
                conversas.setHora(hora);

                conversas.setIdUsuarioRemetente(Base64Custon.criptografar(usuario1.getEmail()));
                conversas.setIdUsuarioDestinatario(grupoRecebido.getIdGrupo());
                if (usuario1.getEmail().equals(firebase.recuperar_emailUsuario())) {
                    conversas.setUltimaMensagem("Você: " + ultima_messagem);
                    conversas.setVisualizado("true");
                    conversas.setConversaNaoLida(0);

                } else {
                    String idUsuario = Base64Custon.criptografar(usuario1.getEmail());
                    int recuperaViz = hashVisualizado.get(idUsuario);
                    int soma = 1 + recuperaViz;
                    conversas.setUltimaMensagem(firebase.firebaseUser().getDisplayName() + ": " + ultima_messagem);
                    conversas.setVisualizado("false");
                    conversas.setConversaNaoLida(soma);

                }

                conversas.setGrupo(grupoRecebido);
                conversas.salvarConversas();

            }
        } else {
            for (Usuario usuario2 : listUsuariosComuns) {
                Conversas conversas = new Conversas();
                conversas.setHora(hora);
                conversas.setIsGrupo("false");

                if (usuario2.getEmail().equals(firebase.recuperar_emailUsuario())) {
                    conversas.setUltimaMensagem("Você: " + ultima_messagem);
                    conversas.setVisualizado("true");
                    conversas.setUsuarioConversa(usuariorecebido);
                    conversas.setIdUsuarioRemetente(usuarioRemetente);
                    conversas.setIdUsuarioDestinatario(usuariodestinatario);
                    conversas.setConversaNaoLida(0);
                } else {

                    int soma = nVisualizado + 1;
                    conversas.setConversaNaoLida(soma);
                    conversas.setIdUsuarioDestinatario(usuarioRemetente);
                    conversas.setVisualizado("false");
                    conversas.setIdUsuarioRemetente(usuariodestinatario);
                    conversas.setUsuarioConversa(firebase.usuarioAtual());
                    conversas.setUltimaMensagem(firebase.firebaseUser().getDisplayName() + ": " + ultima_messagem);

                }
                conversas.salvarConversas();


            }


        }


    }

    public void enviar_imagem(View view) {
        Permissoes.validarPermissoes(this, 1);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Foto de perfil");
        alert.setMessage("Escolha");
        alert.setPositiveButtonIcon(getResources().getDrawable(R.drawable.galeria));
        alert.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent1.resolveActivity(getPackageManager()) != null) {
                    resultadoGaleria.launch(intent1);


                }

            }
        });

        alert.setNegativeButtonIcon(getResources().getDrawable(R.drawable.camera2));
        alert.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        resultadoCamera.launch(intent2);
                    }

                }
        );

        alert.setIcon(R.drawable.usuario);
        alert.create().show();


    }

    private void intentRecebida() {
        conversaRecebida = new Conversas();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("grupo")) {
                conversaRecebida = (Conversas) bundle.getSerializable("grupo");
                grupoRecebido = conversaRecebida.getGrupo();
                conversaRecebida.setIdUsuarioRemetente(firebase.recuperar_idUsuario());
                conversaRecebida.setIdUsuarioDestinatario(grupoRecebido.getIdGrupo());
                conversaRecebida.setVisualizado("true");
                conversaRecebida.setConversaNaoLida(0);
                conversaRecebida.salvarConversas();
                conversaRecebida.salvarConversas();
                textViewNome.setText(grupoRecebido.getNomeGrupo());
                if (grupoRecebido.getFotoGrupo() != "") {
                    Glide.with(ChatActivity.this).load(Uri.parse(grupoRecebido.getFotoGrupo())).into(imageViewNome);
                } else {
                    imageViewNome.setImageResource(R.drawable.padrao);
                }

            } else {

                if (bundle.containsKey("conversa")) {
                    conversaRecebida = (Conversas) bundle.getSerializable("usuario");
                    conversaRecebida.setVisualizado("true");
                    conversaRecebida.setConversaNaoLida(0);
                    conversaRecebida.salvarConversas();
                    usuariorecebido = conversaRecebida.getUsuarioConversa();
                } else if (bundle.containsKey("contato")) {
                    usuariorecebido = (Usuario) bundle.getSerializable("usuario");
                    conversaRecebida.setUsuarioConversa(usuariorecebido);
                }

                listUsuariosComuns.add(firebase.usuarioAtual());
                listUsuariosComuns.add(usuariorecebido);
                textViewNome.setText(usuariorecebido.getNome());
                if (usuariorecebido.getFoto() != "") {
                    Glide.with(ChatActivity.this).load(Uri.parse(usuariorecebido.getFoto())).into(imageViewNome);
                } else {
                    imageViewNome.setImageResource(R.drawable.padrao);
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        recuperarVizualizacao();
    }

    public void recuperarVizualizacao() {
        if (usuariorecebido != null) {
            try {

                DatabaseReference databaseReference = firebase.databaseInstance();
                DatabaseReference referenceConversa = databaseReference.child("Conversas").child(usuariodestinatario).child(usuarioRemetente);

                referenceConversa.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Conversas conversas = snapshot.getValue(Conversas.class);
                        if (conversas != null) {
                            nVisualizado = conversas.getConversaNaoLida();
                        } else nVisualizado = 0;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } catch (Exception e) {
                nVisualizado = 0;
            }


        } else if (grupoRecebido != null) {
            try {

                for (Usuario usuario : grupoRecebido.getMembrosGrupo()) {
                    String idUsuario = Base64Custon.criptografar(usuario.getEmail());
                    if (!idUsuario.equals(firebase.recuperar_idUsuario())) {
                        DatabaseReference databaseReference = firebase.databaseInstance();
                        DatabaseReference referenceConversa = databaseReference.child("Conversas").child(idUsuario).child(usuariodestinatario);
                        referenceConversa.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Conversas conversas = snapshot.getValue(Conversas.class);
                                int vizualizado;
                                if (conversas != null) {
                                    vizualizado = conversas.getConversaNaoLida();
                                } else vizualizado = 0;

                                hashVisualizado.put(idUsuario, vizualizado);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }

            } catch (Exception e) {

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permission : grantResults) {
            if (permission == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this, androidx.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog);
                alert.setCancelable(false);
                alert.setMessage("Não é possivel continuar sem aceitar as permissões");
                alert.setPositiveButton("Retornar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alert.setTitle("Permissões negadas");
                alert.create().show();
            }

        }


    }
}