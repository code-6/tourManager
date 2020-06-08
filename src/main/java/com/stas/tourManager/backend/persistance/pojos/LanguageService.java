package com.stas.tourManager.backend.persistance.pojos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public abstract class LanguageService {
    private static List<String> langList = new ArrayList<>();

    // use in case if language not found to create and add new one
    public static String createLang(String lang) throws InvalidLanguageException {
        if (isValid(lang)) {
            langList.add(lang);
            return lang;
        } else
            throw new InvalidLanguageException("Language: " + lang + " is invalid! For input available only ISO 639");
    }

    // use for fill combo box
    public static List<String> getLangList() {
        return langList;
    }

    public static boolean isValid(String lang) {
        return Arrays.asList(Locale.getAvailableLocales()).stream().anyMatch(l -> l.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase(lang));
    }

    public static class InvalidLanguageException extends Exception {
        public InvalidLanguageException(String message) {
            super(message);
        }
    }


}
