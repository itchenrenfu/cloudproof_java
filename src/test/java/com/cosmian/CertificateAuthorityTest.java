package com.cosmian;

import cn.hutool.http.HttpUtil;

import com.cosmian.jna.covercrypt.structs.Policy;
import com.cosmian.rest.abe.KmsClient;
import com.cosmian.rest.kmip.operations.CertifyResponse;
import com.cosmian.rest.kmip.types.*;
import com.cosmian.utils.CloudproofException;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * 证书颁发
 *
 * @author chenrenfu
 * @date 2024/8/15 15:29
 * @packageName:com.cosmian
 * @className: CertificateAuthorityTest
 */
public class CertificateAuthorityTest {
    private static final String HOSTNAME = "http://192.168.187.130:9998";

    public static void main(String[] args) throws CloudproofException {
        //        authTest();
        ervifyTest();
    }

    public static void ervifyTest() throws CloudproofException {
        // 数字证书ID:55a44536-1000-45b5-89dd-3534db874c2f
        // 数字证书公钥ID:c7a9cb79-c699-460f-8f28-9a461532da79
        // 数字证书私钥ID:5f78ac96-2cd9-4e3c-a018-edcaddac056c
        String jsonStr = verifyCreate("", "59427684-5afc-4c6c-8fcd-a360e74f72d7");
        kmip(jsonStr);
    }

    public static void authTest() throws CloudproofException {
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
        //        String privateMasterKeyUniqueIdentifier = "c7feaae5-acc1-4f6a-a2ab-59b9371a7f7b";
        //        String publicMasterKeyUniqueIdentifier = "62643d4d-6d73-4039-91e7-8f700b7f690a";
        String jsonStr =
                createRequest(privateMasterKeyUniqueIdentifier, publicMasterKeyUniqueIdentifier);
        kmip(jsonStr);
    }

    public static String verifyCreate(String byteString, String privateKeyUniqueIdentifier) {
        String jsonBody =
                "{\n"
                        + "      \"tag\": \"Certify\",\n"
                        + "      \"type\": \"Structure\",\n"
                        + "      \"value\": [\n"
                        + "        {\n"
                        + "          \"tag\": \"CertificateRequestType\",\n"
                        + "          \"type\": \"Enumeration\",\n"
                        + "          \"value\": \"PEM\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"CertificateRequestValue\",\n"
                        + "          \"type\": \"ByteString\",\n"
                        // the PKCS#10 Certificate Signing Request DER bytes encoded in hex
                        + "          \"value\": \"2D2D2D2D2D424547494E20434552544946494341544520524551554553542D2D2D2D2D0A4D494944704443434167774341514177587A454C4D416B474131554542684D43526C49784444414B42674E564241674D41306C6B526A454F4D417747413155450A427777465547467961584D784544414F42674E5642416F4D42304E7663323170595734784444414B42674E564241734D4131496D524445534D424147413155450A4177774A5647567A6443424D5A57466D4D4949426F6A414E42676B71686B6947397730424151454641414F43415938414D49494269674B4341594541773045470A575355754F4E59526C5A3077506139524A7057416C577351515A5050675350786E354D5777464E4F383671676856666378314C387169515079315147687172320A764F766D577A366D752F59772F5663366E44644744694B54555564537341305167566474643770366B71317341393071364C30416E63384D384D46392F6F536F0A7145642F6C4F436774744F6D55667842566C314B6D7146434146464854786E4B5737387954332F3438386B57373952516B6D41367733416246377361787639500A706843365A634F76514F6836644D42326E4E6C574C67537670312B3948674455635956394D53575A6D2B376C524F5468552B41676433363364355A57574F41470A495659544E5A2F6E746B69705270717251352B7356694863752F4E4F544D757733524C632F575347736D6A594E57616465304C6D2F58685032684D67416D6F350A306474792B36307970437342573269504A6652755152743342644249632B3971637946326176786C457431414446556A49726B305353516F39774A45313953440A68534A414A33782B4D31466C6D4C2B34464832726E69777555615A6F6844506938567542367A634430747732524F664471586A2F5A4B356C7A4A6D745A6B53790A5636704B54485035737558372B6A3848324A35554A496F46487A44484764674E315A724C3570773563305A6D65634B516D5756796F394854614B364641674D420A414147674144414E42676B71686B6947397730424151734641414F4341594541594236615738625549306361466443356939334F376542345530535A414745740A612F546B5133486C764456364F2B327A64735042304F4672385262355171784134776A3843536579466C7A775666497172756A48457831557150706E2B7932720A4C6C397236754C7257725A753568664C767749752B774C6A617644425662354E2B7159536548357643334A582B4652385A64787A6B5754776465464C6F4273340A434749456D46494D2F2B666E79676E6B4C455254566B6738337339534B44736838316772755438302F6135365649636D656E373470584830514641615368766F0A7A52486F7670766A5631735A416265595365796B69564E53497179734166594A33327168744173366E5367796751637A546773756A4F746C63776653432B71550A497A4D623932325A654B5445427A7A3859326351394D6245714850787276664B5A675A4A43306B57342F482B736F2B5659776A684956337048705842527944640A6359637275732B434D335A416A456439585A6C39466D37454555736E346D7459486F497541394167756949425152596B473469726863524E5449377A36755A2B0A77544E724D792B47646B436B5271424256714146374E3473696931356E334E716A3535637257452F642F38316B574E36495943504448586C4F38756B4A6E4B750A30524E6A4B52656551624377596B72464C7A4F5A677342674E6C626E364263470A2D2D2D2D2D454E4420434552544946494341544520524551554553542D2D2D2D2D0A\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"Attributes\",\n"
                        + "          \"type\": \"Structure\",\n"
                        + "          \"value\": [\n"
                        + "            {\n"
                        + "              \"tag\": \"Link\",\n"
                        + "              \"type\": \"Structure\",\n"
                        + "              \"value\": [\n"
                        + "                {\n"
                        + "                  \"tag\": \"Link\",\n"
                        + "                  \"type\": \"Structure\",\n"
                        + "                  \"value\": [\n"
                        + "                    {\n"
                        + "                      \"tag\": \"LinkType\",\n"
                        + "                      \"type\": \"Enumeration\",\n"
                        + "                      \"value\": \"PrivateKeyLink\"\n"
                        + "                    },\n"
                        + "                    {\n"
                        + "                      \"tag\": \"LinkedObjectIdentifier\",\n"
                        + "                      \"type\": \"TextString\",\n"
                        // The issuer private key unique identifier\n"
                        + "                      \"value\": \""
                        + privateKeyUniqueIdentifier
                        + "\"\n"
                        + "                    }\n"
                        + "                  ]\n"
                        + "                }\n"
                        + "              ]\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"ObjectType\",\n"
                        + "              \"type\": \"Enumeration\",\n"
                        + "              \"value\": \"Certificate\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"VendorAttributes\",\n"
                        + "              \"type\": \"Structure\",\n"
                        + "              \"value\": [\n"
                        + "                {\n"
                        + "                  \"tag\": \"VendorAttributes\",\n"
                        + "                  \"type\": \"Structure\",\n"
                        + "                  \"value\": [\n"
                        + "                    {\n"
                        + "                      \"tag\": \"VendorIdentification\",\n"
                        + "                      \"type\": \"TextString\",\n"
                        + "                      \"value\": \"cosmian\"\n"
                        + "                    },\n"
                        + "                    {\n"
                        + "                      \"tag\": \"AttributeName\",\n"
                        + "                      \"type\": \"TextString\",\n"
                        + "                      \"value\": \"requested_validity_days\"\n"
                        + "                    },\n"
                        + "                    {\n"
                        + "                      \"tag\": \"AttributeValue\",\n"
                        + "                      \"type\": \"ByteString\",\n"
                        // 365 as a string in UTF-8 bytes encoded in hex\n"
                        + "                      \"value\": \"333635\"\n"
                        + "                    }\n"
                        + "                  ]\n"
                        + "                },\n"
                        + "                {\n"
                        + "                  \"tag\": \"VendorAttributes\",\n"
                        + "                  \"type\": \"Structure\",\n"
                        + "                  \"value\": [\n"
                        + "                    {\n"
                        + "                      \"tag\": \"VendorIdentification\",\n"
                        + "                      \"type\": \"TextString\",\n"
                        + "                      \"value\": \"cosmian\"\n"
                        + "                    },\n"
                        + "                    {\n"
                        + "                      \"tag\": \"AttributeName\",\n"
                        + "                      \"type\": \"TextString\",\n"
                        + "                      \"value\": \"tag\"\n"
                        + "                    },\n"
                        + "                    {\n"
                        + "                      \"tag\": \"AttributeValue\",\n"
                        + "                      \"type\": \"ByteString\",\n"
                        // [\"MyCert\"] as UTF-8 bytes encoded in hex\n"
                        + "                      \"value\": \"5B224D7943657274225D\"\n"
                        + "                    }\n"
                        + "                  ]\n"
                        + "                }\n"
                        + "              ]\n"
                        + "            }\n"
                        + "          ]\n"
                        + "        }\n"
                        + "      ]\n"
                        + "    }";
        return jsonBody;
    }

    public static String createRequest(
            String privateMasterKeyUniqueIdentifier, String publicMasterKeyUniqueIdentifier) {
        String jsonStr =
                "{\n"
                        + "  \"tag\": \"Certify\",\n"
                        + "  \"type\": \"Structure\",\n"
                        + "  \"value\": [\n"
                        + "    {\n"
                        + "      \"tag\": \"UniqueIdentifier\",\n"
                        + "      \"type\": \"TextString\",\n"
                        // the public key unique identifier
                        + "      \"value\": \""
                        + publicMasterKeyUniqueIdentifier
                        + "\"\n"
                        + "    },\n"
                        + "    {\n"
                        + "      \"tag\": \"Attributes\",\n"
                        + "      \"type\": \"Structure\",\n"
                        + "      \"value\": [\n"
                        + "        {\n"
                        + "          \"tag\": \"CertificateAttributes\",\n"
                        + "          \"type\": \"Structure\",\n"
                        + "          \"value\": [\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectCn\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Common Name of the certificate
                        + "              \"value\": \"[email protected]\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectO\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Organization of the certificate
                        + "              \"value\": \"AcmeTest\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectOu\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Organizational Unit of the certificate
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectEmail\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Email of the certificate
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectC\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Country of the certificate
                        + "              \"value\": \"FR\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectSt\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the State of the certificate
                        + "              \"value\": \"IdF\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectL\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Locality of the certificate
                        + "              \"value\": \"Paris\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectUid\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Unique Identifier of the certificate: empty => assigned by the server
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectSerialNumber\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Serial Number of the certificate
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectTitle\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Title of the certificate
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectDc\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Domain Component of the certificate
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateSubjectDnQualifier\",\n"
                        + "              \"type\": \"TextString\",\n"
                        // the Distinguished Name Qualifier of the certificate
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerCn\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerO\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerOu\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerEmail\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerC\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerSt\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerL\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerUid\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerSerialNumber\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerTitle\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerDc\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CertificateIssuerDnQualifier\",\n"
                        + "              \"type\": \"TextString\",\n"
                        + "              \"value\": \"\"\n"
                        + "            }\n"
                        + "          ]\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"Link\",\n"
                        + "          \"type\": \"Structure\",\n"
                        + "          \"value\": [\n"
                        + "            {\n"
                        + "              \"tag\": \"Link\",\n"
                        + "              \"type\": \"Structure\",\n"
                        + "              \"value\": [\n"
                        + "                {\n"
                        + "                  \"tag\": \"LinkType\",\n"
                        + "                  \"type\": \"Enumeration\",\n"
                        // the unique identifier below is that of the issuer private key
                        + "                  \"value\": \"PrivateKeyLink\"\n"
                        + "                },\n"
                        + "                {\n"
                        + "                  \"tag\": \"LinkedObjectIdentifier\",\n"
                        + "                  \"type\": \"TextString\",\n"
                        // the issuer private key unique identifier\n
                        + "                  \"value\": \""
                        + privateMasterKeyUniqueIdentifier
                        + "\"\n"
                        + "                }\n"
                        + "              ]\n"
                        + "            }\n"
                        + "          ]\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"ObjectType\",\n"
                        + "          \"type\": \"Enumeration\",\n"
                        + "          \"value\": \"Certificate\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"VendorAttributes\",\n"
                        + "          \"type\": \"Structure\",\n"
                        + "          \"value\": [\n"
                        + "            {\n"
                        + "              \"tag\": \"VendorAttributes\",\n"
                        + "              \"type\": \"Structure\",\n"
                        + "              \"value\": [\n"
                        + "                {\n"
                        + "                  \"tag\": \"VendorIdentification\",\n"
                        + "                  \"type\": \"TextString\",\n"
                        + "                  \"value\": \"cosmian\"\n"
                        + "                },\n"
                        + "                {\n"
                        + "                  \"tag\": \"AttributeName\",\n"
                        + "                  \"type\": \"TextString\",\n"
                        + "                  \"value\": \"requested_validity_days\"\n"
                        + "                },\n"
                        + "                {\n"
                        + "                  \"tag\": \"AttributeValue\",\n"
                        + "                  \"type\": \"ByteString\",\n"
                        // 365 as a string in UTF-8 bytes encoded in hex
                        + "                  \"value\": \"333635\"\n"
                        + "                }\n"
                        + "              ]\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"VendorAttributes\",\n"
                        + "              \"type\": \"Structure\",\n"
                        + "              \"value\": [\n"
                        + "                {\n"
                        + "                  \"tag\": \"VendorIdentification\",\n"
                        + "                  \"type\": \"TextString\",\n"
                        + "                  \"value\": \"cosmian\"\n"
                        + "                },\n"
                        + "                {\n"
                        + "                  \"tag\": \"AttributeName\",\n"
                        + "                  \"type\": \"TextString\",\n"
                        + "                  \"value\": \"tag\"\n"
                        + "                },\n"
                        + "                {\n"
                        + "                  \"tag\": \"AttributeValue\",\n"
                        + "                  \"type\": \"ByteString\",\n"
                        // [\"Bob\"] as UTF-8 bytes encoded in hex
                        + "                  \"value\": \"5B22426F62225D\"\n"
                        + "                }\n"
                        + "              ]\n"
                        + "            }\n"
                        + "          ]\n"
                        + "        }\n"
                        + "      ]\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}";
        return jsonStr;
    }

    /** 颁发ca证书 */
    @Test
    public void certifyCreate() {
        String fileName = "mocks/certify.json";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        String jsonStr = TestUtil.readInputStreamAsString(inputStream);
        String responseJson = kmip(jsonStr);
        System.out.println(responseJson);
    }

    @Test
    public void certifyCreate2() throws JsonProcessingException, CloudproofException {

        String extensions =
                "[ v3_ca ]\n"
                        + "subjectKeyIdentifier=hash\n"
                        + "authorityKeyIdentifier=keyid,issuer\n";

        if (!TestUtils.serverAvailable(TestUtils.kmsServerUrl())) {
            throw new RuntimeException("Demo: No KMS Server available");
        }
        // acf9ce46-f33f-4eef-8bfb-83d4dfce1ea0  ca证书ID有效期1天
        // 创建KmsClient
        final KmsClient kmsClient = new KmsClient(TestUtils.kmsServerUrl(), TestUtils.apiKey());
        CertifyResponse resp =
                kmsClient.createCertificateRequest(
                        "CN",
                        "HUNAN",
                        "CHANGSHA",
                        "HD",
                        "CRF",
                        "itchenrenfu@163.com",
                        1,
                        extensions);
        System.out.println(resp.getUniqueIdentifier());
    }

    public static String kmip(String jsonBody) {
        String url = "/kmip/2_1";
        String response = HttpUtil.post(HOSTNAME + url, jsonBody);
        System.out.println(response);
        return response;
    }
}
