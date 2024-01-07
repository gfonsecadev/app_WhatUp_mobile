package com.example.whatsapp.recyclers;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.entidades.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterGrupo extends RecyclerView.Adapter<AdapterGrupo.HolderGrupos> {
    private List<Usuario> listUsuario;
    private Context context;




    public AdapterGrupo(List<Usuario> list, Context c) {
        this.context=c;
        this.listUsuario=list;

    }

    @NonNull
    @Override
    public HolderGrupos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);


        return new HolderGrupos(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGrupos holder, int position) {
        Usuario usuario=listUsuario.get(position);
        holder.email.setText(usuario.getEmail());
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

    public class HolderGrupos extends RecyclerView.ViewHolder {
        public CircleImageView circleImageView;
        public  CircleImageView circleImageViewFeito;
        public LinearLayout linearLayout;

        public TextView nome,email;


        public HolderGrupos(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.imagem_contatoR);
            nome=itemView.findViewById(R.id.textNomeR);
            email=itemView.findViewById(R.id.textEmailR);
           circleImageViewFeito=itemView.findViewById(R.id.feito);
           linearLayout=itemView.findViewById(R.id.layoutContatos);
        }
    }
}
