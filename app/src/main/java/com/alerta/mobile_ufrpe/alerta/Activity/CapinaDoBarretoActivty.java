package com.alerta.mobile_ufrpe.alerta.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alerta.mobile_ufrpe.alerta.R;

public class CapinaDoBarretoActivty extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campina);

        WebView webView = (WebView) findViewById(R.id.webviewCampina);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://sjc.salvar.cemaden.gov.br/resources/graficos/interativo/grafico_CEMADEN.php?idpcd=6531&uf=PE");

        setTitle("Campina do Barreto");
    }

}
