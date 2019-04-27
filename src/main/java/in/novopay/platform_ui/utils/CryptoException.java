package in.novopay.platform_ui.utils;

public class CryptoException extends Exception {

    private static final long serialVersionUID = 1L;

    public CryptoException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CryptoException(String msg, Exception cause) {
        super(msg, cause);
    }
}