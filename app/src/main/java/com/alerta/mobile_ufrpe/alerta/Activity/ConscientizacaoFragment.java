package com.alerta.mobile_ufrpe.alerta.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alerta.mobile_ufrpe.alerta.R;
import com.github.barteksc.pdfviewer.PDFView;

public class ConscientizacaoFragment extends android.support.v4.app.Fragment {

    PDFView pdfView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_conscientizacao, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pdfView = (PDFView) view.findViewById(R.id.concietizacaoPDF);
        pdfView.fromAsset("cartilha_concientizacao.pdf").load();

        getActivity().setTitle("Conscientização");
    }

}

