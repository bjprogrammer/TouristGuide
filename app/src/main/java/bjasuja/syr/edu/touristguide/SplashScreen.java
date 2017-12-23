package bjasuja.syr.edu.touristguide;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igalata.bubblepicker.BubblePickerListener;

import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;
import com.pollfish.main.PollFish;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import static android.R.attr.animation;

public class SplashScreen extends AppCompatActivity {
    DatabaseHelper dh;
    TwDatabaseHelper dh2;
    FbDatabaseHelper dh1;
    GlDatabaseHelper dh3;
    public static Boolean logout,fblogout,twlogout,gllogout;
    public String usernamesignin,passwordsignin;
    PrefManager prefManager;

    Handler handler;
    Runnable runnable;
    MediaPlayer song;

    RadioButton rg1, rg2, rg3, rg4;
    BubblePicker picker;
    TextView t;
    ImageView iv;
    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //rg1 = (RadioButton) findViewById(R.id.radioButton);
        //rg2 = (RadioButton) findViewById(R.id.radioButton2);
       // rg3 = (RadioButton) findViewById(R.id.radioButton3);
       // rg4 = (RadioButton) findViewById(R.id.radioButton4);

        dh = new DatabaseHelper(SplashScreen.this);
        dh1 = new FbDatabaseHelper(SplashScreen.this);
        dh2 = new TwDatabaseHelper(SplashScreen.this);
        dh3=new GlDatabaseHelper(SplashScreen.this);

        t=(TextView) findViewById(R.id.textView);
        iv=(ImageView) findViewById(R.id.imageView);
        rl=(RelativeLayout) findViewById(R.id.relativelayout);

