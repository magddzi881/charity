package pl.sii.charity.dto;

import pl.sii.charity.entity.CurrencyType;
import java.math.BigDecimal;

public class FinancialReportDTO {
    public String eventName;
    public BigDecimal amount;
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
