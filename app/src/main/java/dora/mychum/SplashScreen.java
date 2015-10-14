package dora.mychum;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

/**
 * Created by Devendra Dora
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread timerThread = new Thread(){
            public void run() {
                try{
                    sleep(2000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    Intent mainACt=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(mainACt);
                    finish();
                }
            }
        };
        timerThread.start();
    }
    @Override
    protected void	onPause(){
        super.onPause();
        finish();
    }

}
