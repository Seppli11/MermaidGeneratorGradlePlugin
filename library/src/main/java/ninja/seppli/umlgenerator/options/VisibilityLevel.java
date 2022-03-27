package ninja.seppli.umlgenerator.options;

import java.util.Arrays;

import spoon.reflect.declaration.ModifierKind;

public enum VisibilityLevel {
    PRIVATE(ModifierKind.PRIVATE),
    PACKAGE_PROTECTED(null),
    PROTECTED(ModifierKind.PROTECTED),
    PUBLIC(ModifierKind.PUBLIC);

    private ModifierKind modifierKind;

    /**
     * @param modifierKind
     */
    private VisibilityLevel(ModifierKind modifierKind) {
        this.modifierKind = modifierKind;
    }

    /**
     * @return the modifierKind
     */
    public ModifierKind getModifierKind() {
        return modifierKind;
    }

    public static VisibilityLevel fromModifierKind(ModifierKind modifierKind) {
        if (modifierKind == null)
            return PACKAGE_PROTECTED;
        return Arrays.stream(VisibilityLevel.values()).filter(level -> modifierKind == level.getModifierKind())
                .findAny().orElseThrow(() -> new IllegalArgumentException(
                        "Given modfier kind isn't for visibility: " + modifierKind.name() + "\""));
    }

}
