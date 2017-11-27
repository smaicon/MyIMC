package com.codemaicon.myimc;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;



import java.util.List;

import com.codemaicon.myimc.Constantes;
import com.codemaicon.myimc.MainActivity;


public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static int NOTIFICATION_ID_VALUE = 199;
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        int acao = intent.getExtras().getInt("action");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        switch (acao){
            case Constantes.BRODCAST_NOTIFICAR:
                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
                //Gerar notificação
                Notification notification = intent.getParcelableExtra(NOTIFICATION);
                int id = intent.getIntExtra(NOTIFICATION_ID, 0);
                notificationManager.notify(id, notification);
                break;

            case Constantes.BRODCAST_EXECUTAR_ACAO:
                Toast.makeText(context, "Ação realizada", Toast.LENGTH_SHORT).show();
                notificationManager.cancel(NotificationPublisher.NOTIFICATION_ID_VALUE);
                break;

            case Constantes.BRODCAST_INICIAR_APP:
                Intent it = new Intent(context, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);
                break;
        }


    }
}
