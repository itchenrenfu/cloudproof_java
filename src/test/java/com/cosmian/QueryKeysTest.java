package com.cosmian;

import com.cosmian.rest.abe.KmsClient;
import com.cosmian.rest.kmip.objects.PrivateKey;
import com.cosmian.rest.kmip.objects.PublicKey;
import com.cosmian.rest.kmip.types.KeyFormatType;
import com.cosmian.utils.CloudproofException;

import org.junit.jupiter.api.Test;

/**
 * 查询秘钥测试
 *
 * @author chenrenfu
 * @date 2024/8/16 10:11
 * @packageName:com.cosmian
 * @className: QueryKeysTest
 */
public class QueryKeysTest {
    @Test
    public void queryRsaKey() throws CloudproofException {
        if (!TestUtils.serverAvailable(TestUtils.kmsServerUrl())) {
            throw new RuntimeException("Demo: No KMS Server available");
        }
        // 创建KmsClient
        final KmsClient kmsClient = new KmsClient(TestUtils.kmsServerUrl(), TestUtils.apiKey());
        String privateMasterKeyUniqueIdentifier = "59427684-5afc-4c6c-8fcd-a360e74f72d7";

        try {
            PrivateKey privateKey =
                    kmsClient.retrievePrivateMasterKey(
                            privateMasterKeyUniqueIdentifier,
                            KeyFormatType.TransparentRSAPrivateKey);
            System.out.println(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String publicMasterKeyUniqueIdentifier = "c0ea14a8-7b78-45fc-944c-00db6b3a7a18";
            PublicKey publicKey =
                    kmsClient.retrievePublicMasterKey(
                            publicMasterKeyUniqueIdentifier, KeyFormatType.TransparentRSAPublicKey);
            System.out.println(publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
