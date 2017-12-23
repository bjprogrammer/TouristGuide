package bjasuja.syr.edu.touristguide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;

import com.apptracker.android.track.AppTracker;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.Collections;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutusFragment extends Fragment {
    public AboutusFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v=inflater.inflate(R.layout.fragment_aboutus, container, false);
        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0-beta");
        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .setImage(R.drawable.logo)
                .setDescription("This app will find best restaurants and food courts near user location.App will also show reviews and feedback of customers who have been to these places in the past. We will also give relative pricing of food in various nearby cuisines")
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail("bobbyjasuja@gmail.com")
                .addWebsite("https://www.linkedin.com/in/bobbyjasuja")
                .addFacebook("bobby.jasuja")
                .addTwitter("bobbyjasuja")
                .addYoutube("UC7TV4qY9GpNR_dqWA09gigQ")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("bjprogrammer")
                .addInstagram("bobbyjasuja")
                .create();
        return aboutPage;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item . getItemId () ) {

            case R.id.action_unhide:
                MainActivity.b1.purchaseRemoveAds();
                break;


            case R.id.action_logoff:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.exit,null);
                builder.setView(dialogView);
                final AlertDialog alert = builder.create();

                Button b=(Button)dialogView.findViewById(R.id.button51);
                Button b2=(Button)dialogView.findViewById(R.id.button7);

                b2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                b.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             DatabaseHelper dh=new DatabaseHelper(getActivity());
                                             FbDatabaseHelper dh1=new FbDatabaseHelper(getActivity());
                                             TwDatabaseHelper  dh2=new TwDatabaseHelper(getActivity());
                                             GlDatabaseHelper dh3=new GlDatabaseHelper(getActivity());

                                             AccessToken accessToken = AccessToken.getCurrentAccessToken();
                                             TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();

                                             if(accessToken != null) {
                                                 if(SplashScreen.fblogout==true)
                                                 {
                                                     SplashScreen.fblogout=false;
                                                     dh1.deletedata(1);
                                                     LoginManager.getInstance().logOut();
                                                 }
                                             }

                                             if (twitterSession != null)
                                             {
                                                 if(SplashScreen.twlogout==true) {
                                                     SplashScreen.twlogout=false;
                                                     dh2.deletedata(1);
                                                     CookieSyncManager.createInstance(getActivity());
                                                     CookieManager cookieManager = CookieManager.getInstance();
                                                     cookieManager.removeSessionCookie();
                                                     TwitterCore.getInstance().getSessionManager().clearActiveSession();
                                                 }
                                             }

                                             if(SplashScreen.logout==true)
                                             {
                                                 SplashScreen.logout=false;
                                                 dh.deletedata(1);
                                             }

                                             if(SplashScreen.gllogout==true)
                                             {
                                                 SplashScreen.gllogout=false;
                                                 dh3.deletedata(1);

                                             }
                                             Intent i=new Intent(getActivity(), Signin.class);
                                             startActivity(i);
                                             getActivity().finish();
                                         }
                                     }
                );
                alert.show();

                break;

            case R.id.action_privacy:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater2 = getLayoutInflater();
                View dialogView2 = inflater2.inflate(R.layout.termsandconditions,null);
                builder2.setView(dialogView2);
                final AlertDialog alert2 = builder2.create();


                Button b3=(Button)dialogView2.findViewById(R.id.button7);

                b3.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                              alert2.dismiss();
                                          }
                                      }
                );

                alert2.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        startad();
    }

    public void startad()
    {
        if(MainActivity.addisabled) {
            AppTracker.loadModule(getActivity().getApplicationContext(), "inapp");
            //  AppTracker.loadModule(getActivity().getApplicationContext(), "reward");
        }
    }

}
