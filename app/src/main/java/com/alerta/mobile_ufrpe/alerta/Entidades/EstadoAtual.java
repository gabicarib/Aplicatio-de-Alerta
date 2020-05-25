package com.alerta.mobile_ufrpe.alerta.Entidades;

/**
 * Created by alex on 02/07/17.
 */

public enum EstadoAtual {

    CHOVENDO {
        @Override
        public String toString() {
            return "chovendo";
        }
    }, SEM_CHUVA {
        @Override
        public String toString() {
            return "sem chuva";
        }
    }
}
