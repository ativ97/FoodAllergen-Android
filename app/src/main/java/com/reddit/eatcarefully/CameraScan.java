package com.reddit.eatcarefully;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class CameraScan extends AppCompatActivity {
    SurfaceView cameraPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);

        cameraPreview= findViewById(R.id.camera);
        createCameraSource();
    }

    private void createCameraSource() {
        final int requestCode= 100;
        BarcodeDetector barcodeDetector=new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).setRequestedPreviewSize(1600, 1024).build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(CameraScan.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraScan.this, new String[]{Manifest.permission.CAMERA}, requestCode);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() > 0)
                {
                    Intent intent = new Intent();
                    intent.putExtra("barcode",barcodes.valueAt(0));
                    setResult(CommonStatusCodes.SUCCESS,intent);
                    finish();
                }
            }
        });
    }
}
