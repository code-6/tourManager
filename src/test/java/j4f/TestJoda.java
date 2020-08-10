package j4f;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class TestJoda {
    @Test
    void test(){
        var dt = DateTime.now();
        System.out.println(dt.getMonthOfYear());
        System.out.println(dt.monthOfYear().getAsText());
    }
}
