package greenseed.eurecom.fr.greenseed;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

public class ShareOnFacebook extends AppCompatActivity implements View.OnClickListener {

    ImageButton press;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_on_facebook);
        press=(ImageButton) findViewById(R.id.imageButton);
        press.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imageButton)
        {
            String urlToShare = "https://www.facebook.com/dialog/share?app_id=145634995501895&display=popup&href=http://antoniobazco.github.io/&redirect_uri=https%3A%2F%2Fdevelopers.facebook.com%2Ftools%2Fexplorer";
            //https://www.facebook.com/dialog/feed?app_id=145634995501895&display=popup&caption=An%20example%20caption&link=http://antoniobazco.github.io/&redirect_uri=http://antoniobazco.github.io/

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
            intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

// See if official Facebook app is found
            boolean facebookAppFound = false;
            List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("greenseed.eurecom.fr.greenseed")) {
                    intent.setPackage(info.activityInfo.packageName);
                    facebookAppFound = true;
                    break;
                }
            }

// As fallback, launch sharer.php in a browser
            if (!facebookAppFound) {
                String sharerUrl =urlToShare;
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            }

            startActivity(intent);
        }
    }

}
