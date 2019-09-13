package exceptions;

import java.util.function.Supplier;

public class NoSuchPlayerException extends Exception {
    private static  final String EXCEPTION_MESSAGE = "No such player founded ";
    public NoSuchPlayerException() {
        super(EXCEPTION_MESSAGE);
    }


}
