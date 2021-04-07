package com.dvlpr.findme.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dvlpr.findme.Home;
import com.dvlpr.findme.R;
import com.dvlpr.findme.classes.BitmapConverter;
import com.dvlpr.findme.classes.CustomProgressDialog;
import com.dvlpr.findme.classes.ImageBitmapPerfector;
import com.dvlpr.findme.classes.SaveUserInformation;
import com.dvlpr.findme.classes.ScreenInformation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kiprotich.japheth.image.ProfileImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class NuevoUsuario extends Fragment {

    ScreenInformation screenInformation;

    Button btnBack, btnRegistrarme, btnAgregarImagen;

    TextInputEditText inputNombreUsuario, inputCorreo, inputContraseña, inputContraseñaVerificada;

    TextInputLayout layoutNombreUsuario, layoutCorreo, layoutContraseña, layoutContraseñaVerificada;

    CustomProgressDialog customProgressDialog;

    LinearLayout layoutContainer;

    RequestQueue queue;

    SaveUserInformation saver;

    ProfileImage profileImage;

    Bitmap profileImageBitmap;

    int resultGetImage;

    ImageBitmapPerfector perfector;

    BitmapConverter bitmapConverter;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nuevo_usuario, container, false);

        perfector = new ImageBitmapPerfector(getContext());
        saver = new SaveUserInformation(getContext());
        customProgressDialog = new CustomProgressDialog(getContext());
        screenInformation = new ScreenInformation(getContext());
        bitmapConverter = new BitmapConverter();

        btnBack = view.findViewById(R.id.btnBackNuevoUsuario);
        inputNombreUsuario = view.findViewById(R.id.inputNombreUsuario);
        inputCorreo = view.findViewById(R.id.inputCorreo);
        inputContraseña = view.findViewById(R.id.inputContraseña);
        inputContraseñaVerificada = view.findViewById(R.id.inputVerificarContraseña);
        layoutNombreUsuario = view.findViewById(R.id.layoutNombreUsuario);
        layoutCorreo = view.findViewById(R.id.layoutCorreo);
        layoutContraseña = view.findViewById(R.id.layoutContraseña);
        layoutContraseñaVerificada = view.findViewById(R.id.layoutVerificarContraseña);
        btnRegistrarme = view.findViewById(R.id.btnRegistrarme);
        layoutContainer = view.findViewById(R.id.LayoutContainerNuevoUsuario);
        btnAgregarImagen = view.findViewById(R.id.btnAgregarImagenRegistro);
        profileImage = view.findViewById(R.id.profileImageRegistro);

        Bitmap profileImageBase = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.profile_picture_blank);
        profileImageBitmap = profileImageBase;

        layoutContainer.setPadding(12, screenInformation.GetStatusBarHeight(), 12, screenInformation.GetNavigationBarHeight());

        btnBack.setOnClickListener(listenerBtnBack);
        btnRegistrarme.setOnClickListener(listenerRegistrarme);
        btnAgregarImagen.setOnClickListener(listenerAgregarImagen);

        return view;
    }

    View.OnClickListener listenerRegistrarme = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegistrarNuevoUsuario();
        }
    };

    View.OnClickListener listenerAgregarImagen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentGetImage = new Intent(Intent.ACTION_PICK);
            intentGetImage.setType("image/*");
            startActivityForResult(intentGetImage, resultGetImage);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            //Se toma la data de la ubicacion
            Uri imageUri = data.getData();
            Toast.makeText(getContext(), "Path: " + getRealPathFromURI(getContext(), imageUri), Toast.LENGTH_SHORT).show();

            Glide.with(this).asBitmap().load(imageUri).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    profileImage.setImageBitmap(resource);
                    profileImageBitmap = perfector.FixCompressImage(resource, 4);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });


        } else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    void RegistrarNuevoUsuario() {

        layoutContraseñaVerificada.setError(null);
        layoutContraseña.setError(null);
        layoutCorreo.setError(null);
        layoutNombreUsuario.setError(null);

        //Se verifica que todos los campos hayan sido llenados
        boolean camposLlenos = true;
        for (TextInputEditText input : new TextInputEditText[]{inputNombreUsuario, inputCorreo, inputContraseña, inputContraseñaVerificada}) {
            input.setError(null);
            if (input.getText().toString().equals("")) {
                input.setError("Campo vacío");
                camposLlenos = false;
            }
        }

        if (!camposLlenos) {
            Toast.makeText(getContext(), "Alguno de los campos no ha sido llenado", Toast.LENGTH_SHORT).show();
            return;
        }

        //Se verifica si el correo ingresado es invalido
        if (!isValidEmail(inputCorreo.getText().toString())) {
            layoutCorreo.setError("Correo no valido");
            return;
        }

        if (!inputContraseña.getText().toString().equals(inputContraseñaVerificada.getText().toString())) {
            layoutContraseña.setError("Contraseñas no son iguales");
            layoutContraseñaVerificada.setError("Contraseñas no son iguales");
            return;
        }

        customProgressDialog.ShowStandardProgressDialog();

        queue = Volley.newRequestQueue(getContext());
        String url = "https://hexada.000webhostapp.com/findme/postNewUser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listenerNuevoUsuario, errorListenerNuevoUsuario) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                params.put("nombre_usuario", inputNombreUsuario.getText().toString().trim());
                params.put("correo", inputCorreo.getText().toString().trim());
                params.put("contraseña", inputContraseña.getText().toString().trim());
                params.put("imagen", bitmapConverter.BitmapToBase64(profileImageBitmap));

                return params;
            }
        };
        //Aumentar tiempo de espera del request
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        queue.add(stringRequest);

    }


    Response.Listener<String> listenerNuevoUsuario = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            customProgressDialog.dismiss();
            Toast.makeText(getContext(), "Response: "+response, Toast.LENGTH_SHORT).show();
            switch (response.trim()) {

                case "NombreUsuarioExistente":
                    layoutNombreUsuario.setError("El nombre de usuario ya existe");
                    break;
                case "CorreoExistente":
                    layoutCorreo.setError("El correo ya existe");
                    break;
            }

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String id = jsonObject1.getString("id");
                String imagen = jsonObject1.getString("imagen");

                Toast.makeText(getContext(), "Id del nuevo usuario registrado: " + id, Toast.LENGTH_SHORT).show();

                int idUsuario = Integer.parseInt(id);

                saver.SaveUserID(idUsuario);
                saver.SaveUserImage(imagen);
                saver.SaveUserLastLocation("");
                Intent intent = new Intent(getContext(), Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //Se guada el ID del usuario en Shared preferences, para luego, ejecutar la actividad principal

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener errorListenerNuevoUsuario = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            customProgressDialog.dismiss();
            Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener listenerBtnBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager().popBackStack();
        }
    };


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}