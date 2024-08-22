package com.cosmian;

import com.cosmian.rest.abe.KmsClient;
import com.cosmian.rest.kmip.operations.CertifyResponse;
import com.cosmian.utils.CloudproofException;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Test;

/**
 * 证书颁发
 *
 * @author chenrenfu
 * @date 2024/8/15 15:29
 * @packageName:com.cosmian
 * @className: CertificateAuthorityTest
 */
public class CertificateAuthorityTest {
    private String KMS_URL = "http://172.19.100.3:9998";

    @Test
    public void certifyCreate() throws JsonProcessingException, CloudproofException {

        // 数字证书拓展字段
        String extensions =
                "[ v3_ca ]\n"
                        + "subjectKeyIdentifier=hash\n"
                        + "authorityKeyIdentifier=keyid,issuer\n";

        if (!TestUtils.serverAvailable(KMS_URL)) {
            throw new RuntimeException("Demo: No KMS Server available");
        }
        // acf9ce46-f33f-4eef-8bfb-83d4dfce1ea0  ca证书ID有效期1天
        // 创建KmsClient
        final KmsClient kmsClient = new KmsClient(KMS_URL, TestUtils.apiKey());
        CertifyResponse resp =
                kmsClient.createCertificateRequest(
                        "CN",
                        "HUNAN",
                        "CHANGSHA",
                        "HD",
                        "CRF",
                        "itchenrenfu@163.com",
                        365,
                        extensions);
        System.out.println(resp.getUniqueIdentifier());
    }
}
