package com.example.soa_ea2;

public class Constantes {

    public final static String ENV = "PROD";

    public final static String UTF = "UTF-8";
    public final static String METODO_PUT = "PUT";
    public final static String METODO_POST = "POST";

    public final static String EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public final static String URL_LOGIN = "http://so-unlam.net.ar/api/api/login";
    public final static String URL_EVENTS = "http://so-unlam.net.ar/api/api/event";
    public final static String URL_TOKEN = "http://so-unlam.net.ar/api/api/refresh";
    public final static String URL_REGISTER = "http://so-unlam.net.ar/api/api/register";

    public final static String REQUEST_ERROR = "REQUEST ERROR - Houston we have a Problem!";
    public final static String RESPONSE_LOGIN = "com.example.soa_ea2.action.RESPUESTA_LOGIN";
    public final static String RESPONSE_EVENT = "com.example.soa_ea2.action.RESPUESTA_EVENT";
    public final static String RESPONSE_REGISTER = "com.example.soa_ea2.action.RESPUESTA_REGISTER";

    public final static String TYPE_EVENT = "Uso de sensor";

    public final static long MILLIS = 100;
    public final static long MILLIS_TO_SLEEP = 1680000;

    public final static String INDEX = "index";
    public final static String SHARED_LUZ = "SHARED_LUZ";
    public final static String SHARED_GIROSCOPO = "SHARED_GIROSCOPO";
    public final static String SHARED_PROXIMIDAD = "SHARED_PROXIMIDAD";
    public final static String SHARED_ACELEROMETRO = "SHARED_ACELEROMETRO";

    public final static String LOG_TYPE = "## REQUEST LOG ##";
}
