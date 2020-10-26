package com.example.soa_ea2;

import android.content.Context;
import android.widget.Toast;

public class User {

    private static User instance;
    private String token, tokenRefresh;
    private  String name, lastname, dni, password, comision, email;

    private User() {}

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }

        return instance;
    }

    public String getDni() {
        return dni;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getToken() {
        return token;
    }
    public String getLastname() {
        return lastname;
    }
    public String getPassword() {
        return password;
    }
    public String getComision() {
        return comision;
    }
    public String getTokenRefresh() {
        return tokenRefresh;
    }

    public void setDni(String dni) { this.dni = dni; }
    public void setName(String name) { this.name = name; }
    public void setToken(String token) {
        this.token = token;
    }
    public void setEmail(String email) { this.email = email; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public void setPassword(String password) { this.password = password; }
    public void setComision(String comision) { this.comision = comision; }
    public void setTokenRefresh(String tokenRefresh) {
        this.tokenRefresh = tokenRefresh;
    }

    public boolean checkFroRegister(Context context) {
        String text;
        text = getName().trim();

        if(text.length() == 0) {
            error(context,"El nombre no puede estar en blanco" );
            return false;
        }

        text = getLastname().trim();
        if(text.length() == 0) {
            error(context, "El apellido no puede estar en blanco");
            return false;
        }

        text = getDni().trim();
        if(text.length() == 0) {
            error(context, "El DNI no puede estar en blanco");
            return false;
        }

        text = getEmail().trim();
        if(!text.matches(Constantes.EMAIL_REGEX)){
            error(context,"Formato de email invalido");
            return false;
        }

        text = getPassword().trim();
        if(text.length() < 8) {
            error(context, "La longitud del password debe ser de 8 caracteres como minimo");
            return false;
        }

        text = getComision().trim();
        if(text.length() == 0) {
            error(context, "La comision no puede estar en blanco");
            return false;
        }

        return true;
    }

    public boolean checkForLogin(Context context) {
        String text;

        text = getEmail().trim();

        if(!text.matches(Constantes.EMAIL_REGEX)){
            error(context,"Formato de email invalido");
            return false;
        }

        text = getPassword().trim();
        if(text.length() < 8) {
            error(context, "La longitud del password debe ser de 8 caracteres como minimo");
            return false;
        }

        return true;
    }

    private void error(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
