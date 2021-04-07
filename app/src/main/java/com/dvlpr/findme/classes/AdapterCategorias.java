package com.dvlpr.findme.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dvlpr.findme.R;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterCategorias extends RecyclerView.Adapter<AdapterCategorias.VH> {

    ArrayList<Categoria> listaCategoria;
    Context context;

    public AdapterCategorias(ArrayList<Categoria> listaCategoria, Context context) {
        this.listaCategoria = listaCategoria;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_categoria, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.AsignarDatos(listaCategoria.get(position).getId(), listaCategoria.get(position).getCategoria(), listaCategoria.get(position).getIlustracion());
    }

    @Override
    public int getItemCount() {
        return listaCategoria.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tvCategoria;
        ImageView ivIlustracion;
        MaterialCardView cardCategoria;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            ivIlustracion = itemView.findViewById(R.id.ivIlustracion);
            cardCategoria = itemView.findViewById(R.id.cardCategoria);
        }

        public void AsignarDatos(String id, String categoria, String ilustracion){
            if(id.equals("1")){
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) cardCategoria.getLayoutParams();
                layoutParams.setMargins(30, 0, 8, 0);
                cardCategoria.requestLayout();
            }else{
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) cardCategoria.getLayoutParams();
                layoutParams.setMargins(15, 0, 8, 0);
                cardCategoria.requestLayout();
            }
            if(id.equals(String.valueOf(listaCategoria.size()))){
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) cardCategoria.getLayoutParams();
                layoutParams.setMargins(15, 0, 20, 0);
                cardCategoria.requestLayout();
            }

            tvCategoria.setText(categoria);
            Picasso.with(context).load(ilustracion).into(ivIlustracion);
        }

    }

}
