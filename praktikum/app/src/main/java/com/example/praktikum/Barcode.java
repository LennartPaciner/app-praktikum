package com.example.praktikum;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission_group.CAMERA;

public class Barcode extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_barcode);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(Barcode.this, "Permisson OK!", Toast.LENGTH_LONG).show();
            }else{
                requestPermission();
            }
        //}
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.text_entry3, null);

        final EditText Name = textEntryView.findViewById(R.id.edit1);
        final EditText amountET =  textEntryView.findViewById(R.id.edit2);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Amount").setView(textEntryView).setPositiveButton(R.string.save,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        // positive function
                        EinkaufsListeDB einkaufsListeDB = new EinkaufsListeDB(getApplicationContext());
                        einkaufsListeDB.addItemEListe(scanResult+"", Name.getText().toString(), amountET.getText().toString(), null, null);
                        Intent intentEL = new Intent(Barcode.this, EinkaufsListe.class);
                        overridePendingTransition(0, 0);
                        startActivity(intentEL);
                        overridePendingTransition(0, 0);
                    }
                }).setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        Intent intent = new Intent(Barcode.this, EinkaufsListe.class);
                        startActivity(intent);
                    }
                });
        alert.show();

    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(Barcode.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int grantResults[]){
        switch(requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(Barcode.this, "Permission granted", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(Barcode.this, "Permission denied", Toast.LENGTH_LONG).show();
                        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessege("You need camera permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                        //}
                                    }
                                });
                                return;
                            }
                        //}
                    }
                }
                break;
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(Barcode.this);
                scannerView.startCamera();
            }else{
                requestPermission();
            }
        //}
    }

    @Override
    public  void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessege(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(Barcode.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
