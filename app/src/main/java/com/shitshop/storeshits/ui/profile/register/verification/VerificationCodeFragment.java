package com.shitshop.storeshits.ui.profile.register.verification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.shitshop.storeshits.R;


public class VerificationCodeFragment extends Fragment implements VerificationCodeView {

    private VerificationCodePresenter presenter;
    private FirebaseAuth auth;

    // View
    private TextInputLayout textInputLayoutCodeSMS;
    private TextInputLayout textInputLayoutLFName;
    private TextInputLayout textInputLayoutVPassword;
    private TextInputLayout textInputLayoutVPasswordR;
    private Button buttonVerificationDate;
    private ProgressBar progressBarCode;
    private Toolbar toolbarBackToPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_verification_code, container, false);

        auth = FirebaseAuth.getInstance();

        // Presenter
        presenter = new VerificationCodePresenter(this, requireActivity());

        // View
        textInputLayoutCodeSMS = root.findViewById(R.id.textInputLayoutCodeSMS);
        textInputLayoutLFName = root.findViewById(R.id.textInputLayoutLFName);
        textInputLayoutVPassword = root.findViewById(R.id.textInputLayoutVPassword);
        textInputLayoutVPasswordR = root.findViewById(R.id.textInputLayoutVPasswordR);
        buttonVerificationDate = root.findViewById(R.id.buttonVerificationDate);
        progressBarCode = root.findViewById(R.id.progressBarCode);
        toolbarBackToPhone = root.findViewById(R.id.toolbar_back_phone);


        // Event

        // Get phone from RegisterFragment
        VerificationCodeFragmentArgs args = VerificationCodeFragmentArgs.fromBundle(getArguments());
        presenter.sendVerificationCode(args.getNumber());

        // Send Date to firebase
        buttonVerificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (!verificationCodeSMS() | !verificationFLName() | !verificationPassword() | !verificationVPasswordR()) {
                    return;
                }

                auth.signInWithCredential(presenter.verifyCode(textInputLayoutCodeSMS.getEditText().getText().toString().trim()))
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = task.getResult().getUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(textInputLayoutLFName.getEditText().getText().toString().trim())
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.i("UserName", "Complete");
                                                }
                                            });

                                    user.updatePassword(textInputLayoutVPassword.getEditText().getText().toString().trim())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.i("UserPassword", "Complete");
                                                }
                                            });

                                    Navigation.findNavController(v).navigate(R.id.action_global_navigation_profile);
                                } else {
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        textInputLayoutCodeSMS.setError(getString(R.string.verification_send_code));
                                    }
                                }
                            }
                        });
            }
        });

        // Go to phone page
        toolbarBackToPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_verification_code_fragment_to_register_Fragment);
            }
        });

        return root;
    }

    private boolean verificationCodeSMS() {
        String code = textInputLayoutCodeSMS.getEditText().getText().toString().trim();

        if (code.isEmpty()) {
            textInputLayoutCodeSMS.setError(getString(R.string.is_empty));
            return false;
        } else {
            textInputLayoutCodeSMS.setError(null);
            return true;
        }
    }

    private boolean verificationFLName() {
        String name = textInputLayoutLFName.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            textInputLayoutLFName.setError(getString(R.string.is_empty));
            return false;
        } else {
            textInputLayoutLFName.setError(null);
            return true;
        }
    }

    private boolean verificationPassword() {
        String password = textInputLayoutVPassword.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            textInputLayoutVPassword.setError(getString(R.string.is_empty));
            return false;
        } else if (password.length() < 6) {
            textInputLayoutVPassword.setError(getString(R.string.little_password));
            return false;
        } else {
            textInputLayoutVPassword.setError(null);
            return true;
        }
    }

    private boolean verificationVPasswordR() {
        String password = textInputLayoutVPassword.getEditText().getText().toString().trim();
        String passwordR = textInputLayoutVPasswordR.getEditText().getText().toString().trim();

        Log.i("password", password);
        Log.i("password", passwordR);

        if (passwordR.isEmpty()) {
            textInputLayoutVPasswordR.setError(getString(R.string.is_empty));
            return false;
        } else if (!passwordR.equals(password)) {
            textInputLayoutVPasswordR.setError(getString(R.string.dont_repeat_password));
            return false;
        } else {
            textInputLayoutVPasswordR.setError(null);
            return true;
        }
    }

    @Override
    public void toastMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void editText(String code) {
        textInputLayoutCodeSMS.getEditText().setText(code);
    }

    @Override
    public void progressBarVisible() {
        progressBarCode.setVisibility(View.VISIBLE);
    }

    @Override
    public void progressBarGone() {
        progressBarCode.setVisibility(View.GONE);
    }

    @Override
    public void failedVerificationCode() {
        Navigation.findNavController(getView()).navigate(R.id.action_verification_code_fragment_to_register_Fragment);
    }
}