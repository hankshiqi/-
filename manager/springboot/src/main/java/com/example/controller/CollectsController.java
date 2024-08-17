package com.example.controller;

import com.example.common.Result;
import com.example.entity.Collects;
import com.example.service.CollectsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collects")
public class CollectsController {
    @Resource
    private CollectsService collectsService;

    @PostMapping("/changeCollects")
    public Result changeCollects(@RequestBody Collects collects) {
        collectsService.changeCollects(collects);
        return Result.success();
    }

}
