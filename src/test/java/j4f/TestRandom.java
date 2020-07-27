package j4f;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestRandom {

    @Test
    void testRandom(){
        for (int i = 0; i < 100; i++) {
            var rnd = ThreadLocalRandom.current().nextInt(2020, 2022);
            if(rnd > 2022 || rnd < 2020)
                System.out.println("not in range : "+rnd);
            System.out.println(rnd);
        }

    }

    @Test
    void testRandomInterval(){
        //for (int i = 0; i < 1000; i++) {
            var r= getRandomMinuteWithStep(10);
            System.out.println(r);
        //}

    }

    private int getRandomMinuteWithStep(int step){
        int minute = 60;
        List<Integer> list = new ArrayList<>();
        while (minute > 0){
            minute-=step;
            list.add(minute);
        }
        for (int i : list) {
            System.out.print(i+", ");
        }
        System.out.println();

        return list.get(ThreadLocalRandom.current().nextInt(0, list.size() - 1));
    }
}
