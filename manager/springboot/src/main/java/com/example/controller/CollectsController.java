package com.example.controller;

import com.example.common.Result;
import com.example.entity.Likes;
import com.example.service.LikesService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes")
public class LikesController {
    @Resource
    private LikesService likesService;

    @PostMapping("/changeLikes")
    public Result changeLikes(@RequestBody Likes likes) {
        likesService.changeLikes(likes);
        return Result.success();
    }
}
