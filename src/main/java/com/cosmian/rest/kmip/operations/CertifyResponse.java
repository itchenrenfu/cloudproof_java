package com.cosmian.rest.kmip.operations;

import com.cosmian.rest.kmip.json.KmipStruct;
import com.cosmian.rest.kmip.json.KmipStructDeserializer;
import com.cosmian.rest.kmip.json.KmipStructSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author chenrenfu
 * @date 2024/8/20 16:10
 * @packageName:com.cosmian.rest.kmip.operations
 * @className: CertifyResponse
 */
@JsonSerialize(using = KmipStructSerializer.class)
@JsonDeserialize(using = KmipStructDeserializer.class)
public class CertifyResponse implements KmipStruct {

    @JsonProperty(value = "UniqueIdentifier")
    private String uniqueIdentifier;

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }
}
