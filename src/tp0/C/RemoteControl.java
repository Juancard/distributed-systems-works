package tp0.C;

/**
 * User: juan
 * Date: 10/03/17
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public interface RemoteControl {
    static final char NUMBER_0 = '0';
    static final char NUMBER_1 = '1';
    static final char NUMBER_2 = '2';
    static final char NUMBER_3 = '3';
    static final char NUMBER_4 = '4';
    static final char NUMBER_5 = '5';
    static final char NUMBER_6 = '6';
    static final char NUMBER_7 = '7';
    static final char NUMBER_8 = '8';
    static final char NUMBER_9 = '9';

    public void volumeUp();
    public void volumeDown();
    public void powerOnOff();
    public void muteOnOff();
    public void number0();
    public void number1();
    public void number2();
    public void number3();
    public void number4();
    public void number5();
    public void number6();
    public void number7();
    public void number8();
    public void number9();
}
