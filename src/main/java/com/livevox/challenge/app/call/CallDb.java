package com.livevox.challenge.app.call;

import com.livevox.challenge.app.call.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallDb extends JpaRepository<Call, Long> {

}
