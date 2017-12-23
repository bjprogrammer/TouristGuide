package bjasuja.syr.edu.touristguide;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apptracker.android.track.AppTracker;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class UpdateProfile extends Fragment implements AdapterView.OnItemSelectedListener{
    Spinner spinner2,spinner1;
    AvatarView ib;
    EditText e2,e3,e4,e5;
    Button b10;
    String state="Select State",username, email,mobile,password,gender="Select Gender";

    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    byte[] b;
    static String encodedImage ;
    Bitmap bitm,bitmap;
    static UpdateProfile ins2;

    public UpdateProfile() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v= inflater.inflate(R.layout.fragment_update_profile, container, false);

        ins2=this;
        ib=(AvatarView) v.findViewById(R.id.imageButton);


        e2=(EditText) v.findViewById(R.id.editText3);
        e3=(EditText) v.findViewById(R.id.editText4);
        e4=(EditText) v.findViewById(R.id.editText5);
        e5=(EditText) v.findViewById(R.id.editText6);
        b10=(Button)v.findViewById(R.id.button5) ;

        spinner1 = (Spinner) v.findViewById(R.id.spinner1);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);

        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add(MainActivity.state);
        categories.add("Alabama");
        categories.add("Alaska");
        categories.add("Arizona");
        categories.add("Arkansas");
        categories.add("California");
        categories.add("Colorado");
        categories.add("Connecticut");
        categories.add("Delaware");
        categories.add("Florida");
        categories.add("Georgia");
        categories.add("Hawaii");
        categories.add("Idaho");
        categories.add("Illinois");
        categories.add("Indiana");
        categories.add("Iowa");
        categories.add("Kansas");
        categories.add("Kentucky");
        categories.add("Louisiana");
        categories.add("Maine");
        categories.add("Maryland");
        categories.add("Massachusetts");
        categories.add("Michigan");
        categories.add("Minnesota");
        categories.add("Mississippi");
        categories.add("Missouri");
        categories.add("Montana");
        categories.add("Nebraska");
        categories.add("Nevada");
        categories.add("New Hampshire");
        categories.add("New Jersey");
        categories.add("New Mexico");
        categories.add("New York");
        categories.add("North Carolina");
        categories.add("North Dakota");
        categories.add("Ohio");
        categories.add("Oklahoma");
        categories.add("Oregon");
        categories.add("Pennsylvania[");
        categories.add("Rhode Island");
        categories.add("South Carolina");
        categories.add("South Dakota");
        categories.add("Tennessee");
        categories.add("Texas");
        categories.add("Utah");
        categories.add("Vermont");
        categories.add("Virginia[");
        categories.add("Washington");
        categories.add("West Virginia");
        categories.add("Wisconsin");
        categories.add("Wyoming");

        List<String> categories2 = new ArrayList <String>();
        categories2.add(MainActivity.gender);
        categories2.add("Male");
        categories2.add("Female");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        ArrayAdapter <String> dataAdapter2 = new ArrayAdapter <String>(getActivity(), android.R.layout.simple_spinner_item, categories2);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter2);
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if(parent.toString().contains("spinner1")) {
            state = parent.getItemAtPosition(position).toString();

        }
        else if(parent.toString().contains("spinner2")) {
            gender=parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume() {
        super.onResume();

        final  boolean result3=Utility.checkPermission2(getActivity());
        AvatarPlaceholder av=new AvatarPlaceholder(MainActivity.username);

        if(bitmap!=null)
        {
            ib.setImageBitmap(bitmap);
        }
        else if(MainActivity.bitmap!=null)
        {
            ib.setImageBitmap(MainActivity.bitmap);
        }
        else {
            Picasso.with(getActivity())
                    .load("http:/example.com/user/someUserAvatar.png")
                    .placeholder(av)
                    .fit()
                    .into(ib);
        }

        e2.setText(MainActivity.username);
        e3.setText(MainActivity.email);
        e4.setText(MainActivity.mobile.substring(2));
        e5.setText(MainActivity.password);

        e2.setEnabled(false);
        e3.setEnabled(false);
        e4.setEnabled(false);
        state=MainActivity.state;
        gender=MainActivity.gender;

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                build.setTitle("Upload profile image using -");

                build.setPositiveButton("camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final boolean result=Utility.checkPermission(getActivity());
                        if(result) {
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                            try {
                                startActivityForResult(intent, PICK_FROM_CAMERA);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


                build.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                build.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_FROM_FILE);
                    }
                });
                build.show();
            }
        });


        b10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = e2.getText().toString();
                email = e3.getText().toString();
                mobile = "+1" + e4.getText().toString();
                password = e5.getText().toString();


                if (state.equals("Select State") || gender.equals("Select Gender") || e2.getText().toString().isEmpty() || e3.getText().toString().isEmpty() || e4.getText().toString().isEmpty() || e5.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 8) {
                    Toast.makeText(getActivity(), "Password length cannot be less than 8 characters", Toast.LENGTH_SHORT).show();
                } else {
                    new JSONP().execute();
                }
            }
        });

        startad();
    }


    public void startad()
    {
        if(MainActivity.addisabled)
        {
            AppTracker.loadModule(getActivity().getApplicationContext(), "inapp");
            //AppTracker.loadModule(getActivity().getApplicationContext(), "reward");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ib.setImageBitmap(thumbnail);
                saveImage(thumbnail);
                bitmap=thumbnail;
                //decodeFile(thumbnail);
                break;

            case PICK_FROM_FILE:
                Bitmap bitmap =null;
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                        String path = saveImage(bitmap);
                        ib.setImageBitmap(bitmap);
                        this.bitmap=bitmap;
                        //     decodeFile(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "/demonuts");
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public void decodeFile(Bitmap filePath) {
        final int REQUIRED_SIZE = 400;

        int width_tmp = filePath.getWidth(), height_tmp = filePath.getHeight();
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        bitm=Bitmap.createScaledBitmap(filePath, width_tmp, height_tmp, true);

        // ib.setImageBitmap(bitm);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        b = baos.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
    }

    class JSONP extends AsyncTask<Void,Void,String> {
        String jsonurl, jsonstring;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jsonurl ="http://www.touristguide.gq/update.php";

        }

        @Override
        protected String doInBackground(Void... params) {
            if(bitmap!=null) {
                decodeFile(bitmap);
                while (encodedImage == null) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                decodeFile(MainActivity.image);
                while (encodedImage == null) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            Log.d("hello",String.valueOf(encodedImage));

            publishProgress();
            String data = "";
            try {
                URL url = new URL(jsonurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                String postdata = URLEncoder.encode("username") + "=" + URLEncoder.encode(username) + "&" + URLEncoder.encode("password") + "=" + URLEncoder.encode(password)
                        + "&" + URLEncoder.encode("mobile") + "=" + URLEncoder.encode(mobile)+ "&" + URLEncoder.encode("email") + "=" + URLEncoder.encode(email)
                        + "&" + URLEncoder.encode("pic") + "=" + URLEncoder.encode(encodedImage)+ "&" + URLEncoder.encode("gender") + "=" + URLEncoder.encode(gender)
                        + "&" +  URLEncoder.encode("state") + "=" + URLEncoder.encode(state);

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

                MainActivity.password=password;
                MainActivity.gender=gender;
                MainActivity.state=state;
                Toast.makeText(getActivity(),s, Toast.LENGTH_SHORT).show();
                 MainActivity.bitmap=bitmap;
                if(bitmap!=null)
                {
                  MainActivity.imgProfile.setImageBitmap(bitmap);
                }

        }
    }
}
