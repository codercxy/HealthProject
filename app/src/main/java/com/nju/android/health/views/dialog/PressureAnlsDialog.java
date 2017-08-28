package com.nju.android.health.views.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.android.health.R;


/**
 * Created by Administrator on 2016/9/22.
 */
public class PressureAnlsDialog extends DialogFragment {
    private TextView textView;
    //private ImageView faceView;
    private String text;
    private ImageButton closeBtn;
    private Button dialogBtn;

    public PressureAnlsDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.text = getArguments().getString("text");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.pres_dialog, null);
        textView =  (TextView) view.findViewById(R.id.dialog_text);
        //faceView = (ImageView) view.findViewById(R.id.dialogface);
        closeBtn = (ImageButton) view.findViewById(R.id.dialog_close);
        dialogBtn = (Button) view.findViewById(R.id.dialog_button);

        //faceView.setImageResource(R.drawable.img_dialogface1);
        textView.setText(text);
        builder.setView(view);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        /*builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });*/

        return builder.create();
    }
}
