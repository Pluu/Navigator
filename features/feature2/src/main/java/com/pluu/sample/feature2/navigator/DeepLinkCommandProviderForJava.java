package com.pluu.sample.feature2.navigator;

import com.pluu.navigator.DeepLink;
import com.pluu.navigator.provider.Provider;

class DeepLinkCommandProviderForJava implements Provider {
    @Override
    public void provide() {
        new DeepLink("pluu://feature2Java/command")
                .register(Feature2SampleCommand.class);
    }
}
