package com.zhuxun.dmc.apim.domain.page;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageInfo {

  // 总条数
  Long total;

  // 总页数
  Integer pages;

  // 当前页数
  Integer pageNum;

  // 当前条数
  Integer pageSize;

  // 当前行索引
  Integer startRow;

  // 当前尾部索引
  Integer endRow;

  // 上一页索引
  Integer prePage;

  // 下一页的索引
  Integer nextPage;

  // 是否是首页
  Boolean fristPage;

  // 是否是尾页
  Boolean lastPage;

  // 有上一页
  Boolean hasPreviousPage;

  // 有下一页
  Boolean hasNextPage;
}
