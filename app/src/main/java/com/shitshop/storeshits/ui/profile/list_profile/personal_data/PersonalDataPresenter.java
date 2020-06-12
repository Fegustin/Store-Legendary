package com.shitshop.storeshits.ui.profile.list_profile.personal_data;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class PersonalDataPresenter {

    private PersonalDataView view;

    private FirebaseUser user;

    public PersonalDataPresenter(PersonalDataView view) {
        this.view = view;
    }

    public void savePersonalNameAndSurname(String username) {
        user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder ()
                .setDisplayName(username)
                .build ();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        view.toastPerformed();
                    }
                });
    }
}
