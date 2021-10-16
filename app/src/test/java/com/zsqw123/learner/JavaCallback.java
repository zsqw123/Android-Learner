package com.zsqw123.learner;

import java.util.function.Consumer;

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/10/2 8:25
 */
public class JavaCallback {
    int largeWork() {
        return 1;
    }

    int largeWork(int input) {
        return 1;
    }

    void work0(Consumer<Integer> consumer) {
        int workRes = largeWork(); // 耗时任务
        consumer.accept(workRes);
    }

    void work1(int input, Consumer<Integer> consumer) {
        int workRes = largeWork(input); // 耗时任务
        consumer.accept(workRes);
    }

    void work2(int input, Consumer<Integer> consumer) {
        int workRes = largeWork(input); // 耗时任务
        consumer.accept(workRes);
    }

    void javaCallbackHell() {
        work0(
                work0 -> work1(work0,
                        work1 -> work2(work1,
                                work2 -> {
                                })));
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int i = 0;
        for (int j = 0; j < 10000_0000; j++) {
            i++;
        }
        long time = System.currentTimeMillis() - start;
        System.out.println(time + "," + i);
    }
}
