package com.livevox.challenge.app.agent;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class AgentControllerTest {

    private static final String BASE_URL = "/agents/";
    private static final String ASSIGN_URL = String.format("%s%d/assign", BASE_URL, DataGenerator.AGENT_ID);
    private static final String UNASSIGN_URL = String.format("%s%d/unassign", BASE_URL, DataGenerator.AGENT_ID);
    private String requestBody;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AgentService agentService;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        final AssignRequest request = new AssignRequest();
        request.setCallCenterId(DataGenerator.CALL_CENTER_ID);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        final ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        requestBody = objectWriter.writeValueAsString(request);
    }

    @Test
    @DisplayName("When no body then Bad Request")
    void noBody() throws Exception {

        final MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.patch(ASSIGN_URL)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("When Agent doesn't exist then Not Found")
    void agentNotExist() throws Exception {
        given(agentService.assign(DataGenerator.AGENT_ID, DataGenerator.CALL_CENTER_ID))
            .willThrow(new NotFoundException(Constants.AGENT_NOT_FOUND_MESSAGE));

        final MockHttpServletResponse response = getResponse(ASSIGN_URL, requestBody);

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("When Call Center doesn't exist then Bad Request")
    void callCenterNotExist() throws Exception {
        given(agentService.assign(DataGenerator.AGENT_ID, DataGenerator.CALL_CENTER_ID))
            .willThrow(new BadRequestException(Constants.CALL_CENTER_NOT_FOUND_MESSAGE));

        final MockHttpServletResponse response = getResponse(ASSIGN_URL, requestBody);

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("When duplicated extension exists then Conflict")
    void duplicatedExtension() throws Exception {
        given(agentService.assign(DataGenerator.AGENT_ID, DataGenerator.CALL_CENTER_ID))
            .willThrow(new ConflictException(Constants.UNIQUE_EXTENSION_MESSAGE));

        final MockHttpServletResponse response = getResponse(ASSIGN_URL, requestBody);

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("When Agent and Call Center Exist then Successful response")
    void successful() throws Exception {
        final AssignResponse objectResponse = AssignResponse.builder()
            .id(DataGenerator.AGENT_ID)
            .callCenterId(DataGenerator.CALL_CENTER_ID)
            .firstName(DataGenerator.FIRST_NAME)
            .lastName(DataGenerator.LAST_NAME)
            .phone(DataGenerator.PHONE_NUMBER)
            .build();

        given(agentService.assign(DataGenerator.AGENT_ID, DataGenerator.CALL_CENTER_ID))
            .willReturn(objectResponse);

        final MockHttpServletResponse response = getResponse(ASSIGN_URL, requestBody);

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("When Agent doesn't exist then Not Found when unassign")
    void agentNotExistUnassign() throws Exception {
        given(agentService.unassign(DataGenerator.AGENT_ID))
            .willThrow(new NotFoundException(Constants.AGENT_NOT_FOUND_MESSAGE));

        final MockHttpServletResponse response = getResponse(UNASSIGN_URL, requestBody);

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("When Agent Exists then Successful response when unassigned")
    void successfulUnassign() throws Exception {
        final AssignResponse objectResponse = AssignResponse.builder()
            .id(DataGenerator.AGENT_ID)
            .callCenterId(null)
            .firstName(DataGenerator.FIRST_NAME)
            .lastName(DataGenerator.LAST_NAME)
            .phone(null)
            .build();

        given(agentService.unassign(DataGenerator.AGENT_ID))
            .willReturn(objectResponse);

        final MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.patch(UNASSIGN_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("When list then pagination must be correct")
    void pageable() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .param("page", "2")
                        .param("size", "2"))
            .andExpect(status().isOk());

        final ArgumentCaptor<Pageable> pageableCaptor =
            ArgumentCaptor.forClass(Pageable.class);
        verify(agentService).list(pageableCaptor.capture());
        final PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        Assertions.assertThat(pageable.isPaged()).isTrue();
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(2);
        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(2);
    }

    private MockHttpServletResponse getResponse(final String url, final String requestBody) throws Exception {
        return mvc.perform(
                MockMvcRequestBuilders.patch(url)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();
    }
}
