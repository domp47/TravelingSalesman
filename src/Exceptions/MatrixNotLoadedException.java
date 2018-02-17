package Exceptions;

public class MatrixNotLoadedException extends Exception
{
    public MatrixNotLoadedException() {}

    public MatrixNotLoadedException(String message)
    {
        super(message);
    }
}