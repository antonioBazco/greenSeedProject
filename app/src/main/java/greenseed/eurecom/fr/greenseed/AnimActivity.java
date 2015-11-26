package greenseed.eurecom.fr.greenseed;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimActivity extends AppCompatActivity {

    AnimatorSet sunAnimatorSet;
    AnimatorSet cloud1AnimatorSet;
    AnimatorSet cloud2AnimatorSet;
    ValueAnimator skyAnimator;
    ValueAnimator groundAnimator;
    AnimatorSet flowerAnimatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);


        // sun animation configuration
        sunAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.sun_movement);
        ImageView sun = (ImageView) findViewById(R.id.sun);
        sunAnimatorSet.setTarget(sun);
        /***************************/

        // cloud animation configuration
        // cloud1
        ImageView cloud1 = (ImageView) findViewById(R.id.cloud1);
        cloud1AnimatorSet = (AnimatorSet)AnimatorInflater.loadAnimator(this, R.animator.cloud_movement);
        cloud1AnimatorSet.setTarget(cloud1);

        // cloud2
        ImageView cloud2 = (ImageView) findViewById(R.id.cloud2);
        cloud2AnimatorSet = (AnimatorSet)AnimatorInflater.loadAnimator(this, R.animator.cloud_movement);
        cloud2AnimatorSet.setTarget(cloud2);
        /***************************/

        // sky animation configuration
        skyAnimator = ObjectAnimator.ofInt(findViewById(R.id.sky), "backgroundColor",
                        Color.rgb(0x00, 0x00, 0x4c), Color.rgb(0xae, 0xc2, 0xff));
        //set same duration and animation properties as others
        skyAnimator.setDuration(10000);
        skyAnimator.setEvaluator(new ArgbEvaluator());
        skyAnimator.setRepeatCount(ValueAnimator.INFINITE);
        skyAnimator.setRepeatMode(ValueAnimator.REVERSE);
        /*************************************/

        // ground animation configuration
        groundAnimator = ObjectAnimator.ofInt(findViewById(R.id.ground), "backgroundColor",
                        Color.rgb(0x00, 0x47, 0x00), Color.rgb(0x45, 0xae, 0x45));
        //set same duration and animation properties as others
        groundAnimator.setDuration(10000);
        groundAnimator.setEvaluator(new ArgbEvaluator());
        groundAnimator.setRepeatCount(ValueAnimator.INFINITE);
        groundAnimator.setRepeatMode(ValueAnimator.REVERSE);
        /****************************************/

        // ground animation configuration
        ImageView flower = (ImageView) findViewById(R.id.flower);
        flowerAnimatorSet = (AnimatorSet)AnimatorInflater.loadAnimator(this, R.animator.flower_movement);
        flowerAnimatorSet.setTarget(flower);
        /****************************************/

        // starting animations
        sunAnimatorSet.start();
        cloud1AnimatorSet.start();
        cloud2AnimatorSet.start();
        skyAnimator.start();
        groundAnimator.start();
        flowerAnimatorSet.start();



        skyAnimator.addUpdateListener(

                new ValueAnimator.AnimatorUpdateListener() {

                    TextView textView = (TextView) findViewById(R.id.greeting);
                    float animatedFractionPrev = 0.0f;
                    float animatedFractionCurr = 0.0f;

                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
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
                }
        );
    }
}
