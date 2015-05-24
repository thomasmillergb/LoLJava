package testJava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by Thomas on 23/05/2015.
 */

public class multithread  {
    //
    public static void main(String[] args)  throws InterruptedException, ExecutionException, TimeoutException {
        multithread m = new multithread();
        System.out.println(m.run());
    }
    public List<Integer> run() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newWorkStealingPool();
        List<Integer> list = new ArrayList<Integer>(){};

        Map<Integer, Callable<Integer>> t;
        List<Callable<Integer>> callables = Arrays.asList(
                () -> test(1),
                () -> test(2),
                () -> test(3));

        executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(i ->list.add(i));
                //.forEach(System.out::println);
        System.out.println(list);
        return list;

    }
    public static int test(int i ){
        return i;

    }

}