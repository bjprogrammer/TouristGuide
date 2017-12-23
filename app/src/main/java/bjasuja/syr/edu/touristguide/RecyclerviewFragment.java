package bjasuja.syr.edu.touristguide;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.pollfish.constants.Position;
import com.pollfish.main.PollFish;
import com.twitter.sdk.android.core.Twitter;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecyclerviewFragment extends Fragment {
    static List<Movie> movieList = new ArrayList<>();
    RecyclerView recyclerView;
    static MovieAdapter mAdapter;
    static Boolean mShowingBack = false;

    public RecyclerviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public interface CustomOnClickListener {
        public void onClicked ( View v,int position,List<Movie> movieList);
    }

    private CustomOnClickListener customOnClickListener;

    public void buttonClicked ( View v,int position , List<Movie> movieList) {
        customOnClickListener . onClicked (v,position,movieList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.customOnClickListener = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        mAdapter = new MovieAdapter(movieList,getContext());
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleadapter = new ScaleInAnimationAdapter(alphaAdapter);
        scaleadapter.setDuration(700);
        recyclerView.setAdapter(scaleadapter);
        //  recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MovieAdapter.ClickListener() {

            @Override
            public void onItemClick(int position, View v,List<Movie> movielist) {
                    buttonClicked(v, position,movielist);
                }


            @Override
            public void onItemLongClick(int position, View v) {
                getActivity().startActionMode(new ActionBarCallBack(position));

            }

            @Override
            public void onOverflowMenuClick(final int position, View v) {
                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.action_delete:
                                movieList.remove(position);
                                FadeInAnimator animator = new FadeInAnimator();
                                animator.setInterpolator(new OvershootInterpolator());
                                animator.setRemoveDuration(500);
                                recyclerView.setItemAnimator(animator);
                                mAdapter.notifyItemRemoved(position);

                                mAdapter.notifyItemRangeChanged(0, movieList.size());

                                return true;
                            case R.id.action_duplicate:
                                movieList.add(position + 1, movieList.get(position));
                                FlipInBottomXAnimator animator2 = new FlipInBottomXAnimator();
                                animator2.setInterpolator(new OvershootInterpolator());
                                animator2.setRemoveDuration(1000);
                                recyclerView.setItemAnimator(animator2);
                                mAdapter.notifyItemInserted(position + 1);

                                return true;
                        }
                        return false;
                    }
                });
                MenuInflater menuInflater = popup.getMenuInflater();
                menuInflater.inflate(R.menu.cab, popup.getMenu());
                popup.show();

            }

        });


        if(movieList.isEmpty()) {
            new JSONP().execute(ViewPagerFragment.s);
        }


        return v;
    }

    void prepareMovieData(Movie movie[]) {
        movieList.clear();
        for (int i = 0; i <movie.length; i++) {

            movieList.add(movie[i]);
        }
        if(mAdapter!=null)
        mAdapter.notifyDataSetChanged();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_unhide:
                MainActivity.b1.purchaseRemoveAds();
                break;

            case R.id.action_sort:
                    Movie.flag = true;
                    Collections.sort(movieList);
                    mAdapter.notifyDataSetChanged();
                    break;


            case R.id.action_logoff:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.exit, null);
                builder.setView(dialogView);
                final AlertDialog alert = builder.create();

                Button b = (Button) dialogView.findViewById(R.id.button51);
                Button b2 = (Button) dialogView.findViewById(R.id.button7);

                b2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                b.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             DatabaseHelper dh = new DatabaseHelper(getActivity());
                                             FbDatabaseHelper dh1 = new FbDatabaseHelper(getActivity());
                                             TwDatabaseHelper dh2 = new TwDatabaseHelper(getActivity());
                                             GlDatabaseHelper dh3 = new GlDatabaseHelper(getActivity());

                                             AccessToken accessToken = AccessToken.getCurrentAccessToken();
                                             TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();

                                             if (accessToken != null) {
                                                 if (SplashScreen.fblogout == true) {
                                                     SplashScreen.fblogout = false;
                                                     dh1.deletedata(1);
                                                     LoginManager.getInstance().logOut();
                                                 }
                                             }

                                             if (twitterSession != null) {
                                                 if (SplashScreen.twlogout == true) {
                                                     SplashScreen.twlogout = false;
                                                     dh2.deletedata(1);
                                                     CookieSyncManager.createInstance(getActivity());
                                                     CookieManager cookieManager = CookieManager.getInstance();
                                                     cookieManager.removeSessionCookie();
                                                     TwitterCore.getInstance().getSessionManager().clearActiveSession();
                                                 }
                                             }

                                             if (SplashScreen.logout == true) {
                                                 SplashScreen.logout = false;
                                                 dh.deletedata(1);
                                             }

                                             if (SplashScreen.gllogout == true) {
                                                 SplashScreen.gllogout = false;
                                                 dh3.deletedata(1);

                                             }
                                             Intent i = new Intent(getActivity(), Signin.class);
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
                View dialogView2 = inflater2.inflate(R.layout.termsandconditions, null);
                builder2.setView(dialogView2);
                final AlertDialog alert2 = builder2.create();


                Button b3 = (Button) dialogView2.findViewById(R.id.button7);

                b3.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              alert2.dismiss();
                                          }
                                      }
                );

                alert2.show();
                break;

            case R.id.action_map:

                Intent i = new Intent(getActivity(), MapsActivity.class);
                i.putExtra("no", ViewPagerFragment.pageno);
                //startActivity(i);
                flipCard();
                break;

            case R.id.action_search:

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    class ActionBarCallBack implements ActionMode.Callback {
        int position;

        public ActionBarCallBack(int position) {
            this.position = position;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.cab, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // HashMap movie = (HashMap) movieData.getItem(position);
            //  mode.setTitle((String) movie.get(" name "));
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem
                item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_delete:

                    movieList.remove(position);
                    FadeInAnimator animator = new FadeInAnimator();
                    animator.setInterpolator(new OvershootInterpolator());
                    animator.setRemoveDuration(500);
                    recyclerView.setItemAnimator(animator);
                    mAdapter.notifyItemRemoved(position);

                    mAdapter.notifyItemRangeChanged(0, movieList.size());

                    mode.finish();
                    break;
                case R.id.action_duplicate:
                    movieList.add(position + 1, movieList.get(position));
                    FlipInBottomXAnimator animator2 = new FlipInBottomXAnimator();
                    animator2.setInterpolator(new OvershootInterpolator());
                    animator2.setRemoveDuration(1000);
                    recyclerView.setItemAnimator(animator2);
                    mAdapter.notifyItemInserted(position + 1);

                    mode.finish();
                    break;
                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    }

    public void sortyear() {
            Movie.flag = false;
            Collections.sort(movieList);
            mAdapter.notifyDataSetChanged();


    }

    public void startsurvey() {
        if (MainActivity.surveydisabled) {
            PollFish.initWith(getActivity(), new PollFish.ParamsBuilder("5eeccef1-abd4-4f55-b2eb-bde1c5051a11").indicatorPosition(Position.MIDDLE_RIGHT)
                    .customMode(false)
                    .build());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (MainActivity.surveydisabled) {
            PollFish.initWith(getActivity(), new PollFish.ParamsBuilder("5eeccef1-abd4-4f55-b2eb-bde1c5051a11").indicatorPosition(Position.MIDDLE_RIGHT)
                    .customMode(false)
                    .build());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        startsurvey();

    }

    @Override
    public void onPause() {
        super.onPause();
        stopsurvey();
    }

    public void stopsurvey() {
        PollFish.hide();
    }

    public void flipCard() {
        if (mShowingBack) {
            getActivity().getFragmentManager().popBackStack();
            mShowingBack = false;
            return;
        }

        mShowingBack = true;
        MapsFragment mp=new MapsFragment();
        Bundle args = new Bundle();
        args.putInt("no", ViewPagerFragment.pageno);
        mp.setArguments(args);
        getActivity().getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                .replace(R.id.frame, mp)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu.findItem(R.id.action_search) == null) {

            inflater.inflate(R.menu.search2, menu);

        }

        SearchView search = (SearchView)
                menu.findItem(R.id.action_search).getActionView();

        if (search != null) {

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    int pos = -1;

                        for (int i = 0; i < movieList.size(); i++) {
                            if (org.apache.commons.lang3.StringUtils.containsIgnoreCase((String) movieList.get(i).getTitle(), query))
                                pos = i;
                        }


                    if (pos >= 0)
                        recyclerView.scrollToPosition(pos);

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    return true;
                }
            });
        }

        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };

        MenuItem actionMenuItem = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(actionMenuItem, expandListener);
    }

    void setJSON(String s)
    {
        new JSONP().execute(s);
    }

    class JSONP extends AsyncTask<String,Void,String>
    {
        String jsonurl,jsonstring;
        RecyclerviewFragment fragment;
        Movie movie[];
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pd = new ProgressDialog(getContext());
            //pd.setMessage("loading");
            //pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            while(GPSTracker.mLastLocation.getLatitude()==0)
            {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            jsonurl=params[0]+"&location="+MainActivity.latlong;

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
                JSONArray jsonArray = jsonRootObject.optJSONArray("results");
                movie = new Movie[jsonArray.length()];
                String image="www.example.com";
                String opennow="true";
                for(int i=0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String pricing = jsonObject.optString("price_level").toString();
                    String title= jsonObject.optString("name").toString();
                    String rating = jsonObject.optString("rating").toString();
                    String id=jsonObject.optString("place_id").toString();
                    JSONObject openinghours = jsonObject.optJSONObject("opening_hours");
                    if(openinghours!=null) {
                        opennow = String.valueOf(openinghours.optBoolean("open_now"));
                    }

                    JSONArray jsonArray2 = jsonObject.optJSONArray("photos");
                    if(jsonArray2!=null) {
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(0);
                        image = jsonObject2.optString("photo_reference").toString();
                    }
                    movie[i]=new Movie(title,image,id,rating,pricing,opennow);
                }
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
            prepareMovieData(movie);
            if (pd != null)
            {
               // pd.dismiss();
            }
            // new JSONP2().execute();
        }
    }
}
