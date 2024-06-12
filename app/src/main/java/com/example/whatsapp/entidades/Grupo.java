package com.example.whatsapp.entidades;

import android.app.Activity;
import android.content.Intent;

import com.example.whatsapp.activity.ChatActivity;
import com.example.whatsapp.ferramentas.Base64Custon;
import com.example.whatsapp.ferramentas.firebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable {
    private String idGrupo;
    private String fotoGrupo;
    private String nomeGrupo;
    private List<Usuario> membrosGrupo;


    public Grupo() {
        setFotoGrupo("");
    }

    public void salvarGrupo(Activity context) {
        DatabaseReference databaseReference = firebase.databaseInstance();
        DatabaseReference grupoRef = databaseReference.child("Grupos");
        grupoRef.child(getIdGrupo()).setValue(this);
        Conversas conversas = new Conversas();
        Intent intent = new Intent(context, ChatActivity.class);


        for (Usuario usuarioConversa : membrosGrupo) {
            Long hora = System.currentTimeMillis();
            conversas.setIdUsuarioDestinatario(getIdGrupo());
            conversas.setIdUsuarioRemetente(Base64Custon.criptografar(usuarioConversa.getEmail()));
            conversas.setIsGrupo("true");
            conversas.setGrupo(this);
            conversas.setHora(hora);
            conversas.salvarConversas();
            if (usuarioConversa.getEmail().equals(firebase.recuperar_emailUsuario())) {
                intent.putExtra("grupo", conversas);
            }
        }
        context.startActivity(intent);
        context.finish();

    }


    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getFotoGrupo() {
        return fotoGrupo;
    }

    public void setFotoGrupo(String fotoGrupo) {
        this.fotoGrupo = fotoGrupo;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    public List<Usuario> getMembrosGrupo() {
        return membrosGrupo;
    }

    public void setMembrosGrupo(List<Usuario> membrosGrupo) {
        this.membrosGrupo = membrosGrupo;
    }
}
