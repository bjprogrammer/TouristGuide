package bjasuja.syr.edu.touristguide;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SplitToolbar extends Toolbar {
    public SplitToolbar(Context context) {
        super(context);
    }

    public SplitToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SplitToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (child instanceof ActionMenuView) {
            params.width = LayoutParams.MATCH_PARENT;


            child.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }
        super.addView(child, params);
    }
}