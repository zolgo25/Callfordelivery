package com.example.callfordelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;                     //파이어베이스 인증임
    private DatabaseReference mDatabaseRef;                //실시간데베
    private EditText mEtEmail, mEtPwd, mEtPwdCheck;       //회원가입 입력필드
    private Button mBtnRegister;                         //회원가입 버튼인듯

    String emilPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Callfordelivery");

        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtPwdCheck = findViewById(R.id.et_pwdCheck);

        mBtnRegister = findViewById(R.id.btn_register);
        progressDialog = new ProgressDialog(this);


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth();
            }
        });
    }
    private void mFirebaseAuth() {
        String strEmail = mEtEmail.getText().toString();
        String strPwd = mEtPwd.getText().toString();
        String strPwdCheck = mEtPwdCheck.getText().toString();


        if (emilPattern.matches(emilPattern)) {
            mEtEmail.setError("오류라능");
        } else if (strPwd.isEmpty() || strPwd.length()<6) {
            mEtPwd.setError("비번 확인하라능");
        } else if (!strPwd.equals(strPwdCheck)) {
            mEtPwdCheck.setError("비번 불일치임");
        } else {
            progressDialog.setMessage("가입ㄱㄷㄱㄷ");
            progressDialog.setTitle("가입중");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); //가져온 현재의 파베유저로


                                UserAccount account = new UserAccount();
                                account.setIdToken(firebaseUser.getUid());
                                account.setEmailId(firebaseUser.getEmail());
                                account.setPassword(strPwd);
                                account.setPassword(strPwdCheck);

                                // 데베 삽입 행위임
                                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Register Successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}