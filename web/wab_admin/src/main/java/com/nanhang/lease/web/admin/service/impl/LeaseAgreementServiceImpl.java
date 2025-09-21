package com.nanhang.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.model.entity.*;
import com.nanhang.lease.web.admin.mapper.*;
import com.nanhang.lease.web.admin.service.LeaseAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanhang.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.nanhang.lease.web.admin.vo.agreement.AgreementVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    //注入租约mapper
    @Autowired
    private LeaseAgreementMapper leaseAgreementMapper;
    //注入公寓mapper
    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;
    //注入房间信息mapper
    @Autowired
    private RoomInfoMapper roomInfoMapper;
    //注入支付方式mapper
    @Autowired
    private PaymentTypeMapper paymentTypeMapper;
    //注入租期Mapper
    @Autowired
    private LeaseTermMapper leaseTermMapper;
    //分页查询
    @Override
    public IPage<AgreementVo> selectByPage(Page page, AgreementQueryVo queryVo) {
        return leaseAgreementMapper.selectByPage(page,queryVo);
    }

    @Override
    public AgreementVo selectByIdDyi(Long id) {

        //根据id查找租约对象
        LeaseAgreement leaseAgreement = leaseAgreementMapper.selectById(id);
        //根据租约对象中的公寓id查找公寓对象
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(leaseAgreement.getApartmentId());
        //根据租约对象中的房间id查找房间对象
        RoomInfo roomInfo = roomInfoMapper.selectById(leaseAgreement.getRoomId());
        //根据租约对象中的支付方式id查找支付方式对象
        PaymentType paymentType = paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId());
        //根据租约对象中的租期id查找租期对象
        LeaseTerm leaseTerm = leaseTermMapper.selectById(leaseAgreement.getLeaseTermId());

        //创建AgreementVo对象
        AgreementVo agreementVo = new AgreementVo();

        //使用BeanUtils将对象leaseAgreement中的属性复制到AgreementVo对象中
        BeanUtils.copyProperties(leaseAgreement,agreementVo);
        //将公寓对象、房间对象、支付方式对象、租期对象设置到AgreementVo对象中
        agreementVo.setApartmentInfo(apartmentInfo);
        agreementVo.setRoomInfo(roomInfo);
        agreementVo.setPaymentType(paymentType);
        agreementVo.setLeaseTerm(leaseTerm);

        return agreementVo;
    }
}




