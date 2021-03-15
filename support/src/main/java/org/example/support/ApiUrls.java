package org.example.support;

public final class ApiUrls {

    public static final String API_CONTEXT_PATH = "/services";
    public static final String API_CONTEXT_PATTERN = API_CONTEXT_PATH + "/*";
    public static final String ERROR_CONFIG_URI =  API_CONTEXT_PATH + "/error";

    public static final String ROOT_API_URI =  "/api";
    public static final String SETUP_API_URI =  ROOT_API_URI + "/setup";
    public static final String TEAMS_API_URI =  SETUP_API_URI + "/teams";

    private ApiUrls() {
        // Nothing to do
    }
}
