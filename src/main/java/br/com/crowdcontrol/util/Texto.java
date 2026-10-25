package br.com.crowdcontrol.util;

public final class Texto {

    private Texto() { }

    public static boolean estaVazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
