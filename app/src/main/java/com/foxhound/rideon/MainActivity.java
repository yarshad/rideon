package com.foxhound.rideon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.foxhound.rideon.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    RelativeLayout rootLayout;


    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void attachBaseContext(Context newBase){

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Arkhip_font.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build());

        setContentView(R.layout.activity_main);


        //Init Firebase

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("user");

        //Init view

        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn = findViewById(R.id.btnSignIn);
        rootLayout = findViewById(R.id.rootLayout);


        //Event

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View view) {
                showRegisterDialog();
            }
        });
    }

    private void showLoginDialog() {

        final AlertDialog.Builder dialog  = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");

        LayoutInflater inflater =  LayoutInflater.from(this);

        View login_layout = inflater.inflate(R.layout.login, null);

        final MaterialEditText edtEmail  = login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword  = login_layout.findViewById(R.id.edtPassword);

        dialog.setView(login_layout);

        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();

                //Validation

                if(TextUtils.isEmpty(edtEmail.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(edtPassword.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                //Login

                auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        startActivity(new Intent(MainActivity.this,Welcome.class));
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Snackbar.make(rootLayout,"Login failed" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                     }
                });

            }
        });


        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }


        });



        dialog.show();


    }


    private void showRegisterDialog() {


        final AlertDialog.Builder dialog  = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");

        LayoutInflater inflater =  LayoutInflater.from(this);

        View register_layout = inflater.inflate(R.layout.reigster, null);

        final MaterialEditText edtEmail  = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword  = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtName  = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone  = register_layout.findViewById(R.id.edtPhone);


        dialog.setView(register_layout);

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();

                //Validation

                if(TextUtils.isEmpty(edtEmail.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter email address", Snackbar.LENGTH_LONG).show();
                    return;
                }
//                if(TextUtils.isEmpty(edtName.getText().toString())){
//                    Snackbar.make(rootLayout,"Please enter name", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
                if(TextUtils.isEmpty(edtPassword.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter password", Snackbar.LENGTH_LONG).show();
                    return;
                }
//                if((edtPassword.getText().length() < 8 )){
//                    Snackbar.make(rootLayout,"Password too short", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }

                //Register new user

                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>(){

                            @Override
                            public void onSuccess(AuthResult authResult) {

                                User user = new User(
                                        edtEmail.getText().toString(),
                                        edtPassword.getText().toString(),
                                        edtName.getText().toString(),
                                        edtPhone.getText().toString()
                                );

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout, "Registration Successful !!", Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout, "Registration failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });

                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootLayout, "Registration failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });


        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });

        dialog.show();

    }
}
