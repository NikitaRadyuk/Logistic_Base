package org.avaj.radyuk;

import org.avaj.radyuk.entity.LogisticBase;
import org.avaj.radyuk.entity.Truck;
import org.avaj.radyuk.entity.TruckTypeOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        LogisticBase base = LogisticBase.getInstance();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("=== ЛОГИСТИЧЕСКАЯ БАЗА ===\n");

        // Грузовики: ID, скоропорт, операция
        executor.submit(new Truck(1L, false, base, TruckTypeOperation.LOADING));
        executor.submit(new Truck(2L, false, base, TruckTypeOperation.UNLOADING));
        executor.submit(new Truck(3L, true, base, TruckTypeOperation.LOADING));
        executor.submit(new Truck(4L, false, base, TruckTypeOperation.LOADING));
        executor.submit(new Truck(5L, true, base, TruckTypeOperation.UNLOADING));

        executor.shutdown();

        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n" + base.getStatistics());
    }
}