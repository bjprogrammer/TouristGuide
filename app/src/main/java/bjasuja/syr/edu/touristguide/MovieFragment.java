package bjasuja.syr.edu.touristguide;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static bjasuja.syr.edu.touristguide.MainActivity.drawer;
import static bjasuja.syr.edu.touristguide.MainActivity.toolbar;

public class MovieFragment extends Fragment
{
    TextView name,description,count;
    RatingBar rating;
    SimpleDraweeView iv;
    int no;
    String title,overview,homepage,address,call,id,pricinglevel,reviewcount,photo,image;
    Float ratings;
    static Double latitude2,longitude2;
    TextView placeAddress,ratingText,pricing;
    ListView listView;
    FloatingActionButton floatingActionButton;
    ImageButton callButton,websiteButton;
    String[] authorname,authorrating,text,relativetime;

     public MovieFragment()
     {}

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putFloat("ratings", ratings);
        outState.putString("pricing", pricinglevel);
        outState.putString("address", address);
        outState.putString("call", call);
        outState.putString("title", title);
        outState.putString("homepage", homepage);
        outState.putString("id", id);
        outState.putString("image", image);
        outState.putString("photo",photo);
        outState.putString("reviewcount",reviewcount);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);;
        Fresco.initialize(getContext());
        View v=inflater.inflate(R.layout.fragment_movie, container, false);

        listView = (ListView) v.findViewById(R.id.mobile_list);
        setListViewHeightBasedOnChildren(listView);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        iv= (SimpleDraweeView)v. findViewById(R.id.imageView);

        MainActivity.mTitle.setText(title);

        name=(TextView)v.findViewById(R.id.placeName);
        rating=(RatingBar) v.findViewById(R.id.ratingBar);
        placeAddress=(TextView)v.findViewById(R.id.description);
        ratingText = (TextView) v.findViewById(R.id.ratingText);
        pricing = (TextView) v.findViewById(R.id.type);
        count=(TextView) v.findViewById(R.id.ratingcount);

        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        callButton = (ImageButton) v.findViewById(R.id.imageButton2);
        websiteButton = (ImageButton) v.findViewById(R.id.imageButton4);

        if(savedInstanceState!=null)
        {
            ratings=savedInstanceState.getFloat("ratings");
            pricinglevel=savedInstanceState.getString("pricing");
            title=savedInstanceState.getString("title");
            homepage=savedInstanceState.getString("homepage");
            call=savedInstanceState.getString("call");
            address=savedInstanceState.getString("address");
            id=savedInstanceState.getString("id");
            image=savedInstanceState.getString("image");
            photo=savedInstanceState.getString("photo");
            reviewcount=savedInstanceState.getString("reviewcount");

            rating.setEnabled(true);
            if(photo!=null) {
                Uri uri = Uri.parse("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + photo + "&sensor=false&maxheight=1600&maxwidth=1600&key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI");
                iv.setImageURI(uri);
            }
            else
            {
                Uri uri = Uri.parse("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + image + "&sensor=false&maxheight=1600&maxwidth=1600&key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI");
                iv.setImageURI(uri);
            }
            rating.setRating(ratings);
            name.setText(title);
            description.setText(overview);
            ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),
                    R.layout.activity_listview, text);
            listView.setAdapter(adapter);
        }
        else
        {
            if(((MainActivity)getActivity()).id !=null)
            {
                id=((MainActivity)getActivity()).id;
                ((MainActivity)getActivity()).id=null;
            }
            else {
                no = ((MainActivity) getActivity()).movieno;
                id = ((MainActivity) getActivity()).movieList.get(no).getId();
            }
            new JSONP3().execute(id);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null) {
                    ((MainActivity) getActivity()).onBackPressed();
                }
                else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        Bundle b = getArguments();     if (b != null) {
        String transitionName = b.getString("transitionName");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            name.setTransitionName(transitionName);
        }
    }
        return v;
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu , MenuInflater inflater ) {

        inflater.inflate(R.menu.share, menu);

        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu . findItem (R.id.action_share ));
        if( mShareActionProvider != null) {
            Intent intentShare = new Intent( Intent.ACTION_SEND );
            intentShare . setType ("text / plain ");
            intentShare . putExtra ( Intent . EXTRA_TEXT , "Message") ;

            mShareActionProvider.setShareIntent(intentShare);
        }
        super . onCreateOptionsMenu ( menu , inflater );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_share)
        {}
        return super.onOptionsItemSelected(item);
    }

    class JSONP3 extends AsyncTask<Object,Void,String>
    {
        String jsonurl,jsonstring,id;
        RecyclerviewFragment fragment;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... params) {
            id=(String)params[0];
            jsonurl="https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI&placeid="+id;

            String data="";
            try {
                URL url=new URL(jsonurl);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("GET");

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

                JSONObject jsonRootObject = new JSONObject(stringBuilder.toString());
                JSONObject jsonObject = jsonRootObject.optJSONObject("result");

                address = jsonObject.optString("formatted_address").toString();
                call= jsonObject.optString("formatted_phone_number").toString();
                title= jsonObject.optString("name").toString();
                id=jsonObject.optString("place_id").toString();
                ratings=Float.parseFloat(jsonObject.optString("rating").toString());
                homepage=jsonObject.optString("website").toString();
                if(homepage==null)
                {
                    homepage=jsonObject.optString("url").toString();
                }
                pricinglevel=jsonObject.optString("price_level").toString();

                JSONObject  geometry = jsonObject.optJSONObject("geometry");
                JSONObject  location = geometry.optJSONObject("location");
                latitude2 = Double.parseDouble(location.optString("lat").toString());
                longitude2=Double.parseDouble(location.optString("lng").toString());



                JSONArray jsonArray2 = jsonObject.optJSONArray("photos");
                JSONObject jsonObject2=jsonArray2.getJSONObject(0);
                image=jsonObject2.optString("photo_reference").toString();
                JSONObject jsonObject3=jsonArray2.getJSONObject(1);
                photo=jsonObject3.optString("photo_reference").toString();

                JSONArray jsonArray3 = jsonObject.optJSONArray("reviews");
                reviewcount=String.valueOf(jsonArray3.length());

                authorname = new String[jsonArray3.length()];
                authorrating = new String[jsonArray3.length()];
                text = new String[jsonArray3.length()];
                relativetime = new String[jsonArray3.length()];

                for(int j=0; j < jsonArray3.length(); j++) {
                    JSONObject jsonObject4 = jsonArray3.getJSONObject(j);
                    authorname[j] = jsonObject4.optString("author_name").toString();
                    authorrating[j] = jsonObject4.optString("rating").toString();
                    text[j] = jsonObject4.optString("text").toString();
                    relativetime[j] = jsonObject4.optString("relative_time_description").toString();
                }
                Log.d("hello",String.valueOf(jsonArray3.length()));
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return  data;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            rating.setEnabled(true);

            if(photo!=null) {
                Uri uri = Uri.parse("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + photo + "&sensor=false&maxheight=1600&maxwidth=1600&key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI");
                iv.setImageURI(uri);
            }
            else
            {
                Uri uri = Uri.parse("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + image + "&sensor=false&maxheight=1600&maxwidth=1600&key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI");
                iv.setImageURI(uri);
            }

            ratingText.setText(String.valueOf(ratings));
            count.setText("Review Count-"+reviewcount);
            rating.setRating(ratings);
            name.setText(title);
            address=address.replaceAll(", ",",\n");
            placeAddress.setText("Address-\n"+address);
            if(pricinglevel.contains("0")) {
                pricing.setText("Pricing level-Cheap");
            }
            else if(pricinglevel.contains("1")) {
                pricing.setText("Pricing level-Inexpensive");
            }
            else if(pricinglevel.contains("2")) {
                pricing.setText("Pricing level-Affordable");
            }else if(pricinglevel.contains("3")) {
                pricing.setText("Pricing Level-Expensive");
            }
            else if(pricinglevel.contains("4")) {
                pricing.setText("Pricing Level-Very Expensive");
            }
           else {
                pricing.setText("Pricing Level-Cheap");
            }

            //rating.setEnabled(false);

            ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),
                    R.layout.activity_listview, text);
            listView.setAdapter(adapter);
            callButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+call));
                    startActivity(intent);
                }
            });

            floatingActionButton.setOnClickListener(new View.OnClickListener()
            {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view)
                {
                    Intent intent1 = new Intent(getContext(),MapsActivity.class);
                    startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

                }
            });

            websiteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(homepage));
                    startActivity(i);;
                }
            });

        }

    }
}
