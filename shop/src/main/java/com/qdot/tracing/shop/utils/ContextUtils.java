package com.qdot.tracing.shop.utils;

import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;

public final class ContextUtils {

    public static ContextSnapshot.Scope getContextSnapshot(Object contextView) {
        return ContextSnapshot.setThreadLocalsFrom(contextView, ObservationThreadLocalAccessor.KEY);
    }

}
