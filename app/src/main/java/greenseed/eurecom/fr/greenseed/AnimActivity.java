package greenseed.eurecom.fr.greenseed;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.Calendar;

public class AnimActivity extends AppCompatActivity {

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
        startActivity(new Intent(AnimActivity.this, DonationListActivity.class));
    }

    public void logOut(View v){
        // Call the Parse log out method
        ParseUser.logOut();
        // Start and intent for the dispatch activity
        Intent intent = new Intent(AnimActivity.this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
