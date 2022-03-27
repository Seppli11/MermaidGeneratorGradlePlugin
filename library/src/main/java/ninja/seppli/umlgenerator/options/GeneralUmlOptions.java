package ninja.seppli.umlgenerator.options;

import java.util.Objects;

public class GeneralUmlOptions {
    public static final GeneralUmlOptions DEFAULT_OPTIONS = new GeneralUmlOptions(17, VisibilityLevel.PRIVATE,
            VisibilityLevel.PRIVATE);
    public int languageLevel = 17;
    private VisibilityLevel methodLevel = VisibilityLevel.PRIVATE;
    private VisibilityLevel fieldLevel = VisibilityLevel.PRIVATE;

    public GeneralUmlOptions() {
    }

    /**
     * @param languageLevel
     * @param methodLevel
     * @param fieldLevel
     */
    public GeneralUmlOptions(int languageLevel, VisibilityLevel methodLevel, VisibilityLevel fieldLevel) {
        this.languageLevel = languageLevel;
        this.methodLevel = methodLevel;
        this.fieldLevel = fieldLevel;
    }

    /**
     * @return the languageLevel
     */
    public int getLanguageLevel() {
        return languageLevel;
    }

    /**
     * @param languageLevel the languageLevel to set
     */
    public void setLanguageLevel(int languageLevel) {
        this.languageLevel = languageLevel;
    }

    /**
     * @return the methodLevel
     */
    public VisibilityLevel getMethodLevel() {
        return methodLevel;
    }

    /**
     * @param methodLevel the methodLevel to set
     */
    public void setMethodLevel(VisibilityLevel methodLevel) {
        this.methodLevel = methodLevel;
    }

    /**
     * @return the fieldLevel
     */
    public VisibilityLevel getFieldLevel() {
        return fieldLevel;
    }

    /**
     * @param fieldLevel the fieldLevel to set
     */
    public void setFieldLevel(VisibilityLevel fieldLevel) {
        this.fieldLevel = fieldLevel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(fieldLevel, languageLevel, methodLevel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GeneralUmlOptions)) {
            return false;
        }
        GeneralUmlOptions other = (GeneralUmlOptions) obj;
        return fieldLevel == other.fieldLevel && languageLevel == other.languageLevel
                && methodLevel == other.methodLevel;
    }

}
