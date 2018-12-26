package com.zhuxun.dc.apirunner.dao.mapper;

import com.zhuxun.dc.apirunner.dao.entity.DcRoute;
import com.zhuxun.dc.apirunner.dao.entity.DcRouteExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DcRouteMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dc_route
     *
     * @mbg.generated Wed Jun 13 10:53:49 CST 2018
     */
    long countByExample(DcRouteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dc_route
     *
     * @mbg.generated Wed Jun 13 10:53:49 CST 2018
     */
    int deleteByExample(DcRouteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dc_route
     *
     * @mbg.generated Wed Jun 13 10:53:49 CST 2018
     */
    int insert(DcRoute record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dc_route
     *
     * @mbg.generated Wed Jun 13 10:53:49 CST 2018
     */
    int insertSelective(DcRoute record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dc_route
     *
     * @mbg.generated Wed Jun 13 10:53:49 CST 2018
     */
    List<DcRoute> selectByExample(DcRouteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dc_route
     *
     * @mbg.generated Wed Jun 13 10:53:49 CST 2018
     */
    int updateByExampleSelective(@Param("record") DcRoute record, @Param("example") DcRouteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dc_route
     *
     * @mbg.generated Wed Jun 13 10:53:49 CST 2018
     */
    int updateByExample(@Param("record") DcRoute record, @Param("example") DcRouteExample example);
}