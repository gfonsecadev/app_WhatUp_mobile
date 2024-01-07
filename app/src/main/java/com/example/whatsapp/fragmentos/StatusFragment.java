package com.example.whatsapp.fragmentos;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.AplicacaoActivity;
import com.example.whatsapp.activity.VizualizarActivity;
import com.example.whatsapp.databinding.RecyclerChatDestinatarioBinding;
import com.example.whatsapp.entidades.Status;
import com.example.whatsapp.entidades.Usuario;
import com.example.whatsapp.ferramentas.Upload_download;
import com.example.whatsapp.ferramentas.firebase;
import com.example.whatsapp.recyclers.AdapterStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;


public class StatusFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private List<Status> statusList = new ArrayList<>();
    private AdapterStatus adapterStatus;
    private RecyclerView recyclerView;
    private FloatingActionButton fabEditar, fabCamera;
    private LinearLayout linearLayout;


    private ImageView imageView;
    private EditText editText;
    private Bitmap bitmap;
    private List<Usuario> usuarioList = new ArrayList<>();

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
        fabCamera = view.findViewById(R.id.fabCameraStatus);
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
                Intent intent=new Intent(getActivity(),VizualizarActivity.class);
                intent.putExtra("statusPersonalizado","");
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









    public void recuperarStatus(){
        DatabaseReference databaseReference=firebase.databaseInstance().child("Status")
                .child(firebase.recuperar_idUsuario()).child("OutrosStatus");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Status status=snapshot.getValue(Status.class);

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
