package com.springboot.cric.controllers;

import com.springboot.cric.models.Tag;
import com.springboot.cric.services.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.springboot.cric.responses.Response;
import com.springboot.cric.responses.PaginatedResponse;

@RestController
public class TagsController {
    @Autowired
    private TagsService tagsService;

    @GetMapping("/cric/v1/tags")
    public Response getAll(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        List<Tag> tags = tagsService.getAll(page, limit);
        long totalCount = 0L;
        if(page == 1) {
            totalCount = tagsService.getTotalCount();
        }

        PaginatedResponse<Tag> paginatedResponse = new PaginatedResponse<>(totalCount, tags, page, limit);
        return new Response(paginatedResponse);
    }
}