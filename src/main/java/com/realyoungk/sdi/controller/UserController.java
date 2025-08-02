package com.realyoungk.sdi.controller;

import com.realyoungk.sdi.model.UserModel;
import com.realyoungk.sdi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * ✅ 여러 사용자를 한번에 생성하는 Bulk API
     */
    @PostMapping
    public ResponseEntity<List<UserModel>> createUsers(@RequestBody List<UserModel> body) {
        List<UserModel> createdUsers = userService.createUsers(body);
        // 생성된 리소스의 위치를 Location 헤더에 담아 201 Created 응답 반환
        return ResponseEntity
                .created(URI.create("/api/v1/users"))
                .body(createdUsers);
    }

    // 사용자 생성 (POST)
    @PostMapping("/new")
    public ResponseEntity<UserModel> createUsersNew(@RequestBody UserModel body) {
        UserModel createdUser = userService.createUser(body);
        // 생성된 리소스의 위치를 Location 헤더에 담아 201 Created 응답 반환
        return ResponseEntity
                .created(URI.create("/api/v1/users/" + createdUser.getId()))
                .body(createdUser);
    }

    // 사용자 조회 (GET)
    @GetMapping("/{publicId}")
    public ResponseEntity<UserModel> getUserByPublicId(@PathVariable String publicId) {
        return userService.findUserByPublicId(publicId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 모든 사용자 조회 (GET)
    @GetMapping
    public ResponseEntity<List<UserModel>> getUsers(
            @RequestParam(name = "birthday", required = false)
            String birthdayFilter
    ) {
        List<UserModel> users;

        if ("today".equalsIgnoreCase(birthdayFilter)) {
            users = userService.findUsersWithBirthdayOn(LocalDate.now());
        } else if ("this-month".equalsIgnoreCase(birthdayFilter)) {
            users = userService.findUsersWithBirthdayIn(LocalDate.now());
        } else {
            users = userService.findAllUsers();
        }

        return ResponseEntity.ok(users);
    }
}