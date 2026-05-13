package org.avaj.radyuk.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.avaj.radyuk.entity.LogisticBase;
import org.avaj.radyuk.entity.Truck;
import org.avaj.radyuk.entity.TruckTypeOperation;

public class WaitingState extends TruckState{

    private static final Logger logger = LogManager.getLogger();

    public WaitingState(Truck truck) {
        super(truck);
    }

    @Override
    public void truckProcessing() throws InterruptedException {
        logger.info("Truck {} is waiting in queue",  truck.getTruckId());
        try{
            LogisticBase.getInstance().enterBase(truck);
            if (truck.getTruckTypeOperation() == TruckTypeOperation.LOADING){
                truck.setTruckState(new LoadingState(truck));
            } else {
                truck.setTruckState(new UnloadingState(truck));
            }
        } catch (InterruptedException e){
            logger.info("Truck {} interrupted. Cause: {}", truck.getTruckId(), e);
            throw new InterruptedException("Truck" + truck.getTruckId() + " interrupted. Cause: " + e);
        }
    }
}
