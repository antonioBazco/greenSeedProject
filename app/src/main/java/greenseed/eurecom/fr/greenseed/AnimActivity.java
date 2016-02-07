package greenseed.eurecom.fr.greenseed;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AnimActivity extends AppCompatActivity {
    List<Project> projectList;
    private int averagePayment;
    private Project breakingProject;
    private Payment payment;


    AnimatorSet amnestyFlowerAnimatorSet;
    AnimatorSet msfFlowerAnimatorSet;
    AnimatorSet unicefFlowerAnimatorSet;
    AnimatorSet hotairBalloonAnimatorSet;
    AnimatorSet smallCloud1AnimatorSet;
    AnimatorSet smallCloud2AnimatorSet;
    AnimatorSet smallCloud3AnimatorSet;
    AnimatorSet sunraysAnimatorSet;
    ImageView amnestySeedView;
    ImageView msfSeedView;
    ImageView unicefSeedView;
    ImageView plantingGroundView;
    String msg = "DRAG MSG";
    int senderSeed;

    private android.widget.RelativeLayout.LayoutParams amnestyFlowerLayoutParams;
    private android.widget.RelativeLayout.LayoutParams msfFlowerLayoutParams;
    private android.widget.RelativeLayout.LayoutParams unicefFlowerLayoutParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent(); // gets the previously created intent
        String addSeed = myIntent.getStringExtra("addSeed"); // will return "FirstKeyValue"
        setContentView(R.layout.activity_anim);

        //things changed
        ParseObject.registerSubclass(Organization.class);
        ParseObject.registerSubclass(Project.class);
        ParseObject.registerSubclass(Payment.class);
        getBreakingProject();

        // find current minute of day to set animation offset
        /*
        Calendar CurrentDateTime = Calendar.getInstance();
        int hours = CurrentDateTime.get(Calendar.HOUR_OF_DAY);
        int minutes = hours * 60 + CurrentDateTime.get(Calendar.MINUTE);
        float startOffset = (float) minutes / (60 * 24);

        Log.d("OFFSET: ", String.valueOf(-(long) (startOffset * 60000)));
        */

        // amnestyFlower animation configuration
        final ImageView amnestyFlower = (ImageView) findViewById(R.id.amnesty_flower);
        amnestyFlowerAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flower_movement);
        amnestyFlowerAnimatorSet.setTarget(amnestyFlower);
        /****************************************/
        // msfFlower animation configuration
        final ImageView msfFlower = (ImageView) findViewById(R.id.msf_flower);
        msfFlowerAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flower_movement);
        msfFlowerAnimatorSet.setTarget(msfFlower);
        /****************************************/
        // msfFlower animation configuration
        final ImageView unicefFlower = (ImageView) findViewById(R.id.unicef_flower);
        unicefFlowerAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flower_movement);
        unicefFlowerAnimatorSet.setTarget(unicefFlower);
        /****************************************/

        final ImageView hotairBalloon = (ImageView) findViewById(R.id.hotair_amnesty);
        hotairBalloonAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.balloon_movement);
        hotairBalloonAnimatorSet.setTarget(hotairBalloon);
        hotairBalloonAnimatorSet.start();

        ImageView smallCloud1 = (ImageView) findViewById(R.id.cloud1);
        smallCloud1AnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.cloud_movement);
        smallCloud1AnimatorSet.setTarget(smallCloud1);
        smallCloud1AnimatorSet.start();

        ImageView smallCloud2 = (ImageView) findViewById(R.id.cloud2);
        smallCloud2AnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.cloud_movement2);
        smallCloud2AnimatorSet.setTarget(smallCloud2);
        smallCloud2AnimatorSet.start();

        ImageView smallCloud3 = (ImageView) findViewById(R.id.cloud3);
        smallCloud3AnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.cloud_movement3);
        smallCloud3AnimatorSet.setTarget(smallCloud3);
        smallCloud3AnimatorSet.start();

        final ImageView sunrays = (ImageView) findViewById(R.id.sunrays);
        sunraysAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.sun_movement);
        sunraysAnimatorSet.setTarget(sunrays);
        sunraysAnimatorSet.start();

        amnestySeedView = (ImageView) findViewById(R.id.amnesty_seed);
        msfSeedView = (ImageView) findViewById(R.id.msf_seed);
        unicefSeedView = (ImageView) findViewById(R.id.unicef_seed);

        plantingGroundView = (ImageView) findViewById(R.id.planting_ground);


        amnestySeedView.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                        ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(amnestySeedView);

                        v.startDrag(dragData, myShadow, null, 0);
                        Log.d(msg, "ON LONG CLICK: Checking if event is for us " + String.valueOf(v.getId()) + "sjalabajs "
                                + amnestySeedView.getId());

                        return true;
                    }
                });

        amnestySeedView.setOnDragListener(
                new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Log.d(msg, "Checking if event is for us " + String.valueOf(v.getId()) + "sjalabajs "
                                + amnestySeedView.getId() + " ROMPELO " + event.getLocalState().toString());


                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                //Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED amnestySeed");
                                if (senderSeed == 1)
                                    amnestySeedView.setVisibility(View.INVISIBLE);
                                //layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                                break;

                            case DragEvent.ACTION_DROP:
                                Log.d(msg, "ACTION_DROP event is " + String.valueOf(event.getResult()) + "at X: " + String.valueOf(event.getX()) + " Y: " + String.valueOf(event.getY()));
                                if (senderSeed == 1) {
                                    if (event.getResult()) {
                                        amnestySeedView.setVisibility(View.GONE);
                                    } else {
                                        amnestySeedView.setVisibility(View.VISIBLE);
                                    }// Do nothing
                                }
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                }

        );

        amnestySeedView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(amnestySeedView);
                            amnestySeedView.startDrag(data, shadowBuilder, amnestySeedView, 0);
                            //amnestySeedView.setVisibility(View.INVISIBLE);
                            Log.d(msg, "ON TOUCH: Checking if event is for us " + String.valueOf(v.getId()) + "sjalabajs "
                                    + amnestySeedView.getId());
                            Log.d(msg, "SENDERSEED = 1");
                            senderSeed = 1;
                            return true;
                        } else {
                            return false;
                        }
                    }
                }

        );

        msfSeedView.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                        ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(msfSeedView);

                        v.startDrag(dragData, myShadow, null, 0);
                        Log.d(msg, "ON LONG CLICK: Checking if event is for us " + String.valueOf(v.getId()) + "sjalabajs "
                                + msfSeedView.getId());

                        return true;
                    }
                });

        msfSeedView.setOnDragListener(
                new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {

                        Log.d(msg, "Checking if event is for us " + String.valueOf(v.getId()) + "sjalabajs "
                                + msfSeedView.getId() + " ROMPELO " + event.getLocalState().toString());


                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED msfSeed");
                                if (senderSeed == 2)
                                    msfSeedView.setVisibility(View.INVISIBLE);

                                //layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                                break;

                            case DragEvent.ACTION_DROP:
                                //Log.d(msg, "ACTION_DROP event" + "at X: " + String.valueOf(event.getX()) + " Y: " + String.valueOf(event.getY()));
                                if (senderSeed == 2) {
                                    if (event.getResult()) {
                                        msfSeedView.setVisibility(View.GONE);
                                    } else {
                                        msfSeedView.setVisibility(View.VISIBLE);
                                    }
                                }

                                // Do nothing
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                }

        );

        msfSeedView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(msfSeedView);
                            msfSeedView.startDrag(data, shadowBuilder, msfSeedView, 0);
                            //msfSeedView.setVisibility(View.INVISIBLE);
                            Log.d(msg, "ON TOUCH: Checking if event is for us " + String.valueOf(v.getId()) + "sjalabajs "
                                    + msfSeedView.getId());
                            Log.d(msg, "SENDERSEED = 2");
                            senderSeed = 2;
                            return true;
                        } else {
                            return false;
                        }
                    }
                }

        );

        unicefSeedView.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                        ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(unicefSeedView);

                        v.startDrag(dragData, myShadow, null, 0);
                        Log.d(msg, "ON LONG CLICK: Checking if event is for us " + String.valueOf(v.getId()) + "sjalabajs "
                                + unicefSeedView.getId());

                        return true;
                    }
                });

        unicefSeedView.setOnDragListener(
                new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {

                        Log.d(msg, "Checking if event is for us " + String.valueOf(v.getId()) + "sjalabajs "
                                + unicefSeedView.getId() + " ROMPELO " + event.getLocalState().toString());


                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                //Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED unicefSeed");
                                if (senderSeed == 3)
                                    unicefSeedView.setVisibility(View.INVISIBLE);

                                //layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                                break;

                            case DragEvent.ACTION_DROP:
                                Log.d(msg, "ACTION_DROP event" + "at X: " + String.valueOf(event.getX()) + " Y: " + String.valueOf(event.getY()));
                                if (senderSeed == 3) {
                                    if (event.getResult()) {
                                        unicefSeedView.setVisibility(View.GONE);
                                    } else {
                                        unicefSeedView.setVisibility(View.VISIBLE);
                                    }
                                }

                                // Do nothing
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                }

        );

        unicefSeedView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(unicefSeedView);
                            unicefSeedView.startDrag(data, shadowBuilder, unicefSeedView, 0);
                            //unicefSeedView.setVisibility(View.INVISIBLE);
                            Log.d(msg, "ON TOUCH: Checking if event is for us " + String.valueOf(v.getId()) + "sjalabajs "
                                    + unicefSeedView.getId());
                            Log.d(msg, "SENDERSEED = 3");
                            senderSeed = 3;
                            return true;
                        } else {
                            return false;
                        }
                    }
                }

        );

        plantingGroundView.setOnDragListener(new View.OnDragListener()

        {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.d(msg, "Checking if event is for us " + String.valueOf(v.getId()) + "sjalabajs "
                        + plantingGroundView.getId() + " ROMPELO " + event.getLocalState().toString());


                switch (event.getAction()) {

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED by plantground");
                        if (!event.getResult()) {
                            if (senderSeed == 1) {
                                Log.d(msg, "amnestySeed");
                                final ImageView amnestySeed = (ImageView) findViewById(R.id.amnesty_seed);
                                amnestySeed.setVisibility(View.VISIBLE);

                            } else if (senderSeed == 2) {
                                Log.d(msg, "msfSeed");
                                final ImageView msfSeed = (ImageView) findViewById(R.id.msf_seed);
                                msfSeed.setVisibility(View.VISIBLE);
                            } else if (senderSeed == 3) {
                                Log.d(msg, "unicefSeed");
                                final ImageView unicefSeed = (ImageView) findViewById(R.id.unicef_seed);
                                unicefSeed.setVisibility(View.VISIBLE);
                            }
                        }
                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:

                        int plantOffsetX = (int) plantingGroundView.getX();
                        int plantOffsetY = (int) plantingGroundView.getY();
                        Log.d(msg, "ACTION_DROP event  by POT " + String.valueOf(v.getId()) + " sent at X: " + String.valueOf(event.getX()) + " Y: " + String.valueOf(event.getY())
                                + "\n X and Y offsets: " + String.valueOf(plantOffsetX) + ", " + String.valueOf(plantOffsetY));
                        if (senderSeed == 1) {
                            Log.d(msg, "amnestySeed");
                            final ImageView amnestyFlower = (ImageView) findViewById(R.id.amnesty_flower);
                            amnestyFlower.setVisibility(View.VISIBLE);
                            amnestyFlowerLayoutParams = (RelativeLayout.LayoutParams) amnestyFlower.getLayoutParams();
                            amnestyFlowerLayoutParams.leftMargin = (int) event.getX() + plantOffsetX - 350;
                            //amnestyFlowerLayoutParams.topMargin = (int) event.getY() + plantOffsetY - 250;
                            amnestyFlower.setLayoutParams(amnestyFlowerLayoutParams);
                            amnestyFlowerAnimatorSet.start();
                        } else if (senderSeed == 2) {
                            Log.d(msg, "msfSeed");
                            final ImageView msfFlower = (ImageView) findViewById(R.id.msf_flower);
                            msfFlowerLayoutParams = (RelativeLayout.LayoutParams) msfFlower.getLayoutParams();
                            msfFlowerLayoutParams.leftMargin = (int) event.getX() + plantOffsetX - 350;
                            //msfFlowerLayoutParams.topMargin = (int) event.getY() + plantOffsetY - 250;
                            msfFlower.setLayoutParams(msfFlowerLayoutParams);
                            msfFlower.setVisibility(View.VISIBLE);
                            msfFlowerAnimatorSet.start();
                        } else if (senderSeed == 3) {
                            Log.d(msg, "unicefSeed");
                            final ImageView unicefFlower = (ImageView) findViewById(R.id.unicef_flower);
                            unicefFlowerLayoutParams = (RelativeLayout.LayoutParams) unicefFlower.getLayoutParams();
                            unicefFlowerLayoutParams.leftMargin = (int) event.getX() + plantOffsetX - 350;
                            //unicefFlowerLayoutParams.topMargin = (int) event.getY() + plantOffsetY - 250;
                            unicefFlower.setLayoutParams(unicefFlowerLayoutParams);
                            unicefFlower.setVisibility(View.VISIBLE);
                            unicefFlowerAnimatorSet.start();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        NavigationView navView = (NavigationView)findViewById(R.id.navview);

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.menu_seccion_1:
                                Log.i("NavigationView", "Pulsada opción 1");
                                //fragment = new Fragment1();
                                //fragmentTransaction = true;
                                break;
                            case R.id.menu_seccion_2:
                                Log.i("NavigationView", "Pulsada opción 2");
                                //fragment = new Fragment2();
                                //fragmentTransaction = true;
                                break;
                        }
                        return true;
                    }
                });
    }

    public void goToDonate(View v){
//        logOut(null);
        startActivity(new Intent(AnimActivity.this, ProjectListActivity.class));
    }
    public void goToOrganizations(MenuItem item){
//        logOut(null);
        startActivity(new Intent(AnimActivity.this, DonationListActivity.class));
    }
    public void notDone(MenuItem item){
//        logOut(null);
        Toast.makeText(AnimActivity.this, "NOT DONE", Toast.LENGTH_LONG).show();
    }
    public void logOut(View v){
        // Call the Parse log out method
        ParseUser.logOut();
        // Start and intent for the dispatch activity
        Intent intent = new Intent(AnimActivity.this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    public void fastDonation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Project: " + breakingProject.getName() + ". Org: " + breakingProject.getOrganization().getName() + ". Type quantity (€)");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setText(""+String.valueOf(averagePayment));
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("DONATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String m_Text = input.getText().toString();
                // Saving the payment in the database
                payment = new Payment(input.getText().toString(), breakingProject.getMatter(), breakingProject);
                payment.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            paymentRegisteredInDatabase();
                        } else {
                            Toast.makeText(AnimActivity.this, getString(R.string.payment_error), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );
        builder.show();
    }

    public void paymentRegisteredInDatabase() {

        String name = getString(R.string.donation_confirmation);

        String info = getString(R.string.sharing_question_1) +  payment.get("value") + getString(R.string.sharing_question_2);

        openOptionsHelpDialog(name, info);
    }

    private void openOptionsHelpDialog(String name, String info)
    {
        new AlertDialog.Builder(this)
                .setTitle(name).setMessage(info)
                .setPositiveButton("Share",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shareOnFacebook();
//                                Intent myIntent = new Intent(ProjectListActivity.this, ShareOnFacebook.class);
//                                startActivity(myIntent);
                            }
                        }
                )
                .setNegativeButton("Not share",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AnimActivity.this, "Thank you for your donation", Toast.LENGTH_LONG).show();
                                Intent myIntent = new Intent(AnimActivity.this, AnimActivity.class);
                                myIntent.putExtra("addSeed", "addOneSeed");
                                startActivity(myIntent);
                            }
                        }
                )
                .show();
    }

    private void shareOnFacebook () {
        String urlToShare = "https://www.facebook.com/dialog/share?app_id=145634995501895&display=popup&href=http://antoniobazco.github.io/";//&redirect_uri=https%3A%2F%2Fdevelopers.facebook.com%2Ftools%2Fexplorer";
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

    public void getBreakingProject() {
        projectList = Application.getList();

        if (projectList == null) {
            ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
            query.findInBackground(new FindCallback<Project>() {
                @Override
                public void done(List<Project> projects, ParseException error) {
                    Random rand = new Random();
                    int randomPosition;

                    if (projects != null) {
                        projectList = projects;
                        randomPosition = rand.nextInt(projectList.size());

                        breakingProject = projectList.get(randomPosition);

                        //get user payments
                        ParseQuery innerQuery = new ParseQuery("_User");
                        innerQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());

                        ParseQuery<Payment> query = ParseQuery.getQuery(Payment.class);
                        query.whereMatchesQuery("user", innerQuery);

                        query.findInBackground(new FindCallback<Payment>() {
                            @Override
                            public void done(List<Payment> payments, ParseException error) {
                                if (payments != null && payments.size() != 0) {
                                    System.out.println("payments.size()   "+payments.size());
                                    projectList = sortByMatter(payments, projectList);
                                    Application.saveList(projectList);
                                }
                            }
                        });
                        Application.saveList(projectList);
                    }
                }
            });
        }
    }

    private List<Project> sortByMatter(List<Payment> payments,List<Project> projects ) {
        List<Project> subscriberProjects = new ArrayList<Project>(projects);
        List<Project> sortList = new ArrayList<Project>();
        List<String> subscriberProjectsID = new ArrayList<String>();
        List<String> newProjectsID = new ArrayList<String>();
        Map<String,Integer> matterList = new HashMap<String, Integer>();
        int value;

        for (Project p : projects) {
            newProjectsID.add(p.getObjectId());
        }

        for (Payment p : payments) {
            String matter = p.getMatter();
            value = p.getValue();
            averagePayment = averagePayment + value;

            if (matterList.containsKey(matter)) {
                matterList.put(matter, matterList.get(matter) + value);
            } else {
                matterList.put(matter, value);
            }

            Project project = p.getProject();
            String projectID = project.getObjectId();

            if (!subscriberProjectsID.contains(projectID)) {
                subscriberProjectsID.add(projectID);
                newProjectsID.remove(projectID);
            }
        }

        averagePayment = averagePayment / payments.size();
        Application.saveAveragePayment(averagePayment);

        Map<String, Integer> matterSortList = sortByValues(matterList);

        for (String entry : matterSortList.keySet()) {
            for (Project project : projects) {
                if  (newProjectsID.contains(project.getObjectId()) && project.getMatter().equals(entry)) {
                    sortList.add(project);
                    subscriberProjects.remove(project);
                }
            }
        }
        sortList.addAll(subscriberProjects);

        return sortList;
    }


    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

}
