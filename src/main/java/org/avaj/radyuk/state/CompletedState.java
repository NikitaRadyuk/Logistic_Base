package org.avaj.radyuk.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.avaj.radyuk.entity.LogisticBase;
import org.avaj.radyuk.entity.Truck;

public class CompletedState extends TruckState {

    public static final Logger logger = LogManager.getLogger();

    public CompletedState(Truck truck) {
        super(truck);
    }

    @Override
    public void truckProcessing() {
        logger.info("Truck {} completed & exiting the base", truck.getTruckId());

        LogisticBase.getInstance().leaveBase(truck);
    }
}
