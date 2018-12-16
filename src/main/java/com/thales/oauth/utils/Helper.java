package com.thales.oauth.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public class Helper {

    public static final String DATA_PATTERN = "dd-MM-yyyy HH:mm:ss";

    private SimpleDateFormat dateFormatter = new SimpleDateFormat(DATA_PATTERN);

    public String uuidGenerator() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID + "-" + dateFormatter.format(new Date());
    }

    public static String createPasswordByBCrypt(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public static String getHeaderFromRequest(String keyHeader) {
        HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        if (null == curRequest.getHeader(keyHeader))
            throw new RuntimeException("");

        return curRequest.getHeader(keyHeader);
    }

    public static void checkIfStringIsOnlyNumbers(String number, String error) {

        checkIfStringIsBlank(number, "");

        if (!number.matches("[0-9]+")) {
            throw new RuntimeException(error);
        }
    }

    public static void checkIfStringIsBlank(String string, String error) {
        if (null == string || string.isEmpty())
            throw new RuntimeException(error);
    }

    public static void checkIfObjectIsNull(Object object, String error) {
        if (object == null)
            throw new RuntimeException(error);
    }

    public static void checkIfCollectionIsNullOrEmpty(Collection collection, String error) {
        if (CollectionUtils.isEmpty(collection))
            throw new RuntimeException(error);
    }
}
