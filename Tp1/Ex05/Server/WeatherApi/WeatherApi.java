package Tp1.Ex05.Server.WeatherApi;

import Common.JSONReader;
import Tp1.Ex05.Common.Weather;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 18:57
 */
public class WeatherApi {
    private static final String API_PROTOCOL = "http";
    private static final String API_HOST = "api.openweathermap.org";
    private static final String API_PATH = "/data/2.5/weather";
    private static final String IN_CELSIUS = "&units=metric";

    private String apiId;

    public WeatherApi() throws NoApiIdException {
        this.apiId = loadApiId();
    }

    private String loadApiId() throws NoApiIdException {
        Properties prop = null;
        try {
            prop = loadProperties();
            return prop.getProperty("WEATHER_API_ID");
        } catch (IOException e) {
            throw new NoApiIdException(NoApiIdException.DEFAULT_MESSAGE +
                    "To generate an api key, visit this page: https://openweathermap.org/appid.\n" +
                    "The java properties file is a file called \"config.properties\" that is saved in the root directory of this app."
            );
        }
    }

    private Properties loadProperties() throws IOException {
        Properties prop = new Properties();
        FileInputStream input = new FileInputStream("src/config.properties");
        prop.load(input);
        return prop;
    }

    public Weather getByCityAndCountry(String city, String countryCode) throws IOException {
        final String QUERY = String.format("q=%s,%s", city, countryCode);
        return weatherFromApi(QUERY);
    }

    public Weather getByLatLong(String latitude, String longitude) throws IOException {
        final String QUERY = String.format("lat=%s&lon=%s", latitude, longitude);
        return weatherFromApi(QUERY);
    }

    private Weather weatherFromApi(String query) throws IOException {
        JSONObject weatherJson = callApi(query);
        return jsonWeatherToWeather(weatherJson);
    }

    private JSONObject callApi(String query) throws IOException {
        String completeQuery = query + IN_CELSIUS + "&appid=" + this.apiId;
        URI uri = generateURI(completeQuery);
        return JSONReader.readJsonFromUrl(uri.toString());
    }

    private URI generateURI(String query) {
        URI uri = null;
        try {
            uri = new URI(
                    API_PROTOCOL,
                    API_HOST,
                    API_PATH,
                    query,
                    null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private Weather jsonWeatherToWeather(JSONObject weatherJson) {
        Weather weather = new Weather();

        String description = weatherJson.getJSONArray("weather").getJSONObject(0).getString("description");
        double temperature = weatherJson.getJSONObject("main").getDouble("temp");
        String place = weatherJson.getString("name");

        weather.setDescription(description);
        weather.setTemperature(temperature);
        weather.setPlace(place);

        return weather;
    }

}
