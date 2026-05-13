package org.avaj.radyuk.entity;

public class TruckReport {
    private final Truck truck;

    public TruckReport(Truck truck) {
        this.truck = truck;
    }

    public String getStatistics() {
        String stats = String.format(
                "=== Статистика Грузовика ===\n" +
                "ID: %d\n" +
                "время работы(миллисекунды): %d\n" +
                "Скоропорт: %d\n" +
                "=========================",
                truck.getTruckId(),
                truck.getTruckTypeOperation().getOperationTime(),
                truck.isPerishable()?"Yes":"No");
        return stats;
    }

}
