package com.edu.ncu.drawlandmark;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;
    private DatabaseReference mdatabase;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView R_mEmailView;
    private EditText R_mPasswordView;
    private EditText R_mDoubleCheckView;
    private View R_mProgressView;
    private View R_mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {
                    Log.d("onAuthStateChanged", "已註冊:" +
                            user.getUid());
                    userUID = user.getUid();
                }else{
                    Log.d("onAuthStateChanged", "未註冊");
                }
            }
        };

        // Set up the login form.
        R_mEmailView = (AutoCompleteTextView) findViewById(R.id.R_email);

        R_mPasswordView = (EditText) findViewById(R.id.R_password);
        R_mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        R_mDoubleCheckView = (EditText) findViewById(R.id.R_doubleCheck);
        R_mDoubleCheckView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.R_email_register_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        R_mLoginFormView = findViewById(R.id.R_login_form);
        R_mProgressView = findViewById(R.id.R_login_progress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

    private void PasswordDoubleCheck(){
        R_mDoubleCheckView.setError(null);
        String passwaord = R_mPasswordView.getText().toString();
        String check_password = R_mDoubleCheckView.getText().toString();

        View focusView = null;

        if(passwaord != check_password){
            focusView = R_mDoubleCheckView;
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle(R.string.register_error_title)
                    .setMessage(R.string.login_error)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }


    private void attemptLogin() {
        // Reset errors.
        R_mEmailView.setError(null);
        R_mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = R_mEmailView.getText().toString();
        final String password = R_mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            R_mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = R_mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            R_mEmailView.setError(getString(R.string.error_field_required));
            focusView = R_mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            R_mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = R_mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle(R.string.register_error_title)
                    .setMessage(R.string.doublecheck_error)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            Log.d("AUTH", email+"/"+password);
            auth.signInWithEmailAndPassword(email, password).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                register(email, password);
                            }
                        }
                    });
        }

    }

    private void register(final String email, final String password) {
        createUser(email, password);
    }

    private void createUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                String message = task.isSuccessful() ? "註冊成功,歡迎來到Bubble Bang" : "註冊失敗";
                new AlertDialog.Builder(RegisterActivity.this)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(task.isSuccessful()){
                                    mdatabase = FirebaseDatabase.getInstance().getReference("drawlandmark-db");
                                    DatabaseReference userRef = mdatabase.child("users");
                                    User user = new User(userUID);
                                    userRef.push().setValue(user);
                                    startActivity(new Intent(RegisterActivity.this, SetProfileActivity.class));
                                }else{
                                    Log.d("註冊失敗","註冊失敗");
                                    Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                                    startActivity(intent);
                                } }})
                        .show();

                            }
                        });

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            R_mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            R_mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    R_mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            R_mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            R_mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    R_mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            R_mDoubleCheckView.setVisibility(show ? View.VISIBLE : View.GONE);
            R_mDoubleCheckView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    R_mDoubleCheckView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            R_mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            R_mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
