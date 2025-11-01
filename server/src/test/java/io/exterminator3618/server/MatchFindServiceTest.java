package io.exterminator3618.server;

import io.exterminator3618.server.models.UserInfo;
import io.exterminator3618.server.models.UserStatistics;
import io.exterminator3618.server.services.MatchFindService;
import io.exterminator3618.server.utils.ForbiddenAction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class MatchFindServiceTest {

    public final MatchFindService matchFindService = new MatchFindService(null);

    @Test
    public void testMatch2Users() throws InterruptedException {
        matchFindService.joinMatchQueue(1, new UserInfo(
                "1", "TestMatch2Users1", false, LocalDateTime.now(),
                new UserStatistics(0, 0, 0)
        ));
        matchFindService.joinMatchQueue(2, new UserInfo(
                "2", "TestMatch2Users2", false, LocalDateTime.now(),
                new UserStatistics(0, 0, 0)
        ));
        Thread.sleep(1000);
        // worker thread should throw null pointer exception if match is not found
        assertThrows(ForbiddenAction.class, () -> {
            matchFindService.leaveMatchQueue(10);
        });
        assertThrows(ForbiddenAction.class, () -> {
            matchFindService.leaveMatchQueue(10);
        });
    }

    @Test
    public void testUserTimeoutInQueue() throws InterruptedException {
        var user = new UserInfo(
                "10", "TestUserTimeoutInQueue", false, LocalDateTime.now(),
                new UserStatistics(0, 0, 0)
        );
        matchFindService.joinMatchQueue(10, user);
        Thread.sleep(MatchFindService.MATCH_QUEUE_TIMEOUT_SECONDS * 1000 + 500);
        assertThrows(ForbiddenAction.class, () -> {
            matchFindService.leaveMatchQueue(10);
        });
    }

    @Test
    public void testUserJoinQueueTwice() {
        var user = new UserInfo(
                "11", "TestUserTimeoutInQueue", false, LocalDateTime.now(),
                new UserStatistics(0, 0, 0)
        );
        matchFindService.joinMatchQueue(11, user);
        assertThrows(ForbiddenAction.class, () -> {
            matchFindService.joinMatchQueue(11, user);
        });
        matchFindService.leaveMatchQueue(11);
    }

    // @Test
    // public void testUserLeaveAfterAcceptedMatch() throws InterruptedException {
    //     matchFindService.joinMatchQueue(21, new UserInfo(
    //             "21", "testUserLeaveAfterAcceptedMatch1", false, LocalDateTime.now(),
    //             new UserStatistics(0, 0, 0)
    //     ));
    //     matchFindService.joinMatchQueue(22, new UserInfo(
    //             "22", "testUserLeaveAfterAcceptedMatch2", false, LocalDateTime.now(),
    //             new UserStatistics(0, 0, 0)
    //     ));
    //     Thread.sleep(1000);
    //     assertTrue(matchFindService.isMatchFound(21));
    //     assertTrue(matchFindService.isMatchFound(22));
    //     matchFindService.acceptMatch(21);
    //     assertThrows(ForbiddenAction.class, () -> {
    //         matchFindService.leaveMatchQueue(21);
    //     });
    //     matchFindService.leaveMatchQueue(22);
    //     assertFalse(matchFindService.isMatchFound(21));
    // }

}
