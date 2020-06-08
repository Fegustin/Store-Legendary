package com.shitshop.storeshits.ui.profile.register;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.shitshop.storeshits.R;

public class RegisterFragment extends Fragment {

    private String verificationId;

    // View
    private Toolbar toolbarBackToProfile;
    private TextInputLayout textInputLayoutNumber;
    private Button buttonSendCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        // View
        toolbarBackToProfile = root.findViewById(R.id.toolbar_back_profile);
        textInputLayoutNumber = root.findViewById(R.id.textInputLayoutNubmer);
        buttonSendCode = root.findViewById(R.id.buttonSendCode);


        // Event

        // Go to profile
        toolbarBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_global_navigation_profile);
            }
        });

        // Send code to verification fragment
        buttonSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateNumber()) {
                    return;
                }

                String number = textInputLayoutNumber.getEditText().getText().toString().trim();

                RegisterFragmentDirections.ActionRegisterFragmentToVerificationCodeFragment
                        action = RegisterFragmentDirections.actionRegisterFragmentToVerificationCodeFragment(number);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return root;
    }

    private boolean validateNumber() {
        String number = textInputLayoutNumber.getEditText().getText().toString().trim();

        if (number.isEmpty()) {
            textInputLayoutNumber.setError(getString(R.string.is_empty));
            return false;
        } else {
            textInputLayoutNumber.setError(null);
            return true;
        }
    }
}