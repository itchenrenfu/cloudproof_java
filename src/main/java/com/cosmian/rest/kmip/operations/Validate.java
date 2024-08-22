package com.cosmian.rest.kmip.operations;

import cn.hutool.core.codec.Base64;

import com.cosmian.rest.kmip.json.KmipStruct;
import com.cosmian.rest.kmip.json.KmipStructDeserializer;
import com.cosmian.rest.kmip.json.KmipStructSerializer;
import com.cosmian.rest.kmip.objects.Certificate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Optional;

/**
 * @author chenrenfu
 * @date 2024/8/22 13:35
 * @packageName:com.cosmian.rest.kmip.operations
 * @className: Validate
 */
@JsonSerialize(using = KmipStructSerializer.class)
@JsonDeserialize(using = KmipStructDeserializer.class)
public class Validate implements KmipStruct {
    @JsonProperty(value = "Certificate")
    private Optional<Certificate> certificate;

    @JsonProperty(value = "UniqueIdentifier")
    private Optional<String[]> uniqueIdentifier;

    public Validate() {}

    public Validate(Optional<Certificate> certificate, Optional<String[]> uniqueIdentifier) {
        this.certificate = certificate;
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public Optional<Certificate> getCertificate() {
        return certificate;
    }

    public void setCertificate(Optional<Certificate> certificate) {
        this.certificate = certificate;
    }

    public Optional<String[]> getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(Optional<String[]> uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public static void main(String[] args) throws JsonProcessingException {
        String certificate =
                "MIIDyTCCArGgAwIBAgIUAtz5wklQnSnCjUME8zXN+ihUQe4wDQYJKoZIhvcNAQEL\n"
                        + "BQAwdDELMAkGA1UEBhMCQ04xDjAMBgNVBAgMBU5VSEFOMREwDwYDVQQHDAhDSEFO\n"
                        + "R1NIQTERMA8GA1UECgwIWkhPVUxVREExCjAIBgNVBAsMAWYxDDAKBgNVBAMMA2Vy\n"
                        + "dDEVMBMGCSqGSIb3DQEJARYGd2VyZXdyMB4XDTI0MDgxNTA3NDA1MVoXDTM0MDgx\n"
                        + "MzA3NDA1MVowdDELMAkGA1UEBhMCQ04xDjAMBgNVBAgMBU5VSEFOMREwDwYDVQQH\n"
                        + "DAhDSEFOR1NIQTERMA8GA1UECgwIWkhPVUxVREExCjAIBgNVBAsMAWYxDDAKBgNV\n"
                        + "BAMMA2VydDEVMBMGCSqGSIb3DQEJARYGd2VyZXdyMIIBIjANBgkqhkiG9w0BAQEF\n"
                        + "AAOCAQ8AMIIBCgKCAQEA0sQOGCeTnZC0WboRZ8JXrVhVpGgsKWFf/QrHZ56Jx/JH\n"
                        + "4AohPAcZpDiVHSj0QUv5ocwoO88jHM38tRbkq+nFT+c9BKjeCEy1E+8KXys3WZZq\n"
                        + "7lbSd7EVm0R8lK5NXH9EQ05d0focc5AfPHWQxQ1fzz9sFrwMpUV+zmfua8jqNiHT\n"
                        + "MIwPmE5LBu4tMy0DCWTvnh5nvohs2Q6/31/qyG1TXmWcVb1bGInsGNp2Kg3Z8AUT\n"
                        + "yytSKrs+0KlP+cAQQNbzVP8737NA40Xb9Im45YsXITlF+bi259CLoHFaTKF0fusi\n"
                        + "1PB+RvYJKyxCiiciBtu5SmclkO/kkCb3D3+kQZIAswIDAQABo1MwUTAdBgNVHQ4E\n"
                        + "FgQUysdqVCGggItiodxe8tGG07OrxMYwHwYDVR0jBBgwFoAUysdqVCGggItiodxe\n"
                        + "8tGG07OrxMYwDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAJP4N\n"
                        + "gnP9amQzFU3aYHftjK+3mLez0kR1THELrIs7Cgy0AESqD2mxnj84ZXHRHPwa3ref\n"
                        + "XzvNu4NSE9zcOVwaiSKkcTvARTwDLuVAU+IKDBA2auk0C2qcvKHBPjmPsh2zPE3v\n"
                        + "OJYXD7ojFjTjsCUHanG3VeseIpM2TZVXI/kkvwZJhbj9kGXdvz8TXjRwbzZD0a3s\n"
                        + "SLSldLbiSZC94I5j6whwDAnYw4nkFBg4iOVCdaehkgn+NC1PjOLrBGvrEAjgkD/5\n"
                        + "DQcouLVYTjEotcZvDSIA1qo4OB9W5hXFEmlg9BWtdZvVOGbXCL0hIok1zV6t/lbz\n"
                        + "JkutEL76G3I5AB2UBA==\n";
        byte[] bytes3 = Base64.decode(certificate);

        Integer[] carray = new Integer[bytes3.length];
        for (int i = 0; i < bytes3.length; i++) {
            byte b = bytes3[i];
            int intValue = (int) Byte.toUnsignedLong(b);
            carray[i] = intValue;
        }
        Validate validate =
                new Validate(
                        Optional.of(
                                new Certificate(
                                        Optional.empty(), Optional.empty(), Optional.of(carray))),
                        Optional.of(new String[] {"343243242343243242"}));
        String json_req = new ObjectMapper().writeValueAsString(validate);
        System.out.println(json_req);
    }
}
