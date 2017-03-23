package com.jcd.rdbordado;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class DiscountActivity extends Fragment implements View.OnClickListener {

    View view;
    Button btSacn;
    TextView txtQrCode;

    private static final String EXTRA_CODE = "QrCode";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_discount, container, false);
        return view;
    }

    public static DiscountActivity newInstance(String code) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CODE, code);

        DiscountActivity fragment = new DiscountActivity();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btSacn = (Button) view.findViewById(R.id.bt_discount_scan);
        txtQrCode = (TextView) view.findViewById(R.id.txt_discount_value);

        txtQrCode.setText(getValueQR());
        btSacn.setOnClickListener(this);
    }

    private String getValueQR() {

        try
        {
            String value = (String) getArguments().getSerializable(EXTRA_CODE);
            WebServicesRutDB ws = new WebServicesRutDB(getContext());
            ws.posDevices();

            return value;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }


}
