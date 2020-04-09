package com.e.s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;



public class RegisterActivity extends AppCompatActivity {
    private CountryCodePicker ccp;
    private EditText phoneText;
    private EditText codeText;
    private Button continueandnextbtn;
    private String checekr="",phoneNumber="";
    private RelativeLayout relativeLayout;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private FirebaseAuth mauth;
    private  String mverificationId;
    private PhoneAuthProvider.ForceResendingToken mResendtoken;
    private ProgressDialog lodingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_register);
        mauth=FirebaseAuth.getInstance ();
        lodingbar=new ProgressDialog (this);
        phoneText=findViewById (R.id.phoneText);
        codeText=findViewById (R.id.codeText);
        continueandnextbtn=findViewById (R.id.continueNextButton);
        relativeLayout=findViewById (R.id.phoneAuth);
        ccp=findViewById (R.id.ccp);
        ccp.registerCarrierNumberEditText (phoneText);
        continueandnextbtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if (continueandnextbtn.getText ().equals ("Submit")||checekr.equals ("Code sent"))
                {
                    String verificationcode=codeText.getText ().toString ();
                    if (verificationcode.equals (""))
                    {
                        Toast.makeText (RegisterActivity.this, "write vreification coe", Toast.LENGTH_SHORT).show ();
                    }
                    else
                    {
                        lodingbar.setTitle ("Code Verification");
                        lodingbar.setMessage ("please Wait!we are verifing your code");
                        lodingbar.setCanceledOnTouchOutside (false);
                        lodingbar.show ();
                        PhoneAuthCredential credential=PhoneAuthProvider.getCredential (mverificationId,verificationcode);
                        signInWithPhoneAuthCredential (credential);
                    }

                }
                else
                {
                    phoneNumber=ccp.getFullNumberWithPlus ();
                    if (!phoneNumber.equals (""))
                    {
                        lodingbar.setTitle ("Phone number Verification");
                        lodingbar.setMessage ("please Wait!we are verifing your phone number");
                        lodingbar.setCanceledOnTouchOutside (false);
                        lodingbar.show ();

                        PhoneAuthProvider.getInstance ().verifyPhoneNumber(phoneNumber,120, TimeUnit.SECONDS,RegisterActivity.this,mcallbacks);

                    }
                    else
                    {
                        Toast.makeText (RegisterActivity.this, "Write valid phone number", Toast.LENGTH_SHORT).show ();
                    }
                }
            }

        });
        mcallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks () {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential (phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                Toast.makeText (RegisterActivity.this, "Hey your phone number is invalid", Toast.LENGTH_SHORT).show ();
                relativeLayout.setVisibility (View.VISIBLE);
                lodingbar.dismiss ();
                continueandnextbtn.setText ("Continue");
                //if sim in same phone

                codeText.setVisibility (View.GONE);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent (s, forceResendingToken);
                mverificationId=s;
                mResendtoken=forceResendingToken;
                relativeLayout.setVisibility (View.GONE);
                checekr="Code Sent";
                continueandnextbtn.setText ("Submit");
                //if sim in same phone
                codeText.setVisibility (View.VISIBLE);
                lodingbar.dismiss ();
                Toast.makeText (RegisterActivity.this, "Code has been sent,please check", Toast.LENGTH_SHORT).show ();
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart ();

        FirebaseUser firebaseUser=FirebaseAuth.getInstance ().getCurrentUser ();
        if (firebaseUser!=null)
        {
            Intent homeintent = new Intent (RegisterActivity.this, MainActivity.class);
            startActivity (homeintent);
            finish ();
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            lodingbar.dismiss ();
                            Toast.makeText (RegisterActivity.this, "Congratulation logged in successfully", Toast.LENGTH_SHORT).show ();
                            sentusertomainActivity ();
                            FirebaseUser user = task.getResult().getUser();

                        }
                        else {
                            lodingbar.dismiss ();
                            String e = task.getException ().toString ();
                            Toast.makeText (RegisterActivity.this, "Error" + e, Toast.LENGTH_SHORT).show ();


                        }
                    }
                });
    }

    private  void sentusertomainActivity()
    {
        Intent intent=new Intent (RegisterActivity.this, MainActivity.class);
        startActivity (intent);
        finish ();
    }
}
