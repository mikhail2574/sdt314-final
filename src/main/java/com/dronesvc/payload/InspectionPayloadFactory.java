package com.dronesvc.payload;

public class InspectionPayloadFactory implements PayloadFactory {

    @Override
    public Camera createCamera() {
        return new ThermalCamera();
    }

    @Override
    public PayloadModule createModule() {
        return new SensorModule("irradiance sensor", 0.3);
    }
}
