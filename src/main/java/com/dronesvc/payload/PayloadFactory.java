package com.dronesvc.payload;

public interface PayloadFactory {

    Camera createCamera();

    PayloadModule createModule();
}