        Shader textShader=new LinearGradient(0, 0, 0, 20,
                new int[]{Color.GREEN,Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        t.getPaint().setShader(textShader);

        String first = "Find 'access' key to access ";
        String next = "<font color='#EE0000'>Tourist Guide app</font>";
        t.setText(Html.fromHtml(first + next));
        song= MediaPlayer.create(SplashScreen.this, R.raw.spirit);
        picker=(BubblePicker) findViewById(R.id.picker);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startHeavyProcessing();
    }

    private void startHeavyProcessing(){
        new LongOperation().execute();
    }


 class LongOperation extends AsyncTask<Void, Void,String> {
     String jsonurl,jsonstring;

     @Override
     protected void onPreExecute() {
         super.onPreExecute();

     }

     @Override
     protected String doInBackground(Void... params) {

         String data = "";
         Cursor b10 = dh.getalldata();
         if (b10.getCount() == 0) {
             logout = false;
         }
         else
         {
             while (b10.moveToNext())
             {
                 usernamesignin = b10.getString(1);
                 passwordsignin = b10.getString(2);
                 if (b10.getString(3).equalsIgnoreCase("True"))
                 {
                     data="logged";
                     logout = true;
                 }
                 else
                 {
                     logout = false;
                 }
             }
         }

         Cursor b11 = dh1.getalldata();
         if (b11.getCount() == 0) {
             fblogout = false;
         } else {
             while (b11.moveToNext()) {
                 if (b11.getString(1).equalsIgnoreCase("True")) {
                     data="logged";
                     fblogout = true;
                 }
                 else
                 {
                     fblogout = false;
                 }
             }
         }

         Cursor b12 = dh2.getalldata();
         if (b12.getCount() == 0) {
             twlogout = false;
         } else {
             while (b12.moveToNext()) {;
                 if (b12.getString(1).equalsIgnoreCase("True")) {
                     data="logged";
                     twlogout = true;
                 }
                 else
                 {
                     twlogout = false;
                 }
             }
         }

         Cursor b13 = dh3.getalldata();
         if (b13.getCount() == 0) {
             gllogout = false;
         } else {
             while (b13.moveToNext()) {;
                 if (b13.getString(1).equalsIgnoreCase("True")) {
                     data="logged";
                     gllogout = true;
                 }
                 else
                 {
                     gllogout = false;
                 }
             }
         }

         if(!fblogout  && !twlogout && !logout && !gllogout) {
             song.start();
             for (int i = 1; i <= 7; i++) {
                 try {
                     Thread.sleep(500);
                     publishProgress();
                 } catch (InterruptedException e) {
                 }
             }
         }
         return data;
     }


     @Override
     protected void onPostExecute(String s) {
         super.onPostExecute(s);
         if (s.contains("logged")) {
             Intent bj = new Intent(SplashScreen.this, MainActivity.class);
             bj.putExtra("username", usernamesignin);
             startActivity(bj);
             finish();
         }
         else
         {
             prefManager = new PrefManager(SplashScreen.this);
             if (!prefManager.isFirstTimeLaunch()) {
                 launchHomeScreen();
                 finish();
             }
         }
     }

     private void launchHomeScreen() {
         prefManager.setFirstTimeLaunch(false);
         startActivity(new Intent(SplashScreen.this, Signin.class));
         finish();
     }

     @Override
     protected void onProgressUpdate(Void... values) {
         super.onProgressUpdate(values);
                     if (!song.isPlaying())
                     {
                         song.release();
                     }
                 }
             }


    @Override
    protected void onResume() {
        super.onResume();
        picker.onResume();


        final String[] titles = getResources().getStringArray(R.array.countries);
        final int[] color=getResources().getIntArray(R.array.colors);
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        final TypedArray images = getResources().obtainTypedArray(R.array.images);

        picker.setCenterImmediately(false);
        picker.setBubbleSize(1);
        picker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return titles.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();

                item.setTitle(titles[position]);
                if(titles[position].contains("USA"))
                {
                    item.setGradient(new BubbleGradient(colors.getColor(position-2, 0),
                            colors.getColor(position -2, 0), BubbleGradient.VERTICAL));
                }
                else if(titles[position].contains("Canada"))
                {
                    item.setGradient(new BubbleGradient(colors.getColor(position-3, 0),
                            colors.getColor(position -3, 0), BubbleGradient.VERTICAL));
                }
                else if(titles[position].contains("Access"))
                {
                    item.setGradient(new BubbleGradient(colors.getColor(position-4 , 0),
                            colors.getColor(position -4, 0), BubbleGradient.VERTICAL));
                }
                else {
                    item.setGradient(new BubbleGradient(colors.getColor((position*2) % 10, 0),
                            colors.getColor((position *2) % 10 + 1, 0), BubbleGradient.VERTICAL));
                }
                item.setTextColor(ContextCompat.getColor(SplashScreen.this, android.R.color.white));
                item.setBackgroundImage(ContextCompat.getDrawable(SplashScreen.this, images.getResourceId(position, 0)));
                return item;
            }
        });


        picker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {
                int randomAndroidColor = color[new Random().nextInt(color.length)];
                if(item.getTitle().contains("Access") || item.getTitle().contains("USA") || item.getTitle().contains("Canada")) {
                    song.release();
                    iv.setVisibility(View.VISIBLE);
                    rl.setVisibility(View.VISIBLE);
                    t.setVisibility(View.GONE);
                    picker.setVisibility(View.GONE);
                    Animation animation1 =
                            AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide);

                    animation1.setAnimationListener(new Animation.AnimationListener() {
                                                       public void onAnimationStart(Animation animation) {}
                                                       public void onAnimationRepeat(Animation animation) {}
                                                       @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                       public void onAnimationEnd(Animation animation) {
                                                           Intent i = new Intent(SplashScreen.this, SplashScreen2.class);
                                                           prefManager = new PrefManager(SplashScreen.this);
                                                          // prefManager.setFirstTimeLaunch(false);
                                                           startActivity(i, ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this).toBundle());
                                                           finish();
                                                       }
                                                   });
                    iv.startAnimation(animation1);
                }
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {

            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        picker.onPause();

    }
}

