package com.zhuxun.dc.apirunner.dao.entity;

import java.util.Date;

public class DcDataSource extends DcDataSourceKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_data_source.data_source_name
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String dataSourceName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_data_source.create_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private Date createDatetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_data_source.modify_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private Date modifyDatetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_data_source.create_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String createUserid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_data_source.modify_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String modifyUserid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_data_source.status
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private Byte status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_data_source.remark
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_data_source.data_source_type
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String dataSourceType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_data_source.data_source_config
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String dataSourceConfig;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_data_source.data_source_name
     *
     * @return the value of dc_data_source.data_source_name
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_data_source.data_source_name
     *
     * @param dataSourceName the value for dc_data_source.data_source_name
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName == null ? null : dataSourceName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_data_source.create_datetime
     *
     * @return the value of dc_data_source.create_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public Date getCreateDatetime() {
        return createDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_data_source.create_datetime
     *
     * @param createDatetime the value for dc_data_source.create_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_data_source.modify_datetime
     *
     * @return the value of dc_data_source.modify_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public Date getModifyDatetime() {
        return modifyDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_data_source.modify_datetime
     *
     * @param modifyDatetime the value for dc_data_source.modify_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setModifyDatetime(Date modifyDatetime) {
        this.modifyDatetime = modifyDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_data_source.create_userid
     *
     * @return the value of dc_data_source.create_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getCreateUserid() {
        return createUserid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_data_source.create_userid
     *
     * @param createUserid the value for dc_data_source.create_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid == null ? null : createUserid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_data_source.modify_userid
     *
     * @return the value of dc_data_source.modify_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getModifyUserid() {
        return modifyUserid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_data_source.modify_userid
     *
     * @param modifyUserid the value for dc_data_source.modify_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setModifyUserid(String modifyUserid) {
        this.modifyUserid = modifyUserid == null ? null : modifyUserid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_data_source.status
     *
     * @return the value of dc_data_source.status
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_data_source.status
     *
     * @param status the value for dc_data_source.status
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_data_source.remark
     *
     * @return the value of dc_data_source.remark
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_data_source.remark
     *
     * @param remark the value for dc_data_source.remark
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_data_source.data_source_type
     *
     * @return the value of dc_data_source.data_source_type
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getDataSourceType() {
        return dataSourceType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_data_source.data_source_type
     *
     * @param dataSourceType the value for dc_data_source.data_source_type
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType == null ? null : dataSourceType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_data_source.data_source_config
     *
     * @return the value of dc_data_source.data_source_config
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getDataSourceConfig() {
        return dataSourceConfig;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_data_source.data_source_config
     *
     * @param dataSourceConfig the value for dc_data_source.data_source_config
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setDataSourceConfig(String dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig == null ? null : dataSourceConfig.trim();
    }
}