package dora.mychum.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

/**
 * Created by G S ABHAY on 31-05-2015.
 */
public class MathClass extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
        Intent i = new Intent(context, MathDialog.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

//      DialogClass D = new DialogClass("Math");
//        D.show(getFragmentManager(),"alarm");



    }


}

