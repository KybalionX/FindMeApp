package com.dvlpr.findme.fragments;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dvlpr.findme.R;
import com.dvlpr.findme.classes.ScreenInformation;

import pl.droidsonroids.gif.GifImageView;

public class Welcome extends Fragment {

    GifImageView gifBackground;

    Button bntNuevoUsuario, btnIniciarSesion;

    ScreenInformation screenInformation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        //vars Declaration
        screenInformation = new ScreenInformation(getContext());
        int[] Gifs = {R.drawable.background_gif, R.drawable.background_gif_2, R.drawable.background_gif_3, R.drawable.background_gif_4, R.drawable.background_gif_5};

        //Finders
        RelativeLayout LayoutContainer = view.findViewById(R.id.LayoutContainerFragmentWelcome);
        gifBackground = view.findViewById(R.id.gifBackground);
        bntNuevoUsuario = view.findViewById(R.id.btnNuevoUsuario);
        btnIniciarSesion = view.findViewById(R.id.btnIniciarSesion);

        bntNuevoUsuario.setOnClickListener(listenerNuevoUsuario);
        btnIniciarSesion.setOnClickListener(listenerIniciarSesion);

        gifBackground.setBackgroundResource(Gifs[getRandomNumber(0, Gifs.length)]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        LayoutContainer.setPadding(12,screenInformation.GetStatusBarHeight(), 12, screenInformation.GetNavigationBarHeight());

        return view;
    }

    View.OnClickListener listenerIniciarSesion = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.FragmentContainer, new IniciarSesion()).addToBackStack(null).commit();
        }
    };

    View.OnClickListener listenerNuevoUsuario = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.FragmentContainer, new NuevoUsuario()).addToBackStack(null).commit();
        }
    };

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }





}