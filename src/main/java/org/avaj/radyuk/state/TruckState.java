package org.avaj.radyuk.state;

import org.avaj.radyuk.entity.Truck;

public abstract class TruckState {
    Truck truck;

    public TruckState(Truck truck) {
        this.truck = truck;
    }

    public abstract void truckProcessing() throws InterruptedException;
}
