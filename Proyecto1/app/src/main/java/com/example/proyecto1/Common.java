package com.example.proyecto1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.example.proyecto1.fragments.AsyncTaskFragment;

public class Common extends AppCompatActivity  implements AsyncTaskFragment.TaskCallbacks  {

    /**
     * Show a toast in the view
     * @param acrossWindows - if true the toast does not disappear when view changes
     * @param messageId - the message id to show
     */
    public void showToast(Boolean acrossWindows, int messageId){
        int tiempo = Toast.LENGTH_SHORT;
        Context context;
        if (acrossWindows){
            context = getApplicationContext();
        }else{
            context = this;
        }
        Toast aviso = Toast.makeText(context, getResources().getString(messageId), tiempo);
        aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
        aviso.show();
    }
}
