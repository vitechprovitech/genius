package com.faguy.genius.util;

public class Config {

	public static final int USER_ACTIF = 1;

	
    public static int OFFRE_CLOTURE = 0;   // Fermé à toute opération
    public static int OFFRE_OUVERT = 1;   // ouvert au tééléchargement et candidatures

    public static int MIN = 100000;
    public static int MAX = 999999;
    public static String PHONE_REGEX = "(\\+){0,1}\\d{9,}";


    public static  String MAIL_REGEX = "^(.+)@(.+)$";
    public static String IMEI_NULL = "0";
    public static int ATTENTE = 0;
    public static int ACTIF=1;
    public static int BLOCKED=-1;      //
    public static int WAITING_ACTIVATION_CODE=-2;
    public static double MINUTE_DELAY_TOKEN=10;
    public static int TOKEN_NOT_EXIST=0;
    public static  int USER_NOT_EXIST=0;

    public static int ERROR = -500;

    public static Object PHONE_CHANGE = -250;
}
