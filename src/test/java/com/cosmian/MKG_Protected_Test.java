package com.cosmian;

import com.cosmian.jna.covercrypt.structs.Policy;
import com.cosmian.rest.abe.KmsClient;
import com.cosmian.rest.abe.data.DecryptedData;
import com.cosmian.utils.CloudproofException;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author chenrenfu
 * @date 2024/8/13 16:04
 * @packageName:com.cosmian
 * @className: MKG_Protected_Test
 */
public class MKG_Protected_Test {

    /** Department::MKG && Security Level::Protected 加解密测试 */
    @Test
    public void mkgProtectedTest() throws CloudproofException {
        if (!TestUtils.serverAvailable(TestUtils.kmsServerUrl())) {
            throw new RuntimeException("Demo: No KMS Server available");
        }
        // 创建KmsClient
        final KmsClient kmsClient = new KmsClient(TestUtils.kmsServerUrl(), TestUtils.apiKey());

        // 实例化加密策略
        Policy policy = TestNativeCoverCrypt.policy();

        // 创建密钥对
        String[] ids = kmsClient.createCoverCryptMasterKeyPair(policy);
        System.out.println("创建密钥对:" + ids[0] + " " + ids[1]);
        String privateMasterKeyUniqueIdentifier = ids[0];
        String publicMasterKeyUniqueIdentifier = ids[1];

        String protectedMkgMessage = "protectedMkgMessage";
        System.out.println("加密前数据:" + protectedMkgMessage);
        byte[] protectedMkgData = protectedMkgMessage.getBytes(StandardCharsets.UTF_8);
        String protectedMkgEncryptionPolicy = "Department::MKG && Security Level::Protected";
        byte[] protectedMkgCT =
                kmsClient.coverCryptEncrypt(
                        publicMasterKeyUniqueIdentifier,
                        protectedMkgData,
                        protectedMkgEncryptionPolicy);
        System.out.println("加密后数据:" + new String(protectedMkgCT, StandardCharsets.UTF_8));

        // 创建解密秘钥
        String protectedMkgUserKeyUid =
                kmsClient.createCoverCryptUserDecryptionKey(
                        protectedMkgEncryptionPolicy, privateMasterKeyUniqueIdentifier);

        DecryptedData protectedMkg =
                kmsClient.coverCryptDecrypt(protectedMkgUserKeyUid, protectedMkgCT);
        System.out.println(
                "解密后的数据:" + new String(protectedMkg.getPlaintext(), StandardCharsets.UTF_8));

        // 高级别的加密策略可以解密低级别的，低级别的不能机密高级别的
        String confidentialMkgUserKeyUid =
                kmsClient.createCoverCryptUserDecryptionKey(
                        "Department::MKG && Security Level::Confidential",
                        privateMasterKeyUniqueIdentifier);
        DecryptedData protectedMkg2 =
                kmsClient.coverCryptDecrypt(confidentialMkgUserKeyUid, protectedMkgCT);

        System.out.println(
                "解密后的数据2:" + new String(protectedMkg2.getPlaintext(), StandardCharsets.UTF_8));
    }
}
