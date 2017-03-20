package Tp1.Ex05.Server;

import Tp1.Ex05.IWeatherService;
import Tp1.Ex05.Weather;

import java.util.Random;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:37
 */

public class WeatherService implements IWeatherService {

    @Override
    public Weather getWeatherInServer() {
        Weather weather = new Weather();
        weather.setDescription(this.getRandomWeatherDescription());
        weather.setTemperature(this.getRandomTemperature());
        return weather;
    }

    private String getRandomWeatherDescription() {
        final String[] WEATHER_DESCRIPTIONS = {
                "Cloudy", "Sunny", "Clear sky", "Rain", "Snow"
        };
        Random random = new Random();
        int position = random.nextInt(WEATHER_DESCRIPTIONS.length - 1);
        return WEATHER_DESCRIPTIONS[position];
    }

    public double getRandomTemperature(){
        final double MAX_TEMP = 40.0;
        final double MIN_TEMP = 1.0;
        Random random = new Random();

        return MIN_TEMP + (MAX_TEMP - MIN_TEMP) * random.nextDouble();
    }
}
