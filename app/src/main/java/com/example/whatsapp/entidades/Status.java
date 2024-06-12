package com.example.whatsapp.entidades;

import com.example.whatsapp.ferramentas.Base64Custon;
import com.example.whatsapp.ferramentas.firebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Status implements Serializable {
    private String idRemetente;
    private String textoStatus, imagemStatus;
    private Usuario usuarioStatus;
    private long horaPostagem;
    private int codiCorStatus;


    public Status() {
    }

    public void salvarStatus(Usuario usuario) {
        DatabaseReference databaseReference = firebase.databaseInstance();
        if (usuario.getEmail().equals(firebase.recuperar_emailUsuario())) {
            databaseReference.child("Status").child(idRemetente).child("MeuStatus").setValue(this);
        } else {
            databaseReference.child("Status").child(Base64Custon.criptografar(usuario.getEmail())).child("OutrosStatus").child(idRemetente).setValue(this);

        }
    }


    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public long getHoraPostagem() {
        return horaPostagem;
    }

    public void setHoraPostagem(long horaPostagem) {
        this.horaPostagem = horaPostagem;
    }

    public String getTextoStatus() {
        return textoStatus;
    }

    public void setTextoStatus(String textoStatus) {
        this.textoStatus = textoStatus;
    }

    public String getImagemStatus() {
        return imagemStatus;
    }

    public void setImagemStatus(String imagemStatus) {
        this.imagemStatus = imagemStatus;
    }

    public Usuario getUsuarioStatus() {
        return usuarioStatus;
    }

    public void setUsuarioStatus(Usuario usuarioStatus) {
        this.usuarioStatus = usuarioStatus;
    }

    public int getCodiCorStatus() {
        return codiCorStatus;
    }

    public void setCodiCorStatus(int codiCorStatus) {
        this.codiCorStatus = codiCorStatus;
    }
}
