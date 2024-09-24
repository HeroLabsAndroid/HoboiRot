package com.example.hoboirot.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.hoboirot.R;

public class IODialog extends DialogFragment {

    public void export_dat() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "medlogsave");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, 3);
        this.dismiss();
    }

    public void import_dat() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");


        startActivityForResult(intent, 4);
        this.dismiss();
    }

    Button btnI, btnO;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dlg_io, null);

        btnI = (Button) layout.findViewById(R.id.btnImport);
        btnO = (Button) layout.findViewById(R.id.btnExport);
        btnI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                import_dat();
            }
        });

        btnO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export_dat();
            }
        });

        builder.setView(layout);


        // Create the AlertDialog object and return it.
        return builder.create();
    }

}


