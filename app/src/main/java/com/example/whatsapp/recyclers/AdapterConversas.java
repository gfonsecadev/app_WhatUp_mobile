package com.example.whatsapp.recyclers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.activity.ChatActivity;
import com.example.whatsapp.entidades.Conversas;
import com.example.whatsapp.ferramentas.MostrarFoto;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterConversas extends RecyclerView.Adapter<AdapterConversas.HolderConversas> {
    private final List<Conversas> listConversas;
    private final Context context;


    public AdapterConversas(List<Conversas> list, Activity c) {
        this.context = c;
        this.listConversas = list;

    }


    @NonNull
    @Override
    public HolderConversas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview, parent, false);

        return new HolderConversas(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderConversas holder, int position) {
        Conversas conversas = listConversas.get(position);
        if (conversas.getIsGrupo().equals("true")) {
            holder.nome.setText(conversas.getGrupo().getNomeGrupo());
            holder.email.setText(conversas.getUltimaMensagem());

            if (!conversas.getGrupo().getFotoGrupo().equals("")) {

                Glide.with(context).load(conversas.getGrupo().getFotoGrupo()).into(holder.circleImageView);
            } else {
                holder.circleImageView.setImageResource(R.drawable.padrao);
            }

            if (conversas.getVisualizado().equals("false") && conversas.getConversaNaoLida() != 0) {
                holder.imageVizualizado.setVisibility(View.VISIBLE);
                holder.imageVizualizado.setText(String.valueOf(conversas.getConversaNaoLida()));
            } else {
                holder.imageVizualizado.setVisibility(View.GONE);
                holder.imageVizualizado.setText("");
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context.getApplicationContext(), ChatActivity.class);
                    intent.putExtra("grupo", conversas);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.email.setText(conversas.getUltimaMensagem());
            holder.nome.setText(conversas.getUsuarioConversa().getNome());

            if (!conversas.getUsuarioConversa().getFoto().equals("")) {

                Glide.with(context).load(conversas.getUsuarioConversa().getFoto()).into(holder.circleImageView);
            } else {
                holder.circleImageView.setImageResource(R.drawable.padrao);
            }


            if (conversas.getVisualizado().equals("false")) {
                holder.imageVizualizado.setVisibility(View.VISIBLE);
                holder.imageVizualizado.setText(String.valueOf(conversas.getConversaNaoLida()));
            } else {
                holder.imageVizualizado.setVisibility(View.GONE);
                holder.imageVizualizado.setText("");
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context.getApplicationContext(), ChatActivity.class);
                    intent.putExtra("conversa", "conversa");
                    intent.putExtra("usuario", conversas);
                    context.startActivity(intent);
                }
            });


        }
        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarFoto.mostrar_foto(conversas, context);


            }
        });


    }


    @Override
    public int getItemCount() {
        return listConversas.size();
    }

    public class HolderConversas extends RecyclerView.ViewHolder {
        public CircleImageView circleImageView;
        public TextView nome, email;
        public LinearLayout linearLayout;

        public TextView imageVizualizado;

        public HolderConversas(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.imagem_contatoR);
            nome = itemView.findViewById(R.id.textNomeR);
            imageVizualizado = itemView.findViewById(R.id.iconVizualizado);
            email = itemView.findViewById(R.id.textEmailR);
            linearLayout = itemView.findViewById(R.id.layoutContatos);
            email.setTextSize(15);
            nome.setTextSize(18);
        }
    }

}
