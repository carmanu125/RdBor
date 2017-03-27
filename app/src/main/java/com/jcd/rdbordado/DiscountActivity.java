package com.jcd.rdbordado;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jcd.rdbordado.ws.WebServicesRutDB;

public class DiscountActivity extends Activity implements View.OnClickListener {

    Button btSacn;
    public static TextView txtQrCode;

    private static final String EXTRA_CODE = "QrCode";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        btSacn = (Button) findViewById(R.id.bt_discount_scan);
        txtQrCode = (TextView) findViewById(R.id.txt_discount_value);

        //txtQrCode.setText(getValueQR());
        btSacn.setOnClickListener(this);

    }

    private String getValueQR(String value) {

        try
        {
            //String value = getIntent().getStringExtra(EXTRA_CODE);
            String url = WebServicesRutDB.URL_WEB_SERVICES + WebServicesRutDB.URL_POST_DEVICES;
            if(value.equals(url)){

                WebServicesRutDB ws = new WebServicesRutDB(this);
                ws.posDevices();
                value = "Por favor espere...";
            }else{
                Toast.makeText(this, "El codigo Qr no es el indicado", Toast.LENGTH_SHORT).show();
                value = "Intente de nuevo";
            }


            return value;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data);
// handle scan result
        if (scanResult != null) {
            getValueQR(scanResult.getContents());
        }
    }
}
