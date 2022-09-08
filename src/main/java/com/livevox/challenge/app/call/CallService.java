package com.livevox.challenge.app.call;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.livevox.challenge.app.vendor.CountryResponse;
import com.livevox.challenge.app.vendor.RemoteCallResponse;
import com.livevox.challenge.app.vendor.VendorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class CallService {

    private final CallRepository callRepository;
    private final VendorClient vendorClient;

    @Autowired
    public CallService(final CallRepository callRepository, final VendorClient vendorClient) {
        this.callRepository = callRepository;
        this.vendorClient = vendorClient;
    }

    public Long countRoutedCalls() {
        return callRepository.getCallsRoutedPerCountry()
            .stream()
            .filter(countPerCountry -> {
                final Set<String> countryNames = getCountryNamesByPrefix(countPerCountry.getCountryCode());
                return countryNames.contains(countPerCountry.getCountryName());
            })
            .mapToLong(CallCountPerCountry::getCount)
            .sum();
    }

    private Set<String> getCountryNamesByPrefix(final int prefix) {
        return vendorClient.fetchCountriesByPrefix(prefix)
            .stream()
            .map(CountryResponse.Country::getName)
            .collect(Collectors.toSet());
    }

    @Scheduled(cron = "@hourly")
    public void load() {
        vendorClient.fetchCalls()
            .getCalls()
            .stream()
            .map(this::mapFromRemoteCall)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(callRepository::save);
    }

    private Optional<Call> mapFromRemoteCall(final RemoteCallResponse.RemoteCall remoteCall) {
        if (!StringUtils.hasLength(remoteCall.getPhone())) {
            final String message =
                "The call with id {} couldn't be imported due phone is empty . Agent Extension: {}, Date: {}, Time: " +
                "{}, " + "Full Name: {} {}";
            log.warn(message, remoteCall.getId(), remoteCall.getAgentExtension(), remoteCall.getCallDate(),
                     remoteCall.getCallTime(), remoteCall.getFirstName(), remoteCall.getLastName());
            return Optional.empty();
        }
        return Optional.of(createFromRemoteCall(remoteCall));
    }

    private Call createFromRemoteCall(final RemoteCallResponse.RemoteCall remoteCall) {
        final Call call = new Call();
        call.setId(remoteCall.getId());
        call.setAgentExtension(remoteCall.getAgentExtension());
        final String[] phoneNumberParts = getPhoneParts(remoteCall.getPhone());
        call.setCustomerCountryPhoneCode(Integer.parseInt(phoneNumberParts[0]));
        call.setCustomerPhone(phoneNumberParts[1]);
        final String fullName = String.format("%s %s", remoteCall.getFirstName(), remoteCall.getLastName());
        call.setCustomerFullName(fullName);
        final LocalDateTime receivedOn = getCallTime(remoteCall.getCallDate(), remoteCall.getCallTime());
        call.setReceivedOn(receivedOn);
        return call;
    }

    private String[] getPhoneParts(final String phone) {
        return phone.replace("+", "").split("-", 2);
    }

    private LocalDateTime getCallTime(final String date, final String time) {
        final String fullDateTime = String.format("%s %s", date, time);
        return LocalDateTime.parse(fullDateTime, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

}
