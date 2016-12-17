package com.example.lp.webservice;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jonathan on 10/12/16.
 */

public class ToastMessage {

    public static void displayUnexpectedResponseInToast(Context applicationContext) {
        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Échec de récupération des données du serveur.",
                Toast.LENGTH_LONG);
        toast.show();
    };

    public static void displayJSONReadErrorInToast(Context applicationContext) {
        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Erreur lors du chargement.",
                Toast.LENGTH_SHORT);
        toast.show();
    };

    public static void noCityFound(Context applicationContext) {
        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Aucun résultat trouvé.",
                Toast.LENGTH_SHORT);
        toast.show();
    };

    public static void noNetworkConnection(Context applicationContext) {
        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Aucune connection réseau.",
                Toast.LENGTH_SHORT);
        toast.show();
    };
}
