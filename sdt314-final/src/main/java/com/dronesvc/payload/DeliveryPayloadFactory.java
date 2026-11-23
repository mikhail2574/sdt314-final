package com.dronesvc.payload;

public class DeliveryPayloadFactory implements PayloadFactory {

    @Override
    public Camera createCamera() {
        return new RgbCamera();
    }

    @Override
    public PayloadModule createModule() {
        return new CargoBox(2.5);
    }
}
