package com.example.whatsapp.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.entidades.Usuario;
import com.example.whatsapp.ferramentas.AtualizarFirebase;
import com.example.whatsapp.ferramentas.Metodos_Cadastro_Login;
import com.example.whatsapp.ferramentas.Permissoes;
import com.example.whatsapp.ferramentas.Upload_download;
import com.example.whatsapp.ferramentas.firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.security.Key;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracaoActivity extends AppCompatActivity {
    private CircleImageView imageView;
    private EditText editText;
    private Upload_download upload_download;
    private Usuario usuariologado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView=findViewById(R.id.circleImageView);
        editText=findViewById(R.id.nomePerfil);
        editText.setText(firebase.firebaseUser().getDisplayName());

        upload_download=new Upload_download(imageView,ConfiguracaoActivity.this);
        recuperar_foto();

        usuariologado=AtualizarFirebase.usuario();


        String[] permissao=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissoes.validarPermissoes(this,1);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int percorre : grantResults) {
            if(percorre==PackageManager.PERMISSION_DENIED){
                AlertDialog.Builder alert=new AlertDialog.Builder(this,androidx.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog);
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


    public void tirar_foto(View view){

        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("Foto de perfil");
        alert.setMessage("Escolha");
        alert.setPositiveButtonIcon(getResources().getDrawable(R.drawable.galeria));
        alert.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent2=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                resultadoGaleria.launch(intent2);
            }
        });

        alert.setNegativeButtonIcon(getResources().getDrawable(R.drawable.camera2));
        alert.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null){
                    resultadoCamera.launch(intent);

                }

            }
        });
        alert.setIcon(R.drawable.usuario);
        alert.create().show();


    }



    ActivityResultLauncher<Intent> resultadoCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        Bitmap bitmap = null;
                        if (result.getResultCode() == RESULT_OK && result.getData()!=null) {

                            bitmap = (Bitmap) result.getData().getExtras().get("data");
                        }

                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                            upload_download.upload();
                            upload_download.download();

                        }
                    } catch (Exception E) {
                        Toast.makeText(ConfiguracaoActivity.this, E.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }

    );

    ActivityResultLauncher<Intent> resultadoGaleria=registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode()==RESULT_OK && result.getData()!=null){
                        Intent uri=result.getData();
                        imageView.setImageURI(uri.getData());

                        upload_download.upload();
                        upload_download.download();

                    }
                }


            });
    public void salvar_nomePerfil(View view){
        String nomeDigitado=editText.getText().toString();
        usuariologado.setNome(nomeDigitado);
        AtualizarFirebase.atualizarUsuarioFirebase(usuariologado);
        Metodos_Cadastro_Login.cadastrar_firebaUserNome(nomeDigitado);
    }

    public void recuperar_foto(){
        FirebaseUser firebaseUser=firebase.firebaseUser();

        if(firebaseUser.getPhotoUrl()!=null){
            String fotoatual=firebaseUser.getPhotoUrl().toString();
            Glide.with(ConfiguracaoActivity.this).load(fotoatual).into(imageView);
        }else imageView.setImageResource(R.drawable.padrao);


    }

    @Override
    protected void onStart() {

        super.onStart();


    }



}
