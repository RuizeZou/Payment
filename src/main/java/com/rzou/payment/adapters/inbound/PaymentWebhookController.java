package com.rzou.payment.adapters.inbound;

import com.rzou.payment.application.commands.UpdatePaymentStatusCommand;
import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class PaymentWebhookController {

    @Autowired
    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;

    /**
     * Handle payment status update webhook
     *
     * @param command Update payment status command
     * @return Response indicating success or failure
     */
    @PostMapping("/payment-status")
    public BaseResponse<Boolean> handlePaymentStatusUpdate(@RequestBody UpdatePaymentStatusCommand command) {
        boolean result = updatePaymentStatusUseCase.updatePaymentStatus(command);
        return new BaseResponse<>(0, result, "Success");
        //这个就是支付宝回调的地址，然后我会根据支付宝返回的结果来决定调用哪个结果handler
    }
}