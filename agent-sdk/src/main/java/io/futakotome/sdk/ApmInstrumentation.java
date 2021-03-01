package io.futakotome.sdk;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import javax.annotation.Nullable;
import java.security.ProtectionDomain;
import java.util.Collection;

import static net.bytebuddy.matcher.ElementMatchers.any;

public abstract class ApmInstrumentation {
    public ElementMatcher<? super NamedElement> getTypeMatcherPreFilter() {
        return any();
    }

    public ElementMatcher.Junction<ProtectionDomain> getProtectionDomainPostFilter() {
        return any();
    }

    public abstract ElementMatcher<? super TypeDescription> getTypeMatcher();

    public ElementMatcher.Junction<ClassLoader> getClassLoaderMatcher() {
        return any();
    }

    public abstract ElementMatcher<? super MethodDescription> getMethodMatcher();

    public Class<?> getAdviceClass() {
        return getClass();
    }

    public boolean includeWhenInstrumentationIsDisabled() {
        return false;
    }

    public abstract Collection<String> getInstrumentationGroupNames();

    @Nullable
    public Advice.OffsetMapping.Factory<?> getOffsetMapping() {
        return null;
    }

    public void onTypeMatch(TypeDescription typeDescription, ClassLoader classLoader, ProtectionDomain protectionDomain, @Nullable Class<?> classBeingRedefined) {
    }
}
