package Tp1.Ex05.Server;

import Tp1.Ex05.Common.IWeatherService;
import Tp1.Ex05.Common.NoApiIdException;
import Tp1.Ex05.Common.Weather;

import java.io.IOException;
import java.util.Random;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:37
 */

public class WeatherService implements IWeatherService {

    @Override
    public Weather getWeatherInServer() throws NoApiIdException {
        WeatherApi weatherApi = new WeatherApi();
        String place = "Buenos Aires";
        try {
            return weatherApi.getByCityAndCountry(place, "ar");
        } catch (IOException e) {
            System.out.println("Error in calling Weather API. Randomizing data");
            Weather randomWeather = this.getRandomWeather();
            randomWeather.setPlace(place);
            return randomWeather;
        }
    }

    private Weather getRandomWeather(){
        Weather weather = new Weather();
        weather.setTemperature(this.getRandomTemperature());
        weather.setDescription(this.getRandomWeatherDescription());

        return weather;
    }

    private String getRandomWeatherDescription() {
        final String[] WEATHER_DESCRIPTIONS = {
                "cloudy", "sunny", "clear sky", "rainy", "snowy"
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
