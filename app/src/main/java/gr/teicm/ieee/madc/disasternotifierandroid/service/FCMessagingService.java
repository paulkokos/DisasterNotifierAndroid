package gr.teicm.ieee.madc.disasternotifierandroid.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;

import gr.teicm.ieee.madc.disasternotifierandroid.R;
import gr.teicm.ieee.madc.disasternotifierandroid.controller.UserController;
import gr.teicm.ieee.madc.disasternotifierandroid.controller.impl.UserControllerImpl;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NoContentException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;


public class FCMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);

        try {
            AuthService authService = new AuthService(
                    getFilesDir()
                            .getAbsolutePath()
            );
            UserController userController = new UserControllerImpl();
            userController.updateToken(
                    authService
                            .get()
                            .getAccessToken(),
                    refreshedToken
            );
        } catch (UnauthorizedException | MethodNotAllowedException | NotFoundException | ForbiddenException | JSONException | NetworkException | ConflictException | NoContentException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = "";
        String message = "";
        String click_action = "";

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
            click_action = remoteMessage.getNotification().getClickAction();
        }

        sendNotification(title, message, click_action);
    }

    private void sendNotification(String title, String messageBody, String click_action) {
        Intent intent;
        intent = new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT
        );

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1");
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setSound(defaultSoundUri);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify("app", 1, notificationBuilder.build());

    }


}
