package pl.sii.charity.dto;

import pl.sii.charity.entity.CurrencyType;

public class CreateFundraisingEventDTO {
    public String name;
    public CurrencyType currency;

    public CreateFundraisingEventDTO(String name, CurrencyType currency) {
        this.name = name;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }
}
