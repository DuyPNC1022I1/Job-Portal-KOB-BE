package com.example.findjobbe.service.ApplyJob;

import com.example.findjobbe.model.*;
import com.example.findjobbe.repository.ApplyJobRepository;
import com.example.findjobbe.service.CompanyService;
import com.example.findjobbe.service.ICoreService;
import com.example.findjobbe.service.UserService;
import com.example.findjobbe.service.jobs.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ApplyJobService implements ICoreService<ApplyJob> {
    @Autowired
    ApplyJobRepository applyJobRepository;
    @Autowired
    UserService userService;
    @Autowired
    JobService jobService;
    @Autowired
    CompanyService companyService;


    @Override
    public Page<ApplyJob> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public ApplyJob findOne(Long id) {
        return applyJobRepository.findById(id).orElse(null);
    }

    @Override
    public void save(ApplyJob applyJob) {
        applyJobRepository.save(applyJob);
    }

    @Override
    public void delete(Long id) {
        applyJobRepository.deleteById(id);

    }
    public boolean applyJob(Long jobId,Long userId){
        User user = userService.findOne(userId);
        Job job = jobService.findOne(jobId);
        ApplyJob applyJobFind = applyJobRepository.findApplyJobByJob_IdAndUser_Id(jobId,userId);
        if (user!=null && job!=null && applyJobFind==null){
            ApplyJob applyJob = new ApplyJob();
            applyJob.setUser(user);
            applyJob.setJob(job);
            applyJob.setStatus("Pending");
            applyJobRepository.save(applyJob);
            Notification notification = new Notification();
            String text = user.getAccount().getName() + "applied for"+ job.getCareer().getName()
                           +job.getCompany().getAccount().getName() +", please check.";
            notification.setText(text);
            notification.setStatus(true);
            return true;
        }
        return false;
    }

    public boolean CancelApplyJob(Long userId,Long jobId){
        User user = userService.findOne(userId);
        Job job = jobService.findOne(jobId);
        ApplyJob applyJobFind = applyJobRepository.findApplyJobByJob_IdAndUser_Id(jobId,userId);
        if (user!=null && job!=null && applyJobFind != null){
                applyJobFind.setStatus("Canceled");
                applyJobRepository.save(applyJobFind);
                Notification notification = new Notification();
                String text = user.getAccount().getName() + " canceled "+ job.getCareer().getName()
                        + job.getCompany().getAccount().getName() + " !";
                notification.setText(text);
                notification.setStatus(true);
                return true;
        }
        return false;
    }



}
