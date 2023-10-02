package ru.mail.kievsan;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static final int textGenerationCount = 10_000, textSize = 100_000;
    public static final int queueSize = 100;
    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(queueSize);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(queueSize);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(queueSize);

    public static void main(String[] args) throws InterruptedException{
        textGenerator().start();

        Thread threadA = new Thread(() -> {
            MaxText max = charMaxCount(queueA, 'a');
            System.out.println("\nИтог:\t" + max);
        });
        threadA.start();

        Thread threadB = new Thread(() -> {
            MaxText max = charMaxCount(queueB, 'b');
            System.out.println("\nИтог:\t" + max);
        });
        threadB.start();

        Thread threadC = new Thread(() -> {
            MaxText max = charMaxCount(queueC, 'c');
            System.out.println("\nИтог:\t" + max);
        });
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }

    public static Thread textGenerator() {
        return new Thread(() -> {
            System.out.println("Старт генератора текстов:");
            for (int i = 0; i < textGenerationCount; i++) {
                String text = generateText("abc", textSize);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                    if ((i + 1) % 1000 == 0) {
                        System.out.println("\nСгенерили " + (i + 1) + "-й текст");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static MaxText charMaxCount(BlockingQueue<String> queue, char letter) {
        int max, maxTextNumber, count;
        String text, maxText;

        MaxText result = null;
        count = 0;
        max = 0;
        maxTextNumber = 0;
        maxText = "";

        for (int i = 0; i < textGenerationCount; i++) {
            try{
                text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) {
                    max = count;
                    maxTextNumber = i;
                    maxText = text;
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted");
            } finally {
                result = new MaxText(maxText, maxTextNumber, max, letter);
                if ((i + 1) % 1000 == 0) {
                    System.out.println(result);
                }
                count = 0;
            }
        }
        return result;
    }

    public static String generateText(String letters, int len) {
        len = Math.max(len, 3);
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < len; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
