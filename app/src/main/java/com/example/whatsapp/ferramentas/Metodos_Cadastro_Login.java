package com.example.whatsapp.ferramentas;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.whatsapp.activity.AplicacaoActivity;
import com.example.whatsapp.activity.LoginActivity;
import com.example.whatsapp.activity.MainActivity;
import com.example.whatsapp.entidades.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Metodos_Cadastro_Login {


    public static void cadastrarUsuario(TextInputEditText nome, TextInputEditText email, TextInputEditText senha, Context context) {
        FirebaseAuth firebaseAuth;
        firebaseAuth = firebase.firebaseAuthInstance();
        String nomeDig = nome.getText().toString();
        String emailDig = email.getText().toString();
        String senhaDig = senha.getText().toString();

        if (!nomeDig.isEmpty()) {
            if (!emailDig.isEmpty()) {
                if (!senhaDig.isEmpty()) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(nomeDig);
                    usuario.setEmail(emailDig);
                    usuario.setSenha(senhaDig);
                    usuario.setFoto("");
                    firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                usuario.salvarUsuarioFirebase();
                                cadastrar_firebaUserNome(nomeDig);
                                context.startActivity(new Intent(context, AplicacaoActivity.class));
                                Toast.makeText(context, "Bem vindo " + usuario.getNome(), Toast.LENGTH_LONG).show();
                            } else {
                                String excessao = "";
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    excessao = "Email não está cadastrado";

                                } catch (FirebaseAuthWeakPasswordException e) {
                                    excessao = "Senha muito fraca, insira uma senha mais forte!";

                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    excessao = "Este email não é válido";
                                } catch (FirebaseAuthUserCollisionException e) {
                                    excessao = "Este email já está em uso, digite outro!";

                                } catch (Exception e) {
                                    excessao = e.getMessage();
                                }

                                Snackbar.make(nome.getRootView(), excessao, Snackbar.LENGTH_LONG).show();

                            }


                        }


                    });


                } else {
                    senha.setError("Digite uma senha!");
                    senha.requestFocus();
                }
            } else {
                email.setError("Digite um email!");
                email.requestFocus();
            }
        } else {
            nome.setError("Digite um nome!");
            nome.requestFocus();
        }

    }

    public static void loginUsuario(TextInputEditText email, TextInputEditText senha, Context context) {
        FirebaseAuth firebaseAuth;
        firebaseAuth = firebase.firebaseAuthInstance();
        String emailDig = email.getText().toString();
        String senhaDig = senha.getText().toString();


        if (!emailDig.isEmpty()) {
            if (!senhaDig.isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setEmail(emailDig);
                usuario.setSenha(senhaDig);


                firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            context.startActivity(new Intent(context, AplicacaoActivity.class));
                            Toast.makeText(context, "Bem vindo " + firebase.firebaseUser().getDisplayName(), Toast.LENGTH_LONG).show();

                        } else {
                            String excessao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                excessao = "Email não está cadastrado";

                            } catch (FirebaseAuthWeakPasswordException e) {
                                excessao = "Senha muito fraca, insira uma senha mais forte!";

                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excessao = "Este email não é válido";
                            } catch (FirebaseAuthUserCollisionException e) {
                                excessao = "Este email já está em uso, digite outro!";

                            } catch (Exception e) {
                                excessao = e.getMessage();
                            }
                            Snackbar.make(email.getRootView(), excessao, Snackbar.LENGTH_LONG).show();

                        }


                    }


                });


            } else {
                senha.setError("Digite uma senha!");
                senha.requestFocus();
            }
        } else {
            email.setError("Digite um email!");
            email.requestFocus();
        }
    }

    public static void verificar_logado(Context context) {
        FirebaseAuth firebaseAuth;
        firebaseAuth = firebase.firebaseAuthInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            context.startActivity(new Intent(context, AplicacaoActivity.class));

        }
    }

    public static void cadastrar_firebaUserNome(String nome) {
        try {
            FirebaseUser firebaseUser = firebase.firebaseUser();
            UserProfileChangeRequest profileName = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
            firebaseUser.updateProfile(profileName);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void cadastrar_firebaUserUri(Uri uri) {
        try {
            FirebaseUser firebaseUser = firebase.firebaseUser();
            UserProfileChangeRequest profileFoto = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
            firebaseUser.updateProfile(profileFoto);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}



