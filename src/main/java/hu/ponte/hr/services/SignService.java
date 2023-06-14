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




@Service
@Transactional
public class SignService {

    private static final Logger logger = LoggerFactory.getLogger(SignService.class);

    private static final String PRIVATE_KEY_PATH = "src/main/resources/config/keys/key.private";

    private PrivateKey privateKey;

    private Signature signature;


    /**
     * @param picture
     * @return
     * @throws IncorrectKeyException
     * @throws IncorrectAlgorithmException
     * @throws IncorrectSignatureException
     * @throws IncorrectFileException
     */
    public String signAndEncodeToBase64(MultipartFile picture) throws IncorrectKeyException, IncorrectAlgorithmException, IncorrectSignatureException, IncorrectFileException {
        this.privateKey = loadPrivateKey();
        createSignatureInstance();
        updateToBeSigned(picture);
        byte[] signedData = signData();
        return Base64.getEncoder().encodeToString(signedData);
    }

    /**
     * @throws IncorrectAlgorithmException
     * @throws IncorrectKeyException
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
     * @param picture
     * @throws IncorrectFileException
     * @throws IncorrectSignatureException
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
     * @return signed data
     * @throws IncorrectSignatureException
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
     * @return
     * @throws IncorrectFileException
     * @throws IncorrectAlgorithmException
     * @throws IncorrectKeyException
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
