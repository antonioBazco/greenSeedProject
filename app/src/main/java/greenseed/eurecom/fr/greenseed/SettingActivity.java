package greenseed.eurecom.fr.greenseed;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SettingActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    CheckBox askCheckBox;
    CheckBox ask2CheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        askCheckBox = (CheckBox) findViewById(R.id.sharing_checkbox);
        ask2CheckBox = (CheckBox) findViewById(R.id.sharingDef_checkbox);
        Button paymentMethodButton = (Button) findViewById(R.id.payment_button);

        EditText username = (EditText) findViewById(R.id.username_edit_text);

        username.setText(ParseUser.getCurrentUser().getUsername());
        prefs = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        editor = prefs.edit();

        boolean t = checkCheckBox(prefs);

        askCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (askCheckBox.isChecked()) {
                    editor.putBoolean("askAgain", true);
                } else {
                    editor.putBoolean("askAgain", false);
                }
                editor.commit();
                boolean t = checkCheckBox(prefs);
            }
        });

        ask2CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ask2CheckBox.isChecked()) {
                    editor.putBoolean("shareAsDefault", true);
                } else {
                    editor.putBoolean("shareAsDefault", false);
                }
                editor.commit();
            }
        });
    }

    private boolean checkCheckBox (SharedPreferences prefs) {
        if (prefs.getBoolean("askAgain", true)){
            askCheckBox.setChecked(true);
            ask2CheckBox.setVisibility(View.GONE);
        } else {
            askCheckBox.setChecked(false);
            ask2CheckBox.setVisibility(View.VISIBLE);
            if (prefs.getBoolean("shareAsDefault", true)){
                ask2CheckBox.setChecked(true);
            } else {
                ask2CheckBox.setChecked(false);
            }
        }
        return true;
    }

    public void showMethods (View v) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.paypal,
        (ViewGroup) findViewById(R.id.relativeLayout1));

        AlertDialog.Builder paypal = new AlertDialog.Builder(this);
        paypal.setView(view);
        paypal.setPositiveButton("Continue",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }
        );

        AlertDialog dialogPaypal = paypal.create();
        dialogPaypal.show();

        Button b = dialogPaypal.getButton(DialogInterface.BUTTON_POSITIVE);
        if(b != null)
        b.setTextColor(Color.parseColor("#66CC00"));
    }

    public void saveSettings(View v) {
        // Set up a new Parse user
        ParseUser user = ParseUser.getCurrentUser();

        EditText username = (EditText) findViewById(R.id.username_edit_text);
        EditText passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        EditText passwordAgainEditText = (EditText) findViewById(R.id.password_again_edit_text);

        String newUsername = username.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();

        System.out.println("www  "+newUsername);
        System.out.println("www  "+username.getText());
        System.out.println("www  "+newUsername.trim());

        // Validate the sign up data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
        if (newUsername.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }
        System.out.println();
        System.out.println(password.length());
        System.out.println();

        if (password.length() != 0) {
            if (password.length() < 8) {
                if (validationError) {
                    validationErrorMessage.append(getString(R.string.error_join));
                }
                validationError = true;
                validationErrorMessage.append(getString(R.string.error_small_password));
            }
            if (!password.equals(passwordAgain)) {
                if (validationError) {
                    validationErrorMessage.append(getString(R.string.error_join));
                }
                validationError = true;
                validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
            }
        }

        validationErrorMessage.append(getString(R.string.error_end));
        System.out.println("fsdfsdf\n\nsdfsdf " + validationError);
        System.out.println("fsdfsdf\n\nsdfsdf " + newUsername);
        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(SettingActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
//            return;
            Intent intent = new Intent(SettingActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(SettingActivity.this);
        dialog.setMessage(getString(R.string.progress_signup));

        user.setUsername(newUsername);
        if (password.length() != 0) {
            user.setPassword(password);
        }

        // Call the Parse signup method
        user.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SettingActivity.this, "Profile updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SettingActivity.this, AnimActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
