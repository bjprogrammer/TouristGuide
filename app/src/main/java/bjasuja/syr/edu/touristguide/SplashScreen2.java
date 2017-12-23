package bjasuja.syr.edu.touristguide;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.GestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

import bjasuja.syr.edu.touristguide.ImageFlip.PageFlipView;

public class SplashScreen2 extends AppCompatActivity {
    ImageView iv,iv2;
    RadioButton r1,r2,r3,r4,r5;
    Button b1;

    int[] imageArray = { R.drawable.pic1, R.drawable.pic2,
            R.drawable.pic3, R.drawable.pic4, R.drawable.pic1};

    Handler handler;
    Runnable runnable;
    Intent service;
    ViewAnimator animator;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen2);

        Explode enterTransition = new Explode();
        enterTransition.setDuration(1000);

        Explode exitTransaction = new Explode();
        exitTransaction.setDuration(1000);

        getWindow().setEnterTransition(enterTransition);
        getWindow().setExitTransition(exitTransaction);
        service=new Intent(getApplicationContext(), TTSService.class);
        startService(service);

        animator=(ViewAnimator)this.findViewById(R.id.viewflipper);
        iv =(ImageView) findViewById(R.id.imageView5);
        r1=(RadioButton)findViewById(R.id.radioButton5);
        r2=(RadioButton)findViewById(R.id.radioButton6);
        r3=(RadioButton)findViewById(R.id.radioButton7);
        r4=(RadioButton)findViewById(R.id.radioButton8);
        r5=(RadioButton)findViewById(R.id.radioButton9);
        b1=(Button)findViewById(R.id.button);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(SplashScreen2.this,Signin.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashScreen2.this).toBundle());

                finish();
                handler.removeCallbacks(runnable);
            }
        });

        handler = new Handler();
        runnable = new Runnable()
        {
            int i = 0;
            int j=0;
            public void run()
            {
                if (i > imageArray.length-1 )
                {
                    i = 0;
                    stopService(service);
                    Intent intent=new Intent(SplashScreen2.this,Signin.class );
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashScreen2.this).toBundle());
                    finish();

                    return;
                }


               ImageViewAnimatedChange(SplashScreen2.this,iv,imageArray[i],j);
                i++;
                if(i==1)
                {
                    r1.setChecked(true);
                    j=100;
                }
                else if(r1.isChecked())
                {
                    r2.setChecked(true);
                    r1.setChecked(false);
                    b1.setVisibility(View.VISIBLE);
                }
                else if(r2.isChecked())
                {
                    r3.setChecked(true);
                    r2.setChecked(false);
                }
                else if(r3.isChecked())
                {
                    r4.setChecked(true);
                    r3.setChecked(false);
                }
                else if(r4.isChecked())
                {
                    r5.setChecked(true);
                    r4.setChecked(false);
                }
                handler.postDelayed(this, 6500);
            }
        };
        handler.postDelayed(runnable, 10);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public static void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image,final int i) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_in.setDuration(i*10);
        anim_out.setDuration(i*25);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
}
