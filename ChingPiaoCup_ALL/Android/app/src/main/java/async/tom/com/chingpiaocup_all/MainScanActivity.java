package async.tom.com.chingpiaocup_all;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;


public class MainScanActivity extends AppCompatActivity {

    SurfaceView cameraPreview;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scan);

        mView = getWindow().getDecorView().findViewById(android.R.id.content);
        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        Snackbar.make(mView, "啟動QR Code掃描器", Snackbar.LENGTH_LONG).show();
        createCameraSource();

    }

    private void createCameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                int REQUEST_CAMERA = 0;
                try {
                    if (ActivityCompat.checkSelfPermission(MainScanActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainScanActivity.this,"No Permission", Toast.LENGTH_SHORT).show();
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                        int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        ActivityCompat.requestPermissions( MainScanActivity.this, new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA );

                        return;
                    }
                    Toast.makeText(MainScanActivity.this,"Enter", Toast.LENGTH_SHORT).show();
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();
                if(barcode.size()>0){
                    Intent intent = new Intent();
                    //Toast.makeText(ScanBarcode.this, "QR CODE: "+barcode.valueAt(0).displayValue, Toast.LENGTH_LONG).show();
                    intent.putExtra("barcode",barcode.valueAt(0));
                    setResult(CommonStatusCodes.SUCCESS,intent);
                    finish();
                }
            }
        });
    }
}

