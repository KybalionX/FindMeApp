package com.dvlpr.findme.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dvlpr.findme.R;
import com.dvlpr.findme.classes.AutoGridLayout;
import com.dvlpr.findme.classes.BitmapConverter;
import com.dvlpr.findme.classes.CustomProgressDialog;
import com.dvlpr.findme.classes.GetUserInformation;
import com.dvlpr.findme.classes.ImageBitmapPerfector;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import im.delight.android.location.SimpleLocation;

public class NewPost extends Fragment {

    AutoGridLayout gridImages;

    ArrayList<String> imagesToUpload;

    LottieAnimationView lottieAnimation;

    TextView tvUbicacion;

    BitmapConverter bitmapConverter;

    ChipGroup chipGroup;

    CustomProgressDialog customProgressDialog;

    FloatingActionButton btnPostear;

    GetUserInformation userInformation;

    EditText inputDescripcion;

    Button btnAgregarImagenes;

    View view;

    SimpleLocation location;

    String ciudad, departamento, pais;

    Double latitude, longitude;

    int CODE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_post, container, false);

        bitmapConverter = new BitmapConverter();
        customProgressDialog = new CustomProgressDialog(getContext());
        imagesToUpload = new ArrayList<String>();
        customProgressDialog = new CustomProgressDialog(getContext());
        userInformation = new GetUserInformation(getContext());


        location = new SimpleLocation(view.getContext());
        //LoadImages();
        LoadCategories();

        inputDescripcion = view.findViewById(R.id.inputDescripcion);
        chipGroup = view.findViewById(R.id.chipGroup);
        btnPostear = view.findViewById(R.id.btnPostear);
        gridImages = view.findViewById(R.id.gridImages);
        lottieAnimation = view.findViewById(R.id.animationUbicacion);
        tvUbicacion = view.findViewById(R.id.tvUbicacion);
        btnAgregarImagenes = view.findViewById(R.id.btnAgregarImagenes);

        btnAgregarImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadImages();
            }
        });


        btnPostear.setOnClickListener(listenerPostear);

        customProgressDialog.ShowStandardProgressDialog();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        CheckPositionPermission();
    }

    public void CheckPositionPermission(){
        location.beginUpdates();
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(view.getContext());
        }
        if ( ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            location.beginUpdates();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            GetLocationName(location.getLongitude(), location.getLatitude());
        }else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Debes aceptar los permisos de ubicación para continuar").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, CODE );
                    dialog.dismiss();
                }
            }).show();
        }
    }

    public void GetLocationName(double longitude, double latitude){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.bigdatacloud.net/data/reverse-geocode-client?latitude="+latitude+"&longitude="+longitude+"&localityLanguage=es";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listenerUbicacion, errorListenerPost);
        queue.add(request);
    }

    Response.Listener<JSONObject> listenerUbicacion = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                ciudad = response.getString("city");
                departamento = response.getString("principalSubdivision");
                pais = response.getString("countryName");
                tvUbicacion.setText(ciudad+", "+departamento+", "+pais);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lottieAnimation.cancelAnimation();
            lottieAnimation.setVisibility(View.GONE);
            tvUbicacion.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener listenerPostear = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customProgressDialog.ShowStandardProgressDialog();
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String urlPost = "https://hexada.000webhostapp.com/findme/uploadNewPost.php";
            Toast.makeText(getContext(), "size images lis: "+imagesToUpload.size(), Toast.LENGTH_SHORT).show();
            StringRequest request = new StringRequest(Request.Method.POST, urlPost, listenerPost, errorListenerPost){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_usuario", String.valueOf(userInformation.getUserID()));
                    params.put("descripcion", inputDescripcion.getText().toString());
                    params.put("categoria", String.valueOf(chipGroup.getCheckedChipId()+1));
                    params.put("latitud", String.valueOf(latitude));
                    params.put("longitud", String.valueOf(longitude));
                    params.put("ciudad", ciudad);
                    params.put("departamento", departamento);
                    params.put("pais", pais);
                    for(int i=0; i<imagesToUpload.size(); i++){
                        params.put("imagenes["+i+"]", imagesToUpload.get(i));
                    }
                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    6000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );
            queue.add(request);
        }
    };

    Response.Listener<String> listenerPost = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            customProgressDialog.dismiss();
            Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_SHORT).show();
            System.out.println("Response: "+response);
        }
    };

    ErrorListener errorListenerPost = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            customProgressDialog.dismiss();
            Toast.makeText(getContext(), "Error: "+error, Toast.LENGTH_SHORT).show();
        }
    };


    public void LoadCategories(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String urlCategories = "https://hexada.000webhostapp.com/findme/getCategories.php";
        JsonObjectRequest categoriesRequest = new JsonObjectRequest(Request.Method.GET, urlCategories, null, listenerCategories,  errorListenerCategories);
        /*categoriesRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

         */
        queue.add(categoriesRequest);
    }

    Response.Listener<JSONObject> listenerCategories = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            JSONArray jsonArray = response.optJSONArray("categorias");
            try{
                for(int i=0; i<jsonArray.length(); i++){

                    final Chip chip = new Chip(getContext());
                    chip.setText(jsonArray.getJSONObject(i).optString("categoria"));
                    chip.setId(i);
                    chip.setCheckable(true);
                    chip.setClickable(true);
                    chip.setFocusable(true);
                    chipGroup.addView(chip);
                }
                //Setear la categoria perros como seleccionada
                Chip c = chipGroup.findViewById(0);
                c.setChecked(true);


            }catch (Exception e){
                Toast.makeText(getContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
            }
            customProgressDialog.dismiss();

        }
    };

    ErrorListener errorListenerCategories = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            customProgressDialog.dismiss();
            Toast.makeText(getContext(), "Error: "+error, Toast.LENGTH_SHORT).show();
        }
    };


    public void LoadImages(){
        ImagePicker.with(this)
                .setToolbarColor("#FFB504")
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setDirectoryName("Image Picker")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(10)
                .setLimitMessage("You can select up to 10 images")
                .setRequestCode(100)
                .start();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
            ArrayList<com.nguyenhoanglam.imagepicker.model.Image> images = new ArrayList<>();
            images = ImagePicker.getImages(data);
            // Do stuff with image's path or id. For example:
            for (com.nguyenhoanglam.imagepicker.model.Image img:
                    images) {
                try {
                    //Values in dp for params in the new images
                    InputStream imageStream = getContext().getContentResolver().openInputStream(img.getUri());

                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    imagesToUpload.add(bitmapConverter.BitmapToBase64(new ImageBitmapPerfector(getContext()).FixCompressImage(selectedImage, 1)));

                    ImageView imageView = new ImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageBitmap(selectedImage);

                    CardView cardView = new CardView(getContext());
                    cardView.setLayoutParams(paramsItemGrid());
                    cardView.addView(imageView);
                    cardView.setRadius(12);

                    gridImages.addView(cardView);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public LinearLayout.LayoutParams paramsItemGrid(){
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)width, (int)height);
        params.setMargins(12,0,12,0);
        return params;
    }

}