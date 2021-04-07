package com.dvlpr.findme.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dvlpr.findme.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterImagesFeed extends RecyclerView.Adapter<AdapterImagesFeed.VH> {

    ArrayList<Image> listaImagenes;

    public AdapterImagesFeed(ArrayList<Image> listaImagenes) {
        this.listaImagenes = listaImagenes;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_image, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.agregarImagen(listaImagenes.get(position).getImage());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return listaImagenes.size();
    }

    class VH extends RecyclerView.ViewHolder{

        ImageView imageView;

        public VH(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(  R.id.imagenPost);
        }

        public void agregarImagen(String imagen){


            Picasso.with(itemView.getContext()).load(imagen).placeholder(R.drawable.loading_image).into(imageView);

            /*
            byte[] decodedString = Base64.decode(imagen, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);

            Glide.with(context).load(galleryList.get(itemPosition).getImage()).thumbnail(Glide.with(context).load(R.drawable.balls)).apply(options).into(holder.kolamImage);


             */
        }

    }


}
