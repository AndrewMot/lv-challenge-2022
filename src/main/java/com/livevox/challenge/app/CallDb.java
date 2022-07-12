package com.livevox.challenge.app;

import com.livevox.challenge.app.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallDb extends JpaRepository<Call, Long> {

}
