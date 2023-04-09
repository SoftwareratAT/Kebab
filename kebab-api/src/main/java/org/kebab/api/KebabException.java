package org.kebab.api;

public final class KebabException extends RuntimeException {
    public KebabException(String message) {
        super(message);
    }

    public KebabException(String message, Throwable cause) {
        super(message, cause);
    }

    public KebabException(Throwable cause) {
        super(cause);
    }
}
