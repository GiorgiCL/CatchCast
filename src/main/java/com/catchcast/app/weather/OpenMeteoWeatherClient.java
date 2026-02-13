package com.catchcast.app.weather;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OpenMeteoWeatherClient implements WeatherClient {

    private final RestClient restClient;

    public OpenMeteoWeatherClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://api.open-meteo.com")
                .build();
    }

    @Override
    public List<HourlyWeather> getHourlyForecast(
            double latitude,
            double longitude,
            LocalDateTime fromInclusive,
            LocalDateTime toExclusive
    ) {
        if (toExclusive.isBefore(fromInclusive) || toExclusive.isEqual(fromInclusive)) {
            throw new IllegalArgumentException("toExclusive must be after fromInclusive");
        }


        LocalDate startDate = fromInclusive.toLocalDate();
        LocalDate endDate = toExclusive.toLocalDate();

        OpenMeteoForecastResponse resp = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("timezone", "auto")
                        .queryParam("hourly", String.join(",",
                                "temperature_2m",
                                "relative_humidity_2m",
                                "pressure_msl",
                                "wind_speed_10m",
                                "wind_direction_10m",
                                "precipitation",
                                "cloud_cover"
                        ))
                        .queryParam("start_date", startDate)
                        .queryParam("end_date", endDate)
                        .build())
                .retrieve()
                .body(OpenMeteoForecastResponse.class);

        if (resp == null || resp.hourly == null || resp.hourly.time == null) {
            return List.of();
        }

        List<String> times = resp.hourly.time;
        List<Double> temp = resp.hourly.temperature_2m;
        List<Double> hum = resp.hourly.relative_humidity_2m;
        List<Double> pres = resp.hourly.pressure_msl;
        List<Double> wind = resp.hourly.wind_speed_10m;
        List<Double> windDir = resp.hourly.wind_direction_10m;
        List<Double> precip = resp.hourly.precipitation;
        List<Double> cloud = resp.hourly.cloud_cover;

        int n = times.size();
        List<HourlyWeather> out = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {

            LocalDateTime dt = parseToLocalDateTime(times.get(i));
            if (dt.isBefore(fromInclusive) || !dt.isBefore(toExclusive)) {
                continue;
            }

            out.add(new HourlyWeather(
                    dt,
                    getAt(temp, i),
                    getAt(pres, i),
                    getAt(hum, i),
                    getAt(wind, i),
                    getAt(windDir, i),
                    getAt(precip, i),
                    getAt(cloud, i)
            ));
        }

        return out;
    }

    private static LocalDateTime parseToLocalDateTime(String iso) {
        try {
            return OffsetDateTime.parse(iso).toLocalDateTime();
        } catch (Exception ignored) {
            return LocalDateTime.parse(iso);
        }
    }

    private static Double getAt(List<Double> list, int i) {
        if (list == null || i < 0 || i >= list.size()) return null;
        return list.get(i);
    }

    public static class OpenMeteoForecastResponse {
        public Hourly hourly;

        public static class Hourly {
            public List<String> time;

            public List<Double> temperature_2m;
            public List<Double> relative_humidity_2m;
            public List<Double> pressure_msl;
            public List<Double> wind_speed_10m;
            public List<Double> wind_direction_10m;
            public List<Double> precipitation;
            public List<Double> cloud_cover;
        }
    }
}
