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
import com.example.whatsapp.entidades.Conversas;
import com.example.whatsapp.entidades.Usuario;
import com.example.whatsapp.ferramentas.MostrarFoto;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContatos extends RecyclerView.Adapter<AdapterContatos.HolderContatos> {
    private List<Usuario> listUsuario;
    private Context context;


    public AdapterContatos(List<Usuario> list,Context c) {
        this.context=c;
        this.listUsuario=list;

    }

    @NonNull
    @Override
    public HolderContatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);


        return new HolderContatos(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderContatos holder, int position) {
        Usuario usuario=listUsuario.get(position);
        holder.email.setText(usuario.getEmail());
        holder.nome.setText(usuario.getNome());

        if(!usuario.getFoto().equals("")){
            Glide.with(context).load(Uri.parse(usuario.getFoto())).into(holder.circleImageView);
        }else if(usuario.getEmail().equals("")) {
            holder.circleImageView.setImageResource(R.drawable.icone_grupo);
            holder.email.setVisibility(View.GONE);

        }
           else{ holder.circleImageView.setImageResource(R.drawable.padrao);
        }


        if(!usuario.getEmail().equals("")){
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context.getApplicationContext(), ChatActivity.class);
                    intent.putExtra("usuario",usuario);
                    intent.putExtra("contato","contato");
                    context.startActivity(intent);
                }
            });
        }else {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context.getApplicationContext(), GrupoActivity.class);
                    listUsuario.remove(usuario);
                    intent.putExtra("usuario", (Serializable) listUsuario);
                    context.startActivity(intent);
                }
            });
        }

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conversas conversas=new Conversas();
                conversas.setUsuarioConversa(usuario);
                MostrarFoto.mostrar_foto(conversas,context);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listUsuario.size();
    }

    public class HolderContatos extends RecyclerView.ViewHolder {
        public CircleImageView circleImageView;
        public TextView nome,email;
        public LinearLayout linearLayout;
        public HolderContatos(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.imagem_contatoR);
            nome=itemView.findViewById(R.id.textNomeR);
            email=itemView.findViewById(R.id.textEmailR);
            linearLayout=itemView.findViewById(R.id.layoutContatos);
        }
    }
}
