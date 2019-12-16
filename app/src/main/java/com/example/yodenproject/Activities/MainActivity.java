package com.example.yodenproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.yodenproject.R;
import com.example.yodenproject.Registration.UserRegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;
    final int PHONE_STATE_REQUEST =  1;

    boolean isClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText emailEt = findViewById(R.id.sign_in_email);
        final EditText passwordEt = findViewById(R.id.sign_in_password);

        //Sign in
        Button signInBtn = findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage(getResources().getString(R.string.loading));
                progressDialog.show();

                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.failed_details), Toast.LENGTH_SHORT).show();
                } else{
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.welcome), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, MainPage.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.sign_failed), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });

        //Choose user type
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        final RadioButton clientRadioBtn = findViewById(R.id.client_choice);
        final RadioButton professionalRadioBtn = findViewById(R.id.professional_choice);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.client_choice:
                        isClient = true;
                        break;
                    case R.id.professional_choice:
                        isClient = false;
                        break;
                }
            }
        });

        //Sign up
        Button signUpBtn = findViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!clientRadioBtn.isChecked() && !professionalRadioBtn.isChecked()){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.choose), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, UserRegisterActivity.class);
                    intent.putExtra("isClient",isClient);
                    startActivity(intent);
                }

            }
        });

        if(Build.VERSION.SDK_INT>=23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE_REQUEST);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== PHONE_STATE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED )
            Toast.makeText(this, getResources().getString(R.string.thanks), Toast.LENGTH_SHORT).show();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //check if user is null
        if (firebaseUser != null){
            Intent intent = new Intent(MainActivity.this, MainPage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }
}