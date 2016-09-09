package com.example.givemepass.cropimagedemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.util.concurrent.Executors;

/**
 * Created by rick.wu on 2016/9/9.
 */
public class CropDialog extends Dialog{
    private String mPath;
    private ImageView mBack;
    private CropImageView mCropImage;
    private ImageView mCropCheck;
    private Context mContext;
    private OnCropFinishListener mOnCropFinishListener;
    public interface OnCropFinishListener{
        void onCrop(String path);
    }
    public void setOnCropFinishListener(OnCropFinishListener listener){
        mOnCropFinishListener = listener;
    }
    public CropDialog(Context context, String path) {
        super(context, R.style.AppTheme);
        mPath = path;
        mContext = context;
        setContentView(R.layout.crop_image_layout);
        init();
    }

    private void init(){
        mCropImage = (CropImageView) findViewById(R.id.crop_img);
        mBack = (ImageView) findViewById(R.id.pick_image_back);
        mCropCheck = (ImageView) findViewById(R.id.pick_image_select_check);
        mCropImage.setDrawable(mPath, 300, 300);
        setListener();
    }

    private void setListener(){
        mBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
            }

        });

        mCropCheck.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        final String path = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/crop.png";
                        FileUtil.writeImage(mCropImage.getCropImage(), path, 100);
                        ((Activity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mOnCropFinishListener != null){
                                    mOnCropFinishListener.onCrop(path);
                                }
                                dismiss();
                            }
                        });
                    }
                });
            }

        });
    }
}
