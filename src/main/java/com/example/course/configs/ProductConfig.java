package com.example.course.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "product.seasonal")
public class ProductConfig {
    private String campaignName;
    private List<Integer> idsPriceDrop;
    private Integer dropPercent;

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public List<Integer> getIdsPriceDrop() {
        return idsPriceDrop;
    }

    public void setIdsPriceDrop(List<Integer> idsPriceDrop) {
        this.idsPriceDrop = idsPriceDrop;
    }

    public Integer getDropPercent() {
        return dropPercent;
    }

    public void setDropPercent(Integer dropPercent) {
        this.dropPercent = dropPercent;
    }
}
