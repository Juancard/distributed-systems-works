package Tp0.C;

/**
 * User: juan
 * Date: 10/03/17
 * Time: 16:52
 */
public class Tv implements RemoteControl {
	private static final char NUMBER_0 = '0';
	private static final char NUMBER_1 = '1';
	private static final char NUMBER_2 = '2';
	private static final char NUMBER_3 = '3';
    private static final char NUMBER_4 = '4';
    private static final char NUMBER_5 = '5';
    private static final char NUMBER_6 = '6';
    private static final char NUMBER_7 = '7';
    private static final char NUMBER_8 = '8';
    private static final char NUMBER_9 = '9';
	
    public static final int MAX_VOLUME = 50;
    public static final int MIN_VOLUME = 0;
    public static final int MAX_CHANNEL = 999;
    public static final int MIN_CHANNEL = 1;
    public static final String[] validDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    private int volume;
    private boolean isPowerOn;
    private boolean isMuted;

    private int channel;
	private String channelDigitsBeingSet = "";

	public Tv() {
        isPowerOn = true;
        this.volume = 5;
        this.isMuted = false;
        this.channel = 2;
    }

	@Override
    public void powerOnOff() {
        this.isPowerOn = !this.isPowerOn;
    }

    public void channelUp() {
        if (isPowerOn)  {
            this.channelDigitsBeingSet = "";
            this.channel = (this.channel == MAX_CHANNEL)? MIN_CHANNEL : this.channel + 1;
        }
    }

    public void channelDown() {
        if (isPowerOn)  {
            this.channelDigitsBeingSet = "";
            this.channel = (this.channel == MIN_CHANNEL)?  MAX_CHANNEL : this.channel - 1;
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

    @Override
	public String toString() {
    	
    	String tvIs = "Tv está: ";
    	
    	if (!this.isPowerOn) return tvIs + "Apagada.";

    	String[] statesToPrint = {
    			"Encendida.",
				(this.isMuted)? 
						"En volumen: " + "Silenciado" 
						:  "En volumen: " + this.volume + ".",
				"En canal: " + this.channel
    	};
    	
    	for (String state : statesToPrint) {
			tvIs += "\n- " + state;
		}
    	
		return tvIs;
	}
    
    private void onNumber(char number){
        if (isPowerOn) {
            final int MAX_DIGITS = Integer.toString(MAX_CHANNEL).length(); 
            this.channelDigitsBeingSet += number;
            if (this.channelDigitsBeingSet.length() == MAX_DIGITS){
                this.channel = Integer.parseInt(this.channelDigitsBeingSet);
                this.channelDigitsBeingSet = "";
            }
        }
    }
    
    public void number0() {
        onNumber(NUMBER_0);
    }

    public void number1() {
        onNumber(NUMBER_1);
    }

    public void number2() {
        onNumber(NUMBER_2);
    }

    public void number3() {
        onNumber(NUMBER_3);
    }

    public void number4() {
        onNumber(NUMBER_4);
    }

    public void number5() {
        onNumber(NUMBER_5);
    }

    public void number6() {
        onNumber(NUMBER_6);
    }

    public void number7() {
        onNumber(NUMBER_7);
    }

    public void number8() {
        onNumber(NUMBER_8);
    }

    public void number9() {
        onNumber(NUMBER_9);
    }

    public int getChannel() {
		return channel;
	}

    public String getChannelBeingSet() {
		return channelDigitsBeingSet;
	}

	public boolean isPowerOn() {
		return this.isPowerOn;
	}
}
