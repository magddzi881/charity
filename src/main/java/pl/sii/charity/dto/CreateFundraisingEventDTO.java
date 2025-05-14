package pl.sii.charity.dto;

import pl.sii.charity.entity.CurrencyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateFundraisingEventDTO {

    @NotBlank(message = "Event name cannot be blank")
    public String name;

    @NotNull(message = "Currency cannot be null")
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
