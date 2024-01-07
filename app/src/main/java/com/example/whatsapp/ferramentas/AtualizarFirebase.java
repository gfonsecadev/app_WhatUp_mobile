package com.example.whatsapp.ferramentas;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.example.whatsapp.R;
import com.example.whatsapp.entidades.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class AtualizarFirebase {

    public  static void atualizarUsuarioFirebase(Usuario usuario){

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("nome",usuario.getNome());
        hashMap.put("email",usuario.getEmail());
        hashMap.put("foto",usuario.getFoto());

        DatabaseReference databaseReference=firebase.databaseInstance();
        DatabaseReference usuarioRef=databaseReference.child("Usuarios").child(firebase.recuperar_idUsuario());

        usuarioRef.updateChildren(hashMap);


    }

    public static Usuario usuario(){
        FirebaseUser firebaseUser=firebase.firebaseUser();
        Usuario usuario=new Usuario();
        usuario.setNome(firebaseUser.getDisplayName());
        usuario.setEmail(firebaseUser.getEmail());
        if(firebaseUser.getPhotoUrl()==null){
            usuario.setFoto("");
        }else {
            usuario.setFoto(firebaseUser.getPhotoUrl().toString());
        }

        return usuario;
    }

}
