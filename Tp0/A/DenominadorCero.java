package Tp0.A;

public class DenominadorCero extends Exception{
  public DenominadorCero() { super(); }
  public DenominadorCero(String message) { super(message); }
  public DenominadorCero(String message, Throwable cause) { super(message, cause); }
  public DenominadorCero(Throwable cause) { super(cause); }
}
