package com.example.alanflores.dialogosynotificacionesapp;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressDialog barProgressDialog;
    Handler updateBarHandler;

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateBarHandler = new Handler();


        listView = (ListView) findViewById(R.id.list);
        String[] opciones = getResources().getStringArray(R.array.opciones_lista);

        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opciones));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        MiDialogoAlerta miDialogoAlerta = new MiDialogoAlerta();
                        //11 o inferior supor
                        miDialogoAlerta.show(getSupportFragmentManager(),"miDialogo");
                        //No suport
                        //miDialogoAlerta.show(getFragmentManager());
                        break;
                    case 1:
                        MiDialogoOpciones miDialogoOpciones = new MiDialogoOpciones();
                        miDialogoOpciones.show(getSupportFragmentManager(),"miDialogo");
                        break;
                    case 2:
                        MiDialogoPersonalizado miDialogoPersonalizado = new MiDialogoPersonalizado();
                        miDialogoPersonalizado.show(getSupportFragmentManager(),"miDialogo");
                        break;
                    case 3:
                        /*It does not work*/
                        barProgressDialog = new ProgressDialog(MainActivity.this);
                        barProgressDialog.setTitle("Downloading Image ...");
                        barProgressDialog.setMessage("Download in progress ...");
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(1000);
                        barProgressDialog.show();

                        /*Log.v("no Hilo","" + progressDialog.getProgress() + " " + progressDialog.getMax());*/
                        Hilo hilo = new Hilo(barProgressDialog);
                        hilo.start();
                        /*new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Here you should write your time consuming task...
                                    while (barProgressDialog.getProgress() <= barProgressDialog.getMax()) {
                                        Thread.sleep(2000);
                                        updateBarHandler.post(new Runnable() {
                                            public void run() {
                                                barProgressDialog.incrementProgressBy(2);
                                            }
                                        });
                                        if (barProgressDialog.getProgress() == barProgressDialog.getMax()) {
                                            barProgressDialog.dismiss();
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }).start();*/

                        break;
                    case 4:
                        mostartNotificacion();
                        break;
                    case 5:
                        Toast toast = Toast.makeText(MainActivity.this, "Esto es un toast simple",Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case 6:
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view1 = layoutInflater.inflate(R.layout.toast_layout,null);
                        Toast toast1 = new Toast(getApplicationContext());
                        toast1.setDuration(Toast.LENGTH_SHORT);
                        toast1.setView(view1);
                        toast1.show();
                        break;
                }
            }
        });

    }
    private void mostartNotificacion(){
        int notificationId = 1;

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notificacion android")
                .setContentText("Esta es una notificacion en ANdroid. Precioname")
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setSound(defaultSoundUri);

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(notificationId,PendingIntent.FLAG_UPDATE_CURRENT);

        notificationCompat.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationCompat.setAutoCancel(true);
        notificationManager.notify(notificationId,notificationCompat.build());
    }

    public static class MiDialogoAlerta extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Dialogo de alerta");
            return builder.create();
        }
    }

    public static class MiDialogoOpciones extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new  AlertDialog.Builder(getActivity());
            builder.setTitle("Aplicacion dialogos");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setSingleChoiceItems(getResources().getStringArray(R.array.opciones_lista), 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.v("MiDialogoOpciones","Opcion seleccionada:" + which);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.v("MiDialogoOpciones","Cancelar dialogo");
                }
            });
            return builder.create();
        }
    }

    public static class MiDialogoPersonalizado extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.dialogo_layout,null);
            Button acepet = (Button) view.findViewById(R.id.button1);
            acepet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("MiDialogoPersonalizado","acepet");
                }
            });
            builder.setView(view);
            return builder.create();
        }
    }
}
