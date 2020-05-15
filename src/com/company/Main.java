package com.company;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static final int DOWLOADERS_LIMIT = 3;
    private static final int DOWLOADS_LIMIT = 10;

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
            /**
             * Создаем сервис для контроля количества запущенных потоков
             * и отдаем в конструктор лимит одновременно выполняемых задач
             */
            ExecutorService serivce = Executors.newFixedThreadPool(DOWLOADERS_LIMIT);
            /**
             * Создаем лист для хранения всех задач, которые нужно выполнить
             */
            List<Future<Downloader>> downloaders = new ArrayList<>();
            for (int i = 1; i <= DOWLOADS_LIMIT; i++) {
                /**
                 * Внутри создаем экземпляр загрузчика.
                 * Создаем обьект асинхронного менеджера и отдаем ему задачу(поток)
                 * затем добавляем менеджера с задачей в лист
                 */
                Downloader downloader = new Downloader(new Semaphore(3, true), new CountDownLatch(10), 500, 100);
                Future d = serivce.submit(downloader);
                downloaders.add(d);

            }
            /**
             * Пробегаемся по листу с менеджерами асинхронных задач
             * и начинаем их инициализацию и выполнение
             */
            for (Future<Downloader> d : downloaders) {
                d.get();
            }
            /**
             * Завершаем все потоки.
             */
            serivce.shutdown();

            System.out.println(" Файл удален!!! ");

        } catch (InterruptedException | ExecutionException e) {

        }


    }
}