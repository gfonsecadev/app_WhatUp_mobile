package com.example.whatsapp.ferramentas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.activity.VizualizarActivity;
import com.example.whatsapp.entidades.Conversas;
import com.example.whatsapp.entidades.Mensagens;

public class MostrarFoto {

    public static void mostrar_foto(Conversas conversas, Context context) {
        String foto = "";
        if (conversas.getIsGrupo().equals("true")) {
            foto = conversas.getGrupo().getFotoGrupo();
        } else foto = conversas.getUsuarioConversa().getFoto();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ImageView view;
        view = (ImageView) layoutInflater.inflate(R.layout.layout_vizualizar_dialog, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600, 600);
        view.setLayoutParams(layoutParams);
        if (!foto.equals("")) {
            Glide.with(context).load(foto).into(view);
        }

        Dialog dialog = new Dialog(context);
        dialog.addContentView(view, layoutParams);
        dialog.create();
        dialog.show();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VizualizarActivity.class);
                intent.putExtra("contato", conversas);
                context.startActivity(intent);
            }
        });
    }

    public static void mostrar_imagem(Mensagens mensagens, Context context) {
        String foto = "";
        if (!mensagens.getImagem().equals("")) {
            foto = mensagens.getImagem();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            ImageView view;
            view = (ImageView) layoutInflater.inflate(R.layout.layout_vizualizar_dialog, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            Glide.with(context).load(foto).into(view);
            Dialog dialog = new Dialog(context);
            dialog.addContentView(view, layoutParams);


            dialog.create();
            dialog.show();
        }


    }
}
