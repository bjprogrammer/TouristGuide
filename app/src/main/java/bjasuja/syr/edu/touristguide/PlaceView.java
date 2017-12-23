package bjasuja.syr.edu.touristguide;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class PlaceView extends AppCompatImageView {

    private int defaultBorderColor;
    private int defaultBorderWidth;
    private int defaultTextSizePercentage;

    private int borderColor;
    private int borderWidth;
    private int textSizePercentage;

    private int viewSize;
    private Drawable drawable;

    int circleRadius;
    int circleCenterXValue;
    int circleCenterYValue;

    private Paint borderPaint = new Paint();
    private Paint mainPaint = new Paint();
    private Rect circleRect;

    public PlaceView(Context context) {
        super(context);
        init(context, null);
    }

    public PlaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PlaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @Override
    public void onDraw(Canvas canvas) {
        saveBasicValues(canvas);

        if (viewSize == 0) {
            return;
        }

        Bitmap bitmap = cutIntoCircle(drawableToBitmap(drawable));

        if (bitmap == null) {
            return;
        }

        canvas.translate(circleCenterXValue, circleCenterYValue);

        //Draw Border
        canvas.drawCircle(circleRadius + borderWidth, circleRadius + borderWidth, circleRadius + borderWidth, borderPaint);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    public int textSizePercentage() {
        return textSizePercentage;
    }

    private void init(Context context, AttributeSet attrs) {
        setDefaultBorderValues();

        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.AvatarView,
                    0, 0);
            try {
                configureBorderValues(typedArray);
            } finally {
                typedArray.recycle();
            }
        }

        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setColor(borderColor);

        mainPaint.setAntiAlias(true);
        mainPaint.setColor(getResources().getColor(R.color.av_bitmap_background_color));
        mainPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        try {
            Bitmap bitmap = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, viewSize, viewSize);
            drawable.draw(canvas);

            return bitmap;
        } catch (OutOfMemoryError error) {

            return null;
        }
    }

    private Bitmap cutIntoCircle(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        try {
            Bitmap output = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawCircle(circleRadius + borderWidth, circleRadius + borderWidth, circleRadius, borderPaint);

            canvas.drawBitmap(bitmap, circleRect, circleRect, mainPaint);
            return output;
        } catch (OutOfMemoryError error) {

            return null;
        }
    }

    private void setDefaultBorderValues() {
        defaultBorderColor = getResources().getColor(R.color.av_default_border);
        defaultBorderWidth = getResources().getDimensionPixelSize(R.dimen.av_default_border_width);
        defaultTextSizePercentage = AvatarPlaceholder.DEFAULT_TEXT_SIZE_PERCENTAGE;
    }

    private void configureBorderValues(TypedArray typedArray) {
        borderColor = typedArray.getColor(R.styleable.AvatarView_av_border_color, defaultBorderColor);
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.AvatarView_av_border_width, defaultBorderWidth);
        textSizePercentage = typedArray.getInt(R.styleable.AvatarView_av_text_size_percentage, defaultTextSizePercentage);
    }

    private void saveBasicValues(Canvas canvas) {
        int viewHeight = canvas.getHeight();
        int viewWidth = canvas.getWidth();

        viewSize = Math.min(viewWidth, viewHeight);

        circleCenterXValue = (viewWidth - viewSize) / 2;
        circleCenterYValue = (viewHeight - viewSize) / 2;
        circleRadius = (viewSize - (borderWidth * 2)) / 2;

        circleRect = new Rect(0, 0, viewSize, viewSize);

        maximizeAvailableBorderSize();

        if (viewSize != 0) {
            drawable = getDrawable();
        }
    }

    void maximizeAvailableBorderSize() {
        if (viewSize / 3 < borderWidth) {
            borderWidth = viewSize / 3;
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }
}

