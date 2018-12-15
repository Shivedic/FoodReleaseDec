package com.shivedic.foodveda.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.shivedic.foodveda.Activities.SplashScreen;
import com.shivedic.foodveda.MVP.Product;
import com.shivedic.foodveda.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    String value, type;
    int count = 0;
    NotificationManager notificationManager;
    String image, productId, title, message;
    String random_id;
    public static int NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("myTag", "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("myTag", "Data Payload: " + remoteMessage.getData().toString());
            handleDataMessage(remoteMessage.getData());
        } else if (remoteMessage.getNotification() != null) {
            Log.d("myTag", "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

    }

    private void handleNotification(String message) {
        Intent resultIntent = new Intent(getApplicationContext(), SplashScreen.class);
        resultIntent.putExtra("id", "");
        // check for image attachment
        sendNotification(resultIntent);
    }

    private void handleDataMessage(Map<String, String> data) {

        image = data.get("image");
        title = data.get("title");
        message = data.get("message");
        productId = data.get("product_id");
        random_id = data.get("random_id");
        Log.d("myTag", String.valueOf(NOTIFICATION_ID) + " : " + random_id );
        if (NOTIFICATION_ID != Integer.parseInt(random_id)) {
            NOTIFICATION_ID= Integer.parseInt(random_id);
            handleMessage(getApplicationContext());

        }
    }


    private void sendNotification(Intent resultIntent) {
        int requestID = (int) System.currentTimeMillis();


        PendingIntent intent =
                PendingIntent.getActivity(getApplicationContext(), requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setPriority(1);
            mNotifyBuilder.setSmallIcon(R.drawable.food_app_icon);

        } else {
            // Lollipop specific setColor method goes here.
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setColor(Color.WHITE)
                    .setPriority(1);

            mNotifyBuilder.setSmallIcon(R.drawable.food_app_icon);
            mNotifyBuilder.setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.food_app_icon)).getBitmap());

        }


        // Set pending intent

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        mNotifyBuilder.setContentIntent(intent);
        // Post a notification
        mNotificationManager.notify(requestID, mNotifyBuilder.build());
    }


    @SuppressWarnings("deprecation")
    private void handleMessage(Context mContext) {
        Bitmap remote_picture = null;
        int icon = R.drawable.food_app_icon;
        //if message and image url
        if (message != null && image != null) {
            try {


                Log.v("TAG_MESSAGE", "" + message);
                Log.v("TAG_IMAGE", "" + image);

               // NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
                //notiStyle.setSummaryText(message);

                try {
                    remote_picture = BitmapFactory.decodeStream((InputStream) new URL(image).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //notiStyle.bigPicture(remote_picture);
                //notificationManager = (NotificationManager) mContext
                  //      .getSystemService(Context.NOTIFICATION_SERVICE);
                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                     //Create or update.
                    NotificationChannel channel = new NotificationChannel("my_channel_01",
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }*/
                PendingIntent contentIntent = null;
                List<Product> pList = SplashScreen.productList;
                String productName = "";
                for(int i = 0; i < pList.size(); i++){
                    if(pList.get(i).getProductId().equals(productId)){
                        productName = pList.get(i).getProductName();
                    }
                }
                Intent gotoIntent = new Intent();
                gotoIntent.putExtra("id", productId);
                gotoIntent.putExtra("title", title);
                gotoIntent.putExtra("message", message);
                gotoIntent.putExtra("image", image);
                gotoIntent.putExtra("pname", productName);
                gotoIntent.putExtra("random_id", random_id);
                gotoIntent.putExtra("isFromNotif", "true");

                gotoIntent.setClassName(mContext, getApplicationContext().getPackageName()+".Activities.SplashScreen");//Start activity when user taps on notification.
                contentIntent = PendingIntent.getActivity(mContext,
                        (int) (Math.random() * 100), gotoIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

showNotification(mContext,title,message,gotoIntent);
                /*
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        mContext);
                Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                        .setLargeIcon(((BitmapDrawable) getResources().getDrawable(icon)).getBitmap())
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setPriority(1)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentIntent(contentIntent)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                        .setContentText(message)
                        .setStyle(notiStyle).build();


                notification.flags = Notification.FLAG_AUTO_CANCEL;
                count++;
                notificationManager.notify(count, notification);//This will generate seperate notification each time server sends.
*/
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);

        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body).setChannelId(channelId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}