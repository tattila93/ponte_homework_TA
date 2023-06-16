package hu.ponte.hr.services;

import hu.ponte.hr.exceptions.IncorrectAlgorithmException;
import hu.ponte.hr.exceptions.IncorrectFileException;
import hu.ponte.hr.exceptions.IncorrectKeyException;
import hu.ponte.hr.exceptions.IncorrectSignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * loadPrivateKey() is for
 * read the file of the given path
 * it creates a byte array from it
 * with the given byte array it creates a new encoded key with "PKCS#8" standard *
 * then it gets an object("KeyFactory") that converts the keys of the specified algorithm ("RSA")
 * and it returns with "privateKey" which is found in PKCS8 format and converted with RSA algorithm
 *
 *
 *
 * createSignatureInstance()
 * creates a signature instance with SHA256withRSA algorithm
 *
 * updateToBeSigned()
 * Updates the data to be signed
 *
 * signData()
 * Signs the data
 *
 * encodeToBase64()
 * Encode the signed data with BASE64
 */


@Service
@Transactional
public class SignService {

    private static final Logger logger = LoggerFactory.getLogger(SignService.class);

    private static final String PRIVATE_KEY_PATH = "src/main/resources/config/keys/key.private";

    private PrivateKey privateKey;

    private Signature signature;


    /**
     * @param picture (MultipartFile)
     * @return a String which is created with SHA256withRSA algorithm and encoded with BASE64
     * @throws IncorrectKeyException if the given key is invalid
     * @throws IncorrectAlgorithmException if the given algorithm is invalid
     * @throws IncorrectSignatureException if the signature cannot be updated
     * @throws IncorrectFileException if the program cannot find the file in the given Path
     */
    public String signAndEncodeToBase64(MultipartFile picture) throws IncorrectKeyException, IncorrectAlgorithmException, IncorrectSignatureException, IncorrectFileException {
        this.privateKey = loadPrivateKey();
        createSignatureInstance();
        updateToBeSigned(picture);
        byte[] signedData = signData();
        return Base64.getEncoder().encodeToString(signedData);
    }


    /**
     * Creates a signature instance with SHA256withRSA algorithm.
     * @throws IncorrectAlgorithmException if Signature.getInstance() gets incorrect parameter
     * @throws IncorrectKeyException if signature.initSign() gets incorrect parameter
     */
    private void createSignatureInstance() throws IncorrectAlgorithmException, IncorrectKeyException {
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            logger.info("Signature instance is created, with SHA256withRSA algorithm.");
        } catch (NoSuchAlgorithmException e) {
            throw new IncorrectAlgorithmException("Problem appeared in the method createSignatureInstance(): The given algorithm is incorrect.");
        } catch (InvalidKeyException e) {
            throw new IncorrectKeyException("Problem appeared in the method createSignatureInstance(): Given key is invalid.");
        }
    }


    /**
     * Updates the data to be signed.
     * @param picture (MultipartFile)
     * @throws IncorrectFileException if file not found.
     * @throws IncorrectSignatureException if signature cannot be updated.
     */
    private void updateToBeSigned(MultipartFile picture) throws IncorrectFileException, IncorrectSignatureException {
        try {
            signature.update(picture.getBytes());
            logger.info("Data is updated to be signed.");
        } catch (IOException e) {
            throw new IncorrectFileException("Problem appeared in the method updateToBeSigned(): File not found");
        } catch (SignatureException e) {
            throw new IncorrectSignatureException("Problem appeared in the method updateToBeSigned(): Signature cannot be updated.");
        }
    }


    /**
     * Signs the data.
     * @return byte[] which is the signed data
     * @throws IncorrectSignatureException if signature cannot be signed
     */
    private byte[] signData() throws IncorrectSignatureException {
        byte[] signedData;
        try {
            signedData = signature.sign();
            logger.info("Data signed.");
        } catch (SignatureException e) {
            throw new IncorrectSignatureException("Problem appeared in the method signData(): Signature cannot be signed.");
        }
        return signedData;
    }


    /**
     *  loadPrivateKey() is for:
     *  read the file of the given path,
     *  it creates a byte array from it.
     *  With the given byte array it creates a new encoded "PKCS8EncodedKeySpec" with "PKCS#8" standard.
     *  Then it gets an object("KeyFactory") that converts the keys of the specified algorithm ("RSA").
     *  It returns with "privateKey" which is found in PKCS8 format and converted with RSA algorithm.
     * @return PrivateKey, which is found in PKCS8 format and converted with RSA algorithm
     * @throws IncorrectFileException if the file not found on the given Path
     * @throws IncorrectAlgorithmException if the given algorithm is incorrect
     * @throws IncorrectKeyException if the given key is invalid
     */
    private PrivateKey loadPrivateKey() throws IncorrectFileException, IncorrectAlgorithmException, IncorrectKeyException {
        try (FileInputStream inputStream = new FileInputStream(PRIVATE_KEY_PATH)) {
            byte[] keyBytes = inputStream.readAllBytes();
            EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(encodedKeySpec);
        } catch (IOException e) {
            logger.warn("File not found.");
            throw new IncorrectFileException("A problem appeared with the file from the given path: " + PRIVATE_KEY_PATH);
        } catch (NoSuchAlgorithmException e) {
            logger.warn("The given algorithm is incorrect.");
            throw new IncorrectAlgorithmException("A problem appeared with loadPrivateKey() method: KeyFactory.getInstance() incorrect algorithm given");
        } catch (InvalidKeySpecException e) {
            logger.warn("Given key is invalid.");
            throw new IncorrectKeyException("Private key cannot be generated");
        }
    }

}
