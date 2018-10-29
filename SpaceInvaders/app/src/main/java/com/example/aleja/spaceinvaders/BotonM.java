package com.example.aleja.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class BotonM {
    RectF rect;

    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;


    private float length;
    private float height;

    private float x;
    private float y;

    public BotonM(Context context, int screenX, int screenY, float pX, float pY){
        rect = new RectF();

        length = screenX/30;
        height = screenY/25;

        x = screenX - pX;
        y = screenY - pY;

        // Inicializa el bitmap
        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.botonarriba);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.botonabajo);
        bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.botonderecha);
        bitmap4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.botonizquierda);

        // Ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
        bitmap1 = Bitmap.createScaledBitmap(bitmap1,
                (int) (length),
                (int) (height),
                false);

        bitmap2 = Bitmap.createScaledBitmap(bitmap2,
                (int) (length),
                (int) (height),
                false);
        bitmap3 = Bitmap.createScaledBitmap(bitmap3,
                (int) (length),
                (int) (height),
                false);
        bitmap4 = Bitmap.createScaledBitmap(bitmap4,
                (int) (length),
                (int) (height),
                false);
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public Bitmap getBitmap1() {
        return bitmap1;
    }
    public Bitmap getBitmap2() {
        return bitmap2;
    }
    public Bitmap getBitmap3() {
        return bitmap3;
    }
    public Bitmap getBitmap4() {
        return bitmap4;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
