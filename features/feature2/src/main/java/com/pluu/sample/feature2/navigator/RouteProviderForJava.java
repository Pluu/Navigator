package com.pluu.sample.feature2.navigator;

import android.content.Intent;

import com.pluu.navigator.provider.Provider;
import com.pluu.sample.feature2.Feature2Activity;
import com.pluu.sample.routeconst.Routes2;

public class RouteProviderForJava implements Provider {
    @Override
    public void provide() {
        Routes2.Feature2ForJava.INSTANCE.register(starter ->
                new Intent(starter.getContext(), Feature2Activity.class));
    }
}
