package com.example.alanflores.dialogosynotificacionesapp;

import android.app.ProgressDialog;

/**
 * Created by alan.flores on 12/7/16.
 */

public class Hilo extends Thread {
    private ProgressDialog progressDialog;

    public Hilo(ProgressDialog progressDialog){
        this.progressDialog = progressDialog;
    }

    @Override
    public void run() {
        super.run();
        while(progressDialog.getProgress() < progressDialog.getMax()){
            try {
                Thread.sleep(1000);

            }catch (InterruptedException e){
                e.printStackTrace();
            }
            progressDialog.incrementProgressBy(10);
        }
        progressDialog.dismiss();
    }
}
