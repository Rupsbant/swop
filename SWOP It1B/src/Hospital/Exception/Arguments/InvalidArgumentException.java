package Hospital.Exception.Arguments;

public abstract class InvalidArgumentException extends Exception {

    public InvalidArgumentException() {
    }

    public InvalidArgumentException(String str) {
        super(str);
    }
}
