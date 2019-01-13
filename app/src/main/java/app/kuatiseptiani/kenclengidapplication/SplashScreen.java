package app.kuatiseptiani.kenclengidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_screen);

            Thread timerThread = new Thread(){
                public void run(){
                    try{
                        sleep(500);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }finally{
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    }
                }
            };
            timerThread.start();
        }

        @Override
        protected void onPause() {
            // TODO Auto-generated method stub
            super.onPause();
            finish();
        }
}
