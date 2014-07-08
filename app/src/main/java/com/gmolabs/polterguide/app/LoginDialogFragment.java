package com.gmolabs.polterguide.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by geoffmorris on 7/7/14.
 */
public class LoginDialogFragment extends android.support.v4.app.DialogFragment {

    EditText mEditTextUsername;
    EditText mEditTextPassword;
    String mUsernameText = "";
    String mPasswordText = "";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();



        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_signin, null);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        mEditTextUsername = (EditText) v.findViewById(R.id.username);
                        mEditTextPassword = (EditText) v.findViewById(R.id.password);
                        mUsernameText = mEditTextUsername.getText().toString();
                        mPasswordText = mEditTextPassword.getText().toString();
                        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        String defValue = "Ghosty";
                        int r = (int) (Math.random() * 100);
                        defValue += r;
                        SharedPreferences.Editor editor1 = mySharedPreferences.edit();
                        editor1.putString("username", mUsernameText);
                        editor1.putString("password", mPasswordText);
                        editor1.commit();

//                        Toast.makeText(getActivity().getApplicationContext(), mUsernameText, Toast.LENGTH_SHORT).show();

                    }
                });
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        LoginDialogFragment.this.getDialog().cancel();
//                    }
//                });
        return builder.create();
    }

}
