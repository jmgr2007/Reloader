package me.jmgr2007.Reloader;

public class Vars {
    public static int enabled = 0;
    public static int disabled = 0;
    public static int loaded = 0;
    public static int unloaded = 0;
    public static int reloaded = 0;

    public static void addStats(String s, int n) {
        if(s.equalsIgnoreCase("load")) {
            loaded = loaded + n;
        }else if(s.equalsIgnoreCase("unload")) {
            unloaded = unloaded + n;
        }else if(s.equalsIgnoreCase("enable")) {
            enabled = enabled + n;
        }else if(s.equalsIgnoreCase("disable")) {
            disabled = disabled + n;
        }else if (s.equalsIgnoreCase("reload")) {
            reloaded = reloaded + n;
        } else {
        }
    }
}
