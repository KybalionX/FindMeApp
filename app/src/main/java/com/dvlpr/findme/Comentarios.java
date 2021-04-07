package com.dvlpr.findme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.dvlpr.findme.classes.AdapterComentarios;
import com.dvlpr.findme.classes.Comentario;
import com.dvlpr.findme.classes.CustomProgressDialog;
import com.dvlpr.findme.classes.GetUserInformation;
import com.google.android.material.textfield.TextInputEditText;
import com.kiprotich.japheth.image.ProfileImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Comentarios extends AppCompatActivity {

    String idPost;

    RecyclerView recyclerComentarios;
    ProfileImage imagenUsuario;

    TextInputEditText inputComentario;

    GetUserInformation userInformation;

    ArrayList<Comentario> listaComentarios;

    AdapterComentarios adapterComentarios;

    CustomProgressDialog progressDialog;

    Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        idPost = getIntent().getExtras().getString("idPost");
        imagenUsuario = findViewById(R.id.profileImageUser);
        progressDialog = new CustomProgressDialog(this);
        userInformation = new GetUserInformation(getApplicationContext());

        recyclerComentarios = findViewById(R.id.recyclerComentarios);

        listaComentarios = new ArrayList<>();


        btnPost = findViewById(R.id.postComentario);
        inputComentario = findViewById(R.id.inputComentario);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.ShowStandardProgressDialog();
                UploadComentario();
            }
        });

        progressDialog.ShowStandardProgressDialog();

        LoadUserInformation();
        LoadComentarios();

    }

    public void LoadUserInformation(){
        Picasso.with(this).load(userInformation.getImageURL()).into(imagenUsuario);
    }


    public void LoadComentarios(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://hexada.000webhostapp.com/findme/getComentarios.php?idPost="+idPost;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, listenerComentarios, errorListenerComentario);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                12000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        queue.add(jsonObjectRequest);
    }

    Response.Listener<JSONObject> listenerComentarios = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            JSONArray jsonComentarios = response.optJSONArray("comentarios");
            try{
                for(int i=0; i<jsonComentarios.length(); i++){
                    String imagenUsuario = jsonComentarios.getJSONObject(i).optString("imagen");
                    String comentario = jsonComentarios.getJSONObject(i).optString("comentario");
                    String nombreUsuario = jsonComentarios.getJSONObject(i).optString("nombre_usuario");

                    listaComentarios.add(new Comentario(imagenUsuario, nombreUsuario, comentario));

                }
                adapterComentarios = new AdapterComentarios(listaComentarios, getApplicationContext());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerComentarios.setHasFixedSize(true);
                recyclerComentarios.setAdapter(adapterComentarios);
                recyclerComentarios.setLayoutManager(linearLayoutManager);

            }catch (Exception e){

            }
            progressDialog.dismiss();
        }
    };

    ErrorListener errorListenerComentario = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(Comentarios.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    };



    public void UploadComentario(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://hexada.000webhostapp.com/findme/postComentario.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, listenerUploadComentario, errorListenerUploadComentario){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idUsuario", String.valueOf(userInformation.getUserID()));
                params.put("idPost", idPost);
                params.put("comentario", inputComentario.getText().toString());
                return params;
            }
        };
        queue.add(request);
    }

    Response.Listener<String> listenerUploadComentario = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(Comentarios.this, "response: "+response, Toast.LENGTH_SHORT).show();
            listaComentarios.clear();
            adapterComentarios = null;
            LoadComentarios();
            progressDialog.dismiss();
        }
    };

    ErrorListener errorListenerUploadComentario = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(Comentarios.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    };



}