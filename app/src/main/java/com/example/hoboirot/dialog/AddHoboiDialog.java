package com.example.hoboirot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.hoboirot.R;

public class AddHoboiDialog extends DialogFragment {

    public interface AddHoboiDialogListener {
        public void onAddHoboiBtnClick(DialogFragment dialog, String name, String catID);
        void onAddCatBtnClick(DialogFragment dialog, String name);
    }

    AddHoboiDialogListener listener;
    EditText editText;
    Button btn;
    String catID = "";

    boolean add_cat = false;

    public AddHoboiDialog(boolean catmode) {
        add_cat = catmode;
    }

    public AddHoboiDialog(String catID) {
        this.catID = catID;
        add_cat = false;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dlg_addhoboi, null);
        editText = (EditText) layout.findViewById(R.id.EDTXT_hoboiname);
        btn = (Button) layout.findViewById(R.id.BTN_confirmaddhoboi);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(add_cat)
                    listener.onAddCatBtnClick(AddHoboiDialog.this, editText.getText().toString());
                else
                    listener.onAddHoboiBtnClick(AddHoboiDialog.this, editText.getText().toString(), catID);

                AddHoboiDialog.this.dismiss();
            }
        });

        builder.setView(layout);


        // Create the AlertDialog object and return it.
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface.
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = (AddHoboiDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface. Throw exception.
            throw new ClassCastException(context.toString()
                    + " must implement AddHoboiDialogListener");
        }

    }
}

