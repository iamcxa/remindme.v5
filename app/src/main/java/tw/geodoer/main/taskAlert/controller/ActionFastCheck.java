package tw.geodoer.main.taskAlert.controller;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import tw.geodoer.main.taskAlert.view.dialog.AlertNotiDialog;

public class ActionFastCheck extends IntentService {


    public ActionFastCheck() {
        super(null);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        Bundle b = intent.getExtras();
        String taskID = b.getString("taskID");

        Intent it = new Intent(this,AlertNotiDialog.class);
        it.putExtra("taskID", taskID);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);

    }

}
