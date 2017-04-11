package com.ibm.hondaconnect.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ibm.hondaconnect.R;

/**
 * Created by IBM_ADMIN on 2/22/2017.
 */
public class AlertUtil {


    public static AlertDialog getSingleButtonDialog(Activity activityCtx, String message, String btnText, View.OnClickListener clickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activityCtx);
        LayoutInflater inflater = activityCtx.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_one_button, null);
        dialogBuilder.setView(dialogView);
        final TextView tvMsg = (TextView) dialogView.findViewById(R.id.dialog_one_msg);
         tvMsg.setText(message);
        final TextView btnPositive = (TextView) dialogView.findViewById(R.id.dialog_one_btn);
        btnPositive.setText(btnText);
        if (null != clickListener) {
            btnPositive.setOnClickListener(clickListener);
        }
        return dialogBuilder.create();
    }




    public static Dialog getScoreDialog(Activity activityCtx) {
        final Dialog dialog = new Dialog(activityCtx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = activityCtx.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.score_dailogue, null);

        Button btnClose = (Button) dialogView.findViewById(R.id.score_popup_btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        return dialog;
    }


}
