package com.pluu.sample.feature2.navigator;

import android.content.Intent;

import com.pluu.navigator.DeepLink;
import com.pluu.navigator.provider.Provider;
import com.pluu.sample.feature2.Feature2Activity;

import kotlin.Unit;

class DeepLinkProviderForJava implements Provider {
    @Override
    public void provide() {
        new DeepLink("pluu://feature2Java").register((starter, deepLinkMatch) -> {
            starter.start(new Intent(starter.getContext(), Feature2Activity.class));
            return Unit.INSTANCE;
        });
    }
}
