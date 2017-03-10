package tp0.C;

/**
 * Created with IntelliJ IDEA.
 * User: juan
 * Date: 10/03/17
 * Time: 16:52
 * To change this template use File | Settings | File Templates.
 */
public class Tv implements RemoteControl {
    private static final int MAX_VOLUME = 50;
    private static final int MIN_VOLUME = 0;
    private static final String MAX_CHANNEL = "999";
    private static final String MIN_CHANNEL = "000";

    private int volume;
    private boolean isPowerOn;
    private boolean isMuted;

    private String channel;
    private String channelBeingSetted = "";

    public Tv() {
        isPowerOn = false;
        this.volume = 5;
        this.isMuted = false;
        this.channel = "002";
    }

    @Override
    public void powerOnOff() {
        this.isPowerOn = !this.isPowerOn;
    }

    public void channelUp() {
        if (isPowerOn)  {
            this.channelBeingSetted = "";
            if (this.channel == MAX_CHANNEL) {
                this.channel = MIN_CHANNEL;
            } else {
                boolean done = false;
                int position = this.channel.length() - 1;
                while (position >= 0 && !done){
                    char thisChar = this.channel.charAt(position);
                    if (thisChar == NUMBER_9){
                        this.channel = this.channel.substring(0, position)
                                + NUMBER_0
                                + this.channel.substring(position + 1, this.channel.length());
                    } else {
                        int newChannel = Character.getNumericValue(thisChar) + 1;
                        this.channel = Integer.toString(newChannel);
                        done = true;
                    }
                    position--;
                }
            }
        }
    }

    public void channelDown() {
        if (isPowerOn)  {
            this.channelBeingSetted = "";
            if (this.channel == MIN_CHANNEL) {
                this.channel = MAX_CHANNEL;
            } else {
                boolean done = false;
                int position = this.channel.length() - 1;
                while (position >= 0 && !done){
                    char thisChar = this.channel.charAt(position);
                    if (thisChar == NUMBER_0){
                        this.channel = this.channel.substring(0, position)
                                + NUMBER_9
                                + this.channel.substring(position + 1, this.channel.length());
                    } else {
                        int newChannel = Character.getNumericValue(thisChar) - 1;
                        this.channel = Integer.toString(newChannel);
                        done = true;
                    }
                    position--;
                }
            }
        }
    }

    @Override
    public void volumeUp() {
        if (isPowerOn)  {
            this.isMuted = false;
            if (this.volume < MAX_VOLUME) this.volume++;
        }
    }

    @Override
    public void volumeDown() {
        if (isPowerOn){
            this.isMuted = false;
            if (this.volume > MIN_VOLUME) this.volume--;
        }
    }

    @Override
    public void muteOnOff() {
        if (isPowerOn){
            this.isMuted = !this.isMuted;
        }
    }

    private void onNumber(int number){
        if (isPowerOn) {
            this.channelBeingSetted += number;
            if (this.channelBeingSetted.length() == MAX_CHANNEL.length()){
                this.channel = this.channelBeingSetted;
                this.channelBeingSetted = "";
            }
        }
    }

    @Override
    public void number0() {
        onNumber(NUMBER_0);
    }

    @Override
    public void number1() {
        onNumber(NUMBER_1);
    }

    @Override
    public void number2() {
        onNumber(NUMBER_2);
    }

    @Override
    public void number3() {
        onNumber(NUMBER_3);
    }

    @Override
    public void number4() {
        onNumber(NUMBER_4);
    }

    @Override
    public void number5() {
        onNumber(NUMBER_5);
    }

    @Override
    public void number6() {
        onNumber(NUMBER_6);
    }

    @Override
    public void number7() {
        onNumber(NUMBER_7);
    }

    @Override
    public void number8() {
        onNumber(NUMBER_8);
    }

    @Override
    public void number9() {
        onNumber(NUMBER_9);
    }
}
