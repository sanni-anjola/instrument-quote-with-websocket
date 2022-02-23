package io.anjola.instrumentswithwebsocket.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class CandleStick {
    private Instant openTimeStamp;
    private Instant closeTimeStamp;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal openPrice;
    private BigDecimal closePrice;

    public CandleStick(Instant openTimeStamp, Instant closeTimeStamp, BigDecimal highPrice, BigDecimal lowPrice, BigDecimal openPrice, BigDecimal closePrice) {
        this.openTimeStamp = openTimeStamp;
        this.closeTimeStamp = closeTimeStamp;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
    }

    public CandleStick() {
    }

    public Instant getOpenTimeStamp() {
        return openTimeStamp;
    }

    public void setOpenTimeStamp(Instant openTimeStamp) {
        this.openTimeStamp = openTimeStamp;
    }

    public Instant getCloseTimeStamp() {
        return closeTimeStamp;
    }

    public void setCloseTimeStamp(Instant closeTimeStamp) {
        this.closeTimeStamp = closeTimeStamp;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    @Override
    public String toString() {
        return "CandleStick{" +
                "openTimeStamp=" + openTimeStamp +
                ", closeTimeStamp=" + closeTimeStamp +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                '}';
    }
}
