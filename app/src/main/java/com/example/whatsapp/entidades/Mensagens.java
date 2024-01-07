package com.example.whatsapp.entidades;



import com.example.whatsapp.ferramentas.firebase;
import com.google.firebase.database.DatabaseReference;

public class Mensagens {
    private String idUsuario,Imagem,texto,nome,horaEnvio;
    private String destinatario,isGrupo,email;

    public Mensagens() {
    }

    public String getIsGrupo() {
        return isGrupo;
    }

    public String getNome() {
        return nome;
    }

    public String getHoraEnvio() {
        return horaEnvio;
    }

    public void setHoraEnvio(String horaEnvio) {
        this.horaEnvio = horaEnvio;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIsGrupo(String isGrupo) {
        this.isGrupo = isGrupo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Mensagens(String idDestinatario) {
        destinatario=idDestinatario;
        this.setIsGrupo("false");
        this.setEmail("");
        this.setImagem("");
        this.setIdUsuario("");
        this.setNome("");
        this.setTexto("");
        this.setHoraEnvio("");
    }


    public void salvar_mensagens_grupo(String remetente){
        DatabaseReference databaseReference= firebase.databaseInstance();
        databaseReference.child("Mensagens").child(remetente)
                .child(destinatario).push().setValue(this);
    }




    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
