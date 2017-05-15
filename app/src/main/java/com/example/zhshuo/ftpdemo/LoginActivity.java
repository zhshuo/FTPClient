package com.example.zhshuo.ftpdemo;


import  android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import static android.Manifest.permission.READ_CONTACTS;



/**
 登陆
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mHostView;
    private EditText mPortView;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        mHostView = (EditText) findViewById(R.id.host);
        mPortView = (EditText) findViewById(R.id.port);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.user);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        initConfig();
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void initConfig(){
        sp = getSharedPreferences("LoginInfo",0);
        mHostView.setText(sp.getString("host","0.0.0.0"));
        mPortView.setText(sp.getString("port","21"));
        mEmailView.setText(sp.getString("user", ""));
    }


    private void attemptLogin() {


        // Reset errors.
        mHostView.setError(null);
        mPortView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String host = mHostView.getText().toString();
        String port = mPortView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        sp = getSharedPreferences("LoginInfo", Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
        sp.edit().putString("host",host).apply();
        sp.edit().putString("port",port).apply();
        sp.edit().putString("user",email).apply();
        sp.edit().putString("password",password).apply();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }


        if (cancel) {

            focusView.requestFocus();
        } else {
            Intent intent = new Intent();
            intent.setAction("com.example.zhshuo.ftpdemo.action.LOGGEDIN");
            intent.putExtra("host",host);
            intent.putExtra("port",port);
            intent.putExtra("user",email);
            intent.putExtra("password",password);
            startActivity(intent);

        }
    }



    private boolean isPasswordValid(String password) {

        return password.length() >= 0;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




}

