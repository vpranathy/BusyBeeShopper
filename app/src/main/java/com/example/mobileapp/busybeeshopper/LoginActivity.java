package com.example.mobileapp.busybeeshopper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {
    private static final String TAG = "LoginActivity";


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mUserName;
    private EditText mEmail;
    private View mProgressView;
    private View mLoginFormView;
    private String email, password, username;
    Intent onSignIn;
    
    //firebaseAuth
    private FirebaseAuth mAuth;
    
    //Firebase database
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserName = (EditText) findViewById(R.id.UserName);
<<<<<<< HEAD

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String username= mUserName.getText().toString();
                String pwd= mPasswordView.getText().toString();
                if(username.equals("parul")&&pwd.equals("123")){
                    mUserName.getText().clear();
                    mPasswordView.getText().clear();
                    Intent main= new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(main);
                    Toast.makeText(LoginActivity.this,"Sign in successful", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(LoginActivity.this,"Sign in unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
=======
        mEmail = findViewById(R.id.email);
        mPasswordView=findViewById(R.id.password);
        


        //firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

    }



    public void createAccount(View view) {
        Log.d(TAG, "createAccount: starting");
        email= mEmail.getText().toString();
        password= mPasswordView.getText().toString();
        username=mUserName.getText().toString();
        final Users newUser = new Users(email,username,0,username);
        Log.d(TAG, "createAccount: email and password is "+email+"    "+password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this,"New User Registered",Toast.LENGTH_SHORT).show();
                            DatabaseReference myref1 = database.getReference("Users");
                            myref1.child(username).setValue(newUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    public void signIn(View view) {
        Log.d(TAG, "createAccount: starting");
        email= mEmail.getText().toString();
        password= mPasswordView.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "onComplete: successfully signed in");


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
>>>>>>> 10d58cf2488b8824fb84e99fd775d001f2b0a705

                        // ...
                    }
                });
    }


}

