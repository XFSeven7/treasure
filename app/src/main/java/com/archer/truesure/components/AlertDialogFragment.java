package com.archer.truesure.components;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.archer.truesure.R;

/**
 *
 * Created by Administrator on 2016/7/13 0013.
 */
public class AlertDialogFragment extends DialogFragment {

    public static final String KEY_TITLE = "key_title";
    public static final String KEY_MESSAGE = "key_message";

    public static AlertDialogFragment newsIntance(int resTitle,int resMsg) {

        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TITLE, resTitle);
        bundle.putInt(KEY_MESSAGE, resMsg);
        fragment.setArguments(bundle);

        return fragment;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        int title = arguments.getInt(KEY_TITLE);
        int msg = arguments.getInt(KEY_MESSAGE);

        return new AlertDialog.Builder(getActivity(),getTheme())
                .setTitle(getString(title))
                .setMessage(getString(msg))
                .setNeutralButton(R.string.OK, null)
                .create();
    }
}
