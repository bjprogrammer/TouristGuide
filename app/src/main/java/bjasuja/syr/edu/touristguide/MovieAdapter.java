package bjasuja.syr.edu.touristguide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static bjasuja.syr.edu.touristguide.R.id.ratingBar;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private static ClickListener clickListener;
    Bitmap bitmap;
    private List<Movie> moviesList;
    Context context;
    RatingBar ratingBar;
    ImageView iv1,iv2,iv3,iv4;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener {
        public TextView title, description, rating,opennow,pricing;
        ImageView iv,overflow;
        CheckBox cb;
        CardView cv;


        public MyViewHolder(View view) {
            super(view);
            cv=(CardView) view.findViewById(R.id.cv);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            rating = (TextView) view.findViewById(R.id.rating);
            iv=(ImageView)view.findViewById(R.id.imageView10);
            overflow=(ImageView) view.findViewById(R.id.iv);
            opennow=(TextView) view.findViewById(R.id.opennow);
            pricing=(TextView) view.findViewById(R.id.pricing);
            ratingBar=(RatingBar) view.findViewById(R.id.ratingBar);

            iv1=(ImageView)view.findViewById(R.id.imageView11);
            iv2=(ImageView)view.findViewById(R.id.imageView12);
            iv3=(ImageView)view.findViewById(R.id.imageView13);
            iv4=(ImageView)view.findViewById(R.id.imageView14);


            overflow.setOnClickListener(this);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId()==R.id.iv) {
                clickListener.onOverflowMenuClick(getAdapterPosition(), v);
            }
            else
            {
                clickListener.onItemClick(getAdapterPosition(), v,moviesList);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return true;
        }
    }

    public MovieAdapter(List<Movie> moviesList,Context context) {
        this.moviesList = moviesList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        Glide.with(context).load("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + movie.getImage() + "&sensor=false&maxheight=1600&maxwidth=1600&key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI")
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .into(holder.iv);
        holder.rating.setText(String.valueOf(movie.getrating()));
        ratingBar.setRating(Float.parseFloat(movie.getrating().toString()));
        if (movie.getOpenNow().contains("true")) {
            holder.opennow.setTextColor(Color.GREEN);
            holder.opennow.setText("Open");
            iv4.setImageResource(R.drawable.ic_lock_open_black_24dp);

        } else {
            holder.opennow.setText("Closed");
            holder.pricing.setTextColor(Color.parseColor("#cc0202"));
            iv4.setImageResource(R.drawable.ic_lock_black_24dp);
        }

        if (movie.getPricing().contains("0")) {
            holder.pricing.setTextColor(Color.GREEN);
            holder.pricing.setText("Cheap");
            iv1.setImageResource(R.drawable.ic_money_off_black_24dp);
            iv1.setAlpha(1.0f);
            iv2.setAlpha(0.0f);
            iv3.setAlpha(0.0f);
        } else if (movie.getPricing().contains("1")) {

            holder.pricing.setTextColor(Color.GREEN);
            holder.pricing.setText("Inexpensive");
            iv1.setImageResource(R.drawable.ic_money_off_black_24dp);
            iv1.setAlpha(1.0f);
            iv2.setAlpha(0.0f);
            iv3.setAlpha(0.0f);

        } else if (movie.getPricing().contains("2")) {
            holder.pricing.setTextColor(Color.parseColor("#cc0202"));
            holder.pricing.setText("Affordable");
            iv1.setAlpha(1.0f);
            iv2.setAlpha(0.0f);
            iv3.setAlpha(0.0f);

        } else if (movie.getPricing().contains("3") || movie.getPricing().isEmpty()) {
            holder.pricing.setTextColor(Color.parseColor("#cc0202"));

            holder.pricing.setText("Expensive");
            iv1.setAlpha(1.0f);
            iv2.setAlpha(1.0f);
            iv3.setAlpha(0.0f);
        } else if (movie.getPricing().contains("4")) {
            holder.pricing.setTextColor(Color.parseColor("#cc0202"));

            holder.pricing.setText("Very Expensive");
            iv1.setAlpha(1.0f);
            iv2.setAlpha(1.0f);
            iv3.setAlpha(1.0f);

        }
        else
        {
            holder.pricing.setTextColor(Color.GREEN);
            holder.pricing.setText("Cheap");
            iv1.setImageResource(R.drawable.ic_money_off_black_24dp);
            iv1.setAlpha(1.0f);
            iv2.setAlpha(0.0f);
            iv3.setAlpha(0.0f);
        }
        holder.rating.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.iv.setTransitionName("transition" + position+ViewPagerFragment.pageno);     }

}

    public void setOnItemClickListener(ClickListener clickListener) {
        MovieAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(int position, View v,List<Movie> movielist);
        void onItemLongClick(int position, View v);
        void onOverflowMenuClick(int position, View v);
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    @Override
    public int getItemViewType(int position) {

          return  super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }



}


