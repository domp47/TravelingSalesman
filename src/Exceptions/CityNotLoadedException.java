package Exceptions;

public class CityNotLoadedException extends Exception
{
    public CityNotLoadedException() {}

    public CityNotLoadedException(String message)
    {
        super(message);
    }
}