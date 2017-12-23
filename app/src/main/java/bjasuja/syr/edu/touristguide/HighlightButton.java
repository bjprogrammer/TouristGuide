package bjasuja.syr.edu.touristguide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.support.v7.widget.AppCompatButton;


public class HighlightButton extends AppCompatButton{

    public HighlightButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

    }
    public HighlightButton(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public HighlightButton(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            this.setAlpha((int)( 0.6 ));
        }else if(e.getAction() == MotionEvent.ACTION_UP){
            this.setAlpha((int)( 1.0 ));
        }

        return super.onTouchEvent(e);

    }

}
