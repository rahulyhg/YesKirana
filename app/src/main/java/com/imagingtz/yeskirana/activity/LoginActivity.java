package com.imagingtz.yeskirana.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.java.Synchronize;
import com.imagingtz.yeskirana.java.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 0;
    private TextView forgotPassword;
    private TextView signUp;
    private EditText email, password;
    private Button login;
    private JSONObject jsonObject;
    private RequestQueue queue;
    //GPlus Fields
    // private ProfilePictureView profilePictureView;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mSignInClicked = false;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    private FrameLayout googleLogin;
    private FrameLayout facebookLogin;
    //Facebook Fields
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this);
        super.onCreate(savedInstanceState);
        if (Utils.isUserLoggedIn(LoginActivity.this)) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        signUp = (TextView) findViewById(R.id.footer);
        login = (Button) findViewById(R.id.login);
        loginButton = (LoginButton) findViewById(R.id.iv_login_with_fb);
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

        //Google Client init
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        facebookSetups();
    }


    private void login() {
        Utils.show(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                Synchronize.BASE_URL + "login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Login response ", response);
                        Utils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("status")) {
                                try {
                                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                        Utils.setUserCredential(com.imagingtz.yeskirana.activity.LoginActivity.this, jsonObject.getString("UserID"),
                                                jsonObject.getString("UserName"), jsonObject.getString("UserEmail"), true);
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        Toast.makeText(com.imagingtz.yeskirana.activity.LoginActivity.this, "successfully logged in.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(com.imagingtz.yeskirana.activity.LoginActivity.this, "username or password is incorrect",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.toString());
                Utils.dismiss();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", email.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (Utils.isNetworkAvailable(this)) {
                    if (Utils.validateField(email))
                        login();
                    //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
                break;
            case R.id.footer:
                startActivity(new Intent(com.imagingtz.yeskirana.activity.LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.forgot_password:
                showForgotPassword();
                break;
         /*   case R.id.google_login:
                mSignInClicked = true;
                mGoogleApiClient.connect();
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
            case R.id.facebook_login:
                loginButton.performClick();
                break;*/

        }
    }

    public void showForgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password?").setView(forgotPassword);
        View view = getLayoutInflater().inflate(R.layout.forgotpassword, null);
        builder.setView(view);
        final EditText forgotPassword = (EditText) view.findViewById(R.id.forgotPassword);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
                String userEmail = forgotPassword.getText().toString();
                forgotPassword(userEmail);

                //do something with it
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
            }
        });
        builder.create().show();
    }

    private void forgotPassword(final String userEmail) {
        Utils.show(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                Synchronize.BASE_URL + "forgetpassword.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response ", response);
                        Utils.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.toString());
                Utils.dismiss();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    private void facebookSetups() {
        loginButton.setReadPermissions(
                "email",
                "public_profile",
                "user_friends");
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                createFacebookRequest();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
            }
        };
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    createFacebookRequest();
                }
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
            }
        };
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("CricApp", "Connected to GPlus");
        if (mSignInClicked && Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            new Thread(new Runnable() {
                public void run() {
                    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                    Log.d("currentPerson", currentPerson.toString());
                    HashMap<String, String> items = new HashMap<>();
                    items.put("birthday", currentPerson.getBirthday());
                    items.put("email", Plus.AccountApi.getAccountName(mGoogleApiClient));
                    items.put("gender", currentPerson.getGender() == 0 ? "Male" : "Female");
                    items.put("name", currentPerson.hasDisplayName() ? currentPerson.getDisplayName()
                            : currentPerson.getName().getFamilyName());
                    items.put("image_url", currentPerson.getImage().getUrl());

                    items.put("type", String.valueOf(2));
                    items.put("access_token", getGooglePlusAccessToken());
                    items.put("fbID", currentPerson.getId());
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(items));
                        Log.d("currentPerson", currentPerson.toString());
                        doSocialLogin(getGooglePlusAccessToken(), currentPerson.getId(), "2", jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            mSignInClicked = false;
        }
    }

    private String getGooglePlusAccessToken() {
        String accessToken = "";
        try {
            accessToken = GoogleAuthUtil.getToken(
                    getApplicationContext(),
                    Plus.AccountApi.getAccountName(mGoogleApiClient), "oauth2:"
                            + Scopes.PLUS_LOGIN);
        } catch (UserRecoverableAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Intent recover = e.getIntent();
            startActivityForResult(recover, 125);
            return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GoogleAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return accessToken;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("CricApp", "onConnectionSuspended");
        mGoogleApiClient.connect();
    }

   /* protected void onStart() {
        super.onStart();
        registerReceiver(receiver, filter);
    }*/


    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        //  unregisterReceiver(receiver);
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = result;
            if (mSignInClicked) {
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
        callbackManager.onActivityResult(requestCode, responseCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }
            mIntentInProgress = false;
        }
       /* if (requestCode == VERIFY_OTP) {
            if (responseCode == RESULT_OK) {
                appPreferences.put(PROPERTY_LOGGED_IN, true);
                switchActivity(appPreferences.getInt(PROPERTY_USER_TYPE, 0));
            }
        }*/
    }

    private void createFacebookRequest() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Log.d("Token", AccessToken.getCurrentAccessToken().getToken());
                        if (object != null) {
                            Log.d("GraphRequest", object.toString());
                            try {
                                object.put("image_url", Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                                object.put("type", 1);
                                object.put("access_token", AccessToken.getCurrentAccessToken().getToken());
                                object.put("fbID", AccessToken.getCurrentAccessToken().getUserId());

                                doSocialLogin(AccessToken.getCurrentAccessToken().getToken(),
                                        AccessToken.getCurrentAccessToken().getUserId(), "1", object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle(); //Explicitly we need to specify the fields to get values else some values will be null.
        parameters.putString("fields", "id,birthday,email,first_name,gender,last_name,link,location,name");
        request.setParameters(parameters);
        request.executeAsync();


    }

    void doSocialLogin(String token, String id, String accountType, final JSONObject profile) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        params.put("fbid", id);
        params.put("account_type", accountType);
        params.put("device_type", "0");
        params.put("device_token", "");
        try {
            jsonObject = new JSONObject(new Gson().toJson(params));
            Log.d("jsonObject", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, Synchronize.BASE_URL + "login.php",
                        jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        Utils.dismiss();
                        //TODO:Inject the proper logic to start user and business landing pages
                        if (response.has("statusmessage")) {
                            try {
                                if (response.getBoolean("statusmessage")) {
                                    // saveLocally(response);
                                } else {
                                   /* Intent intent = new Intent(getApplicationContext(), SignupFormActivity.class);
                                    intent.putExtra("profile", profile.toString());
                                    startActivity(intent);*/
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("Error: ", error.toString());
                        Utils.dismiss();
                        // FugaDialog.showErrorDialog(LoginActivity.this);
                    }

                });
        queue.add(jsObjRequest);
    }
}
