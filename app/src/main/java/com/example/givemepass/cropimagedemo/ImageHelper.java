package com.example.givemepass.cropimagedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;

/**
 * Created by rick.wu on 2016/9/9.
 */
public class ImageHelper {
    public static Bitmap getBitmapFromPath(String photoPath){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(photoPath, options);
    }

    public static Bitmap fixBitmapRotate(Context context, String pathName, Bitmap bitmap) {
        int rotate = getCameraPhotoOrientation(context, pathName);

        if (0 != rotate) {
            bitmap = rotate(bitmap, rotate);
        }

        return bitmap;
    }

    public static int getCameraPhotoOrientation(Context context, String pathName) {
        int rotate = 0;

        try {
            context.getContentResolver().notifyChange(Uri.parse(pathName), null);
            File imageFile = new File(pathName);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rotate;
    }

    public static Bitmap rotate(Bitmap bitmap, int rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }
}
