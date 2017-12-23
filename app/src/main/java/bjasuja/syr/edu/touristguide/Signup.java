package bjasuja.syr.edu.touristguide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.transition.Explode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Signup extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
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
    Bitmap bitm,bitmap=null;
    CheckBox checkbox;
    TextView t1;
    SmsReceiver receiver;
    static String number,id,message;
    Boolean flag=false;
    EditText t3;
    static Signup ins;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ins=this;
        ib=(AvatarView) findViewById(R.id.imageButton);
        if(savedInstanceState==null) {
            AvatarPlaceholder av = new AvatarPlaceholder("Tourist Guide");
            Picasso.with(this)
                    .load("http:/example.com/user/someUserAvatar.png")
                    .placeholder(av)
                    .fit()
                    .into(ib);
        }

        e2=(EditText) findViewById(R.id.editText3);
        e3=(EditText) findViewById(R.id.editText4);
        e4=(EditText) findViewById(R.id.editText5);
        e5=(EditText) findViewById(R.id.editText6);
        b10=(Button)findViewById(R.id.button5) ;

        checkbox=(CheckBox) findViewById(R.id.checkBox);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        t1 = (TextView) findViewById( R.id.textView2);

        SpannableString ss = new SpannableString("I accept the following Terms and Condition");

        ss.setSpan(new MyClickableSpan(),23 ,42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        t1.setText(ss);
        t1.setMovementMethod(LinkMovementMethod.getInstance());

        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Select State");
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
        categories2.add("Select Gender");
        categories2.add("Male");
        categories2.add("Female");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter <String> dataAdapter2 = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, categories2);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter2);

        Explode enterTransition = new Explode();
        enterTransition.setDuration(1000);

        Explode exitTransaction = new Explode();
        exitTransaction.setDuration(1000);

        getWindow().setEnterTransition(enterTransition);
        getWindow().setExitTransition(exitTransaction);

        if(savedInstanceState!=null)
        {
            e2.setText(savedInstanceState.getString("username"));
            e5.setText(savedInstanceState.getString("password"));
            e3.setText(savedInstanceState.getString("email"));
            e4.setText(savedInstanceState.getString("mobile"));
            if(savedInstanceState.getString("checkbox").contains("true"))
            {
                checkbox.setChecked(true);
            }
            else {
                checkbox.setChecked(false);
            }
            gender=savedInstanceState.getString("gender");
            state=savedInstanceState.getString("state");
            if(savedInstanceState.getString("flag").contains("true"))
            {
                flag=true;
                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.signuptermsandcondition,null);
                builder.setView(dialogView);
                final AlertDialog alert = builder.create();


                Button b=(Button)dialogView.findViewById(R.id.button7);
                b.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             alert.dismiss();
                                             flag=false;
                                         }
                                     }
                );
                alert.show();
            }
            else {}

            byte[] byteArray=savedInstanceState.getByteArray("image");
            if(byteArray!=null) {
                bitmap=BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ib.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username",e2.getText().toString());
        outState.putString("password", e5.getText().toString());
        outState.putString("email",e3.getText().toString());
        outState.putString("mobile",e4.getText().toString());
        outState.putString("checkbox", String.valueOf(checkbox.isChecked()));
        outState.putString("gender",gender);
        outState.putString("state",state);
        outState.putString("flag", String.valueOf(flag));

        if(bitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            outState.putByteArray("image", byteArray);
        }
    }


    class MyClickableSpan extends ClickableSpan {
        public void onClick(View textView) {
            flag=true;
            AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.signuptermsandcondition,null);
            builder.setView(dialogView);
            final AlertDialog alert = builder.create();

            Button b=(Button)dialogView.findViewById(R.id.button7);
            b.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         alert.dismiss();
                                         flag=false;
                                     }
                                 }
            );
            alert.show();
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.RED);
            ds.setUnderlineText(true);
        }
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
    protected void onResume() {
        super.onResume();

        final  boolean result3=Utility.checkPermission2(Signup.this);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(Signup.this);
                build.setTitle("Upload profile image using -");

                build.setPositiveButton("camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final boolean result=Utility.checkPermission(Signup.this);
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
                username= e2.getText().toString();
                email= e3.getText().toString();
                mobile= "+1"+e4.getText().toString();
                password= e5.getText().toString();


                if(state.equals("Select State") || gender.equals("Select Gender")  || e2.getText().toString().isEmpty() || e3.getText().toString().isEmpty() || e4.getText().toString().isEmpty() || e5.getText().toString().isEmpty())
                {
                    Toast.makeText(Signup.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }
                else if(username.length()<5)
                {
                    Toast.makeText(Signup.this, "Username cannot be less than 5 characters", Toast.LENGTH_SHORT).show();
                }
                else if(!email.contains("@") || !email.contains("."))
                {
                    Toast.makeText(Signup.this, "Invalid email id.", Toast.LENGTH_SHORT).show();
                }
                else if(mobile.length()!=12)
                {
                    Toast.makeText(Signup.this, "Please check your mobile no.", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<8)
                {
                    Toast.makeText(Signup.this, "Password length cannot be less than 8 characters", Toast.LENGTH_SHORT).show();
                }
                else if(bitmap==null)
                {
                    Toast.makeText(Signup.this, "Add profile image", Toast.LENGTH_SHORT).show();
                }
                else if(!checkbox.isChecked())
                {
                    Toast.makeText(Signup.this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    number = mobile;
                    id = String.format("%04d", new Random().nextInt(10000));
                    message = "Tourist Guide app verification code-"+id;

                    receiver = new SmsReceiver();
                    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                    registerReceiver(receiver, filter);

                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.otp,null);
                    builder.setView(dialogView);
                    final AlertDialog alert = builder.create();

                    t3=(EditText)dialogView.findViewById(R.id.editText13);
                    final EditText t2=(EditText) dialogView.findViewById(R.id.editText13);
                    Button b=(Button)dialogView.findViewById(R.id.button7);

                    b.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {

                                                 if(t2.getText().toString().equals(id)) {
                                                     new  JSONP().execute();
                                                     alert.dismiss();
                                                 }
                                                 else
                                                 {
                                                     Toast.makeText(Signup.this,"Wrong OTP",Toast.LENGTH_LONG).show();
                                                 }
                                             }
                                         }
                    );

                    alert.show();

                    SmsManager sm = SmsManager.getDefault();
                    sm.sendTextMessage(number, null, message, null, null);

                    //new JSONP().execute();
                }
            }
        });
    }

    void setText(String id)
    {
        t3.setText(id);
    }

    public static  Signup  getInstace(){
        return ins;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                         bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        String path = saveImage(bitmap);
                        ib.setImageBitmap(bitmap);
                        this.bitmap=bitmap;
                   //     decodeFile(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Signup.this, "Failed!", Toast.LENGTH_SHORT).show();
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
            MediaScannerConnection.scanFile(this,
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
            jsonurl ="http://www.touristguide.gq/signup.php";

        }

        @Override
        protected String doInBackground(Void... params) {
            decodeFile(bitmap);
            while (encodedImage==null)
            {
                try {
                    wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.contains("Successfully"))
            {
                unregisterReceiver(receiver);
                Toast.makeText(Signup.this,s, Toast.LENGTH_SHORT).show();
                Intent bj=new Intent(Signup.this,Signin.class);
                startActivity(bj, ActivityOptions.makeSceneTransitionAnimation(Signup.this).toBundle());

                finish();
            }
            else
            {
                Toast.makeText(Signup.this,s, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
