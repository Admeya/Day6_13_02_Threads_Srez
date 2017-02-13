package com.innopolis.admeya;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
//    Вариант 1:
// Реализовать программу из 2-х потоков. Один из потоков каждую секунду генерирует случайное число в интервале [0;99].
// Второй поток раз в пять секунд выводит количество уникальных чисел, сгенерированных первым потоком.
// После того, как будет сгенерировано все 100 чисел, оба потока должны остановить свое выполнение.

    static ConcurrentHashMap<Integer, AtomicInteger> comMap = new ConcurrentHashMap<Integer, AtomicInteger>();
    static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {

        PrintManager pm = new PrintManager();

        Thread myThr1 = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    Random r = new Random();
                    try {
                        System.out.print("Первый поток ");
                        int num = r.nextInt(99);
                        pm.runPrint(num);
                        commonResource(num);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        myThr1.start();

        Thread myThr2 = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    Random r = new Random();
                    try {
                        System.out.print("Второй поток ");
                        pm.runPrint(r.nextInt(99));
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //myThr2.start();
    }

    public static synchronized void commonResource(int num) {
        if (!comMap.isEmpty()) {
            for (Map.Entry map : comMap.entrySet()) {
                int uniq = (int) map.getKey();
                if (uniq == num) {
                    AtomicInteger countNum = (AtomicInteger) map.getValue();
                    countNum.getAndIncrement();
                    comMap.put(num, countNum);
                    System.out.println(map.getKey() + "   " + map.getValue());

                } else {
                    AtomicInteger countNum = new AtomicInteger(1);
                    comMap.put(num, countNum);

                    System.out.println(num + "   " + countNum.get());
                }
            }
        } else {
            AtomicInteger countNum = new AtomicInteger(1);
            comMap.put(num, countNum);

            System.out.println(num + "   " + countNum.get());
        }
    }
}
