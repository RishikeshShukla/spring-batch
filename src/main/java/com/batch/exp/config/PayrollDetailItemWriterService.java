package com.batch.exp.config;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 *
 * @param <PayrollDetails>
 */
@Service
public class PayrollDetailItemWriterService<PayrollDetails> implements ItemWriter<PayrollDetails> {

    private static final String DOCUMENTS_ENDPOINT = "/documents";

    private final RestTemplate restTemplate;

    @Value("${ENDPOINT ? : /endpoint/}")
    private String apiEndpoint;

    /**
     * Constructor which autowires {@link RestTemplate}.
     *
     * @param restTemplate {@link RestTemplate}
     */
    @Autowired
    public PayrollDetailItemWriterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void write(List<? extends PayrollDetails> items) throws Exception {

        byte[] data = SerializationUtils.serialize(items);
        final ByteArrayResource contentsAsResource = new ByteArrayResource(data);
        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file-data", contentsAsResource);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.postForObject(apiEndpoint + DOCUMENTS_ENDPOINT, requestEntity, String.class);
    }
}
