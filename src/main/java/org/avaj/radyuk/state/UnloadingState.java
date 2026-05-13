package org.avaj.radyuk.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.avaj.radyuk.entity.Truck;

import java.util.concurrent.TimeUnit;

public class UnloadingState extends TruckState {

    public static final Logger logger = LogManager.getLogger();

    public UnloadingState(Truck truck) {
        super(truck);
    }

    @Override
    public void truckProcessing() throws InterruptedException {
        int operationTime = truck.getTruckTypeOperation().getOperationTime();

        logger.info("Truck {} started UNLOADING. Time: {}", truck.getTruckId(), operationTime);

        TimeUnit.MILLISECONDS.sleep(operationTime);

        logger.info("Truck {} finished UNLOADING", truck.getTruckId());

        truck.setTruckState(new CompletedState(truck));
        truck.getTruckState().truckProcessing();
    }
}
