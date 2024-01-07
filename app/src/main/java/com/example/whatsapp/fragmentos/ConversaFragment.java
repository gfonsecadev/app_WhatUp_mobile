package com.example.whatsapp.fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.entidades.Conversas;
import com.example.whatsapp.ferramentas.firebase;
import com.example.whatsapp.recyclers.AdapterConversas;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConversaFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ConversaFragment() {
        // Required empty public constructor
    }

    public static ConversaFragment newInstance(String param1, String param2) {
        ConversaFragment fragment = new ConversaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    private RecyclerView recyclerConversas;
    private List<Conversas> listConversas =new ArrayList<>();
    private AdapterConversas adapterConversas;
    private ChildEventListener eventListener;
    private DatabaseReference databaseReference;
    private DatabaseReference conversasRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_conversa, container, false);
        recyclerConversas=view.findViewById(R.id.recyclerConversas);

        adapterConversas=new AdapterConversas(listConversas,getActivity());

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerConversas.setLayoutManager(layoutManager);
        recyclerConversas.setHasFixedSize(true);
        recyclerConversas.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerConversas.setAdapter(adapterConversas);

        String idUsuario=firebase.recuperar_idUsuario();
        databaseReference= firebase.databaseInstance();
        conversasRef =databaseReference.child("Conversas").child(idUsuario);

        excluirConversa();





  return view;  }



    @Override
    public void onStart() {
        super.onStart();
        listConversas.clear();
       listarConversas();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        conversasRef.removeEventListener(eventListener);

    }


    @Override
    public void onStop() {
        super.onStop();
        listConversas.clear();
        conversasRef.removeEventListener(eventListener);

    }

    public void listarProcura(String procurar) {
        List<Conversas> listAchados = new ArrayList<>();

        for (Conversas conversas : listConversas) {
            String nome = conversas.getUsuarioConversa().getNome().toUpperCase();
            String mensagem = conversas.getUltimaMensagem().toUpperCase();
            if (nome.contains(procurar) || mensagem.contains(procurar)) {
                listAchados.add(conversas);
            }
        }
        adapterConversas = new AdapterConversas(listAchados, getActivity());
        recyclerConversas.setAdapter(adapterConversas);
        adapterConversas.notifyDataSetChanged();
    }

    public void atualizarProcura() {
        adapterConversas = new AdapterConversas(listConversas, getActivity());
        recyclerConversas.setAdapter(adapterConversas);
        adapterConversas.notifyDataSetChanged();
    }


    public void listarConversas() {


        eventListener = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Conversas conversas = snapshot.getValue(Conversas.class);
                if(conversas.getHora()!=null){
                    listConversas.add(conversas);
                    Collections.sort(listConversas);
                    adapterConversas.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Conversas conversas = snapshot.getValue(Conversas.class);
                String key=snapshot.getKey();

                List<Conversas> conversasListAux = new ArrayList<>();
                List<Conversas> conversasListkey = new ArrayList<>();
                conversasListAux.indexOf(key);
                conversasListkey.add(conversas);
                conversasListAux.addAll(listConversas);
                int index = 0;

                for (Conversas aux : listConversas) {
                    if (aux.getIdUsuarioDestinatario().equals(conversas.getIdUsuarioDestinatario())) {
                        conversasListAux.remove(index);
                       }
                    index++;
                }

                listConversas.clear();
                listConversas.add(conversas);
                int index2 = 1;

                for (Conversas aux : conversasListAux) {
                    listConversas.add(index2, aux);
                    index2++;
                }

                adapterConversas.notifyDataSetChanged();


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Conversas conversas=snapshot.getValue(Conversas.class);
                List<Conversas> listrenovada=new ArrayList<>();

                for (   Conversas conv :listConversas ) {
                    if(!conv.getIdUsuarioDestinatario().equals(conversas.getIdUsuarioDestinatario())){
                        listrenovada.add(conv);
                    }
                }
                listConversas.clear();
                listConversas.addAll(listrenovada);
                adapterConversas.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void excluirConversa(){
        ItemTouchHelper.Callback touch=new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int flags=ItemTouchHelper.ACTION_STATE_IDLE;
                int swiped=ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(flags,swiped);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                arrastar_e_excluir(viewHolder);
            }
        };
        new ItemTouchHelper(touch).attachToRecyclerView(recyclerConversas);
    }


    public void arrastar_e_excluir(RecyclerView.ViewHolder viewHolder){
        int position=viewHolder.getAdapterPosition();
        ImageView view= (ImageView) getLayoutInflater().inflate(R.layout.layout_vizualizar_dialog,null);
        view.setImageResource(R.drawable.logo);
        view.setScaleType(ImageView.ScaleType.FIT_XY);

        AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
        alert.setTitle("Exclusão de dados");
        alert.setMessage("Tem certeza que deseja excluir essa movimentação?");
        alert.setCancelable(false);
        alert.setView(view);
        alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_LONG).show();
                adapterConversas.notifyDataSetChanged();
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Conversas conversas=listConversas.get(position);
                DatabaseReference databaseListaConversas=databaseReference.child("Conversas").child(firebase.recuperar_idUsuario())
                        .child(conversas.getIdUsuarioDestinatario());
                databaseListaConversas.removeValue();
                DatabaseReference databaseListaMensagens=databaseReference.child("Mensagens").child(firebase.recuperar_idUsuario())
                        .child(conversas.getIdUsuarioDestinatario());
                databaseListaMensagens.removeValue();

                Toast.makeText(getActivity(),"Apagado!",Toast.LENGTH_LONG).show();




            }
        });
        AlertDialog alertDialog=alert.create();
        alertDialog.show();

    }


}