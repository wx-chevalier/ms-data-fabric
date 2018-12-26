import { IntegerSchema } from './../models/oas3/media/IntegerSchema';
import { DateTimeSchema } from './../models/oas3/media/DateTimeSchema';
import { DateSchema } from './../models/oas3/media/DateSchema';
import { StringSchema } from './../models/oas3/media/StringSchema';
import { Schema } from './../models/oas3/media/Schema';
import { apicrAgent } from './agent';
import BaseApi from './BaseApi';
import { ObjectSchema } from '../models/oas3/media/ObjectSchema';
import { Dialect } from '../models/datasource/DatabaseModel';
import { NumberSchema } from '../models/oas3/media/NumberSchema';
import { BooleanSchema } from '../models/oas3/media/BooleanSchema';

export default class SQLApiApi extends BaseApi {
  constructor() {
    super(apicrAgent);
  }

  /**
   * 参数 dialect, sql, datasourceName 和 analyzeParameter 中参数意义一致
   * @param schema 参数 Schema, 其属性对应 Schema 为基本类型，应包含测试执行所需测试值（example），例如
   * {
   *   "type" : "object",
   *   "properties" : {
   *     "v_number" : {
   *       "type" : "number",
   *       "example": 10
   *     }
   *   }
   * }
   * @returns 接口 ID
   */
  async save(
    dialect: Dialect,
    sql: string,
    parameterSchema: ObjectSchema,
    projectId: string,
    folderId: string,
    apiName: string,
    apiDescription: string,
    datasourceName?: string
  ): Promise<string> {
    // tslint:disable-next-line
    let json = await this.post<any>({
      url: '/sqlapi',
      data: {
        typ: 'SQL',
        dialect,
        sql,
        datasourceName,
        parameterSchema,
        projectId,
        folderId,
        name: apiName,
        description: apiDescription
      }
    });
    return json.id;
  }

  /**
   * JSON 对象 -> 对应 Schema 值，只处理一些基本类型
   */
  // tslint:disable-next-line
  toPrimitiveSchema(json: any): Schema<any> {
    switch (json.type) {
      case 'number':
        return new NumberSchema();
      case 'string':
        if (!json.format) {
          return new StringSchema();
        }
        switch (json.format) {
          case 'date':
            return new DateSchema();
          case 'date-time':
            return new DateTimeSchema();
          default:
            throw new Error(`不支持的 Schema: ${json.type} - ${json.format}`);
        }
      case 'integer':
        return new IntegerSchema();
      case 'boolean':
        return new BooleanSchema();
      default:
        throw new Error(`不支持的 Schema: ${json.type} - ${json.format}`);
    }
  }
}
