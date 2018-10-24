package com.example.aleja.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class BotonM {
    RectF rect;

    private Bitmap bitmap;

    private float length;
    private float height;

    private float x;
    private float y;

    public BotonM(Context context, int screenX, int screenY, float pX, float pY){
        rect = new RectF();

        length = screenX/20;
        height = screenY/20;

        x = screenX - pX;
        y = screenY - pY;

        // Inicializa el bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.nave2);

        // Ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
        bitmap = Bitmap.createScaledBitmap(bitmap,
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
