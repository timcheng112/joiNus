package util.exception;

public class MaxParticipantsExceededException extends Exception {

    public MaxParticipantsExceededException() {
    }

    public MaxParticipantsExceededException(String msg) {
        super(msg);
    }
}
