package com.example.whatsapp.ferramentas;

import com.example.whatsapp.entidades.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class firebase {

    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference databaseReference;
    private static StorageReference storageReference;
    private static FirebaseUser firebaseUser;

    public static FirebaseAuth firebaseAuthInstance() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();

        }
        return firebaseAuth;
    }

    public static DatabaseReference databaseInstance() {
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();

        }
        return databaseReference;
    }

    public static StorageReference storageReference() {
        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }

    public static String recuperar_idUsuario() {
        FirebaseAuth usuarioAtual = firebaseAuthInstance();

        return Base64Custon.criptografar(usuarioAtual.getCurrentUser().getEmail());

    }

    public static String recuperar_emailUsuario() {
        FirebaseAuth usuarioAtual = firebaseAuthInstance();
        return usuarioAtual.getCurrentUser().getEmail();
    }

    public static FirebaseUser firebaseUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        return firebaseUser;

    }

    public static Usuario usuarioAtual() {
        firebaseUser = firebaseUser();
        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());
        String foto = (firebaseUser.getPhotoUrl() != null) ? firebaseUser.getPhotoUrl().toString() : "";
        usuario.setFoto(foto);
        return usuario;

    }
}
