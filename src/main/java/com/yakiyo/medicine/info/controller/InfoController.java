package com.yakiyo.medicine.info.controller;

import com.yakiyo.medicine.info.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medicine")
public class InfoController {

    private final InfoService infoService;

    //평생 약 등록


}
