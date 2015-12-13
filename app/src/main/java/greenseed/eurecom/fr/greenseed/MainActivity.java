package greenseed.eurecom.fr.greenseed;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.parse.ParseUser;

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

    public void logOut(View v){
        // Call the Parse log out method
        ParseUser.logOut();
        // Start and intent for the dispatch activity
        Intent intent = new Intent(MainActivity.this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
