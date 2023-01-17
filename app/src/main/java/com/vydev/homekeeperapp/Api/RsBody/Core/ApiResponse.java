package com.vydev.homekeeperapp.Api.RsBody.Core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse {
    @JsonProperty("status")
    private Status status;

    @JsonProperty("payload")
    private Object payload;

    public Status getStatus() {
        return status;
    }

    public ApiResponse setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Object getPayload() {
        return payload;
    }

    public ApiResponse setPayload(Object payload) {
        this.payload = payload;
        return this;
    }
}
