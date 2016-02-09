package greenseed.eurecom.fr.greenseed;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final CheckBox askCheckBox;

        askCheckBox = (CheckBox) findViewById(R.id.sharing_checkbox);

        SharedPreferences prefs = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        editor = prefs.edit();

        if (prefs.getBoolean("askAgain", true)){
            askCheckBox.setChecked(true);
        } else {
            askCheckBox.setChecked(false);
        }

        askCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (askCheckBox.isChecked()) {
                    editor.putBoolean("askAgain",true);
                } else {
                    editor.putBoolean("askAgain", false);
                }
                editor.commit();
            }
        });
    }

}
