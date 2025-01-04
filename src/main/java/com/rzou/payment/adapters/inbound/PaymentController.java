package com.rzou.payment.adapters.inbound;

import com.rzou.payment.application.commands.CreatePaymentCommand;
import com.rzou.payment.application.queries.UpdatePaymentStatusCommand;
import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.ports.inbound.CreatePaymentUseCase;
import com.rzou.payment.ports.inbound.GetPaymentStatusUseCase;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private CreatePaymentUseCase createPaymentUseCase;

    @Autowired
    private GetPaymentStatusUseCase getPaymentStatusUseCase;

    @Autowired
    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;

    /**
     * Create a new payment
     *
     * @param createPaymentCommand Payment creation command
     * @return Payment transaction ID
     */
    @PostMapping("/create")
    public BaseResponse<String> createPayment(@RequestBody CreatePaymentCommand createPaymentCommand) {
        return createPaymentUseCase.createPayment(createPaymentCommand);
    }

    /**
     * Get payment status by transaction ID
     *
     * @param transactionId Payment transaction ID
     * @return Payment status code
     */
    @GetMapping("/status/{transactionId}")
    public BaseResponse<Integer> getPaymentStatus(@PathVariable String transactionId) {
        return getPaymentStatusUseCase.getPaymentStatus(transactionId);
    }

    @PutMapping("/status/{transactionId}")
    public BaseResponse<Boolean> updatePaymentStatus(
            @PathVariable String transactionId,
            @RequestBody UpdatePaymentStatusCommand command) {
        command.setTransactionId(transactionId);
        boolean result = updatePaymentStatusUseCase.updatePaymentStatus(command);
        return new BaseResponse<>(0, result, "Success");
    }
}
