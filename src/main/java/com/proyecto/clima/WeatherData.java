package com.proyecto.clima;

public class WeatherData {
    public final String datetime;
    public final double temperature;
    public final double humidity;
    public final double windSpeed;
    public final double visibility;
    public final double pressure;

    public WeatherData(String datetime, double temperature, double humidity, double windSpeed, double visibility, double pressure) {
        this.datetime = datetime;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.pressure = pressure;
    }
}