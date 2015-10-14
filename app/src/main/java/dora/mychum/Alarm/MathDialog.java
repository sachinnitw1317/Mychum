package dora.mychum.Alarm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static java.lang.Integer.parseInt;
import dora.mychum.R;

/**
 * Created by G S ABHAY on 01-06-2015.
 */
public class MathDialog extends Activity {

    AlertDialog.Builder myAlert;
    AlertDialog alert;
    TextView num1,num2,op;
    char oper[] = {'+', '-' , '*' , '/'};
    EditText ans;
   int number1,number2,op_index,answer;
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.math_ques);
            myAlert = new AlertDialog.Builder(this);
            myAlert.setTitle("Answer this Math Question to Switch off the Alarm ... \n ");
            LayoutInflater inflater = getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.math_ques, null);

            myAlert.setView(dialoglayout);

            myAlert.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                }
            });
                if(AlarmFragment.snoozeOn)
                    myAlert.setNegativeButton("Snooze", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Toast.makeText(MathDialog.this, "Snoozed after " + AlarmFragment.snooze_min + " minutes", Toast.LENGTH_SHORT).show();
                        }
                    });

            alert = myAlert.create();
            num1  = (TextView) dialoglayout.findViewById(R.id.num1);
            num2 = (TextView) dialoglayout.findViewById(R.id.num2);
            op  = (TextView) dialoglayout.findViewById(R.id.op);
            ans = (EditText) dialoglayout.findViewById(R.id.mathAns);
             op_index = randInt(0,oper.length-1);
            //Log.d("MyApp",String.valueOf(oper[op_index]));
            // Toast.makeText(this,op_index, Toast.LENGTH_SHORT).show();
           op.setText(String.valueOf(oper[op_index]));

//
            number1 = randInt(0,10);
            number2 = randInt(0,10);

           num1.setText(String.valueOf(Math.max(number1, number2)));

            num2.setText(String.valueOf(Math.min(number1, number2)));



            alert.show();
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = String.valueOf(ans.getText());
                    boolean result = false;
                    if(!s.isEmpty())
                    {
                        answer = parseInt(s);
                        result = checkAnswer(answer, oper[op_index], Math.max(number1, number2), Math.min(number1, number2));
                    }


                    if (result == true) {
                        alert.dismiss();
                        Intent myIntent = new Intent(MathDialog.this, MathClass.class);
                        final AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(MathDialog.this, 0, myIntent, 0);
                        alarmManager.cancel(pendingIntent);
                           Toast.makeText(MathDialog.this, "Alarm Dismissed", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(MathDialog.this, AlarmFragment.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(i);
                    } else {
                        ans.setText("");
                         Toast.makeText(MathDialog.this, "Try Again", Toast.LENGTH_SHORT).show();
                        //alert.show();
                    }

                }
            });
            }

    private boolean checkAnswer(int answer,char oper,int x,int y) {

        switch(oper)
        {
            case '+' :
                       int res = x + y;
                       if(answer == res)
                           return true;
                       break;
            case '-' :
                 res = x - y;
                if(answer == res)
                    return true;
                break;
            case '*' :
                res = x * y;
                if(answer == res)
                    return true;
                break;
            case '/' :
                 res = x / y;
                if(answer == res)
                    return true;
                break;

        }

        return false;
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }





}
