package com.example.karate_manager.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.karate_manager.Models.GroupModel.Group;
import com.google.gson.Gson;

import static com.example.karate_manager.Utils.PreferencesUtility.*;
public class Storage {



        static SharedPreferences getPreferences(Context context) {
            return PreferenceManager.getDefaultSharedPreferences(context);
        }

    public static boolean saveGroup(Context context, Group group){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(group);
        editor.putString(GROUP, json);
        editor.apply();
        return true;
    }
    public static Group getGroupSelected(Context context){
        SharedPreferences sharedPref = getPreferences(context);


        Gson gson = new Gson();
        String json = sharedPref.getString(GROUP, "");
        Group group = gson.fromJson(json, Group.class);

        return group;
    }
    public static void removeGroup(Context context, String key){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.remove(GROUP).commit();
    }


    public static boolean saveGroupPrincipal(Context context, int id_group){
            SharedPreferences.Editor editor = getPreferences(context).edit();
            editor.putInt(ID_GROUP,id_group);
            editor.apply();
            return true;
        }

    public static int getIdGroupPrincipal(Context context){
        SharedPreferences sharedPref = getPreferences(context);

        int id_group = sharedPref.getInt(ID_GROUP, 0);

        return id_group;
    }
    public static void removeIdGroupPrincipal(Context context, String key){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.remove(ID_GROUP).commit();
    }


        public static boolean saveToken(Context context, String token){
            SharedPreferences.Editor editor = getPreferences(context).edit();
            editor.putString(TOKEN,token);
            editor.apply();
            return true;
        }

        public static String getToken(Context context){
            SharedPreferences sharedPref = getPreferences(context);

            String token = sharedPref.getString(TOKEN, null);

            return token;
        }
        public static void removeToken(Context context, String key){
            SharedPreferences.Editor editor = getPreferences(context).edit();
            editor.remove(TOKEN).commit();
        }
        /**
         * Set the Login Status
         * @param context
         * @param loggedIn
         */
        public static void setLoggedIn(Context context, boolean loggedIn) {
            SharedPreferences.Editor editor = getPreferences(context).edit();
            editor.putBoolean(LOGGED_IN_PREF, loggedIn);
            editor.apply();
        }

        /**
         * Get the Login Status
         * @param context
         * @return boolean: login status
         */
        public static boolean getLoggedStatus(Context context) {
            return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
        }
}
