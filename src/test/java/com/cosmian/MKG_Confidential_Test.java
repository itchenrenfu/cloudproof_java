package com.cosmian;

import com.cosmian.jna.covercrypt.CoverCrypt;
import com.cosmian.jna.covercrypt.structs.Policy;
import com.cosmian.rest.abe.KmsClient;
import com.cosmian.rest.abe.data.DecryptedData;
import com.cosmian.rest.kmip.objects.PrivateKey;
import com.cosmian.utils.CloudproofException;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author chenrenfu
 * @date 2024/8/13 16:04
 * @packageName:com.cosmian
 * @className: MKG_Protected_Test
 */
public class MKG_Confidential_Test {

    /** Department::MKG && Security Level::Confidential 加解密测试 */
    @Test
    public void mkgConfidentialTest() throws CloudproofException {
        if (!TestUtils.serverAvailable(TestUtils.kmsServerUrl())) {
            throw new RuntimeException("Demo: No KMS Server available");
        }
        // 创建KmsClient
        final KmsClient kmsClient = new KmsClient(TestUtils.kmsServerUrl(), TestUtils.apiKey());

        // 实例化加密策略
        Policy policy = TestNativeCoverCrypt.policy();

        // 创建密钥对
        String[] ids = kmsClient.createCoverCryptMasterKeyPair(policy);
        String privateMasterKeyUniqueIdentifier = ids[0];
        String publicMasterKeyUniqueIdentifier = ids[1];

        String confidentialMkgMessage = "confidentialMkgMessage";
        System.out.println("加密前数据:" + confidentialMkgMessage);
        byte[] confidentialMkgData = confidentialMkgMessage.getBytes(StandardCharsets.UTF_8);
        String confidentialMkgEncryptionPolicy = "Department::MKG && Security Level::Confidential";
        byte[] confidentialMkgCT =
                kmsClient.coverCryptEncrypt(
                        publicMasterKeyUniqueIdentifier,
                        confidentialMkgData,
                        confidentialMkgEncryptionPolicy);
        System.out.println("加密后数据:" + new String(confidentialMkgCT, StandardCharsets.UTF_8));

        // 创建解密秘钥
        String confidentialMkgUserKeyUid =
                kmsClient.createCoverCryptUserDecryptionKey(
                        confidentialMkgEncryptionPolicy, privateMasterKeyUniqueIdentifier);

        DecryptedData confidentialMkg =
                kmsClient.coverCryptDecrypt(confidentialMkgUserKeyUid, confidentialMkgCT);
        System.out.println(
                "解密后的数据:" + new String(confidentialMkg.getPlaintext(), StandardCharsets.UTF_8));

        // 重新生成秘钥，所有访问Department::MKG的秘钥都将重新生成，也可以使用精确的访问策略(Department::MKG && Security
        // Level::Confidential)
        kmsClient.rekeyCoverCryptAccessPolicy(privateMasterKeyUniqueIdentifier, "Department::MKG");
        // 重新跟换私钥
        PrivateKey rekeyedConfidentialMkgUserKey =
                kmsClient.retrieveCoverCryptUserDecryptionKey(confidentialMkgUserKeyUid);
        System.out.println("重新生成秘钥");
        DecryptedData confidentialMkg2 =
                CoverCrypt.decrypt(
                        rekeyedConfidentialMkgUserKey.bytes(), confidentialMkgCT, Optional.empty());
        System.out.println(
                "重新生成秘钥解密源加密数据:"
                        + new String(confidentialMkg2.getPlaintext(), StandardCharsets.UTF_8));

        // 修剪秘钥，修剪后，原秘钥将不可用
        kmsClient.pruneCoverCryptAccessPolicy(privateMasterKeyUniqueIdentifier, "Department::MKG");

        DecryptedData confidentialMkg3 =
                kmsClient.coverCryptDecrypt(confidentialMkgUserKeyUid, confidentialMkgCT);
        System.out.println(
                "用旧秘钥解密后的数据:"
                        + new String(confidentialMkg3.getPlaintext(), StandardCharsets.UTF_8));
    }
}
