package com.zhuxun.dmc.apim.service.impl;

import com.zhuxun.dmc.apim.domain.common.Affected;
import com.zhuxun.dmc.apim.domain.model.ModelDefinition;
import com.zhuxun.dmc.apim.dto.model.ModelDefinitionCreation;
import com.zhuxun.dmc.apim.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ModelDefinitionService extends AbstractService {

  /**
   * 新增模型定义
   *
   * @param modelDefinitionCreation
   * @return
   */
  public ModelDefinition addModelById(ModelDefinitionCreation modelDefinitionCreation) {
    return null;
//    DcApiModelDefinition definition = modelDefinitionCreation.of();
//    definition.setId(UUIDUtils.getUUID());
//    definition.setCreateDatetime(new Date());
//    definition.setCreateUserid(getUserId());
//    dcApiModelDefinitionMapper.insert(definition);
//    return getModelById(definition.getId());
  }

  /**
   * 通过modele获取model
   *
   * @param modelId
   * @return
   */
  @Transactional(readOnly = true)
  public ModelDefinition getModelById(String modelId) {
    return null;
//    Preconditions.checkArgument(!Strings.isNullOrEmpty(modelId), "查询模型定义失败,环境ID不能为空");
//    DcApiModelDefinitionKey key = new DcApiModelDefinitionKey();
//    key.setId(modelId);
//    DcApiModelDefinition modelDefinition = dcApiModelDefinitionMapper.selectByPrimaryKey(key);
//    if (modelDefinition == null || modelDefinition.getId() == null) {
//      throw new NotFoundException("查询模型失败,没有发现此模型信息");
//    }
//    return VModelDefinition.of(modelDefinition);
  }

  /**
   * 删除模型定义
   *
   * @param modelId
   * @return
   */
  public Affected deleteModelDefinition(String modelId) {
    return null;
//    Preconditions.checkArgument(!Strings.isNullOrEmpty(modelId), "删除环境失败,环境ID不能为空");
//    DcApiModelDefinitionKey key = new DcApiModelDefinitionKey();
//    key.setId(modelId);
//    int affect = dcApiModelDefinitionMapper.deleteByPrimaryKey(key);
//    return new VAffected(affect);
  }

  /**
   * 更新模型定义
   *
   * @param modelId
   * @param modelDefinition
   * @return
   */
  public ModelDefinition updateModelDefinition(
      String modelId, ModelDefinitionCreation modelDefinition) {

    return null;
//    DcApiModelDefinition dcApiModelDefinition = modelDefinition.of();
//    dcApiModelDefinition.setId(modelId);
//    dcApiModelDefinition.setModifyDatetime(new Date());
//    dcApiModelDefinition.setModifyUserid(getUserId());
//    dcApiModelDefinitionMapper.updateByPrimaryKeySelective(dcApiModelDefinition);
//    return getModelById(modelId);
  }
}
