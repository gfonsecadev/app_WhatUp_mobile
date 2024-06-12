package com.example.whatsapp.ferramentas;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class EsconderTeclado {
    public static void esconderTeclado(View view, Context c) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == false) {
                    InputMethodManager teclado = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
                    teclado.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }
}
