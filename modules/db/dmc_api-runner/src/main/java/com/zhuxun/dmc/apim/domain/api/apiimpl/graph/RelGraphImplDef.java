package com.zhuxun.dmc.apim.domain.api.apiimpl.graph;

import com.zhuxun.dmc.apim.domain.api.apiimpl.APIImplDef;
import com.zhuxun.dmc.apim.domain.api.apiimpl.APIImplType;
import com.zhuxun.dmc.apim.domain.api.apiimpl.SQLImplDef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class RelGraphImplDef extends APIImplDef {
  String graph;

  GraphDef def;

  SQLImplDef compiled;

  @Override
  public APIImplType getType() {
    return APIImplType.REL_GRAPH;
  }

  public SQLImplDef compile() {
    setCompiled(new GraphImplCompiler().compile(getDef()));
    return getCompiled();
  }
}
