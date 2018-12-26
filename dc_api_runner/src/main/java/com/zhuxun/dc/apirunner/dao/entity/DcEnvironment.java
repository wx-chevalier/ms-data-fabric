package com.zhuxun.dc.apirunner.dao.entity;

import java.util.Date;

public class DcEnvironment extends DcEnvironmentKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_environment.env_name
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String envName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_environment.env_value
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String envValue;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_environment.create_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private Date createDatetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_environment.modify_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private Date modifyDatetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_environment.create_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String createUserid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_environment.modify_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String modifyUserid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_environment.remark
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dc_environment.project_id
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    private String projectId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_environment.env_name
     *
     * @return the value of dc_environment.env_name
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getEnvName() {
        return envName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_environment.env_name
     *
     * @param envName the value for dc_environment.env_name
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setEnvName(String envName) {
        this.envName = envName == null ? null : envName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_environment.env_value
     *
     * @return the value of dc_environment.env_value
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getEnvValue() {
        return envValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_environment.env_value
     *
     * @param envValue the value for dc_environment.env_value
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setEnvValue(String envValue) {
        this.envValue = envValue == null ? null : envValue.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_environment.create_datetime
     *
     * @return the value of dc_environment.create_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public Date getCreateDatetime() {
        return createDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_environment.create_datetime
     *
     * @param createDatetime the value for dc_environment.create_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_environment.modify_datetime
     *
     * @return the value of dc_environment.modify_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public Date getModifyDatetime() {
        return modifyDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_environment.modify_datetime
     *
     * @param modifyDatetime the value for dc_environment.modify_datetime
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setModifyDatetime(Date modifyDatetime) {
        this.modifyDatetime = modifyDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_environment.create_userid
     *
     * @return the value of dc_environment.create_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getCreateUserid() {
        return createUserid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_environment.create_userid
     *
     * @param createUserid the value for dc_environment.create_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid == null ? null : createUserid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_environment.modify_userid
     *
     * @return the value of dc_environment.modify_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getModifyUserid() {
        return modifyUserid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_environment.modify_userid
     *
     * @param modifyUserid the value for dc_environment.modify_userid
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setModifyUserid(String modifyUserid) {
        this.modifyUserid = modifyUserid == null ? null : modifyUserid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_environment.remark
     *
     * @return the value of dc_environment.remark
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_environment.remark
     *
     * @param remark the value for dc_environment.remark
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dc_environment.project_id
     *
     * @return the value of dc_environment.project_id
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dc_environment.project_id
     *
     * @param projectId the value for dc_environment.project_id
     *
     * @mbg.generated Wed May 30 15:12:35 CST 2018
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }
}