package com.dvlpr.findme.classes;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dvlpr.findme.Comentarios;
import com.dvlpr.findme.R;
import com.dvlpr.findme.SimplePost;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPostUser extends RecyclerView.Adapter<AdapterPostUser.VH> {

    List<PostUser> listaPostUsers;
    Context context;

    public AdapterPostUser(List<PostUser> listaPostUsers, Context context) {
        this.listaPostUsers = listaPostUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_post_user, parent, false);
        return new AdapterPostUser.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.asignarDatos(listaPostUsers.get(position).getId(), listaPostUsers.get(position).getImagenPrincipal(), listaPostUsers.get(position).getImagenes());
    }

    @Override
    public int getItemCount() {
        return listaPostUsers.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class VH extends RecyclerView.ViewHolder{

        ImageView IVimagenPrincipal;
        ImageView iconImagenes;
        MaterialCardView cardPostUser;


        public VH(@NonNull View itemView) {
            super(itemView);
            IVimagenPrincipal = itemView.findViewById(R.id.ImagePostUser);
            iconImagenes = itemView.findViewById(R.id.iconMultimage);
            cardPostUser = itemView.findViewById(R.id.cardPostUser);
        }

        public void asignarDatos(final String id, String imagenPrincipal, int cantidadImagenes){
            Picasso.with(context).load(imagenPrincipal).into(IVimagenPrincipal);
            if(cantidadImagenes>1){
                iconImagenes.setVisibility(View.VISIBLE);
            }else{
                iconImagenes.setVisibility(View.GONE);
            }

            cardPostUser.getLayoutParams().height = getWithNHeight();
            cardPostUser.getLayoutParams().width = getWithNHeight();

            cardPostUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SimplePost.class);
                    intent.putExtra("idPost", id); // getText() SHOULD NOT be static!!!
                    context.startActivity(intent);
                }
            });
        }

    }

    public int getWithNHeight(){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float screenWidth = metrics.widthPixels / metrics.density;
        int val = (int)(screenWidth)/4;
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (val * scale + 0.5f);
        return pixels;
    }

}
