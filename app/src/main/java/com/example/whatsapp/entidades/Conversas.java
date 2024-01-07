package com.example.whatsapp.entidades;

import androidx.annotation.NonNull;

import com.example.whatsapp.ferramentas.firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class Conversas implements Comparable<Conversas> ,Serializable{




    public Conversas()  {
       this.isGrupo="false";
       this.visualizado="false";
       this.conversaNaoLida=0;
    }

    public void salvarConversas(){

        DatabaseReference conversasRef = firebase.databaseInstance();
        conversasRef.child("Conversas").child(this.idUsuarioRemetente)
                .child(this.idUsuarioDestinatario).setValue(this);


    }


    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;
    private String ultimaMensagem;
    private String isGrupo;
    private String visualizado;
    private Long hora;
    private int conversaNaoLida;
    private Usuario usuarioConversa;
    private Grupo grupo;

    public int getConversaNaoLida() {
        return conversaNaoLida;
    }

    public void setConversaNaoLida(int conversaNaoLida) {
        this.conversaNaoLida = conversaNaoLida;
    }

    public String getVisualizado() {
        return visualizado;
    }

    public void setVisualizado(String visualizado) {
        this.visualizado = visualizado;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }

    public String getIdUsuarioRemetente() {
        return idUsuarioRemetente;
    }

    public String getIsGrupo() {
        return isGrupo;
    }

    public void setIsGrupo(String isGrupo) {
        this.isGrupo = isGrupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public void setIdUsuarioRemetente(String idUsuarioRemetente) {
        this.idUsuarioRemetente = idUsuarioRemetente;
    }

    public String getIdUsuarioDestinatario() {
        return idUsuarioDestinatario;
    }

    public void setIdUsuarioDestinatario(String idUsuarioDestinatario) {
        this.idUsuarioDestinatario = idUsuarioDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public Usuario getUsuarioConversa() {
        return usuarioConversa;
    }

    public void setUsuarioConversa(Usuario usuarioConversa) {
        this.usuarioConversa = usuarioConversa;
    }

    @Override
    public int compareTo(Conversas o) {

            return o.getHora().compareTo(getHora());


    }
}
