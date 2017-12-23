package bjasuja.syr.edu.touristguide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apptracker.android.track.AppTracker;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

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
import java.util.Collections;


public class ContactUs extends Fragment {
    EditText e2,e3,e4,e5;
    Button b1;
    String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v= inflater.inflate(R.layout.fragment_contact_us, container, false);

        e2=(EditText) v.findViewById(R.id.editText20);
        e3=(EditText) v.findViewById(R.id.editText21);
        e4=(EditText) v.findViewById(R.id.editText22);
        e5=(EditText) v.findViewById(R.id.editText23);
        b1=(Button)v.findViewById(R.id.button7) ;

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(SplashScreen.gllogout==true) {
        e2.setText(MainActivity.glname);
        e3.setText(MainActivity.glemail);
        }

        else if(SplashScreen.fblogout==true) {

        e2.setText(MainActivity.fbname);
        e3.setText(MainActivity.fbemail);
       }

        else if(SplashScreen.twlogout==true) {

         e2.setText(MainActivity.twname);
         e3.setText(MainActivity.twemail);
         }


        if(SplashScreen.logout==true) {
        e2.setText(MainActivity.username);
        e3.setText(MainActivity.email);
        e4.setText(MainActivity.mobile);
        e4.setEnabled(false);
        }
        else
        {
            e4.setEnabled(true);
        }

        e2.setEnabled(false);
        e3.setEnabled(false);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!e5.getText().toString().isEmpty())
                {
                    message=e5.getText().toString();
                    new JSONP().execute();
                }
            }
        });

        startad();
    }

    public void startad()
    {
        if(MainActivity.addisabled) {
            AppTracker.loadModule(getActivity().getApplicationContext(), "inapp");
            //  AppTracker.loadModule(getActivity().getApplicationContext(), "reward");
        }
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

    class JSONP extends AsyncTask<Void,Void,String> {
        String jsonurl, jsonstring;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jsonurl ="http://www.touristguide.gq/message.php";

        }

        @Override
        protected String doInBackground(Void... params) {

            String data = "";
            try {
                URL url = new URL(jsonurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                String postdata = URLEncoder.encode("email") + "=" + URLEncoder.encode(MainActivity.email)
                        + "&" + URLEncoder.encode("message") + "=" + URLEncoder.encode(message);

                bufferedWriter.write(postdata);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder  stringBuilder=new StringBuilder();
                while((jsonstring= bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(jsonstring+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                data=stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

          Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
          e5.setText("");
        }
    }
}
