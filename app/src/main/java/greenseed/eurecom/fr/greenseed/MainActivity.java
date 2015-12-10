package greenseed.eurecom.fr.greenseed;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToDonate(View v){
        startActivity(new Intent(MainActivity.this, DonationListActivity.class));
    }

    public void goToAnimActivity(View v){
        startActivity(new Intent(MainActivity.this, AnimActivity.class));
    }
}
