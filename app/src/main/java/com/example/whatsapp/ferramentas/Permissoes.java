package com.example.whatsapp.ferramentas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class Permissoes {
    static  String[]  permissao=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static Boolean validarPermissoes( Activity context, int requestCode){
        List<String> permission=new ArrayList<>();
        if (Build.VERSION.SDK_INT > 26) {
            for (String permissao : permissao) {
                if (!(ContextCompat.checkSelfPermission(context, permissao) == PackageManager.PERMISSION_GRANTED)) {
                    permission.add(permissao);
                }
            }
        }

        if (permission.isEmpty()) {
            return false;
        } else {
            String[] stringsPermissoes = new String[permission.size()];
            permission.toArray(stringsPermissoes);
            ActivityCompat.requestPermissions(context, stringsPermissoes, requestCode);
            return true;
        }
    }
}
