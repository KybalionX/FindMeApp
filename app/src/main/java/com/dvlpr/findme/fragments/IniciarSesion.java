package com.dvlpr.findme.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dvlpr.findme.Home;
import com.dvlpr.findme.R;
import com.dvlpr.findme.classes.CustomProgressDialog;
import com.dvlpr.findme.classes.SaveUserInformation;
import com.dvlpr.findme.classes.ScreenInformation;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

public class IniciarSesion extends Fragment {

    LinearLayout layoutContainer;

    ScreenInformation screenInformation;

    Button btnBack, btnIngresar;

    TextInputEditText inputCorreo, inputContraseña;

    RequestQueue queue;

    CustomProgressDialog progressDialog;

    SaveUserInformation saver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iniciar_sesion, container, false);

        saver = new SaveUserInformation(getContext());
        screenInformation = new ScreenInformation(getContext());
        progressDialog = new CustomProgressDialog(getContext());

        inputCorreo = view.findViewById(R.id.inputCorreo);
        inputContraseña = view.findViewById(R.id.inputContraseña);
        btnIngresar = view.findViewById(R.id.btnIngresar);
        btnBack = view.findViewById(R.id.btnBackIniciarSesion);
        layoutContainer = view.findViewById(R.id.layoutContainerIniciarSesion);

        layoutContainer.setPadding(12, screenInformation.GetStatusBarHeight(), 12, screenInformation.GetNavigationBarHeight());

        btnIngresar.setOnClickListener(listenerIngresar);
        btnBack.setOnClickListener(listenerBack);

        return view;
    }

    View.OnClickListener listenerBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager().popBackStack();
        }
    };

    View.OnClickListener listenerIngresar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ComprobarUsuario();
            progressDialog.ShowStandardProgressDialog();
        }
    };

    void ComprobarUsuario(){

        boolean camposLlenos = true;
        for(TextInputEditText input : new TextInputEditText[]{inputCorreo, inputContraseña}){
            input.setError(null);
            if(input.getText().toString().equals("")){
                input.setError("Campo vacío");
                camposLlenos = true;
            }
        }

        if(!camposLlenos){
            Toast.makeText(getContext(), "Alguno de los campos no ha sido llenado", Toast.LENGTH_SHORT).show();
            return;
        }

        queue = Volley.newRequestQueue(getContext());
        String url = "https://hexada.000webhostapp.com/findme/logInUser.php?correo="+inputCorreo.getText().toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        queue.add(request);


    }

    Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            JSONArray jsonArray = response.optJSONArray("objetos");

            progressDialog.dismiss();

            if(jsonArray == null){
                inputCorreo.setError("El correo no ha sido registrado");
                return;
            }

            int id = jsonArray.optJSONObject(0).optInt("id");
            String imagen = jsonArray.optJSONObject(0).optString("imagen");
            String nombreUsuario = jsonArray.optJSONObject(0).optString("nombre_usuario");

            saver.SaveUserID(id);
            saver.SaveUserImage(imagen);
            saver.SaveUserLastLocation("");
            Intent intent = new Intent(getContext(), Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            Toast.makeText(getContext(), "El usuario "+nombreUsuario+" tiene el id "+id, Toast.LENGTH_SHORT).show();
        }
    };

    ErrorListener errorListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Error: "+error, Toast.LENGTH_SHORT).show();
        }
    };

}