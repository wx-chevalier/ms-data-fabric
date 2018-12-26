package com.zhuxun.dc.apirunner.dao.entity;

import java.util.Date;

public class DcApiImpl extends DcApiImplKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_api_impl.api_id
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String apiId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_api_impl.api_impl
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String apiImpl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_api_impl.create_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private Date createDatetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_api_impl.modify_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private Date modifyDatetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_api_impl.create_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String createUserid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_api_impl.modify_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String modifyUserid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_api_impl.status
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private Byte status;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_api_impl.api_id
     *
     * @return the value of dc_api_impl.api_id
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getApiId() {
        return apiId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_api_impl.api_id
     *
     * @param apiId the value for dc_api_impl.api_id
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setApiId(String apiId) {
        this.apiId = apiId == null ? null : apiId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_api_impl.api_impl
     *
     * @return the value of dc_api_impl.api_impl
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getApiImpl() {
        return apiImpl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_api_impl.api_impl
     *
     * @param apiImpl the value for dc_api_impl.api_impl
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setApiImpl(String apiImpl) {
        this.apiImpl = apiImpl == null ? null : apiImpl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_api_impl.create_datetime
     *
     * @return the value of dc_api_impl.create_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public Date getCreateDatetime() {
        return createDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_api_impl.create_datetime
     *
     * @param createDatetime the value for dc_api_impl.create_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_api_impl.modify_datetime
     *
     * @return the value of dc_api_impl.modify_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public Date getModifyDatetime() {
        return modifyDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_api_impl.modify_datetime
     *
     * @param modifyDatetime the value for dc_api_impl.modify_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setModifyDatetime(Date modifyDatetime) {
        this.modifyDatetime = modifyDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_api_impl.create_userid
     *
     * @return the value of dc_api_impl.create_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getCreateUserid() {
        return createUserid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_api_impl.create_userid
     *
     * @param createUserid the value for dc_api_impl.create_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid == null ? null : createUserid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_api_impl.modify_userid
     *
     * @return the value of dc_api_impl.modify_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getModifyUserid() {
        return modifyUserid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_api_impl.modify_userid
     *
     * @param modifyUserid the value for dc_api_impl.modify_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setModifyUserid(String modifyUserid) {
        this.modifyUserid = modifyUserid == null ? null : modifyUserid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_api_impl.status
     *
     * @return the value of dc_api_impl.status
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_api_impl.status
     *
     * @param status the value for dc_api_impl.status
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}