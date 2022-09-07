package com.livevox.challenge.app.callcenter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.livevox.challenge.app.DataGenerator;
import com.livevox.challenge.app.response.Constants;
import com.livevox.challenge.app.response.exceptions.BadRequestException;
import com.livevox.challenge.app.response.exceptions.ConflictException;
import com.livevox.challenge.app.response.exceptions.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class CallCenterControllerTest {

    private static final String BASE_URL = "/call-centers/";
    private static final String RETRIEVE_URL = String.format("%s%d", BASE_URL, DataGenerator.CALL_CENTER_ID);
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CallCenterService callCenterService;

    @Test
    @DisplayName("When no body then Bad Request")
    void noBody() throws Exception {

        final MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("When Call Center doesn't have a name then Bad Request")
    void noName() throws Exception {
        given(callCenterService.create(any()))
            .willThrow(new BadRequestException(Constants.CALL_CENTER_NAME_REQUIRED_MESSAGE));

        final MockHttpServletResponse response = getCreateResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("When Call Center doesn't have a country name then Bad Request")
    void noCountryName() throws Exception {
        given(callCenterService.create(any()))
            .willThrow(new BadRequestException(Constants.CALL_CENTER_COUNTRY_NAME_REQUIRED_MESSAGE));

        final MockHttpServletResponse response = getCreateResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("When Call Center with same exists for same a country name then Conflict")
    void duplicatedName() throws Exception {
        given(callCenterService.create(any()))
            .willThrow(new ConflictException(Constants.CALL_CENTER_UNIQUE_NAME_MESSAGE));

        final MockHttpServletResponse response = getCreateResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("When all good then ok")
    void success() throws Exception {
        given(callCenterService.create(any())).willReturn(new CallCenter());

        final MockHttpServletResponse response = getCreateResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("When call center doesn't exist then not found")
    void notExist() throws Exception {
        given(callCenterService.retrieve(anyLong())).willThrow(
            new NotFoundException(Constants.CALL_CENTER_NOT_FOUND_MESSAGE));
        final MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.get(RETRIEVE_URL)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("When call center exists then OK")
    void exist() throws Exception {
        final CallCenter callCenter = new CallCenter();
        given(callCenterService.retrieve(anyLong())).willReturn(callCenter);
        final MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.get(RETRIEVE_URL)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private String getBodyFromObject() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        final ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(new CallCenter());
    }

    private MockHttpServletResponse getCreateResponse() throws Exception {
        return mvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                    .content(getBodyFromObject())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();
    }
}
