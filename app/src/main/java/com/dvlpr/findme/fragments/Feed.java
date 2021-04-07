package com.dvlpr.findme.fragments;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dvlpr.findme.R;
import com.dvlpr.findme.classes.AdapterFeed;
import com.dvlpr.findme.classes.CustomProgressDialog;
import com.dvlpr.findme.classes.GetUserInformation;
import com.dvlpr.findme.classes.Image;
import com.dvlpr.findme.classes.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Feed extends Fragment {

    RecyclerView recyclerFeed;

    ArrayList<Post> listaPost;

    CustomProgressDialog customProgressDialog;

    RelativeLayout frameFeed;

    AdapterFeed adapterFeed;
    View view;
    LinearLayout layoutLoading;

    int userID;

    GetUserInformation userInformation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feed, container, false);

        frameFeed = view.findViewById(R.id.frameFeed);
        recyclerFeed = view.findViewById(R.id.recyclerFeed);
        layoutLoading = view.findViewById(R.id.layoutLoading);

        customProgressDialog = new CustomProgressDialog(getContext());

        userInformation = new GetUserInformation(getContext());

        userID = userInformation.getUserID();

        listaPost = new ArrayList<>();
        loadFeed();

        return view;
    }

    public void loadFeed() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://hexada.000webhostapp.com/findme/getFeed.php?id_usuario="+userID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, listenerFeed, errorListenerFeed);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                12000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        queue.add(jsonObjectRequest);
    }

    Listener<JSONObject> listenerFeed = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            layoutLoading.setVisibility(View.GONE);
            JSONArray jsonArray = response.optJSONArray("feed");
            JSONArray jsonLikes = response.optJSONArray("likes");
            try {
                for (int i = 0; i < jsonArray.length(); i++) {

                    String id = jsonArray.getJSONObject(i).optString("0");
                    String imagen = jsonArray.getJSONObject(i).optString("imagen");
                    String nombreUsuario = jsonArray.getJSONObject(i).optString("nombre_usuario");
                    String categoria = jsonArray.getJSONObject(i).optString("categoria");
                    String likes = jsonArray.getJSONObject(i).optString("likes");
                    String comentarios = jsonArray.getJSONObject(i).optString("comentarios");
                    String descripcion = jsonArray.getJSONObject(i).optString("descripcion");

                    String fecha = jsonArray.getJSONObject(i).optString("fecha");

                    String ciudad = jsonArray.getJSONObject(i).optString("ciudad");
                    String departamento = jsonArray.getJSONObject(i).optString("departamento");
                    String pais = jsonArray.getJSONObject(i).optString("pais");

                    boolean liked=false;

                    ArrayList<Image> imagenes = new ArrayList<>();

                    JSONArray arrayImages = jsonArray.getJSONObject(i).getJSONArray("images");

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
                    //jsonArray.getJSONObject(i).optJSONArray("imagenes");

                }
                adapterFeed = new AdapterFeed(listaPost, getContext());

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

                recyclerFeed.setHasFixedSize(true);
                recyclerFeed.setAdapter(adapterFeed);
                recyclerFeed.setLayoutManager(linearLayoutManager);


            } catch (Exception e) {
                Toast.makeText(view.getContext(), "Exception: " + e, Toast.LENGTH_SHORT).show();
                System.out.println("Error: "+e);
            }
            customProgressDialog.dismiss();
        }
    };

    ErrorListener errorListenerFeed = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            layoutLoading.setVisibility(View.GONE);

            recyclerFeed.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            customProgressDialog.dismiss();
        }
    };

}