package com.livevox.challenge.app.callcenter;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CallCenterRepository extends JpaRepository<CallCenter, Long> {

    @Query("SELECT callCenter FROM CallCenter callCenter WHERE LOWER(callCenter.name) = LOWER(:name) AND LOWER" +
           "(callCenter.countryName) = LOWER(:countryName) ")
    Optional<CallCenter> findByNameAndCountryName(@Param("name") final String name,
                                                  @Param("countryName") final String countryName);
}
