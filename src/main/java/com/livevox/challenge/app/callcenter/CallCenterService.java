package com.livevox.challenge.app.callcenter;

import java.util.Optional;

import com.livevox.challenge.app.response.Constants;
import com.livevox.challenge.app.response.exceptions.BadRequestException;
import com.livevox.challenge.app.response.exceptions.ConflictException;
import com.livevox.challenge.app.response.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CallCenterService {

    private final CallCenterRepository callCenterRepository;

    @Autowired
    public CallCenterService(final CallCenterRepository callCenterRepository) {
        this.callCenterRepository = callCenterRepository;
    }

    public CallCenter create(final CallCenter callCenter) {
        if (!StringUtils.hasLength(callCenter.getName())) {
            throw new BadRequestException(Constants.CALL_CENTER_NAME_REQUIRED_MESSAGE);
        }
        if (!StringUtils.hasLength(callCenter.getCountryName())) {
            throw new BadRequestException(Constants.CALL_CENTER_COUNTRY_NAME_REQUIRED_MESSAGE);
        }
        final Optional<CallCenter> duplicatedNameInCountry =
            callCenterRepository.findByNameAndCountryName(callCenter.getName(), callCenter.getCountryName());
        if (duplicatedNameInCountry.isPresent()) {
            throw new ConflictException(Constants.CALL_CENTER_UNIQUE_NAME_MESSAGE);
        }
        return callCenterRepository.save(callCenter);
    }

    public CallCenter retrieve(final Long id) {
        return callCenterRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Constants.CALL_CENTER_NOT_FOUND_MESSAGE));
    }
}
