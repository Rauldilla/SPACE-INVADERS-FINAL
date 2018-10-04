package com.example.aleja.spaceinvaders;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

// SpaceInvadersActivity es el punto de entrada al juego.
// Se va a encargar del ciclo de vida del juego al llamar alos métodos de vistaSpaceInvaders cuando sean solicitados por el SO.
public class SpaceInvaders extends Activity {

    //  visualización del juego
    // Tendrá la lógica del juego
    // y responderá a los toques a la pantalla
    VistaSpaceInvaders vistaSpaceInvaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener un objeto de Display para accesar a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);

        // determinar si es un niño
        Bundle extras = getIntent().getExtras();
        final boolean isAdult = extras.getBoolean("adult");

        // Inicializar gameView y establecerlo como la visualización
        vistaSpaceInvaders = new VistaSpaceInvaders(this, size.x, size.y, isAdult);
        setContentView(vistaSpaceInvaders);

    }

    // Este método se ejecuta cuando el jugador empieza el juego
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        vistaSpaceInvaders.resume();
    }

    // Le dice al método de reanudar del gameView que se ejecute
    @Override
    protected void onPause() {
        super.onPause();

        // Le dice al método de pausa del gameView que se ejecute
        vistaSpaceInvaders.pause();
    }
}