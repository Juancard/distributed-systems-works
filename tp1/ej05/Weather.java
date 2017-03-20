package tp1.ej05;

import java.io.Serializable;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:24
 */
    /*
    @TODO implement with this REST api:
    https://openweathermap.org/current#geo
     */
public class Weather implements Serializable{

    public double temperature;
    public String description;

    public Weather() {}

    @Override
    public String toString() {
        return "Weather{" +
                "temperature=" + temperature +
                ", description='" + description + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

}
