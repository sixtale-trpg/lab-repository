package org.infinity.sixtalebackend.domain.rule.controller;

import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.rule.dto.JobListResponse;
import org.infinity.sixtalebackend.domain.rule.dto.JobOptionListResponse;
import org.infinity.sixtalebackend.domain.rule.service.RuleService;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rules")
@AllArgsConstructor
public class RuleController {

    private final RuleService ruleServiceImpl;

    @GetMapping("/{ruleID}")
    public ResponseEntity readRule(@PathVariable Long ruleID) {
        try {

            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.READ_RULE), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_RULE_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{ruleID}/jobs")
    public ResponseEntity readJobList(@PathVariable Long ruleID) {
        try {
            JobListResponse response = ruleServiceImpl.readJobList(ruleID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.READ_RULE_JOB, response), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_RULE_JOB_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{ruleID}/jobs/{jobID}")
    public ResponseEntity readJobOptionList(@PathVariable Long ruleID, @PathVariable Long jobID) {
        try {
            JobOptionListResponse response = ruleServiceImpl.readJobOptionList(ruleID, jobID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.READ_JOB_OPTION, response), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_JOB_OPTION_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
