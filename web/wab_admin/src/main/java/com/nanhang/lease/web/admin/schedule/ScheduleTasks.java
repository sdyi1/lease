package com.nanhang.lease.web.admin.schedule;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nanhang.lease.model.entity.LeaseAgreement;
import com.nanhang.lease.model.enums.LeaseStatus;
import com.nanhang.lease.web.admin.service.LeaseAgreementService;
import com.nanhang.lease.web.admin.vo.agreement.AgreementVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.Date;

@Component
public class ScheduleTasks {

    @Autowired
    private LeaseAgreementService leaseAgreementService;

    @Scheduled(cron = "0 0 0 * * *")//每天凌晨0点执行
    public void checkLeaseStatus() {
        //检查租约时间是否到期，到期更行状态
        LambdaUpdateWrapper<LeaseAgreement> AgreementLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        AgreementLambdaUpdateWrapper.le(LeaseAgreement::getLeaseEndDate,new Date());
        AgreementLambdaUpdateWrapper.set(LeaseAgreement::getStatus, LeaseStatus.EXPIRED);
        leaseAgreementService.update(AgreementLambdaUpdateWrapper);


    }
}
