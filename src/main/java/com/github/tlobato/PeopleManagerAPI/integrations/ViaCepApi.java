package com.github.tlobato.PeopleManagerAPI.integrations;

import com.github.tlobato.PeopleManagerAPI.dto.request.AddressRequestDto;
import com.github.tlobato.PeopleManagerAPI.exception.InvalidInputException;
import com.github.tlobato.PeopleManagerAPI.shared.enums.ErrorCode;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.Builder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

@Component
public class ViaCepApi {

    private final String VIA_CEP_URL = "https://viacep.com.br/ws/";

    public AddressRequestDto getCompleteAddress(String cep) {

        try {
            String urlString = VIA_CEP_URL + cep + "/json/";
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));
                String inputLine;
                StringBuilder responseString = new StringBuilder();
                while ((inputLine = in.readLine())!= null) {
                    responseString.append(inputLine);
                }
                in.close();
                httpClient.close();
                var address = new Gson().fromJson(responseString.toString(), AddressRequestDto.class);
                if (address.getLocalidade() == null) {
                    throw new InvalidInputException(ErrorCode.EC102.getMessage(), ErrorCode.EC102.getCode());
                }
                return address;
            } else {
                httpClient.close();
                throw new InvalidInputException(ErrorCode.EC102.getMessage(), ErrorCode.EC102.getCode());
            }
        } catch (IOException ex) {
            throw new InvalidInputException(ErrorCode.EC102.getMessage(), ErrorCode.EC102.getCode());
        }
    }
}