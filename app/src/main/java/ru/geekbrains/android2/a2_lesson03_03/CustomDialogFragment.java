package ru.geekbrains.android2.a2_lesson03_03;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;


public class CustomDialogFragment extends DialogFragment {
    private MainActivity ma = null;
    EditText editTextLatitude = null;
    EditText editTextLongitude = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() != null) {
            ma = (MainActivity) getActivity();
        }
        editTextLatitude = (EditText) ma.findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) ma.findViewById(R.id.editTextLongitude);
        editTextLatitude.setText(ma.getLatitude().toString());
        editTextLongitude.setText(ma.getLongitude().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(R.string.write_point)
                .setView(R.layout.dialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ma.setLatitude(Float.valueOf(editTextLatitude.getText().toString()));
                        ma.setLongitude(Float.valueOf(editTextLongitude.getText().toString()));
                        ma.addPoint();
                        dialog.dismiss();
                    }
                })
                .create();
    }
}