package com.example.aleja.spaceinvaders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

public class MenuActivity extends Activity {
    private ScoreDdHelper helper;

    private static final String TAG = "photo log:";
    private static final int CAMERA_CHOICE = CameraCharacteristics.LENS_FACING_FRONT;
    private CameraDevice cameraDevice;
    private CameraCaptureSession session;
    private TextureView textureView;
    private Surface surfacePreview;
    private boolean taken = false;
//    private ImageReader imageReader;


//    protected ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
//        @Override
//        public void onImageAvailable(ImageReader reader) {
//            Log.d(TAG, "onImageAvailable");
//            Image img = reader.acquireLatestImage();
//            if (img != null) {
//                processImage(img);
//                img.close();
//            }
//        }
//    };


    //    private void processImage(Image image) {
//        //Process image data
//        ByteBuffer buffer;
//        byte[] bytes;
//        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures/image.jpg");
//        final File parent = file.getParentFile();
//        if (parent != null) {
//            parent.mkdir();
//        }
//
//        if (image.getFormat() == ImageFormat.JPEG) {
//            buffer = image.getPlanes()[0].getBuffer();
//            bytes = new byte[buffer.remaining()]; // makes byte array large enough to hold image
//            buffer.get(bytes); // copies image from buffer to byte array
//            try (FileOutputStream output = new FileOutputStream(file)) {
//                output.write(bytes);    // write the byte array to file
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//            }
//        }
//    }


    protected CaptureRequest createCaptureRequest() {
        try {
            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
//            builder.addTarget(imageReader.getSurface());
            builder.addTarget(this.surfacePreview);
            return builder.build();
        } catch (CameraAccessException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    protected CameraCaptureSession.StateCallback sessionStateCallback = new CameraCaptureSession.StateCallback() {

        @Override
        public void onReady(CameraCaptureSession session) {
            MenuActivity.this.session = session;
            if (!taken) {
                try {
                    session.capture(createCaptureRequest(), null, null);
                } catch (CameraAccessException e) {
                    Log.e(TAG, e.getMessage());
                }
                taken = true;
            } else {
                session.close();
            }
        }


        @Override
        public void onConfigured(CameraCaptureSession session) {

        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
        }
    };

    public void actOnReadyCameraDevice() {
        try {
//            cameraDevice.createCaptureSession(Arrays.asList(imageReader.getSurface()), sessionStateCallback, null);
            cameraDevice.createCaptureSession(Arrays.asList(this.surfacePreview), sessionStateCallback, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    protected CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.d(TAG, "CameraDevice.StateCallback onOpened");
            cameraDevice = camera;
            actOnReadyCameraDevice();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.w(TAG, "CameraDevice.StateCallback onDisconnected");
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.e(TAG, "CameraDevice.StateCallback onError " + error);
        }
    };

    private String getCamera(CameraManager manager) {
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                Integer cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation != null && cOrientation == CAMERA_CHOICE) {
                    return cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setUpCamera() {
        Log.d(TAG, "set up the camera");
        CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            if (manager != null) {
                String pickedCamera = getCamera(manager);
                if (pickedCamera != null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    manager.openCamera(pickedCamera, cameraStateCallback, null);
//                imageReader = ImageReader.newInstance(720, 1280, ImageFormat.JPEG, 2/* images buffered */);
//                imageReader.setOnImageAvailableListener(onImageAvailableListener, null);
//                Log.d(TAG, "imageReader created");
                }
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            MenuActivity.this.surfacePreview = new Surface(MenuActivity.this.textureView.getSurfaceTexture());
            setUpCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new ScoreDdHelper(this);

        setContentView(R.layout.menu);

        final ImageView imagenFinal = this.findViewById(R.id.imagen);

        Bundle extras = getIntent().getExtras();
        final boolean win = extras.getBoolean(getResources().getString(R.string.victory));
        if (win) {
            imagenFinal.setImageResource(R.drawable.win);
        } else {
            imagenFinal.setImageResource(R.drawable.game_over);
        }

        final TextView scoreLabel = this.findViewById(R.id.score);
        final int score = extras.getInt(getResources().getString(R.string.score));
        scoreLabel.setText(String.valueOf(score));

        final String name = extras.getString(getResources().getString(R.string.name));
        final TextView nameLabel = this.findViewById(R.id.name);
        nameLabel.setText(name);
        Log.d("debug", name);

        final Button back = this.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MenuActivity.this.finish();
            }
        });

        this.helper.insertNewRecord(name, score);

        final Button viewRanking = this.findViewById(R.id.view_ranking);
        viewRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });

        Log.d(TAG, "onCreate try to initialize the surface and the photo");
        this.textureView = this.findViewById(R.id.photo);
        textureView.setSurfaceTextureListener(this.surfaceTextureListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        session.close();
    }
}
