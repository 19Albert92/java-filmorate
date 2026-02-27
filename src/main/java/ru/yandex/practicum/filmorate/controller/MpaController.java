package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.util.validate.CommonValidate;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping()
    public List<Mpa> getMpa() {
        return mpaService.findAll();
    }

    @GetMapping("/{mpaId}")
    public Mpa getMpa(@PathVariable int mpaId) {

        CommonValidate.checkNotNullAndPositive(mpaId, "Параметр mpaId должен быть положительным");

        return mpaService.findById(mpaId);
    }
}
