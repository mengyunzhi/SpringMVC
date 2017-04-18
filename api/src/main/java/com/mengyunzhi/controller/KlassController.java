package com.mengyunzhi.controller;

import com.mengyunzhi.service.KlassService;
import com.mengyunzhi.repository.Klass;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;


/**
 * Created by panjie on 17/4/13.
 */
@RestController
@RequestMapping("/klass")
public class KlassController {
    private final static Logger logger = LoggerFactory.getLogger(KlassController.class);

    @Autowired
    private KlassService klassService;

    @PostMapping("/")
    public Klass save(@Valid @RequestBody JsonInput jsonInput) {
        return klassService.save(jsonInput.getName(), jsonInput.getTeacherId());
    }

    @GetMapping("/{id}")
    public Klass get(@PathVariable Long id) {
        return klassService.get(id);
    }

    @PutMapping("/{id}")
    public Klass update(@PathVariable Long id,@Valid @RequestBody JsonInput jsonInput) {
        return klassService.update(id, jsonInput.getName(), jsonInput.getTeacherId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Klass> delete(@PathVariable Long id) {
        try {
            klassService.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<Klass>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Klass>(new HttpHeaders(), HttpStatus.OK);
    }

    private static class JsonInput {
        @NotEmpty
        @Size(max = 10)
        private String name;
        private Long TeacherId;

        public JsonInput() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getTeacherId() {
            return TeacherId;
        }

        public void setTeacherId(Long teacherId) {
            TeacherId = teacherId;
        }
    }
}
