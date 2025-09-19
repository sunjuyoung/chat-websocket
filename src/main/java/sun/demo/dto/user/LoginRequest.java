package sun.demo.dto.user;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "사용자명은 필수입니다")
        String username,

        @NotBlank(message = "비밀번호는 필수입니다")
        String password
) { }