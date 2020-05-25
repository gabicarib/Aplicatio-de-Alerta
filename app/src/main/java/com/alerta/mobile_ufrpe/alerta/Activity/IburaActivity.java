package com.alerta.mobile_ufrpe.alerta.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alerta.mobile_ufrpe.alerta.R;

public class IburaActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibura);

        WebView webView = (WebView) findViewById(R.id.webviewIbura);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://sjc.salvar.cemaden.gov.br/resources/graficos/interativo/grafico_CEMADEN.php?idpcd=6532&uf=PE");

        setTitle("Ibura");
    }

}

