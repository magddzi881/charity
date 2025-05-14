package pl.sii.charity.dto;

import pl.sii.charity.entity.CurrencyType;
import java.math.BigDecimal;

public class AddMoneyDTO {
    public CurrencyType currency;
    public BigDecimal amount;

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
