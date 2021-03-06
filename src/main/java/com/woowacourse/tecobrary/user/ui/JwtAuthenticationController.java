package com.woowacourse.tecobrary.user.ui;

import com.woowacourse.tecobrary.user.command.application.UserService;
import com.woowacourse.tecobrary.user.infra.util.JwtUtils;
import com.woowacourse.tecobrary.user.ui.dto.GithubApiResponseDto;
import com.woowacourse.tecobrary.user.ui.vo.UserJwtInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class JwtAuthenticationController {

    private final UserService userService;

    @Autowired
    public JwtAuthenticationController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/token/auth")
    @CrossOrigin(allowedHeaders = {"Authorization"})
    public ResponseEntity authenticate(@RequestHeader("Authorization") final String authorization) {
        String token = authorization.substring(authorization.indexOf(" "));
        String userNo = JwtUtils.getUserIdFromToken(token);
        UserJwtInfoVo userJwtInfoVo = userService.findUserJwtInfoByUserNo(userNo);
        return ResponseEntity.ok(new GithubApiResponseDto(userJwtInfoVo, token));
    }
}
