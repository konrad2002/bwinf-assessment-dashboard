package net.myplayplanet.bwinfbackend.controller;


import lombok.RequiredArgsConstructor;
import net.myplayplanet.bwinfbackend.dto.EvaluationDto;
import net.myplayplanet.bwinfbackend.service.integration.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationController {

    private final IntegrationService integrationService;

    @PostMapping("evaluation")
    public void publishNewEvaluation(@RequestBody EvaluationDto evaluationDto) {
        this.integrationService.insertEvaluation(evaluationDto);
    }

}
