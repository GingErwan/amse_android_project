package fr.imt_lille_douai.amse_android_project;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.animation.Animator;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private float screenWidth;
    private float screenHeight;

    private boolean joystickIsPressed = false;
    private boolean gameLost = false;

    private ImageView imgJoystick;
    private ImageView imgJoystickExt;
    private ImageView imgTie;
    private ImageView img_asteroid1;
    private ImageView img_asteroid2;
    private ImageView img_asteroid3;
    private ImageView img_asteroid4;
    private TextView txt_perdu;
    private Button btn_perdu;
    private Switch sw_gyroOn;

    private Handler hMovingTie;
    private Handler hCollision;

    private SensorManager mSensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        imgTie = (ImageView)findViewById(R.id.img_tie);
        imgJoystick = (ImageView)findViewById(R.id.img_pad_center);
        imgJoystickExt = (ImageView)findViewById(R.id.img_pad_exterior);
        img_asteroid1 = (ImageView)findViewById(R.id.img_asteroid1);
        img_asteroid2 = (ImageView)findViewById(R.id.img_asteroid2);
        img_asteroid3 = (ImageView)findViewById(R.id.img_asteroid3);
        img_asteroid4 = (ImageView)findViewById(R.id.img_asteroid4);
        txt_perdu = (TextView)findViewById(R.id.txt_perdu);
        btn_perdu = (Button)findViewById(R.id.btn_perdu);
        sw_gyroOn = (Switch)findViewById(R.id.sw_gyroOn);

        hMovingTie = new Handler();
        hCollision = new Handler();

        Display screensize = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screensize.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        diagonalTranslation(img_asteroid1, 6000, "translationX", "translationY", 700);
        translation(img_asteroid3, 5000, "translationY", 1500);
        ellipse(img_asteroid2, 13000, 359f, 0f, 0f, 1000f, 2000f, 359f);
        ellipse(img_asteroid4, 15000, -359f, 50f, 100f, 700f, 700f, 0f);

        Runnable checkCollison = new Runnable() {
            @Override
            public void run() {
                if(!gameLost){
                    animationCollision(imgTie, img_asteroid1);
                    animationCollision(imgTie, img_asteroid2);
                    animationCollision(imgTie, img_asteroid3);
                    animationCollision(imgTie, img_asteroid4);

                    if(!gameLost){
                        hCollision.postDelayed(this, 10);
                    }else{
                        txt_perdu.setVisibility(View.VISIBLE);
                        btn_perdu.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        checkCollison.run();

        imgJoystick.setOnTouchListener(new View.OnTouchListener() {

            float originX;
            float originY;

            float radiusJoystickExt;
            float radiusJoystickInt;

            float xm;
            float ym;

            float varJoyX;
            float varJoyY;

            float tieX;
            float tieY;

            @Override
            public boolean onTouch(View v, MotionEvent event){

                    switch(event.getAction()){

                        case MotionEvent.ACTION_DOWN:
                            originX = imgJoystick.getX();
                            originY = imgJoystick.getY();

                            radiusJoystickExt = imgJoystickExt.getHeight()/2;
                            radiusJoystickInt = imgJoystick.getHeight()/2;

                            joystickIsPressed = true;
                            break;


                        case MotionEvent.ACTION_MOVE:

                            if(!gameLost){
                                //Instantanate coordonate of the inner circle on the screen
                                xm = event.getX() + v.getX() - radiusJoystickInt;
                                ym = event.getY() + v.getY() - radiusJoystickInt;

                                if (joystickIsPressed){

                                    float displacement = (float) Math.sqrt(Math.pow(xm - originX, 2) + Math.pow(ym - originY, 2));

                                    if(displacement < radiusJoystickExt){
                                        imgJoystick.setX(xm);
                                        imgJoystick.setY(ym);
                                    } else {
                                        imgJoystick.setX((xm-originX)/displacement*radiusJoystickExt+originX);
                                        imgJoystick.setY((ym-originY)/displacement*radiusJoystickExt+originY);
                                    }
                                }

                                Runnable movingTie = new Runnable() {
                                    @Override
                                    public void run() {
                                        varJoyX = (originX - imgJoystick.getX())/1500;
                                        varJoyY = (originY - imgJoystick.getY())/1500;

                                        tieX = imgTie.getX() - varJoyX;
                                        tieY = imgTie.getY() - varJoyY;

                                        //Set the Tie to the bounds of the screen
                                        tieX = tieX + imgTie.getWidth()/2 < 0 ? -imgTie.getWidth()/2 : tieX;
                                        tieX = tieX + imgTie.getWidth()/2 > screenWidth ? screenWidth - imgTie.getWidth()/2 : tieX;

                                        tieY = tieY + imgTie.getHeight()/2 < 0 ? -imgTie.getHeight()/2 : tieY;
                                        tieY = tieY + imgTie.getHeight() > screenHeight ? screenHeight - imgTie.getHeight() : tieY;

                                        imgTie.setX(tieX);
                                        imgTie.setY(tieY);

                                        if (joystickIsPressed) {
                                            hMovingTie.postDelayed(this, 10);
                                        }
                                    }
                                };

                                movingTie.run();

                            }else{
                                v.setX(originX);
                                v.setY(originY);
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                            v.setX(originX);
                            v.setY(originY);
                            joystickIsPressed = false;
                            break;

                        default:
                            break;
                    }

                return true;
            }
        });

    }

    public void translation (View v, int duration, String type, float length) {
        v.setY(0);
        ObjectAnimator animation = ObjectAnimator.ofFloat(v, type, length);
        animation.setDuration(duration);
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.start();

    }

    public void diagonalTranslation (View v, int duration, String type1, String type2, float length) {
        v.setY(300f);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(type1, length);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(type2, length);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, pvhX, pvhY);
        animator.setDuration(duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();

    }

    public void ellipse (View v, int duration, float SweepAngle, float left, float top, float right, float bottom, float startAngle) {
        Path path = new Path();
        path.arcTo(left, top, right, bottom, startAngle,  SweepAngle, true);
        ObjectAnimator trajectoireElliptique = ObjectAnimator.ofFloat(v, View.X, View.Y, path);
        trajectoireElliptique.setDuration(duration);
        trajectoireElliptique.setRepeatCount(Animation.INFINITE);
        trajectoireElliptique.start();
    }

    boolean Collision(ImageView firstView, ImageView secondView){
        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];

        int imgTieW = firstView.getMeasuredWidth();
        int imgTieH = firstView.getMeasuredHeight();
        int imgAstW = secondView.getMeasuredWidth();
        int imgAstH = secondView.getMeasuredHeight();

        firstView.getLocationOnScreen(firstPosition);
        secondView.getLocationOnScreen(secondPosition);

        Rect rectFirstView = new Rect(firstPosition[0] + imgTieW/10, firstPosition[1] + imgTieH/4, firstPosition[0] + imgTieW - imgTieW/10, firstPosition[1] + imgTieH - imgTieH/4);
        Rect rectSecondView = new Rect(secondPosition[0], secondPosition[1], secondPosition[0] + imgAstW, secondPosition[1] + imgAstH);

        return(rectFirstView.intersect(rectSecondView));
    }

    public void animationCollision (ImageView firstView, ImageView secondView){
        if (Collision(firstView, secondView)){
            ImageView Explosion = (ImageView) findViewById(R.id.img_explosion);
            int explosionX = Explosion.getWidth()/2;
            int explosionY = Explosion.getHeight()/2;
            this.gameLost = true;
            Explosion.setX(firstView.getX());
            Explosion.setY(firstView.getY());
            float finalRadius = (float) Math.hypot(explosionX, explosionY);
            Animator animation = ViewAnimationUtils.createCircularReveal(Explosion, explosionX, explosionY, 0f, finalRadius);
            Explosion.setVisibility(View.VISIBLE);
            animation.start();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        float gammaX = event.values[0], gammaY = event.values[1];

        if(!gameLost && sw_gyroOn.isChecked()){
            float tieX;
            float tieY;

            tieX = imgTie.getX()-gammaX*5;
            tieY = imgTie.getY()+gammaY*5;

            //Set the Tie to the bounds of the screen
            tieX = tieX + imgTie.getWidth()/2 < 0 ? -imgTie.getWidth()/2: tieX;
            tieX = tieX + imgTie.getWidth()/2 > screenWidth ? screenWidth -imgTie.getWidth()/2 : tieX;

            tieY = tieY + imgTie.getHeight()/2 < 0 ? -imgTie.getHeight()/2 : tieY;
            tieY = tieY + imgTie.getHeight() > screenHeight ? screenHeight -imgTie.getHeight() : tieY;


            imgTie.setX(tieX);
            imgTie.setY(tieY);
        }

    }

    public void resetGame(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}