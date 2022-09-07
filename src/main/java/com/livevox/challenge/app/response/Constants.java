package com.livevox.challenge.app.response;

public final class Constants {

    public static final String CALL_CENTER_ID_REQUIRED_MESSAGE = "Call center id is required";
    public static final String AGENT_NOT_FOUND_MESSAGE = "Agent not found";
    public static final String CALL_CENTER_NOT_FOUND_MESSAGE = "Call center doesn't exist";
    public static final String UNIQUE_EXTENSION_MESSAGE =
        "An agent with same extension is already assigned to call center";
    public static final String CALL_CENTER_NAME_REQUIRED_MESSAGE = "Name is required";
    public static final String CALL_CENTER_COUNTRY_NAME_REQUIRED_MESSAGE = "Country name is required";
    public static final String CALL_CENTER_UNIQUE_NAME_MESSAGE = "Name must be unique per country";

    private Constants() {
        throw new UnsupportedOperationException("Utility Class");
    }

}
