package pl.sii.charity.dto;

import pl.sii.charity.entity.CurrencyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class FinancialReportDTO {

    @NotBlank(message = "Event name cannot be blank")
    public String eventName;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Amount must be greater than 0")
    public BigDecimal amount;

    @NotNull(message = "Currency cannot be null")
    public CurrencyType currency;

    public FinancialReportDTO(String eventName, BigDecimal amount, CurrencyType currency) {
        this.eventName = eventName;
        this.amount = amount;
        this.currency = currency;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }
}
