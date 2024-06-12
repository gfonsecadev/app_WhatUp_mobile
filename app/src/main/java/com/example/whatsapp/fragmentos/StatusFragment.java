package com.example.whatsapp.fragmentos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.VizualizarActivity;
import com.example.whatsapp.entidades.Status;
import com.example.whatsapp.entidades.Usuario;
import com.example.whatsapp.ferramentas.firebase;
import com.example.whatsapp.recyclers.AdapterStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class StatusFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private final List<Status> statusList = new ArrayList<>();
    private AdapterStatus adapterStatus;
    private RecyclerView recyclerView;
    private FloatingActionButton fabEditar, fabCamera;
    private LinearLayout linearLayout;


    private ImageView imageView;
    private EditText editText;
    private Bitmap bitmap;
    private final List<Usuario> usuarioList = new ArrayList<>();

    public StatusFragment() {
        // Required empty public constructor
    }

    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        recyclerView = view.findViewById(R.id.recyclerStatus);
        fabEditar = view.findViewById(R.id.fabEditarStatus);
        imageView = view.findViewById(R.id.imagemEnviar);
        editText = view.findViewById(R.id.text_send_layout_imagem);
        linearLayout = view.findViewById(R.id.layoutStatus);


        adapterStatus = new AdapterStatus(statusList, getActivity());


        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterStatus);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VizualizarActivity.class);
                intent.putExtra("statusPersonalizado", "");
                startActivity(intent);

            }
        });


        fabEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VizualizarActivity.class);
                intent.putExtra("statusEditar", "");
                startActivity(intent);
            }
        });

        return view;
    }


    public void recuperarStatus() {
        DatabaseReference databaseReference = firebase.databaseInstance().child("Status")
                .child(firebase.recuperar_idUsuario()).child("OutrosStatus");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Status status = snapshot.getValue(Status.class);

                statusList.add(status);

                adapterStatus.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarStatus();

    }

    @Override
    public void onStop() {
        super.onStop();
        statusList.clear();
    }
}
