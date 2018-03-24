package com.frederictech.eventiquette;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeNoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button btnChooseSignIn = (Button) findViewById(R.id.btn_choose_sign_in);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            LaunchMapActivity();
        } else {
            // not signed in
            btnChooseSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.FacebookBuilder().build()))
                                    .setTosUrl("https://superapp.example.com/terms-of-service.html")
                                    .setPrivacyPolicyUrl("https://superapp.example.com/privacy-policy.html")
                                    .setTheme(R.style.SignInTheme)
                                    .build(),
                            RC_SIGN_IN);
                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                LaunchMapActivity();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Snackbar.make(findViewById(R.id.view_sign_in_activity), "Sign In Cancelled", Snackbar.LENGTH_SHORT).show();
                    return;
                }else

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(findViewById(R.id.view_sign_in_activity), "No Internet Connection", Snackbar.LENGTH_SHORT).show();
                    return;
                }else{
                    Snackbar.make(findViewById(R.id.view_sign_in_activity), "Unknown Error", Snackbar.LENGTH_SHORT).show();
                    Log.e("LOGIN ERROR", "Sign-in error: ", response.getError());
                }
            }
        }
    }

    private void LaunchMapActivity(){
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }
}
