package fr.imt_lille_douai.amse_android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean joystickIsPressed = false;

    private ImageView imgJoystick;
    private ImageView imgJoystickExt;
    private ImageView imgTie;

    Handler hMovingTie;

    float screenWidth;
    float screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgTie = (ImageView)findViewById(R.id.img_tie);
        imgJoystick = (ImageView)findViewById(R.id.img_pad_center);
        imgJoystickExt = (ImageView)findViewById(R.id.img_pad_exterior);

        Display screensize = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screensize.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        hMovingTie = new Handler();

        imgJoystick.setOnTouchListener(new View.OnTouchListener() {

            float originX;
            float originY;

            float radiusJoystickExt;
            float radiusJoystickInt;

            float xm;
            float ym;

            float varTieX;
            float varTieY;
            float tieX;
            float tieY;

            @Override
            public boolean onTouch(View v, MotionEvent event){

                switch(event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        radiusJoystickInt = imgJoystick.getHeight()/2;
                        originX = imgJoystick.getX();
                        originY = imgJoystick.getY();

                        radiusJoystickExt = imgJoystickExt.getHeight()/2;

                        joystickIsPressed = true;
                        break;

                    case MotionEvent.ACTION_MOVE:

                        //Instantanate coordonate of the inner circle on the screen
                        xm = event.getX() + v.getX() - radiusJoystickInt;
                        ym = event.getY() + v.getY() - radiusJoystickInt;

                        if (joystickIsPressed == true){

                            float displacement = (float) Math.sqrt(Math.pow(xm - originX, 2) + Math.pow(ym - originY, 2));

                            if(displacement < radiusJoystickExt){
                                imgJoystick.setX(xm);
                                imgJoystick.setY(ym);
                            } else {
                                imgJoystick.setX((xm-originX)/displacement*radiusJoystickExt+originX);
                                imgJoystick.setY((ym-originY)/displacement*radiusJoystickExt+originY);
                            }

                            varTieX = (originX - imgJoystick.getX())/1500;
                            varTieY = (originY - imgJoystick.getY())/1500;

                        }

                        Runnable movingTie = new Runnable() {
                            @Override
                            public void run() {
                                tieX = imgTie.getX() - varTieX;
                                tieY = imgTie.getY() - varTieY;

                                //Set the Tie to the bounds of the screen
                                tieX = tieX + imgTie.getWidth()/2 < 0 ? -imgTie.getWidth()/2: tieX;
                                tieX = tieX + imgTie.getWidth()/2 > screenWidth ? screenWidth -imgTie.getWidth()/2 : tieX;

                                tieY = tieY + imgTie.getHeight()/2 < 0 ? -imgTie.getHeight()/2 : tieY;
                                tieY = tieY + imgTie.getHeight() > screenHeight ? screenHeight -imgTie.getHeight() : tieY;

                                imgTie.setX(tieX);
                                imgTie.setY(tieY);

                                if (joystickIsPressed) {
                                    hMovingTie.postDelayed(this, 10);
                                }
                            }
                        };
                        movingTie.run();

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


}