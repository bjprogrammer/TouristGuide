package bjasuja.syr.edu.touristguide;



import bjasuja.syr.edu.touristguide.POJO.Example;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;


public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyA5wjM9aabJ2FgjEHwx5ZPc1k40NZoJvpI")
    Call<Example> getNearbyPlaces(@Query("types") String type, @Query("location") String location, @Query("radius") int radius);

}