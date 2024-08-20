package com.cosmian.rest.kmip.types;

import com.cosmian.rest.kmip.json.KmipStruct;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chenrenfu
 * @date 2024/8/20 11:27
 * @packageName:com.cosmian.rest.kmip.types
 * @className: CertificateAttributes
 */
public class CertificateAttributes implements KmipStruct {

    @JsonProperty(value = "CertificateSubjectCn")
    private String certificateSubjectCn = "";

    @JsonProperty(value = "CertificateSubjectO")
    private String certificateSubjectO = "";

    @JsonProperty(value = "CertificateSubjectOu")
    private String certificateSubjectOu = "";

    @JsonProperty(value = "CertificateSubjectEmail")
    private String certificateSubjectEmail = "";

    @JsonProperty(value = "CertificateSubjectC")
    private String certificateSubjectC = "";

    @JsonProperty(value = "CertificateSubjectSt")
    private String certificateSubjectSt = "";

    @JsonProperty(value = "CertificateSubjectL")
    private String certificateSubjectL = "";

    @JsonProperty(value = "CertificateSubjectUid")
    private String certificateSubjectUid = "";

    @JsonProperty(value = "CertificateSubjectSerialNumber")
    private String certificateSubjectSerialNumber = "";

    @JsonProperty(value = "CertificateSubjectTitle")
    private String certificateSubjectTitle = "";

    @JsonProperty(value = "CertificateSubjectDc")
    private String certificateSubjectDc = "";

    @JsonProperty(value = "CertificateSubjectDnQualifier")
    private String certificateSubjectDnQualifier = "";

    @JsonProperty(value = "CertificateIssuerCn")
    private String certificateIssuerCn = "";

    @JsonProperty(value = "CertificateIssuerO")
    private String certificateIssuerO = "";

    @JsonProperty(value = "CertificateIssuerOu")
    private String certificateIssuerOu = "";

    @JsonProperty(value = "CertificateIssuerEmail")
    private String certificateIssuerEmail = "";

    @JsonProperty(value = "CertificateIssuerC")
    private String certificateIssuerC = "";

    @JsonProperty(value = "CertificateIssuerSt")
    private String certificateIssuerSt = "";

    @JsonProperty(value = "CertificateIssuerL")
    private String certificateIssuerL = "";

    @JsonProperty(value = "CertificateIssuerUid")
    private String certificateIssuerUid = "";

    @JsonProperty(value = "CertificateIssuerSerialNumber")
    private String certificateIssuerSerialNumber = "";

    @JsonProperty(value = "CertificateIssuerTitle")
    private String certificateIssuerTitle = "";

    @JsonProperty(value = "CertificateIssuerDc")
    private String certificateIssuerDc = "";

    @JsonProperty(value = "CertificateIssuerDnQualifier")
    private String certificateIssuerDnQualifier = "";

    public String getCertificateSubjectCn() {
        return certificateSubjectCn;
    }

    public void setCertificateSubjectCn(String certificateSubjectCn) {
        this.certificateSubjectCn = certificateSubjectCn;
    }

    public String getCertificateSubjectO() {
        return certificateSubjectO;
    }

    public void setCertificateSubjectO(String certificateSubjectO) {
        this.certificateSubjectO = certificateSubjectO;
    }

    public String getCertificateSubjectOu() {
        return certificateSubjectOu;
    }

    public void setCertificateSubjectOu(String certificateSubjectOu) {
        this.certificateSubjectOu = certificateSubjectOu;
    }

    public String getCertificateSubjectEmail() {
        return certificateSubjectEmail;
    }

    public void setCertificateSubjectEmail(String certificateSubjectEmail) {
        this.certificateSubjectEmail = certificateSubjectEmail;
    }

    public String getCertificateSubjectC() {
        return certificateSubjectC;
    }

    public void setCertificateSubjectC(String certificateSubjectC) {
        this.certificateSubjectC = certificateSubjectC;
    }

    public String getCertificateSubjectSt() {
        return certificateSubjectSt;
    }

    public void setCertificateSubjectSt(String certificateSubjectSt) {
        this.certificateSubjectSt = certificateSubjectSt;
    }

    public String getCertificateSubjectL() {
        return certificateSubjectL;
    }

    public void setCertificateSubjectL(String certificateSubjectL) {
        this.certificateSubjectL = certificateSubjectL;
    }

    public String getCertificateSubjectUid() {
        return certificateSubjectUid;
    }

    public void setCertificateSubjectUid(String certificateSubjectUid) {
        this.certificateSubjectUid = certificateSubjectUid;
    }

    public String getCertificateSubjectSerialNumber() {
        return certificateSubjectSerialNumber;
    }

    public void setCertificateSubjectSerialNumber(String certificateSubjectSerialNumber) {
        this.certificateSubjectSerialNumber = certificateSubjectSerialNumber;
    }

    public String getCertificateSubjectTitle() {
        return certificateSubjectTitle;
    }

    public void setCertificateSubjectTitle(String certificateSubjectTitle) {
        this.certificateSubjectTitle = certificateSubjectTitle;
    }

    public String getCertificateSubjectDc() {
        return certificateSubjectDc;
    }

    public void setCertificateSubjectDc(String certificateSubjectDc) {
        this.certificateSubjectDc = certificateSubjectDc;
    }

    public String getCertificateSubjectDnQualifier() {
        return certificateSubjectDnQualifier;
    }

    public void setCertificateSubjectDnQualifier(String certificateSubjectDnQualifier) {
        this.certificateSubjectDnQualifier = certificateSubjectDnQualifier;
    }

    public String getCertificateIssuerCn() {
        return certificateIssuerCn;
    }

    public void setCertificateIssuerCn(String certificateIssuerCn) {
        this.certificateIssuerCn = certificateIssuerCn;
    }

    public String getCertificateIssuerO() {
        return certificateIssuerO;
    }

    public void setCertificateIssuerO(String certificateIssuerO) {
        this.certificateIssuerO = certificateIssuerO;
    }

    public String getCertificateIssuerOu() {
        return certificateIssuerOu;
    }

    public void setCertificateIssuerOu(String certificateIssuerOu) {
        this.certificateIssuerOu = certificateIssuerOu;
    }

    public String getCertificateIssuerEmail() {
        return certificateIssuerEmail;
    }

    public void setCertificateIssuerEmail(String certificateIssuerEmail) {
        this.certificateIssuerEmail = certificateIssuerEmail;
    }

    public String getCertificateIssuerC() {
        return certificateIssuerC;
    }

    public void setCertificateIssuerC(String certificateIssuerC) {
        this.certificateIssuerC = certificateIssuerC;
    }

    public String getCertificateIssuerSt() {
        return certificateIssuerSt;
    }

    public void setCertificateIssuerSt(String certificateIssuerSt) {
        this.certificateIssuerSt = certificateIssuerSt;
    }

    public String getCertificateIssuerL() {
        return certificateIssuerL;
    }

    public void setCertificateIssuerL(String certificateIssuerL) {
        this.certificateIssuerL = certificateIssuerL;
    }

    public String getCertificateIssuerUid() {
        return certificateIssuerUid;
    }

    public void setCertificateIssuerUid(String certificateIssuerUid) {
        this.certificateIssuerUid = certificateIssuerUid;
    }

    public String getCertificateIssuerSerialNumber() {
        return certificateIssuerSerialNumber;
    }

    public void setCertificateIssuerSerialNumber(String certificateIssuerSerialNumber) {
        this.certificateIssuerSerialNumber = certificateIssuerSerialNumber;
    }

    public String getCertificateIssuerTitle() {
        return certificateIssuerTitle;
    }

    public void setCertificateIssuerTitle(String certificateIssuerTitle) {
        this.certificateIssuerTitle = certificateIssuerTitle;
    }

    public String getCertificateIssuerDc() {
        return certificateIssuerDc;
    }

    public void setCertificateIssuerDc(String certificateIssuerDc) {
        this.certificateIssuerDc = certificateIssuerDc;
    }

    public String getCertificateIssuerDnQualifier() {
        return certificateIssuerDnQualifier;
    }

    public void setCertificateIssuerDnQualifier(String certificateIssuerDnQualifier) {
        this.certificateIssuerDnQualifier = certificateIssuerDnQualifier;
    }
}
