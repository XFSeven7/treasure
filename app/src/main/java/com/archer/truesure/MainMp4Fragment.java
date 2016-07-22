package com.archer.truesure;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.archer.truesure.common.ActivityUtils;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Author: qixuefeng on 2016/7/22 0022.
 * E-mail: 377289596@qq.com
 */
public class MainMp4Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    private TextureView textureView;
    private MediaPlayer mediaPlayer;

    private ActivityUtils activityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        textureView = new TextureView(getContext());
        return textureView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textureView.setSurfaceTextureListener(this);
        activityUtils = new ActivityUtils(getActivity());
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {

        try {

            AssetFileDescriptor afd = getContext().getAssets().openFd("welcome.mp4");
            FileDescriptor fileDescriptor = afd.getFileDescriptor();

            mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(fileDescriptor, afd.getStartOffset(), afd.getLength());

            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Surface surface1 = new Surface(surface);
                    mediaPlayer.setSurface(surface1);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }
            });


        } catch (IOException e) {
            activityUtils.showToast("播放失败");
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
