package com.rozangelapm.readmatch.controller;


import com.rozangelapm.readmatch.dto.BookWithReadingResponse;
import com.rozangelapm.readmatch.dto.UpdateReadingResquest;
import com.rozangelapm.readmatch.service.ReadingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/readings")
public class ReadingController {

    private final ReadingService readingService;

    public ReadingController(ReadingService readingService) {
        this.readingService = readingService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchReading(@PathVariable("id") String id,
                                             @Valid @RequestBody UpdateReadingResquest updateReading){

        readingService.updateReading(id, updateReading);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookWithReadingResponse> getReading(@PathVariable("id") String id){

        BookWithReadingResponse reading = readingService.findReadingById(id);

        return ResponseEntity.ok(reading);
    }

    @GetMapping
    public ResponseEntity<List<BookWithReadingResponse>> getAllReadings(
            @RequestParam(name = "status", required = false) String status){

        return ResponseEntity.ok(readingService.findAllReadings(status));
    }

}
