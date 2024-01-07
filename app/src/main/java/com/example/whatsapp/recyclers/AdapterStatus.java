package com.example.whatsapp.recyclers;

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
import com.example.whatsapp.activity.VizualizarActivity;
import com.example.whatsapp.entidades.Status;
import com.example.whatsapp.ferramentas.DataStatusCuston;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterStatus extends RecyclerView.Adapter<AdapterStatus.StatusHolder> {
    private List<Status> statusList;
    private Context context;

    public AdapterStatus(List<Status> statusList, Context context) {
        this.statusList = statusList;
        this.context=context;
    }

    @NonNull
    @Override
    public StatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);
        return new StatusHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusHolder holder, int position) {
        Status status=statusList.get(position);
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        String data= DataStatusCuston.retornarDataStatus(status.getHoraPostagem());


        holder.textViewHora.setText(data);


        if(!status.getUsuarioStatus().getFoto().equals("")){
            Glide.with(context).load(status.getUsuarioStatus().getFoto()).into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.drawable.padrao);
        }

        holder.textViewNome.setText(status.getUsuarioStatus().getNome());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VizualizarActivity.class);
                intent.putExtra("status",status);
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class StatusHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout;
        public CircleImageView imageView;
        public TextView textViewNome,textViewHora;

        public StatusHolder(@NonNull View itemView) {
            super(itemView);
           linearLayout=itemView.findViewById(R.id.layoutContatos);
           imageView=itemView.findViewById(R.id.imagem_contatoR);
           textViewHora=itemView.findViewById(R.id.textEmailR);
           textViewNome=itemView.findViewById(R.id.textNomeR );

        }
    }
}
