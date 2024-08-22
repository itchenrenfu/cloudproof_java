package com.cosmian;

import com.cosmian.rest.abe.KmsClient;
import com.cosmian.utils.CloudproofException;

import org.junit.jupiter.api.Test;

/**
 * @author chenrenfu
 * @date 2024/8/22 10:52
 * @packageName:com.cosmian
 * @className: RsaKeyImport
 */
public class RsaKeyImportTest {
    private String KMS_URL = "http://172.19.100.3:9998";

    @Test
    public void publicKeyImport() throws CloudproofException {
        if (!TestUtils.serverAvailable(KMS_URL)) {
            throw new RuntimeException("Demo: No KMS Server available");
        }
        // 创建KmsClient
        final KmsClient kmsClient = new KmsClient(KMS_URL, TestUtils.apiKey());
        // 公钥导入
        String publisMasterKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0sQOGCeTnZC0WboRZ8JXrVhVpGgsKWFf/QrHZ56Jx/JH4AohPAcZpDiVHSj0QUv5ocwoO88jHM38tRbkq+nFT+c9BKjeCEy1E+8KXys3WZZq7lbSd7EVm0R8lK5NXH9EQ05d0focc5AfPHWQxQ1fzz9sFrwMpUV+zmfua8jqNiHTMIwPmE5LBu4tMy0DCWTvnh5nvohs2Q6/31/qyG1TXmWcVb1bGInsGNp2Kg3Z8AUTyytSKrs+0KlP+cAQQNbzVP8737NA40Xb9Im45YsXITlF+bi259CLoHFaTKF0fusi1PB+RvYJKyxCiiciBtu5SmclkO/kkCb3D3+kQZIAswIDAQAB";

        String id = kmsClient.importRsaPublicMasterKey(publisMasterKey, 4096, false);
        System.out.println(id);
    }

    @Test
    public void privateKeyImport() throws CloudproofException {
        if (!TestUtils.serverAvailable(KMS_URL)) {
            throw new RuntimeException("Demo: No KMS Server available");
        }
        // 创建KmsClient
        final KmsClient kmsClient = new KmsClient(KMS_URL, TestUtils.apiKey());
        // 私钥导入
        String privateMasterKey =
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDSxA4YJ5OdkLRZuhFnwletWFWkaCwpYV/9CsdnnonH8kfgCiE8BxmkOJUdKPRBS/mhzCg7zyMczfy1FuSr6cVP5z0EqN4ITLUT7wpfKzdZlmruVtJ3sRWbRHyUrk1cf0RDTl3R+hxzkB88dZDFDV/PP2wWvAylRX7OZ+5ryOo2IdMwjA+YTksG7i0zLQMJZO+eHme+iGzZDr/fX+rIbVNeZZxVvVsYiewY2nYqDdnwBRPLK1Iquz7QqU/5wBBA1vNU/zvfs0DjRdv0ibjlixchOUX5uLbn0IugcVpMoXR+6yLU8H5G9gkrLEKKJyIG27lKZyWQ7+SQJvcPf6RBkgCzAgMBAAECggEAHxp2uALTKnwJ2Xn3ipfoB3lTPi1vYvXvS/D0x2pii+LJz5q/F9Q2aQE+pCyAi0hSgRmXUGg7lsshQXos6d/YUffejdr71T64tEXhAnVmuC5V/nFKowY7mYvMVMa6PO29Ha+pcczWuQkqSupQoHveST/jTCGZPiSY144H5dkjEzS7DCQ/uprgWgdRseNgVleUaBXvhmnJxxnQOYnZEkPE76Ty41utAtN+qv2Pzj/DLe6MhLG5IRwdgLosHsMkfJEBwx88zCD9T+wj+7bX+fidpSri1vd3c+l+/xyQob59EobFzo3VuK9HgVf1qTPBlW66b6x1Ko7SydH4COKEeAf2aQKBgQDd9gtXVVmAji4rYoO9aukXUswXJo1upTUERyDilTwQLAg/+2fdVLrhMqJj/+PIoqgRGWrePBjUA8wCsVLFCWonNJcHZ0O/hN/2gb9zTt5brayFBOtt9uhtN7kTGEUhxUI9ppTj2kHiW+Wii7CFM/3j4GJB15bTZ+0wlWJSSm+XOQKBgQDzFn8VA76EK8/Ud6EMFO/k2YG7Zva9VCQTupoLabyy2PQkvtmrmMhLAkVI3nWt/yPMR6EJx9LaerYvGJ2zMk2ls+HLe40nHCjH2Jiz3ZInXDCMBWdGBPVvdLV6qpVlc7fXeXYa66AEOVcWwir+rTEwjSI8OXXvpJX/vAWzMytLSwKBgQCPMpKEiHrStmfQ86C8joHM20zyTpwGWy1GEkGEDPWQFWhJJwAOSgC7EvizfT5OjanSRkytDaaS+dQflJkqp3VFz+b0DsolgCshlPmYAbTw/q4Dr3XYHug6ti1/xTOsot+meJcsHP5OdC1FXcvHVBCjeHrLsbQLtcvPgUDKiF1P+QKBgQCf5CU0ymCvCS5Ln2BT1RWx3othm6kiAU01mOCoLyWKMf/sh4gfQYBFLjoqYwDesNp4DIlVKJV9VYet1LAt5SGDo50aPasrlW8RfFt8u3nd39qizw8N1Ecnx4lzJO7da076MQ9JR8DZ5mltM8wZ9gtL1xk0qcr0FLsBGkhZhYetnwKBgAFIbTmWFmz9t32e7BYJQOWa8Y0jYQVJ1WjDZbbsrhqnY/CL9hfLEpZuZ45WG1AZ9OCtmRSeld+KGvj3Ki+TSTnk0CBQC69DVAR1eExVUPZ0s3z8ynHIGJ6k1qd7kwuDThDqHDmpXv5xpnEmefOpZR3S2+I3PM8578hqiFIXjDEi";

        String id = kmsClient.importRsaPrivateMasterKey(privateMasterKey, 4096, false);
        System.out.println(id);
    }
}
