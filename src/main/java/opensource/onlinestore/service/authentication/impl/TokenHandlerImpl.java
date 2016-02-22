package opensource.onlinestore.service.authentication.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import opensource.onlinestore.LoggedUser;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.service.authentication.TokenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by orbot on 29.01.16.
 */
@Component
public class TokenHandlerImpl implements TokenHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TokenHandlerImpl.class);

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String SEPARATOR = ".";
    private static final String SEPARATOR_SPLITTER = "\\.";

    private final Mac hmac;

    @Autowired
    public TokenHandlerImpl(@Value("${token.secret}") String key) {
        byte[] secretKey = DatatypeConverter.parseBase64Binary(key);
        try {
            hmac = Mac.getInstance(HMAC_ALGORITHM);
            hmac.init(new SecretKeySpec(secretKey, HMAC_ALGORITHM));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LOG.info("Could not instantiate TokenHandler", e);
            throw new IllegalStateException("Could not initialize crypto algorithm: " + e.getMessage(), e);
        }
    }

    @Override
    public LoggedUser parseUserFromToken(String token) {
        LOG.info("Parsing user from token: {}", token);
        final String[] parts = token.split(SEPARATOR_SPLITTER);
        if(parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
            final byte[] userBytes = fromBase64(parts[0]);
            final byte[] hash = fromBase64(parts[1]);
            boolean validHash = Arrays.equals(createHmac(userBytes), hash);
            if(validHash) {
                final LoggedUser user = fromJSON(userBytes);
                if(new Date().getTime() < user.getExpires()) {
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public String createTokenForUser(LoggedUser user) {
        byte[] userBytes = toJSON(user);
        byte[] hash = createHmac(userBytes);
        final StringBuilder stringBuilder = new StringBuilder(170);
        stringBuilder.append(toBase64(userBytes));
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(toBase64(hash));
        return stringBuilder.toString();
    }

    private byte[] fromBase64(String content) {
        return DatatypeConverter.parseBase64Binary(content);
    }

    private String toBase64(byte[] content) {
        return DatatypeConverter.printBase64Binary(content);
    }

    private synchronized byte[] createHmac(byte[] content) {
        return hmac.doFinal(content);
    }

    private LoggedUser fromJSON(final byte[] userBytes) {
        try {
            return new ObjectMapper().readValue(new ByteArrayInputStream(userBytes), LoggedUser.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] toJSON(LoggedUser user) {
        try {
            return new ObjectMapper().writeValueAsBytes(user);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
