package com.majedalmoqbeli.newsapp.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.majedalmoqbeli.newsapp.R;
import com.majedalmoqbeli.newsapp.base.MainApplication;
import com.majedalmoqbeli.newsapp.constants.ErrorCode;


public class Function {


    /**
     * @param error form volley
     * @return Error code
     */
    public static String checkErrorType(VolleyError error) {
        String str = "";
        if (error instanceof NoConnectionError) {
            str = ErrorCode.IS_NOT_NETWORK;
        } else if (error instanceof AuthFailureError) {
            str = ErrorCode.AUTH_FAILED;
        } else if (error instanceof TimeoutError) {
            str = ErrorCode.CONNECTION_TIMEOUT;
        } else if (error instanceof ParseError) {
            str = ErrorCode.PARSE_DATA_ERROR;
        } else if (error instanceof ServerError) {
            str = ErrorCode.SERVER_ERROR;
        } else {
            str = ErrorCode.REQUEST_ERROR;
        }
        return str;
    }


    /**
     * @return TRUE if network is connected, otherwise FALSE.
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MainApplication.sMainApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     * for show snackBar
     *
     * @param message : Error message to show in the snackBar
     */
    public static void showSnackBar(Context context, View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.color_links));
        snackbar.setAction(context.getString(R.string.ok), v -> {
            // here to do when clicked
        });
        snackbar.show();
    }

    /**
     * to change Visibility to Visible
     *
     * @param view : any view
     */
    public static void setViewVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }


    /**
     * to change Visibility to GONE
     *
     * @param view : any view
     */
    public static void setViewGone(View view) {
        view.setVisibility(View.GONE);
    }

    /**
     * to show toast message
     *
     * @param context :
     * @param mText   : your Text
     */
    public static void showToast(Context context, String mText) {
        Toast.makeText(context, mText, Toast.LENGTH_LONG).show();
    }


}
