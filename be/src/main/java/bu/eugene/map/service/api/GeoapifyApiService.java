package bu.eugene.map.service.api;

import bu.eugene.map.model.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoapifyApiService {

        @Value("${geoapify.secret}")
        private String secret;

        public  String getCoordinates(String cityName) {
                try {
                        log.info("try to send a request to Geoapify API");

                        String encodedCityName = URLEncoder.encode(cityName, StandardCharsets.UTF_8);

                        // Отправляем запрос с закодированным названием города
                        HttpURLConnection conn = getHttpURLConnection(encodedCityName);
                        int responseCode = conn.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                String inputLine;
                                StringBuilder response = new StringBuilder();

                                while ((inputLine = in.readLine()) != null) {
                                        response.append(inputLine);
                                }
                                in.close();
                                log.info("request successfully processed");
                                return response.toString();
                        } else {
                                log.error("GET запрос не удался: " + responseCode);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }

        private @NotNull HttpURLConnection getHttpURLConnection(String cityName) throws IOException {
                String urlString = "https://api.geoapify.com/v1/geocode/search?text="
                        + cityName
                        +"&lang=ru"
                        +"&format=json&apiKey="
                        + secret;

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                return conn;
        }
}