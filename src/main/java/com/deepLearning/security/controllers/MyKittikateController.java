package com.deepLearning.security.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kit")
@RequiredArgsConstructor
public class MyKittikateController {

    @GetMapping(value = "/kat",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> kat() {
     Resource image = new ClassPathResource("static/photo.jpg");
     return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }


}
