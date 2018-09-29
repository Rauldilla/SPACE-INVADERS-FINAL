package com.example.aleja.spaceinvaders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class VistaSpaceInvaders extends SurfaceView implements Runnable {
    Context context;

    // Esta es nuestra secuencia
    private Thread hiloJuego = null;

    // Nuestro SurfaceHolder para bloquear la superficie antes de que dibujemos nuestros gráficos
    private SurfaceHolder ourHolder;

    // Un booleano el cual vamos a activar y desactivar
    // cuando el juego este activo- o no.
    private volatile boolean jugando;

    // El juego esta pausado al iniciar
    private boolean pausado = true;

    // Un objeto de lienzo (Canvas) y de pintar (Paint)
    private Canvas canvas;
    private Paint paint;

    // Esta variable rastrea los cuadros por segundo del juego
    private long fps;

    // Esto se utiliza para ayudar a calcular los cuadros por segundo
    private long timeThisFrame;

    // El tamaño de la pantalla en pixeles
    private int ejeX;
    private int ejeY;

    // La nave del jugador
    private Nave nave;

    // Laser
    private Laser laser;

    // laseres de los marcianitos
    private Laser[] marcianitoLaser = new Laser[200];
    private int proxLaser;
    private int maxMarcianitosLaser = 10;

    // Hasta 60 Marcianitos
    Marcianito[] marcianito = new Marcianito[60];
    int numMarcianitos = 0;

    // Las guaridas del jugador están construidas a base de ladrillos
    private Bloque[] bloques = new Bloque[400];
    private int numBloque;

    /*// Para los efectos de sonido
    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;*/

    // La puntuación
    int puntuacion = 0;

    // Vidas
    private int vidas = 1;

    // flag que indica si habilita el disparo
    private boolean isAdult;

    /*// ¿Que tan amenazador debe de ser el sonido?
    private long menaceInterval = 1000;
    // Cual amenazante sonido debe de seguir en reproducirse
    private boolean uhOrOh;
    // Cuando fué la última vez que reproducimos un amenazante sonido
    private long lastMenaceTime = System.currentTimeMillis();*/

    // Cuando inicializamos (call new()) en gameView

    // Este método especial de constructor se ejecuta
    public VistaSpaceInvaders(Context context, int x, int y, boolean isAdult) {

        // La siguiente línea del código le pide a
        // la clase de SurfaceView que prepare nuestro objeto.
        // !Que amable¡.
        super(context);

        this.isAdult = isAdult;

        // Hace una copia del "context" disponible globalmete para que la usemos en otro método
        this.context = context;

        // Inicializa los objetos de ourHolder y paint
        ourHolder = getHolder();
        paint = new Paint();

        ejeX = x;
        ejeY = y;


        /*// Este SoundPool está obsoleto pero no te preocupes
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        try{
            // Crea objetos de las 2 clases requeridas
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Carga nuestros efectos de sonido en la memoria listos para usarse
            descriptor = assetManager.openFd("shoot.ogg");
            shootID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderexplode.ogg");
            invaderExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("playerexplode.ogg");
            playerExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("uh.ogg");
            uhID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("oh.ogg");
            ohID = soundPool.load(descriptor, 0);

        }catch(IOException e){
            // Imprime un mensaje de error a la consola
            Log.e("error", "failed to load sound files");
        }*/

        prepararNivel();
    }

    private void prepararNivel() {

        // Aquí vamos a inicializar todos los objetos del juego

        // Hacer una nueva nave espacial del jugador
        // Haz una nave espacial para un jugador nuevo
        nave = new Nave(context, ejeX, ejeY);

        // Prepara la bala del jugador
        laser = new Laser(ejeY);

        // Inicializa la formación de invadersBullets
        for (int i = 0; i < marcianitoLaser.length; i++) {
            marcianitoLaser[i] = new Laser(ejeY);
        }

        // Construye un ejercito de invaders
        numMarcianitos = 0;
        for (int column = 0; column < 6; column++) {
            for (int row = 0; row < 5; row++) {
                marcianito[numMarcianitos] = new Marcianito(context, row, column, ejeX, ejeY);
                numMarcianitos++;
            }
        }

        // Construye las guaridas
        numBloque = 0;
        for (int shelterNumber = 0; shelterNumber < 4; shelterNumber++) {
            for (int column = 0; column < 10; column++) {
                for (int row = 0; row < 5; row++) {
                    bloques[numBloque] = new Bloque(row, column, shelterNumber, ejeX, ejeY);
                    numBloque++;
                }
            }
        }

    }

    @Override
    public void run() {
        while (jugando) {

            // Captura el tiempo actual en milisegundos en startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Actualiza el cuadro
            if (!pausado) {
                update();
            }

            // Dibuja el cuadro
            dibujar();

            // Calcula los cuadros por segundo de este cuadro
            // Ahora podemos usar los resultados para
            // medir el tiempo de animaciones y otras cosas más.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // Vamos a hacer algo nuevo aquí hacia el final de proyecto

        }


    }

    private void dibujar() {
        // Asegurate de que la superficie del dibujo sea valida o tronamos
        if (ourHolder.getSurface().isValid()) {
            // Bloquea el lienzo para que este listo para dibujar
            canvas = ourHolder.lockCanvas();

            // Dibuja el color del fondo
            canvas.drawColor(Color.argb(255, 0, 0, 0)/*, PorterDuff.Mode.CLEAR*/);

            // Escoje el color de la brocha para dibujar
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Dibuja a la nave espacial del jugador
            canvas.drawBitmap(nave.getBitmap(), nave.getX(), ejeY - 100, paint);

            // Dibuja a los invaders
            // Dibuja a los invaders
            for (int i = 0; i < numMarcianitos; i++) {
                if (marcianito[i].getVisibility()) {
                    //if(uhOrOh) {
                    canvas.drawBitmap(marcianito[i].getBitmap(), marcianito[i].getX(), marcianito[i].getY(), paint);
                    //}else{
                    //canvas.drawBitmap(invaders[i].getBitmap2(), invaders[i].getX(), invaders[i].getY(), paint);
                    //}
                }
            }


            // Dibuja los ladrillos si están visibles
            for (int i = 0; i < numBloque; i++) {
                if (bloques[i].getVisibility()) {
                    canvas.drawRect(bloques[i].getRect(), paint);
                }
            }

            // Dibuja la bala del jugador si está activa
            if (laser.getStatus()) {
                canvas.drawRect(laser.getRect(), paint);
            }

            // Dibuja las balas de los invaders

            // Actualiza todas las balas de los invaders si están activas
            for (int i = 0; i < marcianitoLaser.length; i++) {
                if (marcianitoLaser[i].getStatus()) {
                    canvas.drawRect(marcianitoLaser[i].getRect(), paint);
                }
            }

            // Dibuja la puntuación y las vidas restantes
            // Cambia el color de la brocha
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Puntuacion: " + puntuacion + " Vidas: " + vidas, 10, 50, paint);

            // Extrae todo a la pantalla
            ourHolder.unlockCanvasAndPost(canvas);
        }


    }


    private void update() {
        // ¿Chocó el invader contra el lado de la pantalla?
        boolean bumped = false;

        // ¿Ha perdido el jugador?
        boolean pierde = false;

        // Mueve la nave espacial del jugador
        nave.update(fps);

        // Actualiza a todos los invaders si están visibles
        for (int i = 0; i < numMarcianitos; i++) {

            if (marcianito[i].getVisibility()) {
                // Mueve el siguiente invader
                marcianito[i].update(fps);

                // ¿Quiere hacer un disparo?
                if (marcianito[i].takeAim(nave.getX(),
                        nave.getLength())) {

                    // Si sí, intentalo y genera una bala
                    if (marcianitoLaser[proxLaser].shoot(marcianito[i].getX()
                                    + marcianito[i].getLength() / 2,
                            marcianito[i].getY(), laser.ABAJO)) {

                        // Disparo realizado
                        // Preparete para el siguiente disparo
                        proxLaser++;

                        // Inicia el ciclo repetitivo otra vez al
                        // primero si ya hemos llegado al último.
                        if (proxLaser == maxMarcianitosLaser) {
                            // Esto detiene el disparar otra bala hasta
                            // que una haya completado su trayecto.
                            // Por que si bullet 0 todavia está activo,
                            // shoot regresa a false.
                            proxLaser = 0;
                        }
                    }
                }

                // Si ese movimiento causó que golpearan la pantalla,
                // cambia bumped a true.
                if (marcianito[i].getX() > ejeX - marcianito[i].getLength()
                        || marcianito[i].getX() < 0) {

                    bumped = true;

                }
            }

        }
        if (this.isAdult) {
            // Actualiza a todas las balas de los invaders si están activas

            // ¿Chocó algún invader en el extremo de la pantalla?

            if (bumped) {

                // Mueve a todos los invaders hacia abajo y cambia la dirección
                for (int i = 0; i < numMarcianitos; i++) {
                    marcianito[i].dropDownAndReverse();
                    // Han aterrizado los invaders
                    if (marcianito[i].getY() > ejeY - ejeY / 10) {
                        pierde = true;
                    }
                }

                // Incrementa el nivel de amenaza
                // al hacer los sonidos más frecuentes
                //menaceInterval = menaceInterval - 80;
            }
            // Actualiza la bala del jugador

            if (laser.getStatus()) {
                laser.update(fps);
            }


            // Actualiza todas las balas de los invaders si están activas
            for (int i = 0; i < marcianitoLaser.length; i++) {
                if (marcianitoLaser[i].getStatus()) {
                    marcianitoLaser[i].update(fps);
                }
            }

            if (pierde) {
                prepararNivel();
            }

            // Ha tocado la parte alta de la pantalla la bala del jugador
            if (laser.getImpactPointY() < 0) {
                laser.setInactive();
            }

            // Ha tocado la parte baja de la pantalla la bala del invader
            for (int i = 0; i < marcianitoLaser.length; i++) {
                if (marcianitoLaser[i].getImpactPointY() > ejeY) {
                    marcianitoLaser[i].setInactive();
                }
            }

            // Ha tocado la bala del jugador a algún invader
            if (laser.getStatus()) {
                for (int i = 0; i < numMarcianitos; i++) {
                    if (marcianito[i].getVisibility()) {
                        if (RectF.intersects(laser.getRect(), marcianito[i].getRect())) {
                            marcianito[i].setInvisible();

                            laser.setInactive();
                            puntuacion = puntuacion + 10;

                            // Ha ganado el jugador
                            if (puntuacion == numMarcianitos * 10) {
                                pausado = true;
                                puntuacion = 0;
                                vidas = 3;
                                prepararNivel();
                            }
                        }
                    }
                }
            }

            // Actualiza las balas del jugador

            // ¿Han golpeado las balas del jugador la parte superior de la pantalla?

            // ¿Ha golpeado alguna bala de un invader la parte inferior de la pantalla?

            // ¿Ha golpeado la bala del jugador a un invader?


            // Ha impactado una bala alienígena a un ladrillo de la guarida

            for (int i = 0; i < marcianitoLaser.length; i++) {
                if (marcianitoLaser[i].getStatus()) {
                    for (int j = 0; j < numBloque; j++) {
                        if (bloques[j].getVisibility()) {
                            if (RectF.intersects(marcianitoLaser[i].getRect(), bloques[j].getRect())) {
                                // A collision has occurred
                                marcianitoLaser[i].setInactive();
                                bloques[j].setInvisible();
                            }
                        }
                    }
                }

            }
            
            // Ha impactado un marciano con la barrera

            for (int i = 0; i < numMarcianitos; i++) {
                if (marcianito[i].getVisibility()){
                    for (int j = 0; j < numBloque; j++) {
                        if (bloques[j].getVisibility()) {
                            if (RectF.intersects(marcianito[i].getRect(), bloques[j].getRect())) {
                                bloques[j].setInvisible();
                            }
                        }
                    }
                }
            }


            // ¿Ha golpeado una bala del jugador a un ladrillo de guarida?
            // Ha impactado una bala del jugador a un ladrillo de la guarida
            if (laser.getStatus()) {
                for (int i = 0; i < numBloque; i++) {
                    if (bloques[i].getVisibility()) {
                        if (RectF.intersects(laser.getRect(), bloques[i].getRect())) {
                            // A collision has occurred
                            laser.setInactive();
                            bloques[i].setInvisible();

                        }
                    }
                }
            }

            // Ha impactado una bala de un invader a la nave espacial del jugador
            for (int i = 0; i < marcianitoLaser.length; i++) {
                if (marcianitoLaser[i].getStatus()) {
                    if (RectF.intersects(nave.getRect(), marcianitoLaser[i].getRect())) {
                        marcianitoLaser[i].setInactive();
                        vidas--;

                        // ¿Se acabó el juego?
                        if (vidas == 0) {
                            pausado = true;
                            vidas = 1;
                            puntuacion = 0;
                            prepararNivel();

                        }
                    }
                }
            }
        }
    }

    // Si SpaceInvadersActivity es pausado/detenido
    // apaga nuestra secuencia.
    public void pause() {
        jugando = false;
        try {
            hiloJuego.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // Si SpaceInvadersActivity es iniciado entonces
    // inicia nuestra secuencia.
    public void resume() {
        jugando = true;
        hiloJuego = new Thread(this);
        hiloJuego.start();
    }

    // La clase de SurfaceView implementa a onTouchListener
    // Así es que podemos anular este método y detectar toques a la pantalla.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // El jugador ha tocado la pantalla
            case MotionEvent.ACTION_DOWN:

                pausado = false;

                if (motionEvent.getY() > ejeY - ejeY / 10) {
                    if (motionEvent.getX() > ejeX / 2) {
                        nave.setMovementState(nave.RIGHT);
                    } else {
                        nave.setMovementState(nave.LEFT);
                    }

                }

                if (motionEvent.getY() < ejeY - ejeY / 8) {
                    // Disparos lanzados
                    if (laser.shoot(nave.getX() +
                            nave.getLength() / 20, ejeY, laser.ARRIBA)) {
                        // soundPool.play(shootID, 1, 1, 0, 0, 1);
                    }
                }
                break;

            // El jugador ha retirado su dedo de la pantalla
            case MotionEvent.ACTION_UP:

                if (motionEvent.getY() > ejeY - ejeY / 10) {
                    nave.setMovementState(nave.PARADA);
                }

                break;

        }

        return true;
    }
}
