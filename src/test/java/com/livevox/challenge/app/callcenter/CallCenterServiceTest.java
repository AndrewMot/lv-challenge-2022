package com.livevox.challenge.app.callcenter;

import java.util.Optional;

import com.livevox.challenge.app.DataGenerator;
import com.livevox.challenge.app.response.exceptions.BadRequestException;
import com.livevox.challenge.app.response.exceptions.ConflictException;
import com.livevox.challenge.app.response.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CallCenterServiceTest {

    @Mock
    private CallCenterRepository callCenterRepository;

    @InjectMocks
    private CallCenterService callCenterService;

    @Test
    @DisplayName("When no call center name then Bad Request")
    void noName() {
        final CallCenter callCenter = mock(CallCenter.class);
        given(callCenter.getName()).willReturn(null);
        assertThrows(BadRequestException.class,
                     () -> callCenterService.create(callCenter));
    }

    @Test
    @DisplayName("When no country name then Bad Request")
    void noCountryName() {
        final CallCenter callCenter = mock(CallCenter.class);
        given(callCenter.getName()).willReturn(DataGenerator.NAME);
        given(callCenter.getCountryName()).willReturn(null);
        assertThrows(BadRequestException.class,
                     () -> callCenterService.create(callCenter));
    }

    @Test
    @DisplayName("When duplicated Call center Name then Conflict")
    void duplicatedName() {
        final CallCenter callCenter = mock(CallCenter.class);
        given(callCenter.getName()).willReturn(DataGenerator.NAME);
        given(callCenter.getCountryName()).willReturn(DataGenerator.NAME);
        given(callCenterRepository.findByNameAndCountryName(anyString(), anyString()))
            .willReturn(Optional.of(callCenter));
        assertThrows(ConflictException.class,
                     () -> callCenterService.create(callCenter));
    }

    @Test
    @DisplayName("When Call center correct then saved object")
    void successful() {
        final CallCenter callCenter = mock(CallCenter.class);
        given(callCenter.getName()).willReturn(DataGenerator.NAME);
        given(callCenter.getCountryName()).willReturn(DataGenerator.NAME);
        given(callCenterRepository.findByNameAndCountryName(anyString(), anyString()))
            .willReturn(Optional.empty());
        given(callCenterRepository.save(any())).willReturn(callCenter);
        callCenterService.create(callCenter);
        verify(callCenterRepository).save(callCenter);
    }

    @Test
    @DisplayName("When CallCenter doesn't exist then Not found")
    void notFound() {
        given(callCenterRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                     () -> callCenterService.retrieve(1L));
    }

    @Test
    @DisplayName("When Call center exists then object")
    void exists() {
        final CallCenter callCenter = mock(CallCenter.class);
        given(callCenterRepository.findById(anyLong())).willReturn(Optional.of(callCenter));
        assertNotNull(callCenterService.retrieve(1L));
    }
}
