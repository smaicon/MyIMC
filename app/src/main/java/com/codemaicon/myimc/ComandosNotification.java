package com.codemaicon.myimc;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.codemaicon.myimc.NotificationPublisher;
/**
 * Created by Maicon on 24/11/2017.
 */

public class ComandosNotification extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";


    @Override
    public void onReceive(Context context, Intent intent) {

        String acao = intent.getExtras().getString("action", null);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (acao.equals("abrir")) {
            //Abrir o aplicativo
            Intent it = new Intent(context, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);

        } else {
             Toast.makeText(context, "classe comandos notification else", Toast.LENGTH_SHORT).show();
            notificationManager.cancel(NotificationPublisher.NOTIFICATION_ID_VALUE);

        }

    }
}
