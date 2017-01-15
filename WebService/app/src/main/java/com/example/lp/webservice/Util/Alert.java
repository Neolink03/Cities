package com.example.lp.webservice.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.lp.webservice.R;

/**
 * Created by jonathan on 10/12/16.
 */

public class Alert {

    public static final String UPDATE_SUCCESSFUL = " a bien été modifiée";
    public static final String UPDATE_UNSUCCESSFUL = " n'a pas pu être modifiée";

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
    }

    public void noNetworkConnection(Context applicationContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);

        builder.setMessage(applicationContext.getString(R.string.no_network_error_message))
                .setTitle(applicationContext.getString(R.string.no_network_error_title));

        builder.setPositiveButton(applicationContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

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

    public static void cityResultSave(String cityName, String resultOperation, Context applicationContext) {

        if(null == cityName) {
            cityName = "";
        }

        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "La ville " + cityName + resultOperation,
                Toast.LENGTH_SHORT);

        toast.show();
    };

    public static void preferencesSaved(Context applicationContext) {

        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Préférences sauvegardées",
                Toast.LENGTH_SHORT);

        toast.show();
    };

    public static void error(Context applicationContext) {

        android.widget.Toast toast = android.widget.Toast.makeText(
                applicationContext,
                "Erreur",
                Toast.LENGTH_SHORT);

        toast.show();
    };
}
