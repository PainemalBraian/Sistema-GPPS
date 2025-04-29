package Backend.Exceptions;

public class ConnectionException extends Exception{
    public ConnectionException(){}
    public ConnectionException(String message){
        super(message);
    }
}