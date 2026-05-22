package com.muciociosan.theproject.applicationlogic.userarchiving.job;

import com.muciociosan.theproject.applicationlogic.userarchiving.UserArchivingProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserArchivingScheduledJob {
    private final UserArchivingProcess userArchivingProcess;

    // TODO This is a concept of Job that is executed in scheduled manner; bridge between infra(scheduler) and business logic (UserAchivingProcess)
}
