package com.example.whatsapp.recyclers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.whatsapp.R;
import com.example.whatsapp.entidades.Conversas;
import com.example.whatsapp.entidades.Grupo;
import com.example.whatsapp.entidades.Mensagens;
import com.example.whatsapp.entidades.Usuario;
import com.example.whatsapp.ferramentas.MostrarFoto;
import com.example.whatsapp.ferramentas.firebase;
import com.google.android.gms.common.api.Api;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolderChat> {
    private final int REMETENTE=0;
    private final int DESTINATARIO =1;
    private List<Mensagens> List_mensagens;
    private Context context;



    public AdapterChat(List<Mensagens> list_mensagens, Context context) {
        this.List_mensagens = list_mensagens;
        this.context = context;


    }

    @NonNull
    @Override
    public MyHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout=null;
        if(viewType==REMETENTE){
            layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chat_remetente,parent,false);
        }else if(viewType== DESTINATARIO){
            layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chat_destinatario,parent,false);
        }

        return new MyHolderChat(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderChat holder, int position) {

        Mensagens  mensagens=List_mensagens.get(position);
        String texto=mensagens.getTexto();
        String imagem=mensagens.getImagem();
        RequestOptions requestOptions=RequestOptions.placeholderOf(R.drawable.camera).error(R.drawable.emoji);


        if(mensagens.getIsGrupo().equals("true") && !mensagens.getEmail().equals(firebase.recuperar_emailUsuario())){
           holder.textViewNomeEnvio.setVisibility(View.VISIBLE);
           holder.textViewEmailEnvio.setVisibility(View.VISIBLE);
           holder.textViewNomeEnvio.setText("    ~"+mensagens.getNome());
           holder.textViewEmailEnvio.setText(mensagens.getEmail());
        }

        holder.textViewHora.setText(mensagens.getHoraEnvio() );

        if(!imagem.equals("") && !texto.equals("")){
            holder.textViewChat.setVisibility(View.VISIBLE);
            holder.imageViewChat.setVisibility(View.VISIBLE);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(Uri.parse(imagem)).into(holder.imageViewChat);

            holder.textViewChat.setText(texto);
        }else if(!texto.equals("")  && imagem.equals("")){
            holder.textViewChat.setVisibility(View.VISIBLE);
            holder.imageViewChat.setVisibility(View.GONE);
            holder.textViewChat.setText(texto);
        }else if(!imagem.equals("") && texto.equals("")){
            holder.textViewChat.setVisibility(View.INVISIBLE);
            holder.imageViewChat.setVisibility(View.VISIBLE);
            Glide.with(context).load(Uri.parse(imagem)).into(holder.imageViewChat);
        }else if(imagem.equals("") && texto.equals("")){
            holder.imageViewChat.setVisibility(View.GONE);
            holder.textViewChat.setText("");
        }

        holder.imageViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MostrarFoto.mostrar_imagem(mensagens,context);
            }
        });



    }

    @Override
    public int getItemCount() {
        return List_mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagens mensagens=List_mensagens.get(position);
        String usuarioLogado= firebase.recuperar_idUsuario();

        if(usuarioLogado.equals(mensagens.getIdUsuario())){
            return REMETENTE;
        }

        return DESTINATARIO;
    }

    public class MyHolderChat extends RecyclerView.ViewHolder{
        public ImageView imageViewChat;
        public TextView textViewChat,textViewNomeEnvio,textViewHora,textViewEmailEnvio;

        public MyHolderChat(@NonNull View itemView) {
            super(itemView);
            imageViewChat=itemView.findViewById(R.id.imagem_chat);
            textViewChat=itemView.findViewById(R.id.text_chat);
            textViewNomeEnvio=itemView.findViewById(R.id.text_nomeGrupo);
            textViewEmailEnvio=itemView.findViewById(R.id.text_emailGrupo);
            textViewHora=itemView.findViewById(R.id.text_hora);

        }
    }
}
