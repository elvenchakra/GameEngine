package performancetest;
import java.util.*;
import java.util.function.Consumer;

/**
 *
 * @author Kassandra Kaeck
 */
public class PerformanceTest {
    private double value;
    private PerformanceTest (double value) {
        this.value = value;
    }
    public static void main (String[] args) {
        ArrayList<PerformanceTest> data = new ArrayList<> ();
        int i;
        for (i = 0;i < 100000;i++) {
            data.add (new PerformanceTest (Math.random ()));
        }
        for (i = 0;i < 1000;i++) {
            data.forEach ((next) -> next.value = 0.5 * (next.value + Math.random ()));
            data.forEach (new Consumer<PerformanceTest> () {
                public void accept (PerformanceTest next) {
                    next.value = 0.5 * (next.value + Math.random ());
                }
            });
        }
        long time = System.nanoTime ();
        for (i = 0;i < 1000;i++) {
            data.forEach ((next) -> next.value = 0.5 * (next.value + Math.random ()));
        }
        data.forEach ((next) -> System.out.println (next.value));
        long time1 = System.nanoTime () - time;
        System.out.println ("break");
        time = System.nanoTime ();
        for (i = 0;i < 1000;i++) {
        data.forEach (new Consumer<PerformanceTest> () {
                public void accept (PerformanceTest next) {
                    next.value = 0.5 * (next.value + Math.random ());
                }
            });
        }
        data.forEach (new Consumer<PerformanceTest> () {
            public void accept (PerformanceTest next) {
                System.out.println (next.value);
            }
        });
        long time2 = System.nanoTime () - time;
        System.out.println (time1);
        System.out.println (time2);
    }
}
