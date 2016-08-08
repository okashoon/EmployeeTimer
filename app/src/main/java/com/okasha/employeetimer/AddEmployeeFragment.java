package com.okasha.employeetimer;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class AddEmployeeFragment extends DialogFragment {

    EditText name;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_employee, container, false);
        name = (EditText) v.findViewById(R.id.new_employee_et);

        Button addButton = (Button) v.findViewById(R.id.add_employee_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNameDialogListener listener = (EditNameDialogListener) getActivity();
                String s = name.getText().toString();
                listener.onFinishEditDialog(s);
                dismiss();
            }
        });

        // TODO add description or title
        return v;
    }

    public  interface EditNameDialogListener{
        void onFinishEditDialog(String name);
    }


}
