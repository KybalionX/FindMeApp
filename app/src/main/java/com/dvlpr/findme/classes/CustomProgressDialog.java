package com.dvlpr.findme.classes;

import android.app.ProgressDialog;
import android.content.Context;

import com.dvlpr.findme.R;

public class CustomProgressDialog {

    Context context;
    ProgressDialog progressDialog;

    public CustomProgressDialog(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context, R.style.FindMaterial_ProgressDialog_Normal);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.dog_waiting));
    }

    public void ShowStandardProgressDialog(){
        progressDialog.setTitle("Cargando...");
        progressDialog.setMessage("Por favor, espere.");
        progressDialog.show();
    }

    public void dismiss(){
        progressDialog.dismiss();
    }

}
