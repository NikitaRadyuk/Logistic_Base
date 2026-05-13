package org.avaj.radyuk.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LogisticBase {

    private static final Logger logger = LogManager.getLogger();
    private final int totalSpots;
    private final AtomicInteger occupiedSpots;

    private final ReentrantLock lock;
    private final Condition regularCondition;
    private final Condition priorityCondition;

    private final Queue<Truck> regularQueue;
    private final Queue<Truck> priorityQueue;

    private static class Holder  {
        private static final LogisticBase INSTANCE = new LogisticBase(3, true);
    }

    public static LogisticBase getInstance() {
        return Holder.INSTANCE;
    }

    public LogisticBase(int totalSpots, boolean fair) {
        this.totalSpots = totalSpots;
        this.occupiedSpots = new AtomicInteger(0);
        this.lock = new ReentrantLock(fair);
        this.regularCondition = lock.newCondition();
        this.priorityCondition = lock.newCondition();
        this.regularQueue = new LinkedList<>();
        this.priorityQueue = new LinkedList<>();
    }

    public void enterBase(Truck truck) throws InterruptedException {
        boolean isPriority = truck.isPerishable();

        lock.lockInterruptibly();
        try {
            logger.info("Truck {} trying to enter the Base. Priority: {}", truck.getTruckId(), truck.isPerishable());
            while(occupiedSpots.get() >= totalSpots){
                if (isPriority){
                    logger.info("Priority Truck {} waiting (No spots, priorityWaitingQueue: {})",  truck.getTruckId(), priorityQueue.size() + 1);
                    priorityQueue.add(truck);
                    priorityCondition.await();
                } else {
                    logger.info("Truck {} waiting (No spots, regularWaitingQueue: {})",  truck.getTruckId(), regularQueue.size() + 1);
                    regularQueue.add(truck);
                    regularCondition.await();
                }
            }

            int currentOccupiedSpots = occupiedSpots.incrementAndGet();
            logger.info("Truck {} is entering the base and occupying the Spot. OccupiedSpots: {}/{}", truck.getTruckId(), currentOccupiedSpots, totalSpots);
        } finally {
            lock.unlock();
        }
    }

    public void leaveBase(Truck truck){
        lock.lock();
        try {
            int currentOccupiedSpots = occupiedSpots.decrementAndGet();
            logger.info("Truck {} is leaving base. OccupiedSpots: {}/{}", truck.getTruckId(), currentOccupiedSpots, totalSpots);
            if (!priorityQueue.isEmpty()){
                Truck nextPriority = priorityQueue.poll();
                logger.info("Priority Truck {} is going first from VIP Queue", nextPriority.getTruckId());
                priorityCondition.signal();
            } else if (!regularQueue.isEmpty()){
                Truck nextRegular = regularQueue.poll();
                logger.info("Truck {} is going first from Queue", nextRegular.getTruckId());
                regularCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public int getTotalSpots() {return totalSpots;}
    public AtomicInteger getOccupiedSpots() {return occupiedSpots;}
    public ReentrantLock getLock() {return lock;}
    public Condition getRegularCondition() {return regularCondition;}
    public Condition getPriorityCondition() {return priorityCondition;}
    public Queue<Truck> getRegularQueue() {return regularQueue;}
    public Queue<Truck> getPriorityQueue() {return priorityQueue;}

    public String getStatistics() {
        lock.lock();
        try {
            return String.format(
                    "=== Статистика базы ===\n" +
                            "Всего мест: %d\n" +
                            "Занято мест: %d\n" +
                            "Свободно мест: %d\n" +
                            "Обычных в очереди: %d\n" +
                            "Скоропорт в очереди: %d\n" +
                            "Всего в очереди: %d\n" +
                            "=========================",
                    totalSpots, occupiedSpots.get(), totalSpots - occupiedSpots.get(),
                    regularQueue.size(), priorityQueue.size(),
                    regularQueue.size() + priorityQueue.size()
            );
        } finally {
            lock.unlock();
        }
    }
}
