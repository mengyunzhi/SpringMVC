package com.mengyunzhi.controller;

import com.mengyunzhi.repository.Klass;
import com.mengyunzhi.service.KlassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by panjie on 17/4/13.
 */
@RestController
@RequestMapping("/klass")
public class KlassController {
    @Autowired
    private KlassService klassService;

    @PostMapping("/")
    public Klass save(@RequestParam String name, @RequestParam Long teacherId) {
        return klassService.save(name, teacherId);
    }

    @GetMapping("/{id}")
    public Klass get(@PathVariable Long id) {
        return klassService.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        klassService.delete(id);
        return;
    }

    @PutMapping("/{id}")
    public Klass update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam Long teacherId) {
        return klassService.update(id, name, teacherId);
    }
}
