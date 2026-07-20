package com.mzrt.erp_lite.adapter.in.rest.jsonplaceholder;

import com.mzrt.erp_lite.adapter.in.rest.jsonplaceholder.dto.UserDTO;
import com.mzrt.erp_lite.adapter.in.rest.jsonplaceholder.mapper.CustomerMapper;
import com.mzrt.erp_lite.adapter.in.rest.jsonplaceholder.model.JsonPlaceholderConfigModel;
import com.mzrt.erp_lite.domain.customer.CustomerInfo;
import com.mzrt.erp_lite.domain.customer.CustomerProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.Optional;

@Service
@Slf4j
public class JsonPlaceHolderProviderAdapter implements CustomerProviderService {

    private final RestClient restClient;
    private final CustomerMapper customerMapper;
    private final String endpoint;

    public JsonPlaceHolderProviderAdapter(
            @Qualifier("jsonPlaceHolder") RestClient restClient,
            CustomerMapper customerMapper,
            JsonPlaceholderConfigModel jsonConfig) {

        this.restClient = restClient;
        this.customerMapper = customerMapper;
        this.endpoint = jsonConfig.usersEndpoint();
    }

    @Override
    public Optional<CustomerInfo> findById(Long id) {
        try {
            UserDTO user = restClient.get()
                    .uri(endpoint, id)
                    .retrieve()
                    .body(UserDTO.class);

            if (user == null) {
                log.warn("Customer {} not found", id);
                return Optional.empty();
            }

            return Optional.of(customerMapper.toCustomerInfo(user));

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Customer {} not found", id);
            return Optional.empty();

        } catch (HttpClientErrorException e) {
            log.error("Client error retrieving customer {}. Status: {}",
                    id, e.getStatusCode(), e);
            return Optional.empty();

        } catch (HttpServerErrorException e) {
            log.error("Server error retrieving customer {}. Status: {}",
                    id, e.getStatusCode(), e);
            return Optional.empty();

        } catch (ResourceAccessException e) {
            log.error("Could not connect to JsonPlaceholder while retrieving customer {}",
                    id, e);
            return Optional.empty();

        } catch (RestClientException e) {
            log.error("Unexpected error retrieving customer {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean existById(Long id) {
        return findById(id).isPresent();
    }
}