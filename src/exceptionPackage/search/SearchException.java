package exceptionPackage.search;

public class SearchException extends Exception {
    private String wrongValue;

    public SearchException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}