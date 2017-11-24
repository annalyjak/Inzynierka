package lyjak.anna.inzynierka.viewmodel;

import android.content.Context;

/**
 * Created by Anna on 24.11.2017.
 */

public class MainActivityViewModel extends MainViewModel {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2;
    public static final int PERMISSIONS_REQUEST_INTERNET = 3;
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    public static String language = "pl"; // default app language, can change if user change settings

    public MainActivityViewModel(Context context) {
        super(context);
    }

    public static String getLanguage() {
        return language;
    }

    public void setLanguage(String lang) {
        language = lang;
    }

    public boolean isLanguagePolish() {
        return language.equals("pl");
    }

    public boolean isLanguageEng() {
        return language.equals("en");
    }

    public void setPolishLanguage() {
        setLanguage("pl");
    }

    public void setEnglishLanguage() {
        setLanguage("en");
    }
}
