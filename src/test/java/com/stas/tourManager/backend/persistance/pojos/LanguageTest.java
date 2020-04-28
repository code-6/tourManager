package com.stas.tourManager.backend.persistance.pojos;

import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
        assertTrue(Language.isValid(l1));
        assertTrue(Language.isValid(l2));
        assertTrue(Language.isValid(l3));
        assertFalse(Language.isValid(l4));
    }

}