package com.dvlpr.findme.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dvlpr.findme.R;
import com.dvlpr.findme.classes.AdapterCategorias;
import com.dvlpr.findme.classes.Categoria;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Buscar extends Fragment {

    ArrayList<Categoria> listaCategorias;
    AdapterCategorias adapterCategorias;

    RecyclerView recyclerCategorias;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);

        listaCategorias = new ArrayList<>();

        recyclerCategorias = view.findViewById(R.id.recyclerCategorias);

        LoadCategories();

        return view;
    }

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
                    String id = jsonArray.getJSONObject(i).optString("id");
                    String categoria = jsonArray.getJSONObject(i).optString("categoria");
                    String ilustracion = jsonArray.getJSONObject(i).optString("ilustracion");
                    System.out.println("Elemento "+i+": "+ilustracion);
                    listaCategorias.add(new Categoria(id, categoria, ilustracion));

                }
                adapterCategorias = new AdapterCategorias(listaCategorias, getContext());
                recyclerCategorias.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerCategorias.setAdapter(adapterCategorias);
            }catch (Exception e){
                Toast.makeText(getContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
            }
        }
    };

    ErrorListener errorListenerCategories = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };

}