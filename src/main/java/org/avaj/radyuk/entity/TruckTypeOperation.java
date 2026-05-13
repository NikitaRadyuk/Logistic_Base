package org.avaj.radyuk.entity;

public enum TruckTypeOperation {
    LOADING(3000),
    UNLOADING(6000);

    private final int operationTime;

    TruckTypeOperation(int operationTime) {
        this.operationTime = operationTime;
    }

    public int getOperationTime() {
        return operationTime;
    }
}
