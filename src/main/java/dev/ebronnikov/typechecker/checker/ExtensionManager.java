package dev.ebronnikov.typechecker.checker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ExtensionManager {
    private final Set<Extension> enabledExtensions = new HashSet<>();

    public boolean isAmbiguousTypeAsBottom() {
        return enabledExtensions.contains(Extension.AMBIGUOUS_TYPE_AS_BOTTOM);
    }

    public boolean isStructuralSubtyping() {
        return enabledExtensions.contains(Extension.STRUCTURAL_SUBTYPING);
    }

    public boolean isTypeReconstructionEnabled() {
        return enabledExtensions.contains(Extension.TYPE_RECONSTRUCTION);
    }

    public void enabledExtensions(List<Extension> extensions) {
        enabledExtensions.addAll(extensions);
    }
}
