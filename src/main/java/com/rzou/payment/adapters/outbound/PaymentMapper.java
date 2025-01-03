package com.rzou.payment.adapters.outbound;

import com.rzou.payment.domain.valueobj.PaymentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author rzzzzzzz
* @description 针对表【payment(支付交易流水表)】的数据库操作Mapper
* @createDate 2025-01-03 23:55:35
* @Entity generator.domain.Payment
*/
@Mapper
public interface PaymentMapper extends BaseMapper<PaymentVO> {

}




