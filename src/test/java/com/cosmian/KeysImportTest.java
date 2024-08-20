package com.cosmian;

import com.cosmian.jna.covercrypt.structs.Policy;
import com.cosmian.rest.abe.KmsClient;
import com.cosmian.utils.CloudproofException;

/**
 * @author chenrenfu
 * @date 2024/8/16 9:39
 * @packageName:com.cosmian
 * @className: KeysImportTest
 */
public class KeysImportTest {
    public void test() throws CloudproofException {
        if (!TestUtils.serverAvailable(TestUtils.kmsServerUrl())) {
            throw new RuntimeException("No KMS Server available");
        }

        KmsClient kmsClient = new KmsClient(TestUtils.kmsServerUrl(), TestUtils.apiKey());
        Policy policy = TestNativeCoverCrypt.policy();
    }
}
