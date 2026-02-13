package com.catchcast.app.weather;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherClient {


    List<HourlyWeather> getHourlyForecast(
            double latitude,
            double longitude,
            LocalDateTime fromInclusive,
            LocalDateTime toExclusive
    );

    record HourlyWeather(
            LocalDateTime dateTime,
            Double temperatureC,
            Double pressureHpa,
            Double humidityPercent,
            Double windSpeedMps,
            Double windDirectionDeg,
            Double precipitationMm,
            Double cloudCoverPercent
    ) {}
}
