package bjasuja.syr.edu.touristguide;

import android.support.annotation.NonNull;

public class Movie implements Comparable {
    private String title, description,rating;
    String image;
    static Boolean flag=false,flag2=false,flag3=false,flag4=false,flag5=false,flag6=false,flag7=false;
    String id,pricing,opennow;
    public Movie() {
    }

    public Movie(String title, String image, String id,String rating,String pricing,String opennow) {
        this.title = title;
        this.rating = rating;
        this.image=image;
        this.id=id;
        this.pricing=pricing;
        this.opennow=opennow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public Double getrating() {
        if(rating==null || rating.isEmpty())
        return 0.0;
        else
        {
            return Double.parseDouble(rating);
        }
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getOpenNow() {
        return opennow;
    }
    public void setOpenNow(String opennow) {
        this.opennow = opennow;
    }
    public void setPricing(String pricing) {
        this.pricing =pricing;
    }
    public String getPricing() {
        return pricing;
    }


    @Override
    public int compareTo(@NonNull Object another) {
        if(!flag) {
            if(rating==null || rating.isEmpty())
                rating="0.0";
            if (((Movie) another).getrating() > Double.parseDouble(rating)){
                return 1;
            }
            if ((((Movie) another).getrating()) == Double.parseDouble(rating)) {
                return 0;
            } else {
                return -1;
            }
        }
        else
        {
            String s=(String)(((Movie) another).getTitle());
            if  (s.compareTo(title)<0 )
            {
                return 1;
            }
            if ((String)(((Movie) another).getTitle()) == (title)) {
                return 0;
            } else {
                return -1;
            }

        }
    }
}
