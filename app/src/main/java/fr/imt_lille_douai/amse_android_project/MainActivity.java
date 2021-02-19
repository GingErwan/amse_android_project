package fr.imt_lille_douai.amse_android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private boolean joystickIsPressed = false;
    private ImageView imgJoystick;
    private ImageView imgJoystickExt;
    private ImageView imgTie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgTie = (ImageView)findViewById(R.id.img_tie);
        imgJoystick = (ImageView)findViewById(R.id.img_pad_center);
        imgJoystickExt = (ImageView)findViewById(R.id.img_pad_exterior);

        imgJoystick.setOnTouchListener(new View.OnTouchListener() {

            float originX;
            float originY;

            float originXC;
            float originYC;

            float radiusJoystickExt;
            float radiusJoystickInt;

            float xm;
            float ym;

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


}