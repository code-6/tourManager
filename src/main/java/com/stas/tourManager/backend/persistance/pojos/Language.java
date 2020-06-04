package com.stas.tourManager.backend.persistance.pojos;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Language {
    private static List<Language> langList = new ArrayList<>();
    private String lang;

    private Language(String lang) {
        this.lang = lang;
    }

    // use in case if language not found to create and add new one
    public static Language createLang(String lang) throws InvalidLanguageException {
        if (isValid(lang)) {
            var l = new Language(lang);
            langList.add(l);
            return l;
        } else
            throw new InvalidLanguageException("Language: " + lang + " is invalid! For input available only ISO 639");
    }

    // use for fill combo box
    public static List<Language> getLangList() {
        return langList;
    }

    public String getLang() {
        return lang;
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
