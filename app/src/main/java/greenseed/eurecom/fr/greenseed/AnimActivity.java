package greenseed.eurecom.fr.greenseed;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.DragEvent;
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

    AnimatorSet sunAnimatorSet;
    AnimatorSet cloud1AnimatorSet;
    AnimatorSet cloud2AnimatorSet;
    ValueAnimator skyAnimator;
    ValueAnimator groundAnimator;
    AnimatorSet flowerAnimatorSet;

    //AnimatorSet sunYAnimatorSet;
    //AnimatorSet sunXAnimatorSet;

    ImageView seedView;
    ImageView potView;
    String msg = "DRAG MSG";

    private android.widget.RelativeLayout.LayoutParams layoutParams;

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
        Calendar CurrentDateTime = Calendar.getInstance();
        int hours = CurrentDateTime.get(Calendar.HOUR_OF_DAY);
        int minutes = hours * 60 + CurrentDateTime.get(Calendar.MINUTE);
        float startOffset = (float) minutes / (60 * 24);

        Log.d("OFFSET: ", String.valueOf(-(long) (startOffset * 60000)));

        // sun animation configuration
        ImageView sun = (ImageView) findViewById(R.id.sun);
        sunAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.sun_movement);
        sunAnimatorSet.setStartDelay(-(long) (startOffset * 60000));
        sunAnimatorSet.setTarget(sun);
        /***************************/

        // cloud animation configuration
        // cloud1
        ImageView cloud1 = (ImageView) findViewById(R.id.cloud1);
        cloud1AnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.cloud_movement);
        cloud1AnimatorSet.setTarget(cloud1);
        // cloud2
        ImageView cloud2 = (ImageView) findViewById(R.id.cloud2);
        cloud2AnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.cloud_movement);
        cloud2AnimatorSet.setTarget(cloud2);
        /***************************/

        // sky animation configuration
        skyAnimator = ObjectAnimator.ofInt(findViewById(R.id.sky), "backgroundColor",
                Color.rgb(0x00, 0x00, 0x4c), Color.rgb(0xae, 0xc2, 0xff));
        //set same duration and animation properties as others
        skyAnimator.setDuration(60000);
        skyAnimator.setEvaluator(new ArgbEvaluator());
        skyAnimator.setRepeatCount(ValueAnimator.INFINITE);
        skyAnimator.setRepeatMode(ValueAnimator.REVERSE);
        /*************************************/

        // ground animation configuration
        groundAnimator = ObjectAnimator.ofInt(findViewById(R.id.ground), "backgroundColor",
                Color.rgb(0x00, 0x47, 0x00), Color.rgb(0x45, 0xae, 0x45));
        //set same duration and animation properties as others
        groundAnimator.setDuration(60000);
        groundAnimator.setEvaluator(new ArgbEvaluator());
        groundAnimator.setRepeatCount(ValueAnimator.INFINITE);
        groundAnimator.setRepeatMode(ValueAnimator.REVERSE);
        /****************************************/

        // ground animation configuration
        ImageView flower = (ImageView) findViewById(R.id.flower);
        flowerAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flower_movement);
        flowerAnimatorSet.setTarget(flower);
        /****************************************/

        // starting animations
        sunAnimatorSet.start();
        cloud1AnimatorSet.start();
        cloud2AnimatorSet.start();
        skyAnimator.start();
        groundAnimator.start();
        //flowerAnimatorSet.start(); //is started in seed drag


        skyAnimator.addUpdateListener(

                new ValueAnimator.AnimatorUpdateListener() {

                    TextView textView = (TextView) findViewById(R.id.greeting);

                    float animatedFractionPrev = 0.0f;
                    float animatedFractionCurr = 0.0f;
                    int counter = 0;

                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {

                        if (counter > 100) {
                            counter = 0;

                            animatedFractionCurr = valueAnimator.getAnimatedFraction();

                            if (animatedFractionCurr > animatedFractionPrev) {
                                if (animatedFractionCurr > 0.0 && animatedFractionCurr <= 0.70) {
                                    textView.setText("Good morning!");
                                } else {
                                    textView.setText("Good day!");
                                }
                            } else {
                                if (animatedFractionCurr >= 0.8) {
                                    textView.setText("Good day!");
                                } else if (animatedFractionCurr < 0.8 && animatedFractionCurr >= 0.1) {
                                    textView.setText("Good afternoon!");
                                } else {
                                    textView.setText("Good Evening!");
                                }
                            }
                            animatedFractionPrev = animatedFractionCurr;
                        }
                        counter = counter + 1;
                    }
                });

        seedView = (ImageView)findViewById(R.id.seed);
        potView = (ImageView)findViewById(R.id.pot);

        if (addSeed != null)
        {
            if (addSeed.equals("addOneSeed")) {
                seedView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            seedView.setVisibility(View.INVISIBLE);
        }

        seedView.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                        ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(seedView);

                        v.startDrag(dragData, myShadow, null, 0);
                        return true;
                    }
                });

        seedView.setOnDragListener(
                new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                                //layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                                // Do nothing
                                break;

                            case DragEvent.ACTION_DRAG_EXITED:
                                Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                                // Do nothing
                                break;

                            case DragEvent.ACTION_DRAG_LOCATION:
                                Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                                // Do nothing
                                break;

                            case DragEvent.ACTION_DRAG_ENDED:
                                Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
                                // Do nothing
                                break;

                            case DragEvent.ACTION_DROP:
                                Log.d(msg, "ACTION_DROP event" + "at X: " + String.valueOf(event.getX()) + " Y: " + String.valueOf(event.getY()));
                                // Do nothing
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                }

        );

        seedView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(seedView);
                            seedView.startDrag(data, shadowBuilder, seedView, 0);
                            //seedView.setVisibility(View.INVISIBLE);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }

        );

        potView.setOnDragListener(new View.OnDragListener()

        {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED by POT");
                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED by POT");
                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED by POT");
                        if(event.getResult() == true) {
                            seedView.setVisibility(View.GONE);
                        }//else {
                        //    seedView.setVisibility(View.VISIBLE);
                        //}

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event  by POT" + "at X: " + String.valueOf(event.getX()) + " Y: " + String.valueOf(event.getY()));
                        flowerAnimatorSet.start();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
    public void goToDonate(View v){
        startActivity(new Intent(AnimActivity.this, ProjectListActivity.class));
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

                //payment.setACL(new ParseACL(ParseUser.getCurrentUser()));
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
                                int value;
                                if (payments != null) {
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
        return ;
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
