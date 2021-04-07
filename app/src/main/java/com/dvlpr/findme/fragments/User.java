package com.dvlpr.findme.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dvlpr.findme.R;
import com.dvlpr.findme.classes.AdapterPostUser;
import com.dvlpr.findme.classes.CustomProgressDialog;
import com.dvlpr.findme.classes.GetUserInformation;
import com.dvlpr.findme.classes.Image;
import com.dvlpr.findme.classes.PostUser;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.kiprotich.japheth.image.ProfileImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User extends Fragment {

    List<PostUser> postsUser;

    ProfileImage profileImage;

    TextView tvUserName, tvUserID;

    GetUserInformation userInformation;

    RecyclerView recyclerPostUser;

    int userID;

    Context context;

    CustomProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        postsUser = new ArrayList<>();
        context = view.getContext();
        progressDialog = new CustomProgressDialog(context);
        userInformation = new GetUserInformation(view.getContext());
        userID = userInformation.getUserID();
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserID = view.findViewById(R.id.tvUserID);
        recyclerPostUser = view.findViewById(R.id.recyclerPostUser);
        profileImage = view.findViewById(R.id.profileImageUser);
        progressDialog.ShowStandardProgressDialog();

        LoadUserInfo();
        LoadUserPosts();

        return view;
    }

    public void LoadUserInfo(){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://hexada.000webhostapp.com/findme/getUserInformation.php?userID="+userID;
        JsonObjectRequest userRequest = new JsonObjectRequest(url, null, listenerUsuario, errorListenerUsuario);
        queue.add(userRequest);
    }

    Listener<JSONObject> listenerUsuario = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            JSONArray jsonArray = response.optJSONArray("usuario");
            try {
                String nombreUsuario = jsonArray.getJSONObject(0).getString("nombre_usuario");
                String urlImagen = jsonArray.getJSONObject(0).getString("imagen");
                String userID = jsonArray.getJSONObject(0).getString("id");
                tvUserName.setText(nombreUsuario);
                tvUserID.setText(userID);
                Picasso.with(context).load(urlImagen).into(profileImage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    ErrorListener errorListenerUsuario = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context, "error: "+error, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    };

    public void LoadUserPosts(){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://hexada.000webhostapp.com/findme/getUserPosts.php?id_usuario="+userID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, feedRequest, errorFeed);
        queue.add(request);
    }

    Response.Listener<JSONObject> feedRequest = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            JSONArray arrayPost = response.optJSONArray("feed");
            try{
                for(int i=0; i<arrayPost.length(); i++){
                    String id = arrayPost.getJSONObject(i).optString("0");
                    JSONArray arrayImages = arrayPost.getJSONObject(i).getJSONArray("images");
                    int cantidadImagenes = arrayImages.length();
                    postsUser.add(new PostUser(id,arrayImages.get(0).toString(), cantidadImagenes));
                }
                AdapterPostUser adapterPostUser = new AdapterPostUser(postsUser, getContext());

                recyclerPostUser.setHasFixedSize(true);
                recyclerPostUser.setAdapter(adapterPostUser);
                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
                if(arrayPost.length() > 3){
                    layoutManager.setJustifyContent(JustifyContent.SPACE_BETWEEN);
                }else{
                    layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                }
                recyclerPostUser.setLayoutManager(layoutManager);


            }catch (Exception e){
                Toast.makeText(context, "Error: "+e, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }
    };

    ErrorListener errorFeed = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context, "error: "+error, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    };

}