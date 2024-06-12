package com.example.whatsapp.ferramentas;

import java.text.SimpleDateFormat;

public class DataStatusCuston {

    public static String retornarDataStatus(long data) {
        String dataFinal = new SimpleDateFormat().format(data);

        String[] datafracionada = dataFinal.split(" ");


        return "postado em " + datafracionada[0] + " às " + datafracionada[1] + " horas";
    }


}
