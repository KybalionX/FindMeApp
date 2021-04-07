package com.dvlpr.findme.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;

public class ImageBitmapPerfector {

    Context context;

    public ImageBitmapPerfector(Context context) {
        this.context = context;
    }

    public Bitmap FixCompressImage(Bitmap bitmap, int CompressLevel){
        Bitmap bm = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/CompressLevel, bitmap.getHeight()/CompressLevel, true);
        return bm;
    }

}
