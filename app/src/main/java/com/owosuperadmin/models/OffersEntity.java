package com.owosuperadmin.models;

import java.util.Date;

public class OffersEntity {
    private Long offerId;
    private Date offer_start_date;
    private Date offer_end_date;
    private String offer_is_for;
    private String offer_image;
    private String category;
    private boolean enabled;

    public OffersEntity() {
    }

    public OffersEntity(Date offer_start_date, Date offer_end_date,
                        String offer_is_for, String offer_image,
                        String category, boolean enabled)
    {
        this.offer_start_date = offer_start_date;
        this.offer_end_date = offer_end_date;
        this.offer_is_for = offer_is_for;
        this.offer_image = offer_image;
        this.category = category;
        this.enabled = enabled;
    }

    public Date getOffer_start_date() {
        return offer_start_date;
    }

    public void setOffer_start_date(Date offer_start_date) {
        this.offer_start_date = offer_start_date;
    }

    public Date getOffer_end_date() {
        return offer_end_date;
    }

    public void setOffer_end_date(Date offer_end_date) {
        this.offer_end_date = offer_end_date;
    }

    public String getOffer_is_for() {
        return offer_is_for;
    }

    public void setOffer_is_for(String offer_is_for) {
        this.offer_is_for = offer_is_for;
    }

    public String getOffer_image() {
        return offer_image;
    }

    public void setOffer_image(String offer_image) {
        this.offer_image = offer_image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getOfferId() {
        return offerId;
    }
}
