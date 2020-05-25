package com.alerta.mobile_ufrpe.alerta.Entidades;

public class Pluviometro {

    private String nome;
    private EstadoAtual estado;

    public Pluviometro(String nome, EstadoAtual estado) {
        this.nome = nome;
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EstadoAtual getEstado() {
        return estado;
    }

    public void setEstado(EstadoAtual estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pluviômetro: " + nome + "\n" +
                "Estado: " + estado;
    }
}


