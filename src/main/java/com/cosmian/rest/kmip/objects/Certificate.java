package com.cosmian.rest.kmip.objects;

import com.cosmian.rest.kmip.types.CertificateType;
import com.cosmian.rest.kmip.types.ObjectType;
import com.cosmian.utils.CloudproofException;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * A Managed Cryptographic Object that is a digital certificate. It is a DER-encoded X.509 public
 * key certificate. Object Encoding REQUIRED Certificate Structure Certificate Type Enumeration Yes
 * Certificate Value Byte String Yes
 */
public class Certificate extends KmipObject {

    @JsonProperty(value = "CertificateType")
    private Optional<CertificateType> certificateType;

    @JsonProperty(value = "CertificateValue")
    private Optional<byte[]> certificateValue;

    @JsonProperty(value = "Certificate")
    private Optional<Integer[]> certificate;

    protected Certificate() {}

    public Certificate(
            Optional<CertificateType> certificateType, Optional<byte[]> certificateValue) {
        this.certificateType = certificateType;
        this.certificateValue = certificateValue;
    }

    public Certificate(
            Optional<CertificateType> certificateType,
            Optional<byte[]> certificateValue,
            Optional<Integer[]> certificate) {
        this.certificateType = certificateType;
        this.certificateValue = certificateValue;
        this.certificate = certificate;
    }

    /**
     * Get the {@link CertificateType}
     *
     * @return the {@link CertificateType}
     */
    public Optional<CertificateType> getCertificateType() {
        return this.certificateType;
    }

    /**
     * Set the {@link CertificateType}
     *
     * @param certificateType the type
     */
    public void setCertificateType(Optional<CertificateType> certificateType) {
        this.certificateType = certificateType;
    }

    /**
     * Get the {@link Certificate} value
     *
     * @return the value
     */
    public Optional<byte[]> getCertificateValue() {
        return this.certificateValue;
    }

    /**
     * Set the {@link Certificate} value
     *
     * @param certificateValue the value
     */
    public void setCertificateValue(Optional<byte[]> certificateValue) {
        this.certificateValue = certificateValue;
    }

    /**
     * Set the {@link CertificateType}
     *
     * @param certificateType the {@link CertificateType}
     * @return the {@link Certificate}
     */
    public Certificate certificateType(Optional<CertificateType> certificateType) {
        setCertificateType(certificateType);
        return this;
    }

    /**
     * Set the {@link Certificate} value
     *
     * @param certificateValue the value
     * @return the {@link Certificate}
     */
    public Certificate certificateValue(Optional<byte[]> certificateValue) {
        setCertificateValue(certificateValue);
        return this;
    }

    public Optional<Integer[]> getCertificate() {
        return certificate;
    }

    public void setCertificate(Optional<Integer[]> certificate) {
        this.certificate = certificate;
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.Certificate;
    }

    /**
     * Deserialize an instance from its Json representation obtained using toJson()
     *
     * @param json the JSON string
     * @return the {@link Certificate}
     * @throws CloudproofException if the {@link Certificate} cannot be parsed
     */
    public static Certificate fromJson(String json) throws CloudproofException {
        return KmipObject.fromJson(json, Certificate.class);
    }
}
