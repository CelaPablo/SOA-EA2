package com.example.soa_ea2;

public class Conexion implements Runnable {

    private String path;
    private String action;
    private Object user;

    public Conexion(String path, String action, Object user) {
        this.path = path;
        this.action = action;
        this.user = user;
    }

    @Override
    public void run() {

    }

    private void register(Object user) {

    }

    private void login(Object user) {

    }

    private void refreshToken(Object user) {

    }

    //private void
}
