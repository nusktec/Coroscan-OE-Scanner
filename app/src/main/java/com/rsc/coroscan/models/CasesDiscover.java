package com.rsc.coroscan.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "state",
        "ndeath",
        "ncase",
        "ndischarged",
        "nevaded",
        "rid"
})
public class CasesDiscover {

    @JsonProperty("state")
    private String state;
    @JsonProperty("ndeath")
    private String ndeath;
    @JsonProperty("ncase")
    private String ncase;
    @JsonProperty("ndischarged")
    private String ndischarged;
    @JsonProperty("nevaded")
    private String nevaded;
    @JsonProperty("rid")
    private String rid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("ndeath")
    public String getNdeath() {
        return ndeath;
    }

    @JsonProperty("ndeath")
    public void setNdeath(String ndeath) {
        this.ndeath = ndeath;
    }

    @JsonProperty("ncase")
    public String getNcase() {
        return ncase;
    }

    @JsonProperty("ncase")
    public void setNcase(String ncase) {
        this.ncase = ncase;
    }

    @JsonProperty("ndischarged")
    public String getNdischarged() {
        return ndischarged;
    }

    @JsonProperty("ndischarged")
    public void setNdischarged(String ndischarged) {
        this.ndischarged = ndischarged;
    }

    @JsonProperty("nevaded")
    public String getNevaded() {
        return nevaded;
    }

    @JsonProperty("nevaded")
    public void setNevaded(String nevaded) {
        this.nevaded = nevaded;
    }

    @JsonProperty("rid")
    public String getRid() {
        return rid;
    }

    @JsonProperty("rid")
    public void setRid(String rid) {
        this.rid = rid;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}