package com.livevox.challenge.app.call;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CallRepository extends JpaRepository<Call, Long> {

    @Query(value =
        "SELECT COUNT(*) AS count, ca.customer_country_phone_code AS countryCode, c.country_name AS countryName FROM " +
        "calls ca JOIN agents a ON ca.agent_extension = a.extension JOIN call_centers c ON c.id = a.call_center_id " +
        "WHERE a.active = true GROUP BY ca.customer_country_phone_code, c.country_name ",
        nativeQuery = true)
    List<CallCountPerCountry> getCallsRoutedPerCountry();

}
