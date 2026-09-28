package com.mams.controller;

import com.mams.repository.*;
import com.mams.entity.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/meta")
public class MetaController {
    private final BaseRepository bases; private final EquipmentTypeRepository types;
    public MetaController(BaseRepository b,EquipmentTypeRepository t){bases=b;types=t;}
    @GetMapping("/bases") public List<Base> bases(){return bases.findAll();}
    @GetMapping("/equipment-types") public List<EquipmentType> equipment(){return types.findAll();}
}
