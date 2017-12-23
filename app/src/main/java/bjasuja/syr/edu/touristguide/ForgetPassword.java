package bjasuja.syr.edu.touristguide;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ForgetPassword extends AppCompatActivity {
    String emailpass;
    EditText e1;
    Button b1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        b1=(Button)findViewById(R.id.button4);
        e1=(EditText) findViewById(R.id.editText7);

        Explode enterTransition = new Explode();
        enterTransition.setDuration(1000);

        Explode exitTransaction = new Explode();
        exitTransaction.setDuration(1000);

        getWindow().setEnterTransition(enterTransition);
        getWindow().setExitTransition(exitTransaction);

        if(savedInstanceState!=null)
        {
            e1.setText(savedInstanceState.getString("email"));
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailpass= e1.getText().toString();
                if(!emailpass.contains("@") ||  !emailpass.contains("."))
                {
                    Toast.makeText(ForgetPassword.this, "Check your email-id.", Toast.LENGTH_SHORT).show();
                }
                else {
                    new JSONP().execute();
                }

            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email",e1.getText().toString());

    }

    class JSONP extends AsyncTask<Void,Void,String> {
        String jsonurl, jsonstring,regidlogin;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jsonurl ="http://www.touristguide.gq/forgetpassword.php";

        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder  stringBuilder;
            String data = "";
            try {
                URL url = new URL(jsonurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                String postdata = URLEncoder.encode("email") + "=" + URLEncoder.encode(emailpass);

                bufferedWriter.write(postdata);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                stringBuilder=new StringBuilder();
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

            if(s.contains("successfully"))
            {
                Toast.makeText(ForgetPassword.this, "New Password sent to your registered email id", Toast.LENGTH_SHORT).show();
                Intent bj = new Intent(ForgetPassword.this, Signin.class);
                startActivity(bj, ActivityOptions.makeSceneTransitionAnimation(ForgetPassword.this).toBundle());

                finish();
            }
            else
            {
                e1.setText("");
                Toast.makeText(ForgetPassword.this,s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
