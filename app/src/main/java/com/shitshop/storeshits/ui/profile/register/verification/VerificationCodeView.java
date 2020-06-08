package com.shitshop.storeshits.ui.profile.register.verification;

import com.google.firebase.auth.PhoneAuthCredential;

public interface VerificationCodeView {
    void toastMessage(String text);
    void editText(String code);
    void progressBarVisible();
    void progressBarGone();
    void failedVerificationCode();
}
