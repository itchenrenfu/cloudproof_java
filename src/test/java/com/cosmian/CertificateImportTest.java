package com.cosmian;

import cn.hutool.core.codec.Base64;

import com.cosmian.rest.abe.KmsClient;
import com.cosmian.utils.CloudproofException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

/**
 * @author chenrenfu
 * @date 2024/8/21 14:26
 * @packageName:com.cosmian
 * @className: CertificateImportTest
 */
public class CertificateImportTest {
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void improtTest() throws CloudproofException {
        if (!TestUtils.serverAvailable(TestUtils.kmsServerUrl())) {
            throw new RuntimeException("Demo: No KMS Server available");
        }
        // 创建KmsClient
        final KmsClient kmsClient = new KmsClient(TestUtils.kmsServerUrl(), TestUtils.apiKey());
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

        System.out.println(Hex.encodeHexString(Base64.decode(certificate)));

        String certificateId =
                kmsClient.importCertificate(
                        "564d632a-7989-4d3b-b593-0f5ba1b14cf1",
                        "564d632a-7989-4d3b-b593-0f5ba1b14cf1",
                        certificate,
                        false);
        System.out.println(certificateId);
    }

    @Test
    public void hexTest() throws DecoderException {
        String hex =
                "308204BE020100300D06092A864886F70D0101010500048204A8308204A40201000282010100D2C40E1827939D90B459BA1167C257AD5855A4682C29615FFD0AC7679E89C7F247E00A213C0719A438951D28F4414BF9A1CC283BCF231CCDFCB516E4ABE9C54FE73D04A8DE084CB513EF0A5F2B3759966AEE56D277B1159B447C94AE4D5C7F44434E5DD1FA1C73901F3C7590C50D5FCF3F6C16BC0CA5457ECE67EE6BC8EA3621D3308C0F984E4B06EE2D332D030964EF9E1E67BE886CD90EBFDF5FEAC86D535E659C55BD5B1889EC18DA762A0DD9F00513CB2B522ABB3ED0A94FF9C01040D6F354FF3BDFB340E345DBF489B8E58B17213945F9B8B6E7D08BA0715A4CA1747EEB22D4F07E46F6092B2C428A272206DBB94A672590EFE49026F70F7FA4419200B30203010001028201001F1A76B802D32A7C09D979F78A97E80779533E2D6F62F5EF4BF0F4C76A628BE2C9CF9ABF17D43669013EA42C808B485281199750683B96CB21417A2CE9DFD851F7DE8DDAFBD53EB8B445E1027566B82E55FE714AA3063B998BCC54C6BA3CEDBD1DAFA971CCD6B9092A4AEA50A07BDE493FE34C21993E2498D78E07E5D9231334BB0C243FBA9AE05A0751B1E3605657946815EF8669C9C719D03989D91243C4EFA4F2E35BAD02D37EAAFD8FCE3FC32DEE8C84B1B9211C1D80BA2C1EC3247C9101C31F3CCC20FD4FEC23FBB6D7F9F89DA52AE2D6F77773E97EFF1C90A1BE7D1286C5CE8DD5B8AF478157F5A933C1956EBA6FAC752A8ED2C9D1F808E2847807F66902818100DDF60B575559808E2E2B6283BD6AE91752CC17268D6EA535044720E2953C102C083FFB67DD54BAE132A263FFE3C8A2A811196ADE3C18D403CC02B152C5096A273497076743BF84DFF681BF734EDE5BADAC8504EB6DF6E86D37B913184521C5423DA694E3DA41E25BE5A28BB08533FDE3E06241D796D367ED309562524A6F973902818100F3167F1503BE842BCFD477A10C14EFE4D981BB66F6BD542413BA9A0B69BCB2D8F424BED9AB98C84B024548DE75ADFF23CC47A109C7D2DA7AB62F189DB3324DA5B3E1CB7B8D271C28C7D898B3DD92275C308C05674604F56F74B57AAA956573B7D779761AEBA004395716C22AFEAD31308D223C3975EFA495FFBC05B3332B4B4B028181008F329284887AD2B667D0F3A0BC8E81CCDB4CF24E9C065B2D461241840CF59015684927000E4A00BB12F8B37D3E4E8DA9D2464CAD0DA692F9D41F94992AA77545CFE6F40ECA25802B2194F99801B4F0FEAE03AF75D81EE83AB62D7FC533ACA2DFA678972C1CFE4E742D455DCBC75410A3787ACBB1B40BB5CBCF8140CA885D4FF9028181009FE42534CA60AF092E4B9F6053D515B1DE8B619BA922014D3598E0A82F258A31FFEC87881F4180452E3A2A6300DEB0DA780C895528957D5587ADD4B02DE52183A39D1A3DAB2B956F117C5B7CBB79DDDFDAA2CF0F0DD44727C7897324EEDD6B4EFA310F4947C0D9E6696D33CC19F60B4BD71934A9CAF414BB011A48598587AD9F02818001486D3996166CFDB77D9EEC160940E59AF18D23610549D568C365B6ECAE1AA763F08BF617CB12966E678E561B5019F4E0AD99149E95DF8A1AF8F72A2F934939E4D020500BAF43540475784C5550F674B37CFCCA71C8189EA4D6A77B930B834E10EA1C39A95EFE71A6712679F3A9651DD2DBE2373CCF39EFC86A8852178C3122";
        String certificate = Base64.encode(Hex.decodeHex(hex));

        System.out.println(certificate);

        System.out.println(Hex.encodeHexString(Base64.decode(certificate)));
    }
}
