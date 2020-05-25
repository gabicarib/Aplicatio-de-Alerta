package com.alerta.mobile_ufrpe.alerta.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alerta.mobile_ufrpe.alerta.Entidades.EstadoAtual;
import com.alerta.mobile_ufrpe.alerta.Entidades.Pluviometro;
import com.alerta.mobile_ufrpe.alerta.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ListaDePluviometrosActivity extends android.support.v4.app.Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_lista_de_pluviometros, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final List<Pluviometro> pluviometros = todosOsPluviometros();
        final ListView listaDePluviometros = (ListView) view.findViewById(R.id.lista_pluviometro);

        ListAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, pluviometros);
        listaDePluviometros.setAdapter(adapter);

        getActivity().setTitle("Pluviômetros");


        listaDePluviometros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;

                final int result = 1;
                switch (itemPosition) {
                    case 0:
                        Intent campina = new Intent(getActivity(), CapinaDoBarretoActivty.class);
                        ListaDePluviometrosActivity.this.startActivity(campina);
                        break;
                    case 1:
                        Intent ibura = new Intent(getActivity(), IburaActivity.class);
                        ListaDePluviometrosActivity.this.startActivity(ibura);
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 5:

                        break;
                }

            }

        });

    }


    /**
     * Devolução de uma lista de pluviômetros.
     * Para isso será considerado um hard coded.
     *
     * @return lista com todos os pluviômetro
     */


    List<Pluviometro> todosOsPluviometros() {
        return new ArrayList<>(Arrays.asList(
                new Pluviometro("Campina do Barreto", EstadoAtual.SEM_CHUVA),
                new Pluviometro("Ibura", EstadoAtual.CHOVENDO),
                new Pluviometro("Pina", EstadoAtual.CHOVENDO),
                new Pluviometro("Torreão", EstadoAtual.SEM_CHUVA),
                new Pluviometro("Areias", EstadoAtual.CHOVENDO),
                new Pluviometro("Porto", EstadoAtual.CHOVENDO),
                new Pluviometro("Boa Vista", EstadoAtual.SEM_CHUVA),
                new Pluviometro("Morro da Conceição", EstadoAtual.SEM_CHUVA),
                new Pluviometro("Corrego do Jenipapo", EstadoAtual.CHOVENDO),
                new Pluviometro("Estação Experimental UFRPE", EstadoAtual.SEM_CHUVA),
                new Pluviometro("USF Alto Bela Vista - Ibura", EstadoAtual.SEM_CHUVA),
                new Pluviometro("UPA - Nova Descoberta", EstadoAtual.CHOVENDO),
                new Pluviometro("Dois Unidos", EstadoAtual.SEM_CHUVA),
                new Pluviometro("UPA - Imbiribeira", EstadoAtual.SEM_CHUVA),
                new Pluviometro("Alto Mandu", EstadoAtual.CHOVENDO),
                new Pluviometro("San Martin", EstadoAtual.CHOVENDO),
                new Pluviometro("Dois Unidos", EstadoAtual.SEM_CHUVA)));


    }
}