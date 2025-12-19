package by.egoramel.tt.exception;

@SuppressWarnings("unused")
public class CustomException extends Exception {
    public CustomException () {
        super();
    }
    public CustomException(final String errorMessage){
        super(errorMessage);
    }
    public CustomException(final Throwable throwable){
        super(throwable);
    }
    public CustomException(final String errorMessage, final Throwable throwable){
        super(errorMessage, throwable);
    }
}
