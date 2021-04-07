package com.dvlpr.findme.classes;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dvlpr.findme.Comentarios;
import com.dvlpr.findme.R;
import com.kiprotich.japheth.image.ProfileImage;
import com.squareup.picasso.Picasso;

import net.colindodd.toggleimagebutton.ToggleImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.VH> {

    ArrayList<Post> listaPosts;
    Context context;

    GetUserInformation userInformation;

    int userID;

    boolean mLiked;

    public AdapterFeed(ArrayList<Post> listaPosts, Context context) {
        this.listaPosts = listaPosts;
        this.context = context;

        userInformation = new GetUserInformation(context);
        userID = userInformation.getUserID();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_post, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.asignarDatos(listaPosts.get(position).getImagen(),
                listaPosts.get(position).getNombre(),
                listaPosts.get(position).getCategoria(),
                listaPosts.get(position).getLikes(),
                listaPosts.get(position).getComentarios(),
                listaPosts.get(position).getImagenes(),
                listaPosts.get(position).getDescripcion(),
                listaPosts.get(position).getLiked(),
                listaPosts.get(position).getId(),
                listaPosts.get(position).getCiudad(),
                listaPosts.get(position).getDepartamento(),
                listaPosts.get(position).getPais(),
                listaPosts.get(position).getFecha()
        );
    }

    @Override
    public int getItemCount() {
        return listaPosts.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvCategoria;
        TextView tvLikes;
        TextView tvComentarios;
        TextView tvDescripcion;
        TextView tvFecha;
        ProfileImage profileImage;
        RecyclerView recyclerImagenes;
        ScrollingPagerIndicator indicator;
        ToggleImageButton toggleLike;
        ImageButton btnComentario;
        Button btnShare;

        public VH(@NonNull final View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            tvUsername = itemView.findViewById(R.id.nombreUsuario);
            tvCategoria = itemView.findViewById(R.id.categoria);
            recyclerImagenes = itemView.findViewById(R.id.recyclerImagenes);
            tvLikes = itemView.findViewById(R.id.textLikes);
            tvComentarios = itemView.findViewById(R.id.textComentarios);
            indicator = itemView.findViewById(R.id.indicatorImages);
            tvDescripcion = itemView.findViewById(R.id.descripcion);
            toggleLike = itemView.findViewById(R.id.btnLike);
            btnComentario = itemView.findViewById(R.id.btnComentario);
            btnShare = itemView.findViewById(R.id.btnShare);
            tvFecha = itemView.findViewById(R.id.fecha);
        }


        public void asignarDatos(String imagen, String nombreUsuario, String categoria, String likes, String comentarios, ArrayList<Image> imagenes, String descripcion, boolean liked, final String id, String ciudad, String departamento, String pais, String fecha) {

            Picasso.with(itemView.getContext()).load(imagen).placeholder(R.drawable.profile_picture_blank).into(profileImage);

            tvUsername.setText(nombreUsuario);
            tvCategoria.setText(categoria + " - " + ciudad + ", " + departamento + ", " + pais);
            tvLikes.setText(likes);
            tvComentarios.setText(comentarios);

            tvFecha.setText(fecha);

            if (descripcion.equals("")) {
                tvDescripcion.setVisibility(View.GONE);
            } else {
                tvDescripcion.setText(descripcion);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

            recyclerImagenes.setHasFixedSize(true);
            recyclerImagenes.setAdapter(new AdapterImagesFeed(imagenes));
            recyclerImagenes.setLayoutManager(linearLayoutManager);

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.findme.com/" + id);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    context.startActivity(shareIntent);
                }
            });

            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
            if (recyclerImagenes.getOnFlingListener() == null) {
                pagerSnapHelper.attachToRecyclerView(recyclerImagenes);
            }
            indicator.attachToRecyclerView(recyclerImagenes);

            if (liked) {
                toggleLike.setChecked(true);
            } else {
                toggleLike.setChecked(false);
            }

            btnComentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Comentarios.class);
                    intent.putExtra("idPost", id); // getText() SHOULD NOT be static!!!
                    context.startActivity(intent);
                }
            });

            toggleLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(buttonView.isPressed()){

                        if(buttonView.isChecked()){

                        Context context = itemView.getContext();
                        RequestQueue queue = Volley.newRequestQueue(context);
                        String url = "https://hexada.000webhostapp.com/findme/postLike.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listenerLikes, errorListener){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("idUsuario", String.valueOf(userID));
                                params.put("idPost", String.valueOf(listaPosts.get(getAdapterPosition()).getId()));
                                return params;
                            }
                        };
                        queue.add(stringRequest);

                        }else{
                        Context context = itemView.getContext();
                        RequestQueue queue = Volley.newRequestQueue(context);
                        String url = "https://hexada.000webhostapp.com/findme/postUnlike.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listenerUnlike, errorListener){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<>();
                                params.put("idUsuario", String.valueOf(userID));
                                params.put("idPost", String.valueOf(listaPosts.get(getAdapterPosition()).getId()));
                                return params;
                            }
                        };
                        queue.add(stringRequest);

                        }

                    }

                }
            });

        }

        Listener<String> listenerUnlike = new Listener<String>() {
            @Override
            public void onResponse(String response) {
                int likes = Integer.parseInt(tvLikes.getText().toString());
                likes -= 1;
                tvLikes.setText(String.valueOf(likes));
            }
        };

        Listener<String> listenerLikes = new Listener<String>() {
            @Override
            public void onResponse(String response) {
                int likes = Integer.parseInt(tvLikes.getText().toString());
                likes += 1;
                tvLikes.setText(String.valueOf(likes));
            }
        };

        ErrorListener errorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

    }

}
