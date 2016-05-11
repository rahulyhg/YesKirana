package com.imagingtz.yeskirana.java;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.imagingtz.yeskirana.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Utils {
    public static final String USER_CREDENTIAL_PREFS = "user_credential_prefs";
    public static final String IS_LOGGED_IN = "is_logged_in";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static String categoryId;


    private static ProgressDialog progressDialog;

    public static ProgressDialog show(Context context) {
        progressDialog = ProgressDialog.show(context, null, "Please wait...", false, false, null);
        return progressDialog;
    }

    public static void dismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    public static boolean validateField(EditText editText) {
        if (editText.getText().toString().length() > 0) {
            return true;
        } else {
            editText.setError("This filed is required.");
            return false;
        }
    }


    public static boolean validatePassword(EditText editText, boolean password) {
        if (editText.getText().toString().length() > 0) {
            if (password) {
                if (editText.getText().toString().length() >= 6)
                    return true;
                else {
                    editText.setError("Password should be minimum of 6 characters!");
                    return false;
                }
            }
            return true;
        } else {
            editText.setError("This filed is required.");
            return false;
        }
    }

    public static boolean isUserLoggedIn(Context context) {

        SharedPreferences userCredentials = context.getSharedPreferences(
                USER_CREDENTIAL_PREFS, Activity.MODE_PRIVATE);
        return userCredentials.getBoolean(IS_LOGGED_IN, false);
    }

    public static String getUserName(Context context) {

        SharedPreferences userCredentials = context.getSharedPreferences(
                USER_CREDENTIAL_PREFS, Activity.MODE_PRIVATE);
        return userCredentials.getString(USER_NAME, "");
    }

    public static String getUserId(Context context) {

        SharedPreferences userCredentials = context.getSharedPreferences(
                USER_CREDENTIAL_PREFS, Activity.MODE_PRIVATE);
        return userCredentials.getString(USER_ID, "");
    }


    public static String getUserEmail(Context context) {

        SharedPreferences userCredentials = context.getSharedPreferences(
                USER_CREDENTIAL_PREFS, Activity.MODE_PRIVATE);
        return userCredentials.getString(USER_EMAIL, "");
    }

    public static void setUserCredential(Context context, String user_Id, String user_name,String email, boolean loggedIn) {

        SharedPreferences userCredentials = context.getSharedPreferences(
                USER_CREDENTIAL_PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userCredentials.edit();
        editor.putString(USER_ID, user_Id);
        editor.putString(USER_NAME, user_name);
        editor.putString(USER_EMAIL, email);
        editor.putBoolean(IS_LOGGED_IN, loggedIn);
        editor.apply();
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        if (!(haveConnectedWifi || haveConnectedMobile)) {
            Toast.makeText(context, "No Network Connectivity", Toast.LENGTH_SHORT).show();
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void setImageWithProgress(Context context, ImageView image, String url, final ProgressBar progressBar) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default_icon)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(image);
    }

    public static String getCityName(Context context, double lat, double lang) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lang, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0)
            return addresses.get(0).getAddressLine(2);
        else
            return "";
    }


}
