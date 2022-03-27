package ninja.seppli.umlgenerator.gradle;

import org.gradle.api.provider.Property;

import ninja.seppli.umlgenerator.options.VisibilityLevel;

public class GradleUmlOptions {
    private Property<Integer> languageProperty;
    private Property<VisibilityLevel> methodLevelProperty;
    private Property<VisibilityLevel> fieldLevelProperty;

    /**
     * @param languageProperty the languageProperty to set
     */
    public void setLanguageProperty(Property<Integer> languageProperty) {
        this.languageProperty = languageProperty;
    }
}
