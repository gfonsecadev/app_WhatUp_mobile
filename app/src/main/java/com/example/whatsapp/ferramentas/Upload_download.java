package com.example.whatsapp.ferramentas;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.whatsapp.entidades.Mensagens;
import com.example.whatsapp.entidades.Status;
import com.example.whatsapp.entidades.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Upload_download {

    private CircleImageView imagem;
    private Bitmap bitmap;
    private final Activity context;
    private StorageReference storageReference;
    private StorageReference storageReference_enviar;
    private final String aleatorio;
    private Uri uri;


    public Upload_download(CircleImageView imageView, Activity context) {
        this.imagem = imageView;
        this.context = context;
        aleatorio = UUID.randomUUID().toString();
        storageReference = firebase.storageReference().child("Imagens")
                .child("Perfil").child(firebase.recuperar_emailUsuario()).child("perfil.jpg");

    }

    public Upload_download(Bitmap bitmap, Activity context) {
        this.bitmap = bitmap;
        this.context = context;
        aleatorio = UUID.randomUUID().toString();
        storageReference_enviar = firebase.storageReference().child("Imagens")
                .child("Fotos").child(firebase.recuperar_emailUsuario()).child(aleatorio + ".jpg");

    }

    public Upload_download(Bitmap bitmap, Activity context, String status) {
        this.bitmap = bitmap;
        this.context = context;
        aleatorio = UUID.randomUUID().toString();
        storageReference_enviar = firebase.storageReference().child("Imagens")
                .child(status).child(firebase.recuperar_emailUsuario()).child(aleatorio + ".jpg");

    }


    public void upload() {


        BitmapDrawable drawable = (BitmapDrawable) imagem.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);

        byte[] imagem = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(imagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Erro ao salvar imagem de perfil,causa " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Sucesso ao salvar imagem de perfil!", Toast.LENGTH_SHORT).show();
                            Uri uri = task.getResult();
                            Usuario usuario = AtualizarFirebase.usuario();
                            Metodos_Cadastro_Login.cadastrar_firebaUserUri(uri);
                            usuario.setFoto(uri.toString());
                            AtualizarFirebase.atualizarUsuarioFirebase(usuario);
                        } else
                            Toast.makeText(context, "erro  " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

    }

    public void download() {


        try {
            File file = File.createTempFile("pefil", ".JPG");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    File arquivo = new File(Environment.getExternalStorageDirectory(), "/Meus Apps/WhatsApp/Users" + "/" + firebase.recuperar_emailUsuario() + "/perfil");
                    File imagemSalva = new File(arquivo, "foto.jpeg");
                    try {
                        arquivo.mkdirs();
                        imagemSalva.createNewFile();
                        OutputStream outputStream = new FileOutputStream(imagemSalva);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                        Toast.makeText(context, "Imagem salva no dispositivo!", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }


    public void upload_enviar(Mensagens mensagens, String remetente, String destinatario) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        byte[] imagem = baos.toByteArray();

        UploadTask uploadTask = storageReference_enviar.putBytes(imagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Erro ao salvar imagem" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference_enviar.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            uri = task.getResult();
                            Mensagens mensagensTask = new Mensagens(destinatario);
                            mensagensTask.setTexto(mensagens.getTexto());
                            mensagensTask.setImagem(String.valueOf(uri));
                            mensagensTask.setIsGrupo(mensagens.getIsGrupo());
                            mensagensTask.setNome(mensagens.getNome());
                            mensagensTask.setEmail(mensagens.getEmail());
                            mensagensTask.setHoraEnvio(mensagens.getHoraEnvio());
                            mensagensTask.setIdUsuario(mensagens.getIdUsuario());
                            mensagensTask.salvar_mensagens_grupo(remetente);


                        } else
                            Toast.makeText(context, "erro  " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

    }

    public void download_enviar() {


        try {
            File file = File.createTempFile("pefil", ".JPG");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    File arquivo = new File(Environment.getExternalStorageDirectory(), "/Meus Apps/WhatsApp/Users" + "/" + firebase.recuperar_emailUsuario() + "/perfil");
                    File imagemSalva = new File(arquivo, "foto.jpeg");
                    try {
                        arquivo.mkdirs();
                        imagemSalva.createNewFile();
                        OutputStream outputStream = new FileOutputStream(imagemSalva);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                        Toast.makeText(context, "Imagem salva no dispositivo!", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }

    public void upload_status(Status status, List<Usuario> listUsuario) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        byte[] imagem = baos.toByteArray();

        UploadTask uploadTask = storageReference_enviar.putBytes(imagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Erro ao salvar status" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference_enviar.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            uri = task.getResult();
                            long hora = System.currentTimeMillis();
                            Status statusTask = new Status();
                            for (Usuario usuario : listUsuario) {
                                statusTask.setTextoStatus(status.getTextoStatus());
                                statusTask.setHoraPostagem(hora);
                                statusTask.setImagemStatus(String.valueOf(uri));
                                statusTask.setIdRemetente(status.getIdRemetente());
                                statusTask.setUsuarioStatus(status.getUsuarioStatus());
                                statusTask.salvarStatus(usuario);
                            }


                        } else
                            Toast.makeText(context, "erro  " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

    }


}
