package model;

/**
 * Created by Art on 1/21/14.
 */
public enum ExpertiseLevel {

    BASIC("Basic"),DEVELOPER("Developer"), EXPERT("Expert");

    private String displayName;

    private ExpertiseLevel(String displayName){
        this.displayName = displayName;
    }

    public String prettyPrint(){
        return displayName;
    }



}
