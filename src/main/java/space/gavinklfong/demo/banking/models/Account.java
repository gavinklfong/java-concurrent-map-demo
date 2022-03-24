package space.gavinklfong.demo.banking.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Account {
    String accountNumber;
    Double balance = 0D;
}
