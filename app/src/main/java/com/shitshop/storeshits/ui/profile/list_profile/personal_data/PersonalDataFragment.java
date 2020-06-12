package com.shitshop.storeshits.ui.profile.list_profile.personal_data;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shitshop.storeshits.R;

public class PersonalDataFragment extends Fragment implements PersonalDataView {

    // View
    private TextInputLayout textInputLayoutPDName;
    private TextInputLayout textInputLayoutPDSurname;
    private Button buttonPDSubmit;
    private PersonalDataPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Navigation.findNavController(getView()).navigate(R.id.action_global_navigation_profile);
        }

        View root = inflater.inflate(R.layout.fragment_personal_data, container, false);

        presenter = new PersonalDataPresenter(this);

        // View
        textInputLayoutPDName = root.findViewById(R.id.textInputLayoutPDName);
        textInputLayoutPDSurname = root.findViewById(R.id.textInputLayoutPDSurname);
        buttonPDSubmit = root.findViewById(R.id.buttonPDSubmit);

        // Event

        buttonPDSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate(textInputLayoutPDName) | !validate(textInputLayoutPDSurname)) {
                    return;
                }

                String name = textInputLayoutPDName.getEditText().getText().toString().trim();
                String surname = textInputLayoutPDSurname.getEditText().getText().toString().trim();

                presenter.savePersonalNameAndSurname(name + " " + surname);
            }
        });

        return root;
    }

    private boolean validate(TextInputLayout textInputLayout) {
        String name = textInputLayout.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            textInputLayout.setError(getString(R.string.is_empty));
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    @Override
    public void toastPerformed() {
        Toast.makeText(getActivity(), R.string.changed_personal_name, Toast.LENGTH_SHORT).show();
    }
}