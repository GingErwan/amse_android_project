package fr.imt_lille_douai.amse_android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.Rect;
import android. os. Bundle;
import android. app.Activity ;
import android. view. Menu;
import android. view. View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android. widget.ImageView;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import java.time.Duration;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img_asteroid1 = (ImageView)findViewById(R.id.img_asteroid1);
        ImageView img_asteroid2 = (ImageView)findViewById(R.id.img_asteroid2);
        ImageView img_asteroid3 = (ImageView)findViewById(R.id.img_asteroid3);
        ImageView img_asteroid4 = (ImageView)findViewById(R.id.img_asteroid4);
        diagonalTranslation(img_asteroid1, 6000, "translationX", "translationY", 600);
        translation(img_asteroid3, 3000, "translationY", 400);
        ellipse(img_asteroid2, 13000, 359f, 0f, 0f, 1000f, 1000f);
        ellipse(img_asteroid4, 10000, -359f, 50f, 100f, 700f, 700f);
        allRotations(img_asteroid1, 5000, img_asteroid2, 6000, img_asteroid3, 4000, img_asteroid4, 10000);

    }


    public void translation (View v, int duration, String type, float length) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(v, type, length);
        animation.setDuration(duration);
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.start();

    }

    public void diagonalTranslation (View v, int duration, String type1, String type2, float length) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(type1, length);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(type2, length);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, pvhX, pvhY);
        animator.setDuration(duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();

    }


    public void allRotations (View Asteroid1, float durationAsteroid1, View Asteroid2, float durationAsteroid2, View Asteroid3, float durationAsteroid3, View Asteroid4, float durationAsteroid4){
        rotation(Asteroid1, 14000);
        rotation(Asteroid2, 6000);
        rotation(Asteroid3, 8000);
        rotation(Asteroid4, 7000);
    }


    public void rotation( View v, int duration){
        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "rotation", 360);
        animation.setDuration(duration);
        animation.start();
        animation.setRepeatCount(Animation.INFINITE);

    }

    public void ellipse (View v, int duration, float SweepAngle, float left, float top, float right, float bottom) {
        Path path = new Path();
        path.arcTo(left, top, right, bottom, 0f,  SweepAngle, true);
        ObjectAnimator trajectoireElliptique = ObjectAnimator.ofFloat(v, View.X, View.Y, path);
        trajectoireElliptique.setDuration(duration);
        trajectoireElliptique.setRepeatCount(Animation.INFINITE);
        trajectoireElliptique.start();
    }

    boolean Collision(ImageView firstView, ImageView secondView){
        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];
        firstView.getLocationOnScreen(firstPosition);
        secondView.getLocationOnScreen(secondPosition);

        Rect rectFirstView = new Rect(firstPosition[0], firstPosition[1], firstPosition[0] + firstView.getMeasuredWidth(), firstPosition[1] + firstView.getMeasuredHeight());
        Rect rectSecondView = new Rect(secondPosition[0], secondPosition[1], secondPosition[0] + secondView.getMeasuredWidth(), secondPosition[1] + secondView.getMeasuredHeight());

        return(rectFirstView.intersect(rectSecondView));
    }

}