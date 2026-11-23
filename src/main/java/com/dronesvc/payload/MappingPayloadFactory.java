package com.dronesvc.payload;

public class MappingPayloadFactory implements PayloadFactory {

    @Override
    public Camera createCamera() {
        return new RgbCamera();
    }

    @Override
    public PayloadModule createModule() {
        return new SensorModule("RTK GPS receiver", 0.4);
    }
}
