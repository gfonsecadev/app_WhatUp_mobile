package com.example.whatsapp.ferramentas;

import android.util.Base64;

public class Base64Custon {

    public static String criptografar(String email) {
        return Base64.encodeToString(email.getBytes(), Base64.DEFAULT).replaceAll("\\n|\\r", "");
    }

    public static String descriptografar(String email_codificado) {
        return new String(Base64.decode(email_codificado, Base64.DEFAULT));
    }
}
