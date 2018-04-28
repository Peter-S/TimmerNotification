package com.example.lmnop.timmernotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private MessageHandler messageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Counting");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);

        messageHandler =new MessageHandler();
    }

    public void startCounter(View view) {
        progressDialog.show();

        Thread thread = new Thread(new Timer());
        thread.start();
    }

    public void doNotify() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent intent = PendingIntent.getActivity(this,100,
                new Intent(this, BoomActivity.class), 0);


        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setSmallIcon(R.drawable.ic_launcher_background);
        nb.setSound(sound);
        nb.setContentTitle("Knock, Knock...");
        nb.setContentText("You've got a delivery");
        nb.setContentIntent(intent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(100, nb.build());

    }

    private class Timer implements Runnable {

        @Override
        public void run() {
            for (int i = 5000; i >= 0; i--) {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }

                Bundle bundle = new Bundle();
                bundle.putInt("current count", i);

                Message message = new Message();
                message.setData(bundle);

                messageHandler.sendMessage(message);
            }
            progressDialog.dismiss();
        }
    }

    private class MessageHandler extends Handler{

        @Override
        public void handleMessage(Message message) {
            int currentCount = message.getData().getInt("current count");
            progressDialog.setMessage("Please wait in ... " + currentCount +
                    " milliseconds, this will close");

            if (currentCount == 0) {
                doNotify();
            }
        }
    }
}
