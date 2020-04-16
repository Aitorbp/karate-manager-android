package com.example.karate_manager.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import static com.example.karate_manager.Utils.PreferencesUtility.*;
public class Storage {



        static SharedPreferences getPreferences(Context context) {
            return PreferenceManager.getDefaultSharedPreferences(context);
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
