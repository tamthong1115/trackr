package com.tamthong.trackr.sync;

import com.tamthong.trackr.core.security.UserPrincipal;
import com.tamthong.trackr.sync.dto.SyncPullResponse;
import com.tamthong.trackr.sync.dto.SyncPushRequest;
import com.tamthong.trackr.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/v1/sync")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;

    @PostMapping("/push")
    public ResponseEntity<Void> push(@RequestBody SyncPushRequest request,
                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        syncService.push(request, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pull")
    public ResponseEntity<SyncPullResponse> pull(@RequestParam(required = false) OffsetDateTime since,
                                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        SyncPullResponse response = syncService.pull(user.getId(), since);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reset")
    public ResponseEntity<Void> hardReset(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        syncService.hardReset(user.getId());
        return ResponseEntity.ok().build();
    }
}
