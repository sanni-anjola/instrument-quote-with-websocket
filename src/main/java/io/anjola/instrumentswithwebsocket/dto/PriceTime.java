package io.anjola.instrumentswithwebsocket.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class PriceTime {
    private BigDecimal price;
    private Instant datetime;

    public PriceTime(BigDecimal price, Instant datetime) {
        this.price = price;
        this.datetime = datetime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "PriceTime{" +
                "price=" + price +
                ", datetime=" + datetime +
                '}';
    }
}
