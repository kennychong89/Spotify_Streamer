package com.nano.kenny.spotifystreamer_final.utils;

/**
 * Created by Kenny on 7/12/2015.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import com.squareup.picasso.Transformation;
/**
 * Creates rounded border around an image.
 * Original code found at:
 * http://stackoverflow.com/questions/22363515/rounded-corners-with-picasso
 */
public class RoundedTransformation implements Transformation {
    private final int radius;
    private final int margin;

    public RoundedTransformation(final int radius, final int margin) {
        this.radius = radius;
        this.margin = margin;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(source.getWidth(),
                source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF rectF = new RectF(margin, margin,
                source.getWidth() - margin, source.getHeight() - margin);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        if (source != output) {
            source.recycle();
        }
        return output;
    }

    @Override
    public String key() {
        return "rounded";
    }
}
