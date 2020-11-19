package com.example.jbzdlite;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.jbzdlite.Util.println;

public class VideoManager implements TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, Runnable {

    private ImageView image;
    private ImageView icon;
    private TextureView texture;
    private VideoArticle article;
    private int width;
    private int height;

    private Handler handler;
    private Lock lock = new ReentrantLock();
    private Condition preparedCondition = lock.newCondition();
    private boolean playerPrepared;

    private MediaPlayer player;
    private Thread visibilityThread;

    public VideoManager(ImageView image, TextureView texture, ImageView icon, VideoArticle article) {
        this.image = image;
        this.texture = texture;
        this.icon = icon;
        this.article = article;
        this.playerPrepared = false;
        handler = new Handler(texture.getContext().getMainLooper());

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        texture.setSurfaceTextureListener(this);

        if(texture.isAvailable())
            onSurfaceTextureAvailable(texture.getSurfaceTexture(), texture.getWidth(), texture.getHeight());
    }

    public void prepareToStart() {
        texture.setOnClickListener(null);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        texture.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);
        Picasso.get().load(article.getPreviewUrl()).placeholder(R.drawable.loading_image).into(image);
        icon.setImageResource(R.drawable.play_icon);
        icon.setVisibility(View.VISIBLE);
    }

    private void start() {
        image.setOnClickListener(null);
        image.setVisibility(View.GONE);
        texture.setVisibility(View.VISIBLE);

        icon.setImageResource(R.drawable.loading_image_s);

        width = image.getWidth();
        height = image.getHeight();

        ViewGroup.LayoutParams params = texture.getLayoutParams();
        params.width = width;
        params.height = height;
        texture.setLayoutParams(params);

        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    while(playerPrepared == false)
                        preparedCondition.await();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        icon.setVisibility(View.GONE);
                        player.start();
                    }
                });
            }
        }).start();

        texture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });

        //visibilityThread = new Thread(this);
        //visibilityThread.start();
    }

    private void pause() {
        player.pause();
        icon.setImageResource(R.drawable.play_icon);
        icon.setVisibility(View.VISIBLE);

        texture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resume();
            }
        });
    }

    private void resume() {
        player.start();
        icon.setVisibility(View.GONE);

        texture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        println("Available");
        try {
            player.setDataSource(article.getVideoUrl());
            player.setSurface(new Surface(surfaceTexture));
            player.prepareAsync();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        println("Prepared");
        lock.lock();
        try {
            playerPrepared = true;
            preparedCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean isVisible(final View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)texture.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (view == null) {
            return false;
        }
        if (!view.isShown()) {
            return false;
        }
        final Rect actualPosition = new Rect();
        view.getGlobalVisibleRect(actualPosition);
        //println(actualPosition.top);
        final Rect screen = new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        return actualPosition.intersect(screen);
    }

    @Override
    public void run() {
        println("Run start");
        try {
            while (!Thread.interrupted()) {
                boolean visible = isVisible(texture);
                if(!visible) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            println("Run hide detected");
                            player.pause();
                            prepareToStart();
                        }
                    });
                    break;
                }

                TimeUnit.MILLISECONDS.sleep(10);
            }
        } catch(InterruptedException e) {

        }
        println("Run end");
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        println("Completion");
        visibilityThread.interrupt();
        prepareToStart();
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) { }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) { return false; }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) { }
}
