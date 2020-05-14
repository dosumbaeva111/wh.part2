package com.company;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Main {
    //a)	Написать многопоточное приложение которое бы симулировало загрузку файлов на сервер. И их скачивание.
    // Используя классы Semaphore и CountDownLatch. Либо можно использовать методы wait(), notify(), notifyAll().
    //b)	Uploader загружает  1 файл 500 мб на сервер. Скорость загрузки 20 мб в секунду.
    // Нужно в консоли отобразить процесс загрузки симулируя через sleep().
    //c)	После того как весь файл загружен на сервер Downloaders начинают его скачивать со скоростью 100 мб в секунду.
    // Должны скачать файл 10 человек, одновременно могут скачивать не более 3х человек.
    //d)	После того как файл был скачан 10 раз, он удаляется с сервера

    public static void main(String[] args) {
        Uploader uploader = new Uploader(500, 20);
        uploader.start();
        try {
            uploader.join();
            new Downloader(new Semaphore(3, true), new CountDownLatch(10), 500, 100).start();
            for (int i = 1; i <= 10; i++) {
                if (i == 10) {
                    System.out.println(" Файл удален!!! ");
                }

            }
        } catch (InterruptedException e) {

        }


    }
}
