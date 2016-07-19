package com.ravijain.sankalp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpUser;

public class SpUserSetupActivity extends AppCompatActivity {

    // UI references.
    private EditText _mEmailView, _mNameView, _mMobileView;
    private TextInputLayout _inputLayoutName, _inputLayoutEmail, _inputLayoutMobile;

    private View _mProgressView;
    private View _mUserSetUpView;

    private UserRegisterTask mRegisterTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        _mNameView = (EditText) findViewById(R.id.userName);
        _mMobileView = (EditText) findViewById(R.id.userMobile);
        _mEmailView = (EditText) findViewById(R.id.userEmail);

        _inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        _inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        _inputLayoutMobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);

        _mNameView.addTextChangedListener(new FormInputTextWatcher(_mNameView));
        _mMobileView.addTextChangedListener(new FormInputTextWatcher(_mMobileView));
        _mEmailView.addTextChangedListener(new FormInputTextWatcher(_mEmailView));

        Button mEmailSignInButton = (Button) findViewById(R.id.userRegister_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _registerUser();
            }
        });

        _mUserSetUpView = findViewById(R.id.userSetup_form);
        _mProgressView = findViewById(R.id.userSetup_progress);
    }

    private void _registerUser() {
        {
            if (mRegisterTask != null) {
                return;
            }

            if (!_validateName()) {
                return;
            }

            if (!_validateEmail()) {
                return;
            }

            if (!_validateMobile()) {
                return;
            }

            // Reset errors.
            _mEmailView.setError(null);
            _mMobileView.setError(null);
            _mNameView.setError(null);

            // Store values at the time of the login attempt.
            String name = _mNameView.getText().toString();
            String mobile = _mMobileView.getText().toString();
            String email = _mEmailView.getText().toString();

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            _showProgress(true);
            mRegisterTask = new UserRegisterTask(new SpUser(name, mobile, email));
            mRegisterTask.execute((Void) null);
        }
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
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void _handleSuccessfulRegistration() {
        Toast.makeText(getApplicationContext(), "Registration Complete", Toast.LENGTH_SHORT).show();
        NavUtils.navigateUpFromSameTask(this);
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
            // TODO: attempt authentication against a network service.

            SpContentProvider provider = SpContentProvider.getInstance(getApplicationContext());
            //provider.addUser(_user);
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
