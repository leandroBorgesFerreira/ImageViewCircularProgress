package br.com.simplepass.circularprogressanimation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.simplepass.circularprogressimagelib.CircularAnimatedDrawableImage;
import br.com.simplepass.circularprogressimagelib.CircularProgressImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CircularProgressImageView progressImageView = (CircularProgressImageView)
                findViewById(R.id.progress_image);

        progressImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();

                progressImageView.startAnimation();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        progressImageView.doneLoagingAnimation(R.color.colorAccent,
                                BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                    }
                };

                handler.postDelayed(runnable, 1500);
            }
        });
    }



}
