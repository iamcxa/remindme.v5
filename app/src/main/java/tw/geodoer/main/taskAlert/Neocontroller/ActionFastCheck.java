package tw.geodoer.main.taskAlert.Neocontroller;

import android.app.IntentService;
import android.content.Intent;

import tw.geodoer.main.taskAlert.view.dialog.AlertNotiDialog;

public class ActionFastCheck extends IntentService {

    public ActionFastCheck() {
        super(null);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        Intent intentDialog = new Intent(this, AlertNotiDialog.class);
        intentDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intentDialog.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentDialog);

    }

}
