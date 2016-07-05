package com.ravijain.sankalp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpUser;
import com.ravijain.sankalp.data.SpContentProvider;

public class SpUserSetupFragment extends Fragment {

    // UI references.
    private EditText mEmailView;
    private EditText mNameView;
    private EditText mMobileView;
    private View mProgressView;
    private View mUserSetUpView;

    private UserRegisterTask mRegisterTask = null;

    public SpUserSetupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_setup, container, false);

        mNameView = (EditText) view.findViewById(R.id.userName);
        mMobileView = (EditText) view.findViewById(R.id.userMobile);
        mEmailView = (EditText) view.findViewById(R.id.userEmail);

        Button mEmailSignInButton = (Button) view.findViewById(R.id.userRegister_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _registerUser();
            }
        });

        mUserSetUpView = view.findViewById(R.id.userSetup_form);
        mProgressView = view.findViewById(R.id.userSetup_progress);

        return view;
    }

    private void _registerUser() {
        {
            if (mRegisterTask != null) {
                return;
            }

            // Reset errors.
            mEmailView.setError(null);
            mMobileView.setError(null);
            mNameView.setError(null);

            // Store values at the time of the login attempt.
            String name = mNameView.getText().toString();
            String mobile = mMobileView.getText().toString();
            String email = mEmailView.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid email address.
            if (TextUtils.isEmpty(name)) {
                mNameView.setError(getString(R.string.error_field_required));
                focusView = mNameView;
                cancel = true;
            } else if (!_isNameValid(name)) {
                mNameView.setError(getString(R.string.error_invalid_email));
                focusView = mNameView;
                cancel = true;
            }
            if (TextUtils.isEmpty(mobile)) {
                mMobileView.setError(getString(R.string.error_field_required));
                focusView = mMobileView;
                cancel = true;
            } else if (!_isMobileValid(mobile)) {
                mMobileView.setError(getString(R.string.error_invalid_email));
                focusView = mMobileView;
                cancel = true;
            }
            if (TextUtils.isEmpty(email)) {
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            } else if (!_isEmailValid(email)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                _showProgress(true);
                mRegisterTask = new UserRegisterTask(new SpUser(name, mobile, email));
                mRegisterTask.execute((Void) null);
            }
        }
    }

    private boolean _isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean _isNameValid(String name) {
        //TODO: Replace this with your own logic
        return true;
    }
    private boolean _isMobileValid(String mobile) {
        //TODO: Replace this with your own logic
        return true;
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

            mUserSetUpView.setVisibility(show ? View.GONE : View.VISIBLE);
            mUserSetUpView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mUserSetUpView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mUserSetUpView.setVisibility(show ? View.GONE : View.VISIBLE);
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

            SpContentProvider provider = SpContentProvider.getInstance(getContext());
            provider.addUser(_user);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            _showProgress(false);

            if (success) {
                Toast.makeText(getContext(), "Registration Complete", Toast.LENGTH_SHORT).show();
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
