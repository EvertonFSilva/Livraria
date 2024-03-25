package br.edu.iff.livraria.exception;

public class ErrorDetails {

    private final String requestUrl;
    private final String errorMessage;

    public ErrorDetails(String requestUrl, String errorMessage) {
        this.requestUrl = requestUrl;
        this.errorMessage = errorMessage;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
