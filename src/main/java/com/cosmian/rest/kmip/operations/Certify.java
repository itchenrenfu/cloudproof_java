package com.cosmian.rest.kmip.operations;

import com.cosmian.rest.kmip.json.KmipStruct;
import com.cosmian.rest.kmip.json.KmipStructDeserializer;
import com.cosmian.rest.kmip.json.KmipStructSerializer;
import com.cosmian.rest.kmip.types.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * This operation requests the server to generate a new symmetric key or generate Secret Data as a
 * Managed Cryptographic Object. The request contains information about the type of object being
 * created, and some of the attributes to be assigned to the object (e.g., Cryptographic Algorithm,
 * Cryptographic Length, etc.). The response contains the Unique Identifier of the created object.
 * The server SHALL copy the Unique Identifier returned by this operation into the ID Placeholder
 * variable.
 */
@JsonSerialize(using = KmipStructSerializer.class)
@JsonDeserialize(using = KmipStructDeserializer.class)
public class Certify implements KmipStruct {

    /// Specifies desired attributes to be associated with the new object.
    @JsonProperty("Attributes")
    private Attributes attributes;

    public Certify() {}

    public Certify(Attributes attributes) {

        this.attributes = attributes;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Certify attributes(Attributes attributes) {
        setAttributes(attributes);
        return this;
    }

    //    @Override
    //    public boolean equals(Object o) {
    //        if (o == this) {
    //            return true;
    //        }
    //        if (!(o instanceof Certify)) {
    //            return false;
    //        }
    //        Certify create = (Certify) o;
    //        return Objects.equals(objectType, create.objectType)
    //                && Objects.equals(attributes, create.attributes)
    //                && Objects.equals(protection_storage_masks, create.protection_storage_masks);
    //    }
    //
    //    @Override
    //    public int hashCode() {
    //        return Objects.hash(objectType, attributes, protection_storage_masks);
    //    }
}
