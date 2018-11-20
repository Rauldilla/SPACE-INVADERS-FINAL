package com.example.aleja.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

public class Marcianito {
    RectF rect;

    Random generator = new Random();

    //los invader van a ser representados por un Bitmap
    private static Bitmap bitmap1;
    private static Bitmap bitmap2;

    private static Bitmap bitmap1Destructor;
    private static Bitmap bitmap2Destructor;

    private Bitmap thisBitmap1;
    private Bitmap thisBitmap2;

    // Selector de bitmap
    private final int PRIMERO = 1;
    private final int SEGUNDO = 2;

    private int select = PRIMERO;

    // Qué tan largo y ancho será nuestro Invader
    private float length;
    private float height;

    // X es el extremo a la izquierda del rectángulo que le da forma a nuestro invader
    private float x;

    // Y es la coordenada superior
    private float y;

    // Esto mantendrá la rapidez de los pixeles por segundo a la que el invader se moverá.
    private float shipSpeed;

    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Se está moviendo la nave espacial y en qué dirección
    private int shipMoving = RIGHT;

    boolean isVisible;

    int padding;

    public Marcianito(Context context, int row, int column, int screenX, int screenY) {

        // Inicializa un RectF vacío
        rect = new RectF();

        length = screenX / 21;
        height = screenY / 20;

        isVisible = true;

        padding = screenX / 25;

        x = column * (length + padding);
        y = row * (height + padding/2);

        // Inicializa el bitmap
        if (bitmap1 == null) {
            bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.marciano1);
            // Ajusta el primer bitmap a un tamaño apropiado para la resolución de la pantalla
            bitmap1 = Bitmap.createScaledBitmap(bitmap1,
                    (int) (length),
                    (int) (height),
                    false);
        }
        if (bitmap2 == null) {
            bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.marciano2);
            // Ajusta el primer bitmap a un tamaño apropiado para la resolución de la pantalla
            bitmap2 = Bitmap.createScaledBitmap(bitmap2,
                    (int) (length),
                    (int) (height),
                    false);
        }
        this.thisBitmap1 = bitmap1;
        this.thisBitmap2 = bitmap2;

        // Qué tan rápido va el invader en pixeles por segundo
        shipSpeed = screenX/20;
    }

    public Marcianito(Context context, int screenX, int screenY) {

        // Inicializa un RectF vacío
        rect = new RectF();

        length = screenX / 15;
        height = screenY / 20;

        isVisible = false;

        padding = screenX / 25;

        x = 0;
        y = height + padding/5;

        // Inicializa el bitmap
        if (bitmap1Destructor == null) {
            bitmap1Destructor = BitmapFactory.decodeResource(context.getResources(), R.drawable.destructor1);
            // Ajusta el primer bitmap a un tamaño apropiado para la resolución de la pantalla
            bitmap1Destructor = Bitmap.createScaledBitmap(bitmap1Destructor,
                    (int) (length),
                    (int) (height),
                    false);
        }

        if (bitmap2Destructor == null) {
            bitmap2Destructor = BitmapFactory.decodeResource(context.getResources(), R.drawable.destructor2);
            bitmap2Destructor = Bitmap.createScaledBitmap(bitmap2Destructor,
                    (int) (length),
                    (int) (height),
                    false);
        }

        this.thisBitmap1 = bitmap1Destructor;
        this.thisBitmap2 = bitmap2Destructor;

        // Qué tan rápido va el invader en pixeles por segundo
        shipSpeed = screenX/11;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }

    public RectF getRect(){
        return rect;
    }

    public void changeBitmap(){
        if (select == PRIMERO){
            select = SEGUNDO;
        } else {
            select = PRIMERO;
        }
    }

    public Bitmap getBitmap(){
        if (select == PRIMERO) {
            return this.thisBitmap1;
        } else {
            return this.thisBitmap2;
        }
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getLength(){
        return length;
    }

    public float getHeight() {
        return height;
    }

    public void reinicio(){
        x = 0;
        y = height + padding/5;
        this.isVisible = true;
    }

    public void update(long fps){
        if(shipMoving == LEFT){
            x = x - shipSpeed / fps;
        }

        if(shipMoving == RIGHT){
            x = x + shipSpeed / fps;
        }

        // Actualiza rect el cual es usado para detectar impactos
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

    public void dropDownAndReverse(){
        if(shipMoving == LEFT){
            shipMoving = RIGHT;
        }else{
            shipMoving = LEFT;
        }

        y = y + height;

        shipSpeed = shipSpeed * 1.10f;
    }

    public boolean takeAim(float playerShipX, float playerShipLength){

        int randomNumber = -1;

        // Si está cerca del jugador
        if((playerShipX + playerShipLength > x &&
                playerShipX + playerShipLength < x + length) || (playerShipX > x && playerShipX < x + length)) {

            // Una probabilidad de 1 en 150 chance para disparar
            randomNumber = generator.nextInt(150);
            if(randomNumber == 0) {
                return true;
            }
        }

        // Si está disparando aleatoriamente (sin estar cerca del jugador) una probabilidad de 1 en 2000
        randomNumber = generator.nextInt(2000);
        if(randomNumber == 0){
            return true;
        }

        return false;
    }

    public boolean takeAimEsp(float playerShipX, float playerShipLength){

        int randomNumber;

        randomNumber = generator.nextInt(50);
        if(randomNumber == 0) {
            return true;
        }

        return false;
    }
}
