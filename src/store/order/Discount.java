package store.order;

import java.math.BigDecimal;

public interface Discount {
    BigDecimal applyDiscount(BigDecimal amount);

    static Discount blackFridayDiscount() {
        return amount -> amount.multiply(BigDecimal.valueOf(0.5));
    }

    static Discount employeeDiscount() {
        return amount -> amount.multiply(BigDecimal.valueOf(0.3));
    }

    static Discount tenPercentDiscount() {
        return amount -> amount.multiply(BigDecimal.valueOf(0.1));
    }
}
