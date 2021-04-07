package com.dvlpr.findme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dvlpr.findme.classes.AdapterFeed;
import com.dvlpr.findme.classes.CustomProgressDialog;
import com.dvlpr.findme.classes.GetUserInformation;
import com.dvlpr.findme.classes.Image;
import com.dvlpr.findme.classes.Post;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SimplePost extends AppCompatActivity {

    RecyclerView recyclerSimplePost;
    AdapterFeed adapterPost;

    String idPost;

    boolean liked=false;

    ArrayList<Post> listaPost;
    ArrayList<Image> imagenes = new ArrayList<>();

    CustomProgressDialog customProgressDialog;


    GetUserInformation userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_post);

        recyclerSimplePost = findViewById(R.id.recyclerSimplePost);

        customProgressDialog = new CustomProgressDialog(SimplePost.this);

        listaPost = new ArrayList<>();

        userInformation = new GetUserInformation(getApplicationContext());
        idPost = getIntent().getExtras().getString("idPost");

        LoadPostInfo();
        customProgressDialog.ShowStandardProgressDialog();
    }

    public void LoadPostInfo(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://hexada.000webhostapp.com/findme/getSimplePost.php?id_post="+idPost+"&id_usuario="+userInformation.getUserID();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listenerPost, errorListener);
        requestQueue.add(request);
    }

    Response.Listener<JSONObject> listenerPost = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            JSONArray jsonArray = response.optJSONArray("feed");
            JSONArray jsonLikes = response.optJSONArray("likes");
            try {
                String id = jsonArray.getJSONObject(0).optString("0");
                String nombreUsuario = jsonArray.getJSONObject(0).getString("nombre_usuario");
                String imagen = jsonArray.getJSONObject(0).getString("imagen");
                String categoria = jsonArray.getJSONObject(0).getString("categoria");
                String likes = jsonArray.getJSONObject(0).getString("likes");
                String comentarios = jsonArray.getJSONObject(0).getString("comentarios");
                String descripcion = jsonArray.getJSONObject(0).getString("descripcion");

                String fecha = jsonArray.getJSONObject(0).getString("fecha");

                String ciudad = jsonArray.getJSONObject(0).getJSONArray("location").getJSONObject(0).optString("ciudad");
                String departamento = jsonArray.getJSONObject(0).getJSONArray("location").getJSONObject(0).optString("departamento");
                String pais = jsonArray.getJSONObject(0).getJSONArray("location").getJSONObject(0).optString("pais");

                JSONArray arrayImages = jsonArray.getJSONObject(0).getJSONArray("images");

                for(int k=0; k<arrayImages.length(); k++){
                    String imagenBase = arrayImages.optString(k);
                    imagenes.add(new Image(imagenBase));
                }

                if(jsonLikes!=null){
                    for(int f=0; f<jsonLikes.length(); f++){
                        if(id.equals(jsonLikes.getJSONObject(f).optString("id_post"))){
                            liked=true;
                        }
                    }
                }

                listaPost.add(new Post(id, imagen, nombreUsuario, categoria, imagenes, likes, comentarios, descripcion, liked, ciudad, departamento, pais, fecha));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapterPost = new AdapterFeed(listaPost, SimplePost.this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SimplePost.this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

            recyclerSimplePost.setHasFixedSize(true);
            recyclerSimplePost.setAdapter(adapterPost);
            recyclerSimplePost.setLayoutManager(linearLayoutManager);

            customProgressDialog.dismiss();
        }
    };

    ErrorListener errorListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(SimplePost.this, "error: "+error, Toast.LENGTH_SHORT).show();
            customProgressDialog.dismiss();
        }
    };

    /*

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setData(null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



     */
}