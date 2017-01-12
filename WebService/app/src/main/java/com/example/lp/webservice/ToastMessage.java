package com.example.lp.webservice;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jonathan on 10/12/16.
 */

public class ToastMessage {

    public static void displayUnexpectedServerResponse(Context applicationContext) {
        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Échec de récupération des données du serveur.",
                Toast.LENGTH_LONG);
        toast.show();
    };

    public static void displayJSONReadError(Context applicationContext) {
        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Erreur lors du chargement des données.",
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

    public static void citySucessfullyDeleted(String cityName, Context applicationContext) {

        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "La ville " + cityName + " a bien été supprimée",
                Toast.LENGTH_SHORT);

        toast.show();
    };

    public static void cityUnSucessfullyDeleted(String cityName, Context applicationContext) {

        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "La ville " + cityName + " n'a pas pu être supprimée",
                Toast.LENGTH_SHORT);

        toast.show();
    };

    public static void citySucessfullyCreated(String cityName, Context applicationContext) {

        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "La ville " + cityName + " a bien été créée",
                Toast.LENGTH_SHORT);

        toast.show();
    };

    public static void cityUnSucessfullyCreated(String cityName, Context applicationContext) {

        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "La ville " + cityName + " n'a pas pu être créée",
                Toast.LENGTH_SHORT);

        toast.show();
    };
}
