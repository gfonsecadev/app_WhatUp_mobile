package com.example.whatsapp.entidades;

import com.example.whatsapp.ferramentas.Base64Custon;
import com.example.whatsapp.ferramentas.firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;


public class Usuario implements Serializable {
    private String nome, email, senha, foto = "";


    public void salvarUsuarioFirebase() {
        DatabaseReference databaseReference = firebase.databaseInstance();
        databaseReference.child("Usuarios").child(Base64Custon.criptografar(email)).setValue(this);
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


}
