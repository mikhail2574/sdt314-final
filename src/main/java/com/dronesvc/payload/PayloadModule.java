package com.dronesvc.payload;

import com.dronesvc.mission.GeoPoint;

public interface PayloadModule {

    String name();

    double weightKg();

    void onTarget(GeoPoint point);
}
