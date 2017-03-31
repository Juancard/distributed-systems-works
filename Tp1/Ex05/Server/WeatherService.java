package Tp1.Ex05.Server;

import Tp1.Ex05.Common.IWeatherService;
import Tp1.Ex05.Common.Weather;
import Tp1.Ex05.Server.Location.GeoLocator;
import Tp1.Ex05.Server.Location.Location;
import Tp1.Ex05.Server.WeatherApi.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import org.json.JSONException;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:37
 */

public class WeatherService implements IWeatherService {

    private WeatherApi weatherApi;

    public WeatherService() throws NoApiIdException {
        this.weatherApi = new WeatherApi();
    }

    @Override
    public Weather getWeatherInServer(){


        try {
            Location serverLocation = this.getServerLocation();
            try {
                return this.weatherApi.getByCityAndCountry(serverLocation.getCity(), serverLocation.getCountryCode());
            } catch (IOException e) {
                System.out.println("Error in Weather Api while calling the API: " + e.getMessage());
                System.out.println("Randomizing data");
                return this.getRandomWeather(serverLocation.getCity());
            } catch (JSONException e) {
                System.out.println("Error in Weather Api while handling Json: " + e.getMessage());
                System.out.println("Randomizing data");
                return this.getRandomWeather(serverLocation.getCity());
			}
        } catch (IOException e) {
            System.out.println("Error in Getting Server Location: " + e.toString());
            System.out.println("Randomizing data.");
            return this.getRandomWeather("Buenos Aires");
        }

    }

    private Location getServerLocation() throws IOException {
        String myIp = this.getMyPublicIpAddress();
        GeoLocator geoLocator = new GeoLocator();
        return geoLocator.getLocation(myIp);
    }

    private String getMyPublicIpAddress() throws IOException {
        URL url = new URL("http://bot.whatismyipaddress.com");
        BufferedReader sc = new BufferedReader(new InputStreamReader(url.openStream()));
        return sc.readLine().trim();
    }

    private Weather getRandomWeather(String place){
        Weather weather = new Weather();

        weather.setTemperature(this.getRandomTemperature());
        weather.setDescription(this.getRandomWeatherDescription());
        weather.setPlace(place);

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
