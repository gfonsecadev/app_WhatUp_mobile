package com.example.whatsapp.recyclers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.activity.ChatActivity;
import com.example.whatsapp.activity.GrupoActivity;
import com.example.whatsapp.entidades.Usuario;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterGrupoSelecionado extends RecyclerView.Adapter<AdapterGrupoSelecionado.HolderSelecionados> {
    private List<Usuario> listUsuario;
    private Context context;


    public AdapterGrupoSelecionado(List<Usuario> list,Context c) {
        this.context=c;
        this.listUsuario=list;

    }

    @NonNull
    @Override
    public HolderSelecionados onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_grupo_selecionado,parent,false);


        return new HolderSelecionados(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSelecionados holder, int position) {
        Usuario usuario=listUsuario.get(position);
        holder.nome.setText(usuario.getNome());

        if(!usuario.getFoto().equals("")){
            Glide.with(context).load(Uri.parse(usuario.getFoto())).into(holder.circleImageView);
        }
        else{ holder.circleImageView.setImageResource(R.drawable.padrao);
        }








    }

    @Override
    public int getItemCount() {
        return listUsuario.size();
    }

    public class HolderSelecionados extends RecyclerView.ViewHolder {
        public CircleImageView circleImageView;
        public CircleImageView circleImageViewremove;
        public TextView nome;
        public HolderSelecionados(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.imageGrupoSelecionado);
            circleImageViewremove=itemView.findViewById(R.id.remove);
            nome=itemView.findViewById(R.id.textGrupoSelecionado);


        }
    }
}