package com.supreme.admin.controller.api;

import com.supreme.admin.model.dto.PenaltyDTO;
import com.supreme.admin.model.network.Header;
import com.supreme.admin.service.PenaltyApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/penalty")
@RequiredArgsConstructor
public class PenaltyApiController {
    private final PenaltyApiLogicService penaltyApiLogicService;

    @PostMapping("")
    public Header<PenaltyDTO> create(@RequestBody Header<PenaltyDTO> request){
        return penaltyApiLogicService.create(request.getData());
    }

    @GetMapping("{idx}")
    public PenaltyDTO read(@PathVariable(name="idx") Long idx){
        return penaltyApiLogicService.detail(idx);
    }

    @DeleteMapping("{idx}")
    public Header delete(@PathVariable(name="idx") Long idx){
        return penaltyApiLogicService.delete(idx);
    }
}