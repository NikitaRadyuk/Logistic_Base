package org.avaj.radyuk.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.avaj.radyuk.state.CompletedState;
import org.avaj.radyuk.state.TruckState;
import org.avaj.radyuk.state.WaitingState;

import java.util.Objects;
import java.util.concurrent.Callable;

public class Truck implements Callable<TruckReport> {
    public static final Logger logger = LogManager.getLogger();
    private final Long truckId;
    private final boolean isPerishable;
    private final LogisticBase base;
    private final TruckTypeOperation truckTypeOperation;
    private TruckState truckState;

    public Truck(Long truckId, boolean isPerishable, LogisticBase base, TruckTypeOperation truckTypeOperation) {
        this.truckId = truckId;
        this.isPerishable = isPerishable;
        this.base = base;
        this.truckTypeOperation = truckTypeOperation;
        this.truckState = new WaitingState(this);
    }
    @Override
    public TruckReport call() throws InterruptedException {
        logger.info("Truck {} ready to work. Priority: {}. OperationTime: {}", truckId, isPerishable, truckTypeOperation.getOperationTime());

        while(!(truckState instanceof CompletedState)){
            truckState.truckProcessing();
        }
        return new TruckReport(this);
    }


    public Long getTruckId() {return truckId;}
    public boolean isPerishable() {return isPerishable;}
    public LogisticBase getBase() {return base;}
    public TruckTypeOperation getTruckTypeOperation() {return truckTypeOperation;}
    public TruckState getTruckState() {return truckState;}

    public void setTruckState(TruckState truckState) {
        this.truckState = truckState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return Objects.equals(truckId, truck.truckId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(truckId);
    }


}
