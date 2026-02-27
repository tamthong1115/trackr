package com.tamthong.trackr.core.importer;

import com.tamthong.trackr.core.exception.ResourceNotFoundException;
import com.tamthong.trackr.core.importer.dto.ImportDataDto;
import com.tamthong.trackr.core.security.UserPrincipal;
import com.tamthong.trackr.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/import")
@RequiredArgsConstructor
public class ImportController {
    private final ImportService importService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> importData(@RequestParam("file") MultipartFile file,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        User user = userPrincipal.getUser();
        if (user == null) {
            throw new ResourceNotFoundException("User not found in security context");
        }

        ImportDataDto data = objectMapper.readValue(file.getInputStream(), ImportDataDto.class);

        importService.importData(data, user.getId());

        return ResponseEntity.ok().build();
    }
}
