package com.example.findjobbe.controller;

import com.example.findjobbe.model.Job;
import com.example.findjobbe.service.jobs.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

//    @GetMapping
//    public ResponseEntity<Page<Job>> findAll(@PageableDefault(page = 5) Pageable pageable) {
//        return new ResponseEntity<> (jobService.findAll(pageable), HttpStatus.OK);
//    }
    @GetMapping
    public ResponseEntity<List<Job>> findAllTest() {
        return new ResponseEntity<> (jobService.findAllTest(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> findOne(@PathVariable Long id) {
        if (jobService.findOne(id) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(jobService.findOne(id), HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Job job) {
        job.setStartDate(java.time.LocalDate.now());
        try {
            jobService.save(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Get data listJob by Company
    @GetMapping("/findByCompany/{id}")
    public ResponseEntity<List<Job>> findAllByCompany(@PathVariable Long id) {
        return new ResponseEntity<>(jobService.findAllByCompany(id), HttpStatus.OK);
    }

    //Block Job
    @GetMapping("/blockOrUnlockJob/{id}")
    public ResponseEntity<List<Job>> blockOrUnlockJob(@PathVariable Long id) {
        Job jobBlock = jobService.findOne(id);
        if (jobBlock.getStatus()) {
            jobBlock.setStatus(false); //block job
        } else {
            jobBlock.setStatus(true); //unlock job
        }
        jobService.save(jobBlock);
        return new ResponseEntity<>(jobService.findAllTest(), HttpStatus.OK);
    }

    //Unlock job
    @GetMapping("/unlockJob/{id}")
    public ResponseEntity<List<Job>> unlockJob(@PathVariable Long id) {
        Job jobUnlock = jobService.findOne(id);
        jobUnlock.setStatus(true);
        jobService.save(jobUnlock);
        return new ResponseEntity<>(jobService.findAllTest(), HttpStatus.OK);
    }
}
