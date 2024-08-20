package com.cosmian.rest.abe;

import com.alibaba.fastjson2.JSON;
import com.cosmian.jna.covercrypt.structs.AccessPolicy;
import com.cosmian.jna.covercrypt.structs.Policy;
import com.cosmian.rest.abe.data.DataToEncrypt;
import com.cosmian.rest.abe.data.DecryptedData;
import com.cosmian.rest.kmip.Kmip;
import com.cosmian.rest.kmip.data_structures.RekeyAction;
import com.cosmian.rest.kmip.objects.PrivateKey;
import com.cosmian.rest.kmip.objects.PublicKey;
import com.cosmian.rest.kmip.operations.*;
import com.cosmian.rest.kmip.types.*;
import com.cosmian.utils.CloudproofException;
import com.cosmian.utils.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Logger;

/** Attribute Based Encryption endpoints */
public class KmsClient {

    private static final Logger logger = Logger.getLogger(KmsClient.class.getName());

    private final Kmip kmip;

    /**
     * Instantiate a new KmipClient with DEFAULT_CONNECT_TIMEOUT and DEFAULT_READ_TIMEOUT
     *
     * @param server_url the REST Server URL e.g. http://localhost:9000
     * @param api_key he optional API Key to use to authenticate
     */
    public KmsClient(String server_url, Optional<String> api_key) {
        this(new RestClient(server_url, api_key));
    }

    /**
     * Instantiate a new Kmip client using a {@link RestClient}
     *
     * @param rest_client the {@link RestClient}
     */
    public KmsClient(RestClient rest_client) {
        this.kmip = new Kmip(rest_client);
    }

    /**
     * Generate inside the KMS, a master private and public key pair for the {@link Policy}
     *
     * @param policy the Key Policy
     * @return a tuple containing the master private key UID and the master public key UID
     * @throws CloudproofException if the creation fails
     */
    public String[] createCoverCryptMasterKeyPair(Policy policy) throws CloudproofException {
        try {
            Attributes commonAttributes =
                    new Attributes(
                            ObjectType.Private_Key, Optional.of(CryptographicAlgorithm.CoverCrypt));
            commonAttributes.setKeyFormatType(Optional.of(KeyFormatType.CoverCryptSecretKey));

            // convert the Policy to attributes and attach it to the common attributes
            VendorAttribute policy_attribute = policy.toVendorAttribute();

            commonAttributes.setVendorAttributes(
                    Optional.of(new VendorAttribute[] {policy_attribute}));

            CreateKeyPair request =
                    new CreateKeyPair(Optional.of(commonAttributes), Optional.empty());
            logger.info(
                    "CoverCrypt"
                            + ": Master Key generation request: "
                            + JSON.toJSONString(request));
            CreateKeyPairResponse response = this.kmip.createKeyPair(request);
            return new String[] {
                response.getPrivateKeyUniqueIdentifier(), response.getPublicKeyUniqueIdentifier()
            };
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt"
                            + ": Master Key generation failed: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Retrieve the Master Private Key from the KMS
     *
     * @param privateMasterKeyUniqueIdentifier the key UID
     * @param KeyFormatType the Key format type
     * @return the Private Key
     * @throws CloudproofException if the retrieval fails
     */
    public PrivateKey retrievePrivateMasterKey(
            String privateMasterKeyUniqueIdentifier, KeyFormatType KeyFormatType)
            throws CloudproofException {
        try {
            Get request = new Get(privateMasterKeyUniqueIdentifier);
            request.setKeyFormatType(Optional.of(KeyFormatType));
            //
            GetResponse response = this.kmip.get(request);
            Object object = response.getObject();
            if (!(object instanceof PrivateKey)) {
                throw new CloudproofException(
                        "No "
                                + "CoverCrypt"
                                + " Private Master Key at identifier "
                                + privateMasterKeyUniqueIdentifier);
            }
            PrivateKey sk = (PrivateKey) object;
            return sk;
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt"
                            + ": Private Master Key could not be retrieved: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Retrieve the Master public Key from the KMS
     *
     * @param publicMasterKeyUniqueIdentifier the key UID
     * @param KeyFormatType the Key format type
     * @return the public Key
     * @throws CloudproofException if the retrieval fails
     */
    public PublicKey retrievePublicMasterKey(
            String publicMasterKeyUniqueIdentifier, KeyFormatType KeyFormatType)
            throws CloudproofException {
        try {
            Get request = new Get(publicMasterKeyUniqueIdentifier);
            request.setKeyFormatType(Optional.of(KeyFormatType));
            //
            GetResponse response = this.kmip.get(request);
            Object object = response.getObject();
            if (!(object instanceof PublicKey)) {
                throw new CloudproofException(
                        "No "
                                + " PublicKey Master Key at identifier "
                                + publicMasterKeyUniqueIdentifier);
            }
            PublicKey sk = (PublicKey) object;
            return sk;
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    ": PublicKey Master Key could not be retrieved: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * @param subjectCountry 国家代码
     * @param subjectState 州/省
     * @param subjectLocality 城市
     * @param subjectOrganization 组织机构代码
     * @param subjectCommonName 通用名
     * @param subjectEmail 邮件
     * @param numberOfDay 有效期天数
     * @param certificateExtensions 扩展
     */
    public CertifyResponse createCertificateRequest(
            String subjectCountry,
            String subjectState,
            String subjectLocality,
            String subjectOrganization,
            String subjectCommonName,
            String subjectEmail,
            Integer numberOfDay,
            String certificateExtensions)
            throws CloudproofException {
        Attributes commonAttributes =
                new Attributes(ObjectType.Private_Key, Optional.of(CryptographicAlgorithm.RSA));
        commonAttributes.setKeyFormatType(Optional.of(KeyFormatType.TransparentRSAPrivateKey));
        commonAttributes.setCryptographicLength(Optional.of(4096));

        CertificateAttributes certificateAttributes = new CertificateAttributes();
        certificateAttributes.setCertificateSubjectC(subjectCountry);
        certificateAttributes.setCertificateSubjectSt(subjectState);
        certificateAttributes.setCertificateSubjectL(subjectLocality);
        certificateAttributes.setCertificateSubjectO(subjectOrganization);
        certificateAttributes.setCertificateSubjectCn(subjectCommonName);
        certificateAttributes.setCertificateSubjectEmail(subjectEmail);

        commonAttributes.setCertificateAttributes(Optional.of(certificateAttributes));

        VendorAttribute vendorAttribute =
                new VendorAttribute(
                        "cosmian",
                        "requested_validity_days",
                        String.valueOf(numberOfDay).getBytes(StandardCharsets.UTF_8));

        VendorAttribute vendorAttribute2 =
                new VendorAttribute("cosmian", "tag", "[]".getBytes(StandardCharsets.UTF_8));
        VendorAttribute vendorAttribute3 =
                new VendorAttribute(
                        "cosmian",
                        "x509-extension",
                        certificateExtensions.getBytes(StandardCharsets.UTF_8));

        commonAttributes.setVendorAttributes(
                Optional.of(
                        new VendorAttribute[] {
                            vendorAttribute, vendorAttribute2, vendorAttribute3
                        }));

        Certify request = new Certify(commonAttributes);
        return this.kmip.certify(request);
    }

    /**
     * Retrieve the Master Private Key from the KMS
     *
     * @param privateMasterKeyUniqueIdentifier the key UID
     * @return the Private Key
     * @throws CloudproofException if the retrieval fails
     */
    public PrivateKey retrieveCoverCryptPrivateMasterKey(String privateMasterKeyUniqueIdentifier)
            throws CloudproofException {
        try {
            Get request = new Get(privateMasterKeyUniqueIdentifier);
            request.setKeyFormatType(Optional.of(KeyFormatType.CoverCryptSecretKey));
            //
            GetResponse response = this.kmip.get(request);
            Object object = response.getObject();
            if (!(object instanceof PrivateKey)) {
                throw new CloudproofException(
                        "No "
                                + "CoverCrypt"
                                + " Private Master Key at identifier "
                                + privateMasterKeyUniqueIdentifier);
            }
            PrivateKey sk = (PrivateKey) object;
            if (!sk.getKeyBlock().getKeyFormatType().equals(KeyFormatType.CoverCryptSecretKey)) {
                throw new CloudproofException(
                        "No "
                                + "CoverCrypt"
                                + " Private Master Key at identifier "
                                + privateMasterKeyUniqueIdentifier);
            }
            return sk;
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt"
                            + ": Private Master Key could not be retrieved: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Import a Private Master Key in the KMS
     *
     * @param uniqueIdentifier the UID of the key
     * @param privateMasterKey the key
     * @param replaceExisting if a key exists under this UID, replace it
     * @return the UID of the imported key
     * @throws CloudproofException if the import fails
     */
    public String importCoverCryptPrivateMasterKey(
            String uniqueIdentifier, PrivateKey privateMasterKey, boolean replaceExisting)
            throws CloudproofException {
        try {
            Import request =
                    new Import(
                            uniqueIdentifier,
                            ObjectType.Private_Key,
                            Optional.of(replaceExisting),
                            Optional.empty(),
                            privateMasterKey.attributes(),
                            privateMasterKey);
            ImportResponse response = this.kmip.importObject(request);
            return response.getUniqueIdentifier();
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt"
                            + ": Private Master Key could not be imported: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Retrieve the Master Public Key from the KMS
     *
     * @param publicMasterKeyUniqueIdentifier the key UID
     * @return the Public Key
     * @throws CloudproofException if the retrieval fails
     */
    public PublicKey retrieveCoverCryptPublicMasterKey(String publicMasterKeyUniqueIdentifier)
            throws CloudproofException {
        try {
            Get request = new Get(publicMasterKeyUniqueIdentifier);
            request.setKeyFormatType(Optional.of(KeyFormatType.CoverCryptPublicKey));
            //
            GetResponse response = this.kmip.get(request);
            Object object = response.getObject();
            if (!(object instanceof PublicKey)) {
                throw new CloudproofException(
                        "No "
                                + "CoverCrypt"
                                + " Public Master Key at identifier "
                                + publicMasterKeyUniqueIdentifier);
            }
            PublicKey sk = (PublicKey) object;
            if (!sk.getKeyBlock().getKeyFormatType().equals(KeyFormatType.CoverCryptPublicKey)) {
                throw new CloudproofException(
                        "No "
                                + "CoverCrypt"
                                + " Public Master Key at identifier "
                                + publicMasterKeyUniqueIdentifier);
            }
            return sk;
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt"
                            + ": Public Master Key could not be retrieved: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Import a Public Master Key in the KMS
     *
     * @param uniqueIdentifier the UID of the key
     * @param publicMasterKey the key
     * @param replaceExisting if a key exists under this UID, replace it
     * @return the UID of the imported key
     * @throws CloudproofException if the import fails
     */
    public String importCoverCryptPublicMasterKey(
            String uniqueIdentifier, PublicKey publicMasterKey, boolean replaceExisting)
            throws CloudproofException {
        try {
            Import request =
                    new Import(
                            uniqueIdentifier,
                            ObjectType.Public_Key,
                            Optional.of(replaceExisting),
                            Optional.empty(),
                            publicMasterKey.attributes(),
                            publicMasterKey);
            ImportResponse response = this.kmip.importObject(request);
            return response.getUniqueIdentifier();
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt"
                            + ": Public Master Key could not be imported: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Create a User Decryption Key for the given {@link AccessPolicy} expressed as a boolean
     * expression
     *
     * @param accessPolicy the {@link AccessPolicy} as a string
     * @param privateMasterKeyUniqueIdentifier the UID of the Master Private Key
     * @return the UID of the newly created key
     * @throws CloudproofException if the creation fails
     */
    public String createCoverCryptUserDecryptionKey(
            String accessPolicy, String privateMasterKeyUniqueIdentifier)
            throws CloudproofException {

        // not at the class level, so the rest of the methods can be used without a
        // native library
        VendorAttribute accessPolicyAttribute =
                new VendorAttribute(
                        VendorAttribute.VENDOR_ID_COSMIAN,
                        VendorAttribute.VENDOR_ATTR_COVER_CRYPT_ACCESS_POLICY,
                        accessPolicy.getBytes(StandardCharsets.UTF_8));

        return createCoverCryptUserDecryptionKey(
                accessPolicyAttribute, privateMasterKeyUniqueIdentifier);
    }

    /**
     * Create a User Decryption Key for the given {@link AccessPolicy} in the KMS
     *
     * @param accessPolicy the {@link AccessPolicy}
     * @param privateMasterKeyUniqueIdentifier the UID of the Master Private Key
     * @return the UID of the newly created key
     * @throws CloudproofException if the creation fails
     */
    public String createCoverCryptUserDecryptionKey(
            AccessPolicy accessPolicy, String privateMasterKeyUniqueIdentifier)
            throws CloudproofException {
        // convert the Access Policy to attributes and attach it to the common
        // attributes
        VendorAttribute accessPolicyAttribute = accessPolicy.toVendorAttribute();

        return createCoverCryptUserDecryptionKey(
                accessPolicyAttribute, privateMasterKeyUniqueIdentifier);
    }

    /**
     * Create a User Decryption Key for the given {@link AccessPolicy} in the KMS
     *
     * @param accessPolicyAttribute the {@link AccessPolicy} as a {@link VendorAttribute}
     * @param privateMasterKeyUniqueIdentifier the UID of the Master Private Key
     * @return the UID of the newly created key
     * @throws CloudproofException if the creation fails
     */
    String createCoverCryptUserDecryptionKey(
            VendorAttribute accessPolicyAttribute, String privateMasterKeyUniqueIdentifier)
            throws CloudproofException {
        try {
            Attributes commonAttributes =
                    new Attributes(
                            ObjectType.Private_Key, Optional.of(CryptographicAlgorithm.CoverCrypt));
            commonAttributes.setKeyFormatType(Optional.of(KeyFormatType.CoverCryptSecretKey));

            // convert the Access Policy to attributes and attach it to the common
            // attributes
            commonAttributes.setVendorAttributes(
                    Optional.of(new VendorAttribute[] {accessPolicyAttribute}));
            // link to the master private key
            commonAttributes.setLink(
                    Optional.of(
                            new Link[] {
                                new Link(
                                        LinkType.Parent_Link,
                                        new LinkedObjectIdentifier(
                                                privateMasterKeyUniqueIdentifier))
                            }));

            Create request = new Create(ObjectType.Private_Key, commonAttributes, Optional.empty());
            CreateResponse response = this.kmip.create(request);
            return response.getUniqueIdentifier();
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt"
                            + ": Master Key generation failed: "
                            + e.getMessage()
                            + " "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Retrieve a User Decryption Key from the KMS
     *
     * @param userDecryptionKeyUniqueIdentifier the key UID
     * @return the User Decryption Key
     * @throws CloudproofException if the retrieval fails
     */
    public PrivateKey retrieveCoverCryptUserDecryptionKey(String userDecryptionKeyUniqueIdentifier)
            throws CloudproofException {
        try {
            Get request = new Get(userDecryptionKeyUniqueIdentifier);
            request.setKeyFormatType(Optional.of(KeyFormatType.CoverCryptSecretKey));
            //
            GetResponse response = this.kmip.get(request);
            Object object = response.getObject();
            if (!(object instanceof PrivateKey)) {
                throw new CloudproofException(
                        "No "
                                + "CoverCrypt"
                                + " User Decryption Key at identifier "
                                + userDecryptionKeyUniqueIdentifier);
            }
            PrivateKey sk = (PrivateKey) object;
            if (!sk.getKeyBlock().getKeyFormatType().equals(KeyFormatType.CoverCryptSecretKey)) {
                throw new CloudproofException(
                        "No "
                                + "CoverCrypt"
                                + " User Decryption Key at identifier "
                                + userDecryptionKeyUniqueIdentifier);
            }
            return sk;
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt"
                            + ": User Decryption Key could not be retrieved: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Import a User Decryption Key in the KMS
     *
     * @param uniqueIdentifier the UID of the key
     * @param userDecryptionKey the key
     * @param replaceExisting if a key exists under this UID, replace it
     * @return the UID of the imported key
     * @throws CloudproofException if the import fails
     */
    public String importCoverCryptUserDecryptionKey(
            String uniqueIdentifier, PrivateKey userDecryptionKey, boolean replaceExisting)
            throws CloudproofException {
        try {
            Import request =
                    new Import(
                            uniqueIdentifier,
                            ObjectType.Private_Key,
                            Optional.of(replaceExisting),
                            Optional.empty(),
                            userDecryptionKey.attributes(),
                            userDecryptionKey);
            ImportResponse response = this.kmip.importObject(request);
            return response.getUniqueIdentifier();
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt"
                            + ": User Decryption Key could not be imported: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Encrypt data in the KMS using the given encryption policy and Public Master Key. The
     * generated cipher text is made of 2 parts: a header containing the encapsulation of the
     * ephemeral symmetric key and the symmetrically encrypted content under that key.
     *
     * @param publicMasterKeyUniqueIdentifier the UID of the Public Key
     * @param plaintext the data to encrypt
     * @param encryptionPolicy the encryption policy as a boolean expression
     * @return the encrypted data
     * @throws CloudproofException if the encryption fails
     */
    public byte[] coverCryptEncrypt(
            String publicMasterKeyUniqueIdentifier, byte[] plaintext, String encryptionPolicy)
            throws CloudproofException {
        return coverCryptEncrypt(
                publicMasterKeyUniqueIdentifier,
                plaintext,
                encryptionPolicy,
                Optional.empty(),
                Optional.empty());
    }

    /**
     * Encrypt data in the KMS using the given encryption policy and Public Master Key. The
     * generated cipher text is made of 2 parts: a header containing the encapsulation of the
     * ephemeral symmetric key and the symmetrically encrypted content under that key.
     *
     * @param publicMasterKeyUniqueIdentifier the UID of the Public Key
     * @param plaintext the data to encrypt
     * @param encryptionPolicy the encryption policy as a boolean expression
     * @param authenticationData the authentication data used in the AEAD of the symmetric scheme
     * @return the encrypted data
     * @throws CloudproofException if the encryption fails
     */
    public byte[] coverCryptEncrypt(
            String publicMasterKeyUniqueIdentifier,
            byte[] plaintext,
            String encryptionPolicy,
            byte[] authenticationData)
            throws CloudproofException {
        return coverCryptEncrypt(
                publicMasterKeyUniqueIdentifier,
                plaintext,
                encryptionPolicy,
                Optional.of(authenticationData),
                Optional.empty());
    }

    /**
     * Encrypt data in the KMS using the given encryption policy and Public Master Key. The
     * generated cipher text is made of 2 parts: a header containing the encapsulation of the
     * ephemeral symmetric key and the symmetrically encrypted content under that key.
     *
     * @param publicMasterKeyUniqueIdentifier the UID of the Public Key
     * @param plaintext the data to encrypt
     * @param encryptionPolicy the encryption policy as a boolean expression
     * @param authenticationData the authentication data used in the AEAD of the symmetric scheme
     * @param headerMetaData Metadata to encrypt within the header
     * @return the encrypted data
     * @throws CloudproofException if the encryption fails
     */
    public byte[] coverCryptEncrypt(
            String publicMasterKeyUniqueIdentifier,
            byte[] plaintext,
            String encryptionPolicy,
            byte[] authenticationData,
            byte[] headerMetaData)
            throws CloudproofException {

        return coverCryptEncrypt(
                publicMasterKeyUniqueIdentifier,
                plaintext,
                encryptionPolicy,
                Optional.of(authenticationData),
                Optional.of(headerMetaData));
    }

    /**
     * Encrypt data in the KMS using the given encryption policy and Public Master Key. The
     * generated cipher text is made of 2 parts: a header containing the encapsulation of the
     * ephemeral symmetric key and the symmetrically encrypted content under that key.
     *
     * @param publicMasterKeyUniqueIdentifier the UID of the Public Key
     * @param plaintext the data to encrypt
     * @param encryptionPolicy the encryption policy as a boolean expression
     * @param authenticationData the authentication data used in the AEAD of the symmetric scheme
     * @param headerMetaData Optional metadata to encrypt within the header
     * @return the encrypted data
     * @throws CloudproofException if the encryption fails
     */
    byte[] coverCryptEncrypt(
            String publicMasterKeyUniqueIdentifier,
            byte[] plaintext,
            String encryptionPolicy,
            Optional<byte[]> authenticationData,
            Optional<byte[]> headerMetaData)
            throws CloudproofException {
        try {
            DataToEncrypt dataToEncrypt =
                    new DataToEncrypt(encryptionPolicy, plaintext, headerMetaData);
            Encrypt request =
                    new Encrypt(
                            publicMasterKeyUniqueIdentifier,
                            dataToEncrypt.toBytes(),
                            Optional.empty(),
                            authenticationData.isPresent()
                                    ? Optional.of(authenticationData.get())
                                    : Optional.empty());
            EncryptResponse response = this.kmip.encrypt(request);
            if (response.getData().isPresent()) {
                return response.getData().get();
            }
            throw new CloudproofException("No encrypted data in response !");
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt" + " encryption failed: " + e.getMessage() + "  " + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Decrypt the data in the KMS using the given User Decryption Key The encryptedData should be
     * made of 3 parts: - the length of the encrypted header as a u32 in big endian format (4 bytes)
     * - the header - the AES GCM encrypted content
     *
     * @param userDecryptionKeyUniqueIdentifier the key UID
     * @param encryptedData the cipher text
     * @return the clear text data
     * @throws CloudproofException if the decryption fails
     */
    public DecryptedData coverCryptDecrypt(
            String userDecryptionKeyUniqueIdentifier, byte[] encryptedData)
            throws CloudproofException {
        return this.coverCryptDecrypt(
                userDecryptionKeyUniqueIdentifier, encryptedData, Optional.empty());
    }

    /**
     * Decrypt the data in the KMS using the given User Decryption Key The encryptedData should be
     * made of 3 parts: - the length of the encrypted header as a u32 in big endian format (4 bytes)
     * - the header - the AES GCM encrypted content
     *
     * @param userDecryptionKeyUniqueIdentifier the key UID
     * @param encryptedData the cipher text
     * @param authenticationData the data to use in the authentication of the symmetric scheme
     * @return the clear text data
     * @throws CloudproofException if the decryption fails
     */
    public DecryptedData coverCryptDecrypt(
            String userDecryptionKeyUniqueIdentifier,
            byte[] encryptedData,
            byte[] authenticationData)
            throws CloudproofException {
        return coverCryptDecrypt(
                userDecryptionKeyUniqueIdentifier, encryptedData, Optional.of(authenticationData));
    }

    /**
     * Decrypt the data in the KMS using the given User Decryption Key The encryptedData should be
     * made of 3 parts: - the length of the encrypted header as a u32 in big endian format (4 bytes)
     * - the header - the AES GCM encrypted content
     *
     * @param userDecryptionKeyUniqueIdentifier the key UID
     * @param encryptedData the cipher text
     * @param authenticationData the data to use in the authentication of the symmetric scheme
     * @return the clear text data
     * @throws CloudproofException if the decryption fails
     */
    DecryptedData coverCryptDecrypt(
            String userDecryptionKeyUniqueIdentifier,
            byte[] encryptedData,
            Optional<byte[]> authenticationData)
            throws CloudproofException {
        try {
            Decrypt request =
                    new Decrypt(
                            userDecryptionKeyUniqueIdentifier, encryptedData, authenticationData);
            System.out.println(request);
            DecryptResponse response = this.kmip.decrypt(request);
            if (response.getData().isPresent()) {
                return DecryptedData.fromBytes(response.getData().get());
            }
            throw new CloudproofException("No decrypted data in response !");
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt" + " decryption failed: " + e.getMessage() + "  " + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    private String processCoverCryptRekeyRequest(
            String privateMasterKeyUniqueIdentifier, RekeyAction action)
            throws CloudproofException {
        Attributes attributes =
                new Attributes(
                        ObjectType.Private_Key, Optional.of(CryptographicAlgorithm.CoverCrypt));
        attributes.keyFormatType(Optional.of(KeyFormatType.CoverCryptSecretKey));
        attributes.vendorAttributes(
                Optional.of(new VendorAttribute[] {action.toVendorAttribute()}));
        ReKeyKeyPair request =
                new ReKeyKeyPair(
                        Optional.of(privateMasterKeyUniqueIdentifier),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(attributes),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty());
        ReKeyKeyPairResponse response = this.kmip.reKeyKeyPair(request);
        return response.getPublicKeyUniqueIdentifier();
    }

    /**
     * Rekey the given access policy. This will rekey in the KMS:
     *
     * <ul>
     *   <li>the Master Keys
     *   <li>any User Key associated to the access policy
     * </ul>
     *
     * Non Rekeyed User Decryption Keys cannot decrypt data encrypted with the rekeyed Master Public
     * Key and the given attributes. <br>
     * Rekeyed User Decryption Keys however will be able to decrypt data encrypted by the previous
     * Master Public Key and the rekeyed one. <br>
     *
     * @param privateMasterKeyUniqueIdentifier the UID of the private master key
     * @param accessPolicy the access policy to rekey
     * @return the Master Public Key UID
     * @throws CloudproofException if the revocation fails
     */
    public String rekeyCoverCryptAccessPolicy(
            String privateMasterKeyUniqueIdentifier, String accessPolicy)
            throws CloudproofException {
        try {
            return processCoverCryptRekeyRequest(
                    privateMasterKeyUniqueIdentifier,
                    new RekeyAction().rekeyAccessPolicy(accessPolicy));
        } catch (Exception e) {
            String err =
                    "Rekeying of CoverCrypt access policy failed: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Prune the given access policy. This will rekey in the KMS:
     *
     * <ul>
     *   <li>the Master Keys
     *   <li>any User Key associated to the access policy
     * </ul>
     *
     * This operation will permanently remove access to old ciphers for the pruned access policy.
     * <br>
     *
     * @param privateMasterKeyUniqueIdentifier the UID of the private master key
     * @param accessPolicy the access policy to prune
     * @return the Master Public Key UID
     * @throws CloudproofException if the revocation fails
     */
    public String pruneCoverCryptAccessPolicy(
            String privateMasterKeyUniqueIdentifier, String accessPolicy)
            throws CloudproofException {
        try {
            return processCoverCryptRekeyRequest(
                    privateMasterKeyUniqueIdentifier,
                    new RekeyAction().pruneAccessPolicy(accessPolicy));
        } catch (Exception e) {
            String err =
                    "Pruning of CoverCrypt access policy failed: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Remove an attribute from a keypair's policy. Permanently removes the ability to encrypt new
     * messages and decrypt all existing ciphers associated with this attribute. This will rekey in
     * the KMS:
     *
     * <ul>
     *   <li>the Master Keys
     *   <li>any User Key associated to the attribute
     * </ul>
     *
     * @param privateMasterKeyUniqueIdentifier the UID of the private master key
     * @param attribute to remove e.g. "Department::HR"
     * @return the Master Public Key UID
     * @throws CloudproofException if the revocation fails
     */
    public String removeCoverCryptAttribute(
            String privateMasterKeyUniqueIdentifier, String attribute) throws CloudproofException {
        try {
            return processCoverCryptRekeyRequest(
                    privateMasterKeyUniqueIdentifier, new RekeyAction().removeAttribute(attribute));
        } catch (Exception e) {
            String err =
                    "Pruning of CoverCrypt access policy failed: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Disable an attribute from a keypair's policy. Prevents the encryption of new messages for
     * this attribute while keeping the ability to decrypt existing ciphers. This will rekey in the
     * KMS:
     *
     * <ul>
     *   <li>the Master Public Key
     * </ul>
     *
     * @param privateMasterKeyUniqueIdentifier the UID of the private master key
     * @param attribute to disable e.g. "Department::HR"
     * @return the Master Public Key UID
     * @throws CloudproofException if the revocation fails
     */
    public String disableCoverCryptAttribute(
            String privateMasterKeyUniqueIdentifier, String attribute) throws CloudproofException {
        try {
            return processCoverCryptRekeyRequest(
                    privateMasterKeyUniqueIdentifier,
                    new RekeyAction().disableAttribute(attribute));
        } catch (Exception e) {
            String err =
                    "Pruning of CoverCrypt access policy failed: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Add a new attribute to a keypair's policy. This will rekey in the KMS:
     *
     * <ul>
     *   <li>the Master Keys
     * </ul>
     *
     * @param privateMasterKeyUniqueIdentifier the UID of the private master key
     * @param attribute to add e.g. "Department::HR"
     * @param isHybridized hint for encryption
     * @return the Master Public Key UID
     * @throws CloudproofException if the revocation fails
     */
    public String addCoverCryptAttribute(
            String privateMasterKeyUniqueIdentifier, String attribute, boolean isHybridized)
            throws CloudproofException {
        try {
            return processCoverCryptRekeyRequest(
                    privateMasterKeyUniqueIdentifier,
                    new RekeyAction().addAttribute(attribute, isHybridized));
        } catch (Exception e) {
            String err =
                    "Pruning of CoverCrypt access policy failed: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Rename an attribute in a keypair's policy.
     *
     * @param privateMasterKeyUniqueIdentifier the UID of the private master key
     * @param attribute to rename e.g. "Department::HR"
     * @param newName the new name for the attribute
     * @return the Master Public Key UID
     * @throws CloudproofException if the revocation fails
     */
    public String renameCoverCryptAttribute(
            String privateMasterKeyUniqueIdentifier, String attribute, String newName)
            throws CloudproofException {
        try {
            return processCoverCryptRekeyRequest(
                    privateMasterKeyUniqueIdentifier,
                    new RekeyAction().renameAttribute(attribute, newName));
        } catch (Exception e) {
            String err =
                    "Pruning of CoverCrypt access policy failed: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Revoke a key in the KMS which makes it unavailable to use in the KMS to perform {@link
     * #coverCryptEncrypt(String, byte[], String)} or {@link #coverCryptDecrypt(String, byte[],
     * Optional)} operations. <br>
     * <br>
     * If this key is a User Decryption Key, it will not be rekeyed in case of attribute revocation.
     * <br>
     * <br>
     * Note: this revokes the key **inside** the KMS: it does not prevent an user who has a local
     * copy of a User Decryption Key to perform decryption operations.
     *
     * @param keyUniqueIdentifier the UID of the key to revoke
     * @return the UID of the revoked key
     * @throws CloudproofException if the revocation fails
     */
    public String revokeKey(String keyUniqueIdentifier) throws CloudproofException {
        try {
            Revoke request =
                    new Revoke(
                            Optional.of(keyUniqueIdentifier),
                            new RevocationReason("Revoked"),
                            Optional.empty());
            RevokeResponse response = this.kmip.revoke(request);
            return response.getUniqueIdentifier();
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt"
                            + " key revocation failed: "
                            + e.getMessage()
                            + "  "
                            + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }

    /**
     * Destroy a key in the KMS which makes it unavailable to use in the KMS to perform {@link
     * #coverCryptEncrypt(String, byte[], String)} or {@link #coverCryptDecrypt(String, byte[],
     * Optional)} operations. <br>
     * <br>
     * Note: this destroy the key **inside** the KMS: it does not prevent an user who has a local
     * copy of a User Decryption Key to perform decryption operations.
     *
     * @param uniqueIdentifier the UID of the key to revoke
     * @return the UID of the destroyed key
     * @throws CloudproofException if the destruction fails
     */
    public String destroyKey(String uniqueIdentifier) throws CloudproofException {
        try {
            Destroy request = new Destroy(Optional.of(uniqueIdentifier));
            DestroyResponse response = kmip.destroy(request);
            return response.getUniqueIdentifier();
        } catch (CloudproofException e) {
            throw e;
        } catch (Exception e) {
            String err =
                    "CoverCrypt" + " destroy key failed: " + e.getMessage() + "  " + e.getClass();
            logger.severe(err);
            throw new CloudproofException(err, e);
        }
    }
}
