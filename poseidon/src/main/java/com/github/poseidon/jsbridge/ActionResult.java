package com.github.poseidon.jsbridge;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;


public class ActionResult {

    private final int status;
    private String strMessage;
    private String encodedMessage;
    private boolean keepCallback = false;

    ActionResult(Status status, JSONObject message) {
        this.status = transform(status);
        this.encodedMessage = message.toString();
    }

    ActionResult(Status status, String message) {
        this.status = transform(status);
        this.strMessage = message;
    }

    ActionResult(Status status, JSONArray message) {
        this.status = transform(status);
        this.encodedMessage = message.toString();
    }

    ActionResult(Status status, byte[] message) {
        this.status = transform(status);
        this.encodedMessage = Base64.encodeToString(message, Base64.NO_WRAP);
    }

    ActionResult(Status status, int message) {
        this.status = transform(status);
        this.encodedMessage = "" + message;
    }

    ActionResult(Status status) {
        this(status, StatusMessages[status.ordinal()]);
    }

    private int transform(Status status) {
        return status == Status.OK ? 1 : 0;
    }

    public String getMessage() {
        if (encodedMessage == null) {
            encodedMessage = JSONObject.quote(strMessage);
        }
        return encodedMessage;
    }

    public boolean getKeepCallBack() {
        return keepCallback;
    }

    public ActionResult setKeepCallback(boolean keepCallback) {
        this.keepCallback = keepCallback;
        return this;
    }

    public String getJSONString() {
        return "{\"status\":\"" + this.status + "\",\"message\":" + this.getMessage() + ",\"keepCallback\":" + this.keepCallback + "}";
    }

    public static String[] StatusMessages = new String[]{
            "OK",
            "Error",
            "Invalid action",
            "JSON error",
            "Class not found",
            "Illegal access",
            "Instantiation error",
            "Malformed url",
            "IO error"
    };

    public enum Status {
        OK,
        ERROR,
        INVALID_ACTION,
        JSON_EXCEPTION,
        CLASS_NOT_FOUND_EXCEPTION,
        ILLEGAL_ACCESS_EXCEPTION,
        INSTANTIATION_EXCEPTION,
        MALFORMED_URL_EXCEPTION,
        IO_EXCEPTION
    }
}
