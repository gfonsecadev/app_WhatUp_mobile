package com.example.whatsapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.entidades.Conversas;
import com.example.whatsapp.entidades.Status;
import com.example.whatsapp.entidades.Usuario;
import com.example.whatsapp.ferramentas.DataStatusCuston;
import com.example.whatsapp.ferramentas.Upload_download;
import com.example.whatsapp.ferramentas.firebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class VizualizarActivity extends AppCompatActivity {
    public ScaleGestureDetector sgd;
    public Float scale = 1.0f;
    List<Usuario> usuarioList = new ArrayList<>();
    Float x, y;
    Float dx, dy;
    private Conversas conversas;
    private ImageView imageViewVizualizar, buttomPaleta, buttomEstilo, buttonEmoji, imageViewEnviar;
    private String foto, nome, contato;
    private TextView textViewPersonalizado;
    private ProgressBar progressBar;
    private LinearLayout linearLayout, linearLayoutEnviarImagem;
    private CircleImageView circleImageToolBar;
    private FloatingActionButton fabStatusPersonalizar, fabEnviarImagem;
    private Toolbar toolbar;
    private EditText editTextStatusPersonalizar, editTextEnviarImagem;
    private TextView textViewStatus, textViewNome, textViewHora, textViewpersonalizado;
    private int progress = 0;
    private int cores = 0;
    private int style = 1;
    private Bitmap bitmap;
    ActivityResultLauncher camera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                linearLayoutEnviarImagem.setVisibility(View.VISIBLE);
                bitmap = (Bitmap) result.getData().getExtras().get("data");
                imageViewEnviar.setImageBitmap(bitmap);

            } else {
                startActivity(new Intent(getApplicationContext(), AplicacaoActivity.class));
                finish();
            }
        }
    });
    private int corEscolhida = 500000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_vizualizar);

        imageViewVizualizar = findViewById(R.id.imageLayoutVizualizar);
        imageViewEnviar = findViewById(R.id.imagemEnviar);
        toolbar = findViewById(R.id.toolbarVizualizar);
        textViewStatus = findViewById(R.id.textStatus);
        circleImageToolBar = findViewById(R.id.imageStatusToolBar);
        textViewHora = findViewById(R.id.textStatusHora);
        textViewPersonalizado = findViewById(R.id.textStatusPersonalizado);
        textViewNome = findViewById(R.id.textStatusNome);
        progressBar = findViewById(R.id.progressStatus);
        linearLayout = findViewById(R.id.layoutStatusPersonalizar);
        editTextStatusPersonalizar = findViewById(R.id.textDigiteSeuStatus);
        editTextEnviarImagem = findViewById(R.id.text_send_layout_imagem);
        buttomPaleta = findViewById(R.id.buttonPaletaStatus);
        buttomEstilo = findViewById(R.id.buttonStyleStatus);
        fabStatusPersonalizar = findViewById(R.id.fabStausPersonalizarEnviar);
        textViewPersonalizado = findViewById(R.id.textStatusPersonalizado);
        linearLayoutEnviarImagem = findViewById(R.id.layoutEnviarImagem);


        fabEnviarImagem = findViewById(R.id.fabSendImagem);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        intentRecebida();


        sgd = new ScaleGestureDetector(VizualizarActivity.this, new ScaleListener());

        fabEnviarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarStatus();
            }
        });

        fabStatusPersonalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarStatusTexto();
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sgd.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x = event.getX();
            y = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            dx = event.getX() - x;
            dy = event.getY() - y;

            imageViewVizualizar.setX(imageViewVizualizar.getX() + dx);
            imageViewVizualizar.setY(imageViewVizualizar.getY() + dy);

            x = event.getX();
            y = event.getY();

        }


        buttomPaleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sortearCor();
            }
        });

        buttomEstilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortearFontes();
            }
        });


        return true;
    }


    @SuppressLint("ResourceAsColor")
    public void intentRecebida() {
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("contato")) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            linearLayout.setVisibility(View.GONE);

            conversas = (Conversas) bundle.getSerializable("contato");
            if (conversas.getIsGrupo().equals("false")) {
                foto = conversas.getUsuarioConversa().getFoto();
                nome = conversas.getUsuarioConversa().getNome();
                contato = conversas.getUsuarioConversa().getEmail();
                Glide.with(this).load(foto).into(imageViewVizualizar);
            } else {
                nome = conversas.getGrupo().getNomeGrupo();
                contato = conversas.getGrupo().getMembrosGrupo().size() + " pessoas neste grupo";
                if (conversas.getGrupo().getFotoGrupo().equals("")) {
                    imageViewVizualizar.setImageResource(R.drawable.padrao);
                } else {
                    foto = conversas.getGrupo().getFotoGrupo();
                    Glide.with(this).load(foto).into(imageViewVizualizar);
                }
            }
            textViewHora.setText(contato);
            textViewNome.setText(nome);


        } else if (bundle.containsKey("status")) {
            Status status = (Status) bundle.getSerializable("status");
            linearLayout.setVisibility(View.GONE);
            circleImageToolBar.setVisibility(View.VISIBLE);
            toolbar.setBackgroundColor(R.color.black);
            progressBar.setVisibility(View.VISIBLE);
            String hora = DataStatusCuston.retornarDataStatus(status.getHoraPostagem());
            textViewNome.setText(status.getUsuarioStatus().getNome());
            textViewHora.setText(hora);
            if (!status.getUsuarioStatus().getFoto().equals("")) {
                Glide.with(this).load(status.getUsuarioStatus().getFoto()).into(circleImageToolBar);
            } else {
                circleImageToolBar.setImageResource(R.drawable.padrao);
            }

            if (status.getImagemStatus().equals("")) {
                textViewPersonalizado.setVisibility(View.VISIBLE);
                textViewPersonalizado.setText(status.getTextoStatus());
                textViewPersonalizado.setBackground(getDrawable(status.getCodiCorStatus()));
                toolbar.setBackground(getDrawable(status.getCodiCorStatus()));
                progressBar.setBackgroundColor(status.getCodiCorStatus());

            } else {
                Glide.with(this).load(status.getImagemStatus()).into(imageViewVizualizar);
                textViewStatus.setText(status.getTextoStatus());
            }


            Timer t = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    progress++;
                    progressBar.setProgress(progress);

                    if (progress == 100) {
                        t.cancel();
                        finish();
                    }

                }
            };

            t.schedule(timerTask, 0, 100);


        } else if (bundle.containsKey("statusEditar")) {
            linearLayout.setVisibility(View.VISIBLE);
            ouvirToque();

        } else if (bundle.containsKey("statusPersonalizado")) {
            cameraStatus();
        }
    }


    @SuppressLint("ResourceAsColor")
    public void sortearCor() {

        int[] corPick = new int[]{android.R.color.holo_orange_light, R.color.purple_200, R.color.teal_200,
                android.R.color.holo_red_dark, R.color.colorPrimaryDark,
                android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.darker_gray
                , android.R.color.system_accent3_900};
        if (cores == 8) {
            cores = 0;
        }
        corEscolhida = corPick[cores];


        editTextStatusPersonalizar.setBackground(getDrawable(corEscolhida));
        linearLayout.setBackground(getDrawable(corEscolhida));

        cores++;
    }

    @SuppressLint("WrongConstant")
    public void sortearFontes() {
        if (style == 4) {
            style = 1;
        }


        editTextStatusPersonalizar.setTypeface(Typeface.defaultFromStyle(style));
        style++;
    }


    public void ouvirToque() {
        editTextStatusPersonalizar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count > 0) {
                    fabStatusPersonalizar.setVisibility(View.VISIBLE);

                } else {
                    fabStatusPersonalizar.setVisibility(View.INVISIBLE);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @SuppressLint("ResourceType")
    public void salvarStatusTexto() {
        long hora = System.currentTimeMillis();
        String texto = editTextStatusPersonalizar.getText().toString();
        if (!texto.equals("")) {
            Status status = new Status();
            for (Usuario usuario : usuarioList) {
                status.setTextoStatus(texto);
                status.setImagemStatus("");
                status.setHoraPostagem(hora);
                status.setCodiCorStatus(corEscolhida);
                status.setIdRemetente(firebase.recuperar_idUsuario());
                status.setUsuarioStatus(firebase.usuarioAtual());
                status.salvarStatus(usuario);
            }

            finish();

        } else {
            editTextStatusPersonalizar.requestFocus();
            editTextStatusPersonalizar.setError("Digite algo!");
        }

    }


    public void cameraStatus() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            camera.launch(intent);
        }

    }

    public void salvarStatus() {
        Status status = new Status();
        String texto = editTextEnviarImagem.getText().toString();
        status.setTextoStatus(texto);
        status.setIdRemetente(firebase.recuperar_idUsuario());
        status.setUsuarioStatus(firebase.usuarioAtual());
        Upload_download upload_download = new Upload_download(bitmap, VizualizarActivity.this, "Status");
        upload_download.upload_status(status, usuarioList);
        editTextEnviarImagem.setText("");
        startActivity(new Intent(getApplicationContext(), AplicacaoActivity.class));
        finish();

    }

    public void recuperarUsuarios() {
        DatabaseReference databaseReference = firebase.databaseInstance().child("Usuarios");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario;
                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                    usuario = usuarioSnapshot.getValue(Usuario.class);
                    usuarioList.add(usuario);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarUsuarios();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5f));
            imageViewVizualizar.setScaleX(scale);
            imageViewVizualizar.setScaleY(scale);


            return true;
        }

    }
}