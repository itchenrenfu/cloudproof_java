package com.cosmian;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.cosmian.rest.abe.KmsClient;
import com.cosmian.rest.kmip.operations.ValidateResponse;
import com.cosmian.utils.CloudproofException;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author chenrenfu
 * @date 2024/8/22 12:16
 * @packageName:com.cosmian
 * @className: ValidateTest
 */
public class ValidateTest {
    private static final String HOSTNAME = "http://172.19.203.185:9998";

    static String CERTIFICATE =
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

    public static void main(String[] args) {
        String caJson =
                "{\"object_type\":\"Certificate\",\"object\":{\"CertificateType\":\"X509\",\"CertificateValue\":[48,130,3,201,48,130,2,177,160,3,2,1,2,2,20,2,220,249,194,73,80,157,41,194,141,67,4,243,53,205,250,40,84,65,238,48,13,6,9,42,134,72,134,247,13,1,1,11,5,0,48,116,49,11,48,9,6,3,85,4,6,19,2,67,78,49,14,48,12,6,3,85,4,8,12,5,78,85,72,65,78,49,17,48,15,6,3,85,4,7,12,8,67,72,65,78,71,83,72,65,49,17,48,15,6,3,85,4,10,12,8,90,72,79,85,76,85,68,65,49,10,48,8,6,3,85,4,11,12,1,102,49,12,48,10,6,3,85,4,3,12,3,101,114,116,49,21,48,19,6,9,42,134,72,134,247,13,1,9,1,22,6,119,101,114,101,119,114,48,30,23,13,50,52,48,56,49,53,48,55,52,48,53,49,90,23,13,51,52,48,56,49,51,48,55,52,48,53,49,90,48,116,49,11,48,9,6,3,85,4,6,19,2,67,78,49,14,48,12,6,3,85,4,8,12,5,78,85,72,65,78,49,17,48,15,6,3,85,4,7,12,8,67,72,65,78,71,83,72,65,49,17,48,15,6,3,85,4,10,12,8,90,72,79,85,76,85,68,65,49,10,48,8,6,3,85,4,11,12,1,102,49,12,48,10,6,3,85,4,3,12,3,101,114,116,49,21,48,19,6,9,42,134,72,134,247,13,1,9,1,22,6,119,101,114,101,119,114,48,130,1,34,48,13,6,9,42,134,72,134,247,13,1,1,1,5,0,3,130,1,15,0,48,130,1,10,2,130,1,1,0,210,196,14,24,39,147,157,144,180,89,186,17,103,194,87,173,88,85,164,104,44,41,97,95,253,10,199,103,158,137,199,242,71,224,10,33,60,7,25,164,56,149,29,40,244,65,75,249,161,204,40,59,207,35,28,205,252,181,22,228,171,233,197,79,231,61,4,168,222,8,76,181,19,239,10,95,43,55,89,150,106,238,86,210,119,177,21,155,68,124,148,174,77,92,127,68,67,78,93,209,250,28,115,144,31,60,117,144,197,13,95,207,63,108,22,188,12,165,69,126,206,103,238,107,200,234,54,33,211,48,140,15,152,78,75,6,238,45,51,45,3,9,100,239,158,30,103,190,136,108,217,14,191,223,95,234,200,109,83,94,101,156,85,189,91,24,137,236,24,218,118,42,13,217,240,5,19,203,43,82,42,187,62,208,169,79,249,192,16,64,214,243,84,255,59,223,179,64,227,69,219,244,137,184,229,139,23,33,57,69,249,184,182,231,208,139,160,113,90,76,161,116,126,235,34,212,240,126,70,246,9,43,44,66,138,39,34,6,219,185,74,103,37,144,239,228,144,38,247,15,127,164,65,146,0,179,2,3,1,0,1,163,83,48,81,48,29,6,3,85,29,14,4,22,4,20,202,199,106,84,33,160,128,139,98,161,220,94,242,209,134,211,179,171,196,198,48,31,6,3,85,29,35,4,24,48,22,128,20,202,199,106,84,33,160,128,139,98,161,220,94,242,209,134,211,179,171,196,198,48,15,6,3,85,29,19,1,1,255,4,5,48,3,1,1,255,48,13,6,9,42,134,72,134,247,13,1,1,11,5,0,3,130,1,1,0,36,254,13,130,115,253,106,100,51,21,77,218,96,119,237,140,175,183,152,183,179,210,68,117,76,113,11,172,139,59,10,12,180,0,68,170,15,105,177,158,63,56,101,113,209,28,252,26,222,183,159,95,59,205,187,131,82,19,220,220,57,92,26,137,34,164,113,59,192,69,60,3,46,229,64,83,226,10,12,16,54,106,233,52,11,106,156,188,161,193,62,57,143,178,29,179,60,77,239,56,150,23,15,186,35,22,52,227,176,37,7,106,113,183,85,235,30,34,147,54,77,149,87,35,249,36,191,6,73,133,184,253,144,101,221,191,63,19,94,52,112,111,54,67,209,173,236,72,180,165,116,182,226,73,144,189,224,142,99,235,8,112,12,9,216,195,137,228,20,24,56,136,229,66,117,167,161,146,9,254,52,45,79,140,226,235,4,107,235,16,8,224,144,63,249,13,7,40,184,181,88,78,49,40,181,198,111,13,34,0,214,170,56,56,31,86,230,21,197,18,105,96,244,21,173,117,155,213,56,102,215,8,189,33,34,137,53,205,94,173,254,86,243,38,75,173,16,190,250,27,114,57,0,29,148,4]}}";
        JSONObject jsonObject = JSON.parseObject(caJson, JSONObject.class);
        JSONArray jsonArray = jsonObject.getJSONObject("object").getJSONArray("CertificateValue");

        List<Integer> list = JSONArray.parseArray(jsonArray.toJSONString(), Integer.class);

        byte[] byteArray = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            byteArray[i] = list.get(i).byteValue();
        }

        System.out.println(JSON.toJSONString(byteArray));

        byte[] bytes3 = Base64.decode(CERTIFICATE);

        System.out.println(JSON.toJSONString(bytes3));

        for (byte b : bytes3) {
            int intValue = (int) Byte.toUnsignedLong(b);
            System.out.print(intValue + ",");
        }

        //        for (int i = 0; i < jsonArray.size(); i++) {
        //            Integer integer = (Integer) jsonArray.get(i);
        //            String valueJson =
        //                    "{\n"
        //                            + "\t\t\t\t\"tag\": \"\",\n"
        //                            + "\t\t\t\t\"type\": \"Integer\",\n"
        //                            + "\t\t\t\t\"value\": "
        //                            + integer
        //                            + "\n"
        //                            + "\t\t\t},";
        //            System.out.println(valueJson);
        //        }
    }

    @Test
    public void validateTest() {
        String fileName = "mocks/validate.json";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        String jsonStr = TestUtil.readInputStreamAsString(inputStream);
        String responseJson = kmip(jsonStr);
    }

    @Test
    public void validateCertificateTest() {
        if (!TestUtils.serverAvailable(TestUtils.kmsServerUrl())) {
            throw new RuntimeException("Demo: No KMS Server available");
        }

        // Access to the KMS server.
        // Change the Cosmian Server Server URL and API key as appropriate
        final KmsClient kmsClient = new KmsClient(TestUtils.kmsServerUrl(), TestUtils.apiKey());
        try {
            ValidateResponse validateResponse =
                    kmsClient.validateCertificate("", "f1c381ce-0805-4c82-9dac-880add1d77e3");
            System.out.println(JSON.toJSONString(validateResponse));
        } catch (CloudproofException e) {
            throw new RuntimeException(e);
        }
    }

    public static String kmip(String jsonBody) {
        String url = "/kmip/2_1";
        String response = HttpUtil.post(HOSTNAME + url, jsonBody);
        System.out.println(response);
        return response;
    }
}
