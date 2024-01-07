package com.example.whatsapp.activity;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.ferramentas.EsconderTeclado;
import com.example.whatsapp.ferramentas.firebase;
import com.example.whatsapp.fragmentos.CameraFragment;
import com.example.whatsapp.fragmentos.ChamadasFragment;
import com.example.whatsapp.fragmentos.ConversaFragment;
import com.example.whatsapp.fragmentos.StatusFragment;
import com.example.whatsapp.recyclers.AdapterConversas;
import com.google.firebase.auth.FirebaseAuth;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class AplicacaoActivity extends AppCompatActivity{
    FragmentPagerItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aplicacao);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("WhatsApp");

        ViewPager viewPager=findViewById(R.id.viewpager);
        SmartTabLayout tabLayout=findViewById(R.id.smarttab);

        adapter=new FragmentPagerItemAdapter(getSupportFragmentManager(),
                FragmentPagerItems.with(this).add("Conversas",ConversaFragment.class)
                                                    .add("Status",StatusFragment.class)
                                                    .add("Chamadas", ChamadasFragment.class)
                                                    .create());


        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        MenuItem menuItem=menu.findItem(R.id.procurar);
        SearchView searchView= (SearchView) menuItem.getActionView();






        ConversaFragment conversaFragment= (ConversaFragment) adapter.getPage(0);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText!=null && !newText.equals("")){
                    conversaFragment.listarProcura(newText.toUpperCase());
                }else {
                    conversaFragment.atualizarProcura();
                    searchView.clearFocus();

                }

                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                conversaFragment.atualizarProcura();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.perfil:
                    startActivity(new Intent(AplicacaoActivity.this,ConfiguracaoActivity.class));
                break;
            case R.id.sair:
                deslogar();
                break;
            case R.id.criarGrupo:
               startActivity(new Intent(this,GrupoActivity.class));
                break;



        }
        return super.onOptionsItemSelected(item);
    }

    public void chamar_contatos(View view) {
        startActivity(new Intent(AplicacaoActivity.this, ContatoActivity.class));
    }
    public void deslogar(){

        AlertDialog.Builder alert= new AlertDialog.Builder(this, androidx.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog);
        alert.setTitle("Logout de usuário");
        alert.setMessage("Confirmar saída do WhatsApp");
        alert.setCancelable(false);
        alert.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth user= firebase.firebaseAuthInstance();
                user.signOut();
                finish();
                startActivity(new Intent(AplicacaoActivity.this,MainActivity.class));
            }
        });

        alert.setNegativeButton("Permanecer",null);
        alert.create().show();
    }

}