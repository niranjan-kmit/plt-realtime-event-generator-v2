package org.kafka.loadgenerator.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.kafka.loadgenerator.model.PayLoadMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


@Service
public class ParserService {
	
	public PayLoadMessage getObjectfromXml() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        // Reads from XML and converts to POJO
        Resource resource = new ClassPathResource("PricingPayload.xml");
        InputStream inputStream = resource.getInputStream();
        byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
        PayLoadMessage payload = objectMapper.readValue(
                StringUtils.toEncodedString(bdata, StandardCharsets.UTF_8),
                PayLoadMessage.class);
		return payload;
	}

}
