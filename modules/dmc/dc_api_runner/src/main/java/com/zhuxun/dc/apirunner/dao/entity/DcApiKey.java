package com.zhuxun.dc.apirunner.dao.entity;

public class DcApiKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_api.id
     *
     * @mbg.generated Wed May 30 15:17:29 CST 2018
     */
    private String id;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_api.id
     *
     * @return the value of dc_api.id
     *
     * @mbg.generated Wed May 30 15:17:29 CST 2018
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_api.id
     *
     * @param id the value for dc_api.id
     *
     * @mbg.generated Wed May 30 15:17:29 CST 2018
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }
}