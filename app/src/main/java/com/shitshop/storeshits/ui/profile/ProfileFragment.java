package com.shitshop.storeshits.ui.profile;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shitshop.storeshits.R;

public class ProfileFragment extends Fragment {

    // Firebase
    private FirebaseAuth mAuth;

    //View
    private TextInputLayout textInputLayoutLoginPhone;
    private Button buttonLogin;
    private TextView textViewTitleRegister;
    private TextView textViewProfile;
    private ConstraintLayout constraintLayoutData;
    private ConstraintLayout constraintLayoutFavorite;
    private ConstraintLayout constraintLayoutFeedback;
    private ConstraintLayout constraintLayoutLogOut;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // View
        textInputLayoutLoginPhone = root.findViewById(R.id.textInputLayoutLoginPhone);
        buttonLogin = root.findViewById(R.id.buttonLogin);
        textViewTitleRegister = root.findViewById(R.id.textViewTitleRegister);
        textViewProfile = root.findViewById(R.id.textViewProfile);
        constraintLayoutData = root.findViewById(R.id.constraintLayoutData);
        constraintLayoutFavorite = root.findViewById(R.id.constraintLayoutFavorite);
        constraintLayoutFeedback = root.findViewById(R.id.constraintLayoutFeedback);
        constraintLayoutLogOut = root.findViewById(R.id.constraintLayoutLogOut);


        if (currentUser == null) {
            visibilityProfile(View.GONE);
            visibilityLogin(View.VISIBLE);
        } else {
            visibilityProfile(View.VISIBLE);
            visibilityLogin(View.GONE);
            textViewProfile.setText(currentUser.getDisplayName());
        }

        // Event

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateRNumber()) {
                    return;
                }

                String number = textInputLayoutLoginPhone.getEditText().getText().toString().trim();

                ProfileFragmentDirections.ActionNavigationProfileToVerificationCodeFragment
                        action = ProfileFragmentDirections.actionNavigationProfileToVerificationCodeFragment(number);
                Navigation.findNavController(v).navigate(action);
            }
        });

        constraintLayoutData.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.personal_data_fragment, null));

        constraintLayoutFavorite.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.favorites_fragment, null));

        constraintLayoutFeedback.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.feedback_fragment, null));

        constraintLayoutLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Navigation.findNavController(v).navigate(R.id.action_global_navigation_profile);
            }
        });

        return root;
    }

    private boolean validateRNumber() {
        String number = textInputLayoutLoginPhone.getEditText().getText().toString().trim();

        if (number.isEmpty()) {
            textInputLayoutLoginPhone.setError(getString(R.string.is_empty));
            return false;
        } else {
            textInputLayoutLoginPhone.setError(null);
            return true;
        }
    }

    private void visibilityProfile(int view) {
        constraintLayoutData.setVisibility(view);
        constraintLayoutFavorite.setVisibility(view);
        constraintLayoutFeedback.setVisibility(view);
        constraintLayoutLogOut.setVisibility(view);
    }

    private void visibilityLogin(int view) {
        textInputLayoutLoginPhone.setVisibility(view);
        buttonLogin.setVisibility(view);
        textViewTitleRegister.setVisibility(view);
    }
}