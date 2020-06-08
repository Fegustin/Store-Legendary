package com.shitshop.storeshits.ui.profile.register.verification;

import android.app.Activity;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.shitshop.storeshits.R;

import java.util.concurrent.TimeUnit;

public class VerificationCodePresenter {

    private VerificationCodeView view;
    private Activity activity;

    private String verificationId;

    public VerificationCodePresenter(VerificationCodeView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    public void sendVerificationCode(String number) {
        view.progressBarVisible();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(number, "RU");
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164),
                    60,
                    TimeUnit.SECONDS,
                    activity,
                    mCallbacks);
        } catch (NumberParseException e) {
            Log.i("ErrorFormat", e.toString());
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            view.progressBarGone();
            if (code != null) {
                view.editText(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            view.failedVerificationCode();

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                view.toastMessage(activity.getString(R.string.number_correct));
                Log.i("Error 1", e.getMessage());
            } else if (e instanceof FirebaseTooManyRequestsException) {
                view.toastMessage(activity.getString(R.string.quota));
                Log.i("Error 2", e.getMessage());
            }
            Log.i("Error 3", e.getMessage());
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }
    };

    public PhoneAuthCredential verifyCode(String code) {
        return PhoneAuthProvider.getCredential(verificationId, code);
    }
}
