package com.ravijain.sankalp.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.activities.SpMainActivity;
import com.ravijain.sankalp.activities.SpUserSetupActivity;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpUserProfileFragment extends Fragment {

    // UI references.
    private EditText _mEmailView, _mNameView, _mMobileView, _mCityView;
    private TextInputLayout _inputLayoutName, _inputLayoutEmail, _inputLayoutMobile, _inputLayoutCity;

    private View _mProgressView;
    private View _mUserSetUpView;

    private boolean _isUserAlreadyCreated = false;
    private SpUser _loadedUser;

    private UserRegisterTask mRegisterTask = null;

    public SpUserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        if (getArguments() != null) {
            Boolean b = getArguments().getBoolean(SpConstants.IS_USER_ALREADY_CREATED);
            if (b != null) _isUserAlreadyCreated = b.booleanValue();
        }

        setHasOptionsMenu(true);
        _mNameView = (EditText) v.findViewById(R.id.userName);
        _mMobileView = (EditText) v.findViewById(R.id.userMobile);
        _mEmailView = (EditText) v.findViewById(R.id.userEmail);
        _mCityView = (EditText) v.findViewById(R.id.userCity);

        _inputLayoutName = (TextInputLayout) v.findViewById(R.id.input_layout_name);
        _inputLayoutEmail = (TextInputLayout) v.findViewById(R.id.input_layout_email);
        _inputLayoutMobile = (TextInputLayout) v.findViewById(R.id.input_layout_mobile);
        _inputLayoutCity = (TextInputLayout) v.findViewById(R.id.input_layout_city);

        _mNameView.addTextChangedListener(new FormInputTextWatcher(_mNameView));
        _mMobileView.addTextChangedListener(new FormInputTextWatcher(_mMobileView));
        _mEmailView.addTextChangedListener(new FormInputTextWatcher(_mEmailView));
        _mCityView.addTextChangedListener(new FormInputTextWatcher(_mCityView));

        /*Button mEmailSignInButton = (Button) v.findViewById(R.id.userRegister_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _registerUser();
            }
        });*/

        _mUserSetUpView = v.findViewById(R.id.userSetup_form);
        _mProgressView = v.findViewById(R.id.userSetup_progress);

        if (_isUserAlreadyCreated) {
            _loadedUser = SpContentProvider.getInstance(getContext()).getUser();
            String name = _loadedUser.getName();
            String mobile = _loadedUser.getMobile();
            String email = _loadedUser.getEmail();
            String city = _loadedUser.getCity();
            _mNameView.setText(name);
            _mMobileView.setText(mobile);
            if (email != null) {
                _mEmailView.setText(email);
            }
            if (city != null) {
                _mCityView.setText(city);
            }
        }
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_profile, menu);
        if (_isUserAlreadyCreated) {
            menu.findItem(R.id.action_registerUser).setVisible(false);
        } else {
            menu.findItem(R.id.action_updateProfile).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_updateProfile) {
            _updateUser();
            return true;
        } else if (id == R.id.action_registerUser) {
            _registerUser();
        }

        return super.onOptionsItemSelected(item);
    }

    private void _updateUser() {
        SpUser u = _getCurrentUser();
        if (!u.equals(_loadedUser)) {
            u.setId(_loadedUser.getId());
            mRegisterTask = new UserRegisterTask(u);
            mRegisterTask.execute((Void) null);
        }
    }

    private void _registerUser() {
        {
            if (mRegisterTask != null) {
                return;
            }

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            _showProgress(true);
            SpUser u = _getCurrentUser();
            if (u != null) {
                mRegisterTask = new UserRegisterTask(u);
                mRegisterTask.execute((Void) null);
            }
        }
    }

    private SpUser _getCurrentUser() {
        if (!_validateName()) {
            return null;
        }

        if (!_validateEmail()) {
            return null;
        }

        if (!_validateMobile()) {
            return null;
        }

        // Reset errors.
        _mEmailView.setError(null);
        _mMobileView.setError(null);
        _mNameView.setError(null);
        _mCityView.setError(null);

        // Store values at the time of the login attempt.
        String name = _mNameView.getText().toString();
        String mobile = _mMobileView.getText().toString();
        String email = _mEmailView.getText().toString();
        String city = _mCityView.getText().toString();

        return new SpUser(name, mobile, email, city);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void _showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            _mUserSetUpView.setVisibility(show ? View.GONE : View.VISIBLE);
            _mUserSetUpView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    _mUserSetUpView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            _mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            _mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    _mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            _mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            _mUserSetUpView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean _validateName() {
        if (_mNameView.getText().toString().trim().isEmpty()) {
            _inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(_mNameView);
            return false;
        } else {
            _inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean _validateEmail() {
        String email = _mEmailView.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            _inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(_mEmailView);
            return false;
        } else {
            _inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean _validateMobile() {
        if (_mMobileView.getText().toString().trim().isEmpty()) {
            _inputLayoutMobile.setError(getString(R.string.err_msg_mobile));
            requestFocus(_mMobileView);
            return false;
        } else {
            _inputLayoutMobile.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void _handleSuccessfulRegistration() {
        //Toast.makeText(getActivity(), "Registration Complete", Toast.LENGTH_SHORT).show();
        //NavUtils.navigateUpFromSameTask(this);
        Intent intent = new Intent(getContext(), SpMainActivity.class);
        startActivity(intent);
    }

    private class FormInputTextWatcher implements TextWatcher {

        private View view;

        private FormInputTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.userName:
                    _validateName();
                    break;
                case R.id.userEmail:
                    _validateEmail();
                    break;
                case R.id.userMobile:
                    _validateMobile();
                    break;
            }
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private SpUser _user;

        UserRegisterTask(SpUser user) {
            _user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            SpContentProvider provider = SpContentProvider.getInstance(getContext());
            if (_isUserAlreadyCreated) {
                provider.updateUser(_user);
            }
            else {
                provider.addUser(_user);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            _showProgress(false);

            if (success) {
                _handleSuccessfulRegistration();
            } else {
                // Error
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            _showProgress(false);
        }
    }

}
