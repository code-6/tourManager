package com.stas.tourManager.backend.persistance.pojos;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {

    @Test
    void createLang(){
        var l1 = "english";
        var l2 = "German";
        var l3 = "deutsch";
        var l4 = "asddd";
        System.out.println(Locale.US.getDisplayLanguage());
        assertTrue(LanguageService.isValid(l1));
        assertTrue(LanguageService.isValid(l2));
        assertTrue(LanguageService.isValid(l3));
        assertFalse(LanguageService.isValid(l4));
    }

}