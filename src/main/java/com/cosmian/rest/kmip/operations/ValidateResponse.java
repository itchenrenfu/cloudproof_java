package com.cosmian.rest.kmip.operations;

import com.cosmian.rest.kmip.json.KmipStruct;
import com.cosmian.rest.kmip.json.KmipStructDeserializer;
import com.cosmian.rest.kmip.json.KmipStructSerializer;
import com.cosmian.rest.kmip.types.ValidityIndicator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = KmipStructSerializer.class)
@JsonDeserialize(using = KmipStructDeserializer.class)
public class ValidateResponse implements KmipStruct {

    /** The Unique Identifier of the object imported */
    @JsonProperty(value = "ValidityIndicator")
    private ValidityIndicator validityIndicator;

    public ValidateResponse() {}

    public ValidateResponse(ValidityIndicator validityIndicator) {
        this.validityIndicator = validityIndicator;
    }

    public ValidityIndicator getValidityIndicator() {
        return validityIndicator;
    }

    public void setValidityIndicator(ValidityIndicator validityIndicator) {
        this.validityIndicator = validityIndicator;
    }
}
