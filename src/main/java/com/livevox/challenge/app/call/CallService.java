package com.livevox.challenge.app.call;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

    @Scheduled(cron = "@hourly")
    public void load() {
        vendorClient.fetch()
            .getCalls()
            .stream()
            .map(this::mapFromVendor)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(callRepository::save);
    }

    private Optional<Call> mapFromVendor(final VendorResponse.VendorCall vendorCall) {
        if (!StringUtils.hasLength(vendorCall.getPhone())) {
            final String message =
                "The call with id {} couldn't be imported due phone is empty . Agent Extension: {}, Date: {}, Time: " +
                "{}, " +
                "Full Name: {} {}";
            log.warn(message,
                     vendorCall.getId(),
                     vendorCall.getAgentExtension(),
                     vendorCall.getCallDate(),
                     vendorCall.getCallTime(),
                     vendorCall.getFirstName(),
                     vendorCall.getLastName());
            return Optional.empty();
        }
        return Optional.of(createFromVendor(vendorCall));
    }

    private Call createFromVendor(final VendorResponse.VendorCall vendorCall) {
        final Call call = new Call();
        call.setId(vendorCall.getId());
        call.setAgentExtension(vendorCall.getAgentExtension());
        final String[] phoneNumberParts = getPhoneParts(vendorCall.getPhone());
        call.setCustomerCountryPhoneCode(Integer.parseInt(phoneNumberParts[0]));
        call.setCustomerPhone(phoneNumberParts[1]);
        final String fullName = String.format("%s %s", vendorCall.getFirstName(), vendorCall.getLastName());
        call.setCustomerFullName(fullName);
        final LocalDateTime receivedOn = getCallTime(vendorCall.getCallDate(), vendorCall.getCallTime());
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
