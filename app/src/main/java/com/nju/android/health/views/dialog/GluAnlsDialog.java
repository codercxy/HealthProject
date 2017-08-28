package com.nju.android.health.views.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nju.android.health.R;

/**
 * Created by 57248 on 2016/10/13.
 */

public class GluAnlsDialog extends DialogFragment {
    private String text;
    private TextView dialogText;
    public GluAnlsDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.text = getArguments().getString("text");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.glu_dialog, null);

        dialogText = (TextView) view.findViewById(R.id.glu_anls_dialog);

        dialogText.setText(text);
        builder.setView(view)
                .setTitle("血糖分析结果");


        return builder.create();
    }
}
