package ninja.seppli.umlgenerator.options;

public class UmlOptions {
    private GeneralUmlOptions generalUmlOptions = new GeneralUmlOptions();
    private PlantumlOptions plantumlOptions = new PlantumlOptions();

    public UmlOptions() {
    }

    /**
     * @param generalUmlOptions
     * @param plantumlOptions
     */
    public UmlOptions(GeneralUmlOptions generalUmlOptions, PlantumlOptions plantumlOptions) {
        this.generalUmlOptions = generalUmlOptions;
        this.plantumlOptions = plantumlOptions;
    }

    /**
     * @return the generalUmlOptions
     */
    public GeneralUmlOptions getGeneralUmlOptions() {
        return generalUmlOptions;
    }

    /**
     * @param generalUmlOptions the generalUmlOptions to set
     */
    public void setGeneralUmlOptions(GeneralUmlOptions generalUmlOptions) {
        this.generalUmlOptions = generalUmlOptions;
    }

    /**
     * @return the plantumlOptions
     */
    public PlantumlOptions getPlantumlOptions() {
        return plantumlOptions;
    }

    /**
     * @param plantumlOptions the plantumlOptions to set
     */
    public void setPlantumlOptions(PlantumlOptions plantumlOptions) {
        this.plantumlOptions = plantumlOptions;
    }

}
