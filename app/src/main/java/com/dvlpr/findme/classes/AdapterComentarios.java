package com.dvlpr.findme.classes;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dvlpr.findme.R;
import com.kiprotich.japheth.image.ProfileImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterComentarios extends RecyclerView.Adapter<AdapterComentarios.VH> {

    ArrayList<Comentario> listaComentarios;
    Context context;

    public AdapterComentarios(ArrayList<Comentario> listaComentarios, Context context) {
        this.listaComentarios = listaComentarios;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_comentario, parent, false);
        return new AdapterComentarios.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.asignarDatos(listaComentarios.get(position).getImagenUsuario(), listaComentarios.get(position).getNombreUsuario(), listaComentarios.get(position).getComentario());
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class VH extends RecyclerView.ViewHolder{

        ProfileImage profileImageUser;
        TextView tvComentario;

        public VH(@NonNull View itemView) {
            super(itemView);
            profileImageUser = itemView.findViewById(R.id.profileImageUser);
            tvComentario = itemView.findViewById(R.id.tvComentario);
        }

        public void asignarDatos(String imagenUsuario, String nombreUsuario, String Comentario){

            Picasso.with(context).load(imagenUsuario).into(profileImageUser);
            tvComentario.setText(Html.fromHtml("<b>" + nombreUsuario + "</b>" + ": "+ Comentario));

        }

    }

}
