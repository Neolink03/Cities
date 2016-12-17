package com.example.lp.webservice;

import android.content.Context;

/**
 * Created by jonathan on 10/12/16.
 */

public class ToastMessage {

    public static void displayUnexpectedResponseInToast(Context applicationContext) {
        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Échec de récupération des données du serveur.",
                android.widget.Toast.LENGTH_LONG);
        toast.show();
    };

    public static void displayJSONReadErrorInToast(Context applicationContext) {
        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Erreur lors du chargement.",
                android.widget.Toast.LENGTH_LONG);
        toast.show();
    };

    public static void noCityFoundMessage(Context applicationContext) {
        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Aucun résultat trouvé.",
                android.widget.Toast.LENGTH_LONG);
        toast.show();
    };
}
