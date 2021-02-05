package fr.imt_lille_douai.amse_android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Path;
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
        translation(img_asteroid1, 8000, "translationX", 300);
        translation(img_asteroid3, 3000, "translationY", 400);
        ellipse1(img_asteroid2, 13000);
        rotation(img_asteroid1, 14000);
        rotation(img_asteroid2, 6000);
        rotation(img_asteroid3, 8000);
        rotation(img_asteroid4, 10000);

    }


    public void translation (View v, int duration, String type, float length) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(v, type, length);
        animation.setDuration(duration);
        ObjectAnimator animation_retour = ObjectAnimator.ofFloat(v, type, -length);
        animation_retour.setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animation_retour).after(animation);
        animatorSet.start();

    }






    public void rotation( View v, int duration){
        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "rotation", 360);
        animation.setDuration(duration);
        animation.start();
        animation.setRepeatCount(Animation.INFINITE);

    }
    public void ellipse1 (View v, int duration) {
        Path path = new Path();
        path.arcTo(0f, 0f, 1000f, 1000f, 0f,  359f, true);
        ObjectAnimator trajectoireElliptique = ObjectAnimator.ofFloat(v, View.X, View.Y, path);
        trajectoireElliptique.setDuration(duration);
        trajectoireElliptique.setRepeatCount(Animation.INFINITE);
        trajectoireElliptique.start();
    }


}