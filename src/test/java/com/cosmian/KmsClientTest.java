package com.cosmian;

import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.cosmian.jna.covercrypt.structs.Policy;
import com.cosmian.rest.abe.KmsClient;
import com.cosmian.utils.CloudproofException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenrenfu
 * @date 2024/8/13 11:30
 * @packageName:com.cosmian
 * @className: KmsClientTest
 */
public class KmsClientTest {
    private static final String HOSTNAME = "http://192.168.187.130:9998";
    public static final String UNIQUE_IDENTIFIER = "UniqueIdentifier";
    public static final String DATA = "Data";
    public static final String IV_COUNTER_NONCE = "IvCounterNonce";

    public static final String AUTHENTICATED_ENCRYPTION_TAG = "AuthenticatedEncryptionTag";

    public static void main(String[] args) throws Exception {
        String ca =
                "MIIFwjCCA6qgAwIBAgIBADANBgkqhkiG9w0BAQsFADBWMRYwFAYDVQQDDA1ib2Iy\n"
                        + "QGFjbWUuY29tMQswCQYDVQQGEwJGUjEMMAoGA1UECAwDSWRGMQ4wDAYDVQQHDAVQ\n"
                        + "YXJpczERMA8GA1UECgwIQWNtZVRlc3QwHhcNMjQwODE5MDkwMzE4WhcNMjUwODE5\n"
                        + "MDkwMzE4WjBWMRYwFAYDVQQDDA1ib2IyQGFjbWUuY29tMQswCQYDVQQGEwJGUjEM\n"
                        + "MAoGA1UECAwDSWRGMQ4wDAYDVQQHDAVQYXJpczERMA8GA1UECgwIQWNtZVRlc3Qw\n"
                        + "ggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQDIREBR4UQI3HM6yPp+5KFN\n"
                        + "XENa0V7dCSlo9Lay0Cq4AbOEeJ3NA1SlU25peRV74NM28Dliqfibx3RXOWyiEFwN\n"
                        + "W1YlEaSY+YsX6OJyeC3QET2uHJ1Lci8TUVAJF+YdvJiXAuCAuU8xu9Mwvs5glTVL\n"
                        + "oI+2iY97ZVLAZe5rSw25pYFy8tFK7P0hWXSSjetP9H4R3/MB70Z/yUij27ZqlQq2\n"
                        + "CgY/y5vGW1DIkcRNJ9R4eE1nxTaTbR0BPejQPwcqUclF7OgCYsz72FXkzmu8yfp3\n"
                        + "BjhDoyLPf17uJbcAIrl3Q1WouC37Sx7bkqYnzaNbLoHhlVVMtQhxTsxWDsahrSVB\n"
                        + "gdvFNq8T8eh/VkRu/RHthPpfM8imB+mCH7cBA+AwosUIyhAI9Hfi0Rutp8CuxiCh\n"
                        + "KPVsnFgQo++LdWoxx4Dgv7nk3oZUrr/RqRB/EmNAzYrZSyKncl29HnH9NejrKG2b\n"
                        + "wFxUeTPBYlSdkhcEkQUcFynSE6AMo5lZESrAdy0766EzAwz8rw2jUHunGN6fFH47\n"
                        + "r49BaIvs3tHLcqS6cYhXhE8ZLEfiWLl4WSJJ3Fo5u7WudPv+RUxA+QkiQDZNuJUQ\n"
                        + "kXt41FQVwtpO7gCHq/UrsWFwe1/7Vu8ezk5tx5c/yXJ6pJr74ksFdYT+48v9Ro/T\n"
                        + "/i8me6ddNlhpsgUKfNQaLQIDAQABo4GaMIGXMB0GA1UdDgQWBBSTEpsnB63VHNoS\n"
                        + "WlLM1dRHLe63TTBoBgNVHSMEYTBfoVqkWDBWMRYwFAYDVQQDDA1ib2IyQGFjbWUu\n"
                        + "Y29tMQswCQYDVQQGEwJGUjEMMAoGA1UECAwDSWRGMQ4wDAYDVQQHDAVQYXJpczER\n"
                        + "MA8GA1UECgwIQWNtZVRlc3SCAQAwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsF\n"
                        + "AAOCAgEAE3xyq/yt1I9C15TsjYVVgtkpUfpVrtOYF10v1XA+1C25K8NR2TSTErDn\n"
                        + "gKfpJxl8JTg09IfO3VC4/Renj+u4OeCExhq7nRfr3k8GAtQwvN5CdKOIs2jYM2Z6\n"
                        + "5gLlkSXONtm0YzwLq9OaX7gdpT0M0sqIxFMPscA/I/3sqeX0HSNa0zStrVxtC4Cr\n"
                        + "35KtS+RHE7UhM4OLDG09EXid5wORJcUs97iqE1z1IkNEOn0lhr9ieVKeuAI24vTt\n"
                        + "9CqJk/MqPHsZFa5IAsBYCZH2lHZnsdn7JPR6nUNEFsXgzAnHxpb4R0gu1Ooa78cl\n"
                        + "ez6S6PG3DPwze+Tbz4ikBWzl7LISAptadTwSF5oZWBGFRsYSHCbjNWsClIal1qnc\n"
                        + "lidILA4LgLfmjmK1RaqoKHk5AXan+KyN+Wjx9zuHAfozCyyg44hyVFbXAPJ2GtPc\n"
                        + "nAwJ+JCKrnrBvC8woEZFG7EbGe5D1fWQVv+lk2kTtMSLV2wrENpGvNkl92fpJlov\n"
                        + "5jsD9JIJEKOBwlA/cFt5HxmZMk+8fNO2+yoATQo4V+59ev0V2b7hORKPx20YRs3d\n"
                        + "o6rMgiKExKy/G4HVBVd3Bvx4jSLJnKIx41r7YYrSWnWXBUP+LG3TW1gImqSu0Az+\n"
                        + "XgrB6KqN6QFzldiJgIyB+lKMIE7qb8WbzS8CUxgymbGYrlQOY5w=";

        System.out.println(stringToHex(ca));
        //        kmipTest();
    }

    public static void certifyTest() throws CloudproofException {
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
        Response certify = certify(privateMasterKeyUniqueIdentifier);
        System.out.println(certify);
    }

    public static void kmipTest() {
        Response createResponse = create("testTag");
        List<Response> createValues =
                ((JSONArray) createResponse.getValue()).toJavaList(Response.class);
        Map<String, Response> createValueMap =
                createValues.stream()
                        .collect(
                                Collectors.toMap(
                                        Response::getTag, Function.identity(), (v1, v2) -> v2));
        String keyId = createValueMap.get(UNIQUE_IDENTIFIER).getValue().toString();

        System.out.println("------加密------");
        Response encrypt = encrypt(keyId, "hello");
        Object encryptValue = encrypt.getValue();
        List<Response> responses = ((JSONArray) encryptValue).toJavaList(Response.class);
        Map<String, Response> resultMap =
                responses.stream()
                        .collect(
                                Collectors.toMap(
                                        Response::getTag, Function.identity(), (v1, v2) -> v2));
        System.out.println("加密后的hex内容：" + resultMap.get(DATA).getValue());
        System.out.println("------解密------");
        Response decrypt = decrypt(keyId, resultMap);
        Object decryptValue = decrypt.getValue();
        List<Response> response1 = ((JSONArray) decryptValue).toJavaList(Response.class);
        Map<String, Response> resultMap1 =
                response1.stream()
                        .collect(
                                Collectors.toMap(
                                        Response::getTag, Function.identity(), (v1, v2) -> v2));
        String s = resultMap1.get(DATA).getValue().toString();
        System.out.println("解密后的hex内容：" + s);
        hexToString(s);
        System.out.println("------删除keyId------");
        revoke(keyId);
        destroy(keyId);
    }

    /** 列出用户拥有的对象 */
    public static void listOwned() {
        String url = "/access/owned";
        String body = HttpUtil.get(HOSTNAME + url);
        System.out.println(body);
    }

    public static String kmip(String jsonBody) {
        String url = "/kmip/2_1";
        String response = HttpUtil.post(HOSTNAME + url, jsonBody);
        System.out.println(response);
        return response;
    }

    public static Response create(String tag) {
        System.out.println("创建keyId");
        String jsonBody =
                "    {\n"
                        + "      \"tag\": \"Create\",\n"
                        + "      \"type\": \"Structure\",\n"
                        + "      \"value\": [\n"
                        + "        {\n"
                        + "          \"tag\": \"ObjectType\",\n"
                        + "          \"type\": \"Enumeration\",\n"
                        + "          \"value\": \"SymmetricKey\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"Attributes\",\n"
                        + "          \"type\": \"Structure\",\n"
                        + "          \"value\": [\n"
                        + "            {\n"
                        + "              \"tag\": \"CryptographicAlgorithm\",\n"
                        + "              \"type\": \"Enumeration\",\n"
                        + "              \"value\": \"AES\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CryptographicLength\",\n"
                        + "              \"type\": \"Integer\",\n"
                        + "              \"value\": 256\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"CryptographicUsageMask\",\n"
                        + "              \"type\": \"Integer\",\n"
                        + "              \"value\": 2108\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"KeyFormatType\",\n"
                        + "              \"type\": \"Enumeration\",\n"
                        + "              \"value\": \"TransparentSymmetricKey\"\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"tag\": \"ObjectType\",\n"
                        + "              \"type\": \"Enumeration\",\n"
                        + "              \"value\": \"SymmetricKey\"\n"
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
                        + "                      \"value\": \"tag\"\n"
                        + "                    },\n"
                        + "                    {\n"
                        + "                      \"tag\": \"AttributeValue\",\n"
                        + "                      \"type\": \"ByteString\",\n"
                        + "                      \"value\": \""
                        + stringToHex(tag)
                        + "\"\n"
                        + "                    }\n"
                        + "                  ]\n"
                        + "                }\n"
                        + "              ]\n"
                        + "            }\n"
                        + "          ]\n"
                        + "        }\n"
                        + "      ]\n"
                        + "    }";
        String response = kmip(jsonBody);
        return JSON.parseObject(response, Response.class);
    }

    public static Response encrypt(String keyId, String content) {
        String jsonBody =
                "    {\n"
                        + "      \"tag\": \"Encrypt\",\n"
                        + "      \"type\": \"Structure\",\n"
                        + "      \"value\": [\n"
                        + "        {\n"
                        + "          \"tag\": \"UniqueIdentifier\",\n"
                        + "          \"type\": \"TextString\",\n"
                        + "          \"value\": \""
                        + keyId
                        + "\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"Data\",\n"
                        + "          \"type\": \"ByteString\",\n"
                        + "          \"value\": \""
                        + stringToHex(content)
                        + "\"\n"
                        + "        }\n"
                        + "      ]\n"
                        + "    }";
        System.out.println("加密前的hex内容：" + stringToHex(content));

        String response = kmip(jsonBody);
        return JSON.parseObject(response, Response.class);
    }

    /** 解密 */
    public static Response decrypt(String keyId, Map<String, Response> encryptResponseMap) {
        String jsonBody =
                "    {\n"
                        + "      \"tag\": \"Decrypt\",\n"
                        + "      \"type\": \"Structure\",\n"
                        + "      \"value\": [\n"
                        + "        {\n"
                        + "          \"tag\": \"UniqueIdentifier\",\n"
                        + "          \"type\": \"TextString\",\n"
                        + "          \"value\": \""
                        + encryptResponseMap.get(UNIQUE_IDENTIFIER).getValue()
                        + "\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"Data\",\n"
                        + "          \"type\": \"ByteString\",\n"
                        + "          \"value\": \""
                        + encryptResponseMap.get(DATA).getValue()
                        + "\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"IvCounterNonce\",\n"
                        + "          \"type\": \"ByteString\",\n"
                        + "          \"value\": \""
                        + encryptResponseMap.get(IV_COUNTER_NONCE).getValue()
                        + "\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"AuthenticatedEncryptionTag\",\n"
                        + "          \"type\": \"ByteString\",\n"
                        + "          \"value\": \""
                        + encryptResponseMap.get(AUTHENTICATED_ENCRYPTION_TAG).getValue()
                        + "\"\n"
                        + "        }\n"
                        + "      ]\n"
                        + "    }";
        String response = kmip(jsonBody);
        return JSON.parseObject(response, Response.class);
    }

    public static String hexToString(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        System.out.println(output);
        return output.toString();
    }

    /** 删除keyId */
    public static Response destroy(String keyId) {
        System.out.println("删除keyId：" + keyId);
        String jsonBody =
                "    {\n"
                        + "      \"tag\": \"Destroy\",\n"
                        + "      \"type\": \"Structure\",\n"
                        + "      \"value\": [\n"
                        + "        {\n"
                        + "          \"tag\": \"UniqueIdentifier\",\n"
                        + "          \"type\": \"TextString\",\n"
                        + "          \"value\": \""
                        + keyId
                        + "\"\n"
                        + "        }\n"
                        + "      ]\n"
                        + "    }";
        String response = kmip(jsonBody);
        return JSON.parseObject(response, Response.class);
    }

    public static Response revoke(String keyId) {
        System.out.println("撤销keyId：" + keyId);
        String jsonBody =
                "{\n"
                        + "      \"tag\": \"Revoke\",\n"
                        + "      \"type\": \"Structure\",\n"
                        + "      \"value\": [\n"
                        + "        {\n"
                        + "          \"tag\": \"UniqueIdentifier\",\n"
                        + "          \"type\": \"TextString\",\n"
                        + "          \"value\": \""
                        + keyId
                        + "\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"tag\": \"RevocationReason\",\n"
                        + "          \"type\": \"TextString\",\n"
                        + "          \"value\": \"key was compromised\"\n"
                        + "        }\n"
                        + "      ]\n"
                        + "    }";
        String response = kmip(jsonBody);
        return JSON.parseObject(response, Response.class);
    }

    public static Response certify(String keyId) {
        String jsonBody =
                " {\n"
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
                        + "                      \"value\": \""
                        + keyId
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

        String response = kmip(jsonBody);
        return JSON.parseObject(response, Response.class);
    }

    public static String stringToHex(String str) {
        StringBuilder hex = new StringBuilder();
        for (char c : str.toCharArray()) {
            hex.append(String.format("%02X", (int) c));
        }
        return hex.toString();
    }
}

class Response {
    private String tag;
    private String type;
    private Object value;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
