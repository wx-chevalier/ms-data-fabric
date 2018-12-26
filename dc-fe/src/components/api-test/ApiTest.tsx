import './ApiTest.scss';
import React from 'react';
import { Row, Col, Input, Button, Form, message } from 'antd';
import ApiModel from '../../models/ApiModel';
import AceEditor from 'react-ace';
import { FormComponentProps } from '../../../node_modules/antd/lib/form';
import { ParameterObject, SchemaObject } from '../../apis/OpenApi3Interface';
import { inject, observer } from 'mobx-react';
import ApiStore from '../../stores/ApiStore';

import 'brace/mode/json';
import 'brace/theme/tomorrow';
import { HttpMethods } from '../../apis/BaseApi';
import { autorun } from '../../../node_modules/mobx';

const TextArea = Input.TextArea;
const FormItem = Form.Item;

interface ApiTestProps extends FormComponentProps {
  apiStore?: ApiStore;
  isApiManager: boolean;
  api: ApiModel;
}

interface ApiTestState {
  urlParams: Object;
  queryParams: Object;
  headerParams: Object;
  body?: string;
  status?: number;
  response?: string;
  loading: boolean;
}

const formItemLayout = {
  labelCol: {
    span: 12
  },
  wrapperCol: {
    span: 12
  }
};

@inject('apiStore')
@observer
export class ApiTest extends React.Component<ApiTestProps, ApiTestState> {
  constructor(props: ApiTestProps) {
    super(props);
    const api = this.props.api;
    this.state = {
      urlParams: this.parseUrlParamsToObject(api.accessUrl),
      queryParams: this.parseParamSchemaToObject(api.queries),
      headerParams: this.parseParamSchemaToObject(api.headers),
      loading: false
    };
    autorun(() => {
      const urlParams = this.parseUrlParamsToObject(api.accessUrl);
      this.setState({
        urlParams
      });
    });
  }

  // 渲染表头
  renderTableHeader = () => {
    return (
      <Row className="table-header">
        <Col span={12} style={{ textAlign: 'center' }}>
          参数名称
        </Col>
        <Col span={12} style={{ textAlign: 'center' }}>
          参数值
        </Col>
      </Row>
    );
  };

  // 将url中的路径参数解析为obj
  parseUrlParamsToObject(url: string) {
    if (url !== '') {
      let urlParams;
      // 匹配{xxx}
      const blocks = url.match(/{{1}\w+}{1}/g);
      // 匹配{{xxx}}，无用参数，从参数列表中剔除
      const uselessBlocks = url.match(/{{2}\w+}{2}/g);
      if (blocks) {
        const params = blocks.map(p => p.match(/\w+/)![0]);
        if (uselessBlocks) {
          const uselessParams = uselessBlocks.map(p => p.match(/\w+/)![0]);
          urlParams = params.filter(p => uselessParams.indexOf(p) === -1);
        } else {
          urlParams = params;
        }
        let paramsObj = {};
        urlParams.forEach(p => (paramsObj[p] = undefined));
        return paramsObj;
      } else {
        return {};
      }
    } else {
      return {};
    }
  }

  // 将schema对象解析为obj
  parseParamSchemaToObject(paramSchema: ParameterObject[]) {
    if (paramSchema.length !== 0) {
      let paramObj = {};
      paramSchema.forEach(p => {
        paramObj[p.name] = {
          required: p.required ? true : false,
          value: undefined
        };
      });
      return paramObj;
    } else {
      return {};
    }
  }

  // 渲染url路径参数表格内容
  renderUrlParamsTable(url: string) {
    const { urlParams } = this.state;
    const form = this.props.form;
    const { getFieldDecorator } = form;

    return (
      <div className="url-params-table">
        {Object.keys(urlParams).length !== 0 ? this.renderTableHeader() : null}
        {url !== ''
          ? Object.keys(urlParams).map(k => {
              return (
                <FormItem key={k} label={k} {...formItemLayout} colon={false}>
                  {getFieldDecorator(k, {
                    rules: [{ required: true }]
                  })(
                    <Input
                      onChange={e => {
                        const v = e.target.value;
                        let param = Object.assign({}, urlParams);
                        param[k] = v;
                        this.setState({
                          urlParams: param
                        });
                      }}
                    />
                  )}
                </FormItem>
              );
            })
          : null}
      </div>
    );
  }

  renderReqBodyEditor(api: ApiModel) {
    const body = this.state.body;

    return api.verb !== 'GET' ? (
      <div className="body">
        <h3>请求体</h3>
        <AceEditor
          mode="json"
          theme="tomorrow"
          name="sqlEditor"
          onChange={v => {
            this.setState({
              body: v
            });
          }}
          fontSize={14}
          showPrintMargin={true}
          showGutter={true}
          highlightActiveLine={true}
          value={body ? body : this.parseSchemaToDataSource(api.requestBody)}
          defaultValue={this.parseSchemaToDataSource(api.requestBody)}
          width="100%"
          height="300px"
          style={{ borderRadius: '6px', border: '2px solid rgb(233, 233, 233)' }}
          setOptions={{
            enableBasicAutocompletion: false,
            enableLiveAutocompletion: false,
            enableSnippets: false,
            showLineNumbers: true,
            tabSize: 2
          }}
        />
      </div>
    ) : null;
  }

  // 渲染查询参数、请求头部表格内容
  renderParamsTable(type: 'queries' | 'headers') {
    const { queryParams, headerParams } = this.state;
    const form = this.props.form;
    const { getFieldDecorator } = form;

    return (
      <div className={`${type}-params-table`}>
        {type === 'headers'
          ? this.renderTableHeader()
          : Object.keys(queryParams).length !== 0
            ? this.renderTableHeader()
            : null}
        {type === 'queries'
          ? Object.keys(queryParams).map(k => {
              return (
                <FormItem key={k} label={k} {...formItemLayout} colon={false}>
                  {getFieldDecorator(k, {
                    rules: [{ required: queryParams[k].required }]
                  })(
                    <Input
                      onChange={e => {
                        const v = e.target.value;
                        let param = Object.assign({}, queryParams);
                        param[k] = {
                          required: param[k].required,
                          value: v
                        };
                        this.setState({
                          queryParams: param
                        });
                      }}
                    />
                  )}
                </FormItem>
              );
            })
          : Object.keys(headerParams).map(k => {
              return (
                <FormItem key={k} label={k} {...formItemLayout} colon={false}>
                  {getFieldDecorator(k, {
                    rules: [{ required: headerParams[k].required }]
                  })(
                    <Input
                      onChange={e => {
                        const v = e.target.value;
                        let param = Object.assign({}, headerParams);
                        param[k] = {
                          required: param[k].required,
                          value: v
                        };
                        this.setState({
                          headerParams: param
                        });
                      }}
                    />
                  )}
                </FormItem>
              );
            })}
      </div>
    );
  }

  // 可以优化的地方——根据深度控制缩进
  // tslint:disable-next-line
  parseSchemaToDataSource(data: SchemaObject, title?: string): any {
    if (data.type! === 'array') {
      try {
        return `${title ? `"${title}": ` : ''}[\n  ${this.parseSchemaToDataSource(data.items!)}]`;
      } catch (e) {
        return `${title ? `"${title}":` : ''}[]`;
      }
    } else if (data.type! === 'object') {
      try {
        return `${title ? `"${title}": ` : ''}{\n  ${Object.keys(data.properties!).map(
          (k, id) =>
            `${id !== 0 ? '\n  ' : ''}${this.parseSchemaToDataSource(data.properties![k], k)}`
        )}\n}`;
      } catch (e) {
        return `${title ? `"${title}": ` : ''}{}`;
      }
    } else {
      return `${title ? `"${title}": ` : ''}"${data.type!}"`;
    }
  }

  // 提交时生成的BaseUrl地址
  generateBaseUrl(): string {
    const { api } = this.props;
    const { urlParams } = this.state;
    const url = api.accessUrl;

    if (Object.keys(urlParams).length !== 0) {
      return Object.keys(urlParams).reduce((prev, cur) => {
        const reg = new RegExp(`{{1}(${cur})}{1}`, 'g');
        return prev.replace(reg, urlParams[cur]);
      }, url);
    } else {
      return url;
    }
  }

  // 生成最终Url地址
  generateUrl(baseUrl: string) {
    const { queryParams } = this.state;
    if (Object.keys(queryParams).length !== 0) {
      return Object.keys(queryParams).reduce((prev, cur, id) => {
        if (id === 0) {
          return prev + `${cur}=${queryParams[cur].value}`;
        } else {
          return prev + `&${cur}=${queryParams[cur].value}`;
        }
      }, baseUrl + '?');
    } else {
      return baseUrl;
    }
  }

  // 生成请求所需的header格式
  generateHeader(token: string) {
    const { headerParams } = this.state;
    let obj = {};
    Object.keys(headerParams).forEach(k => {
      obj[k] = headerParams[k].value;
    });
    obj['truelore-key'] = token;
    return obj;
  }

  // tslint:disable-next-line
  handleSubmit = (e: any) => {
    const { api, apiStore, form } = this.props;
    const { body } = this.state;
    e.preventDefault();
    this.setState({
      loading: true
    });
    // 一个折衷的解决方案 —— 如果token未填写，校验后再次获取token无法写入
    if (api.token !== '') {
      // tslint:disable-next-line
      form.validateFieldsAndScroll(async (err: any, values: any) => {
        if (!err) {
          try {
            let res;
            if (body) {
              if (body === this.parseSchemaToDataSource(api.requestBody)) {
                res = await apiStore!.apiTest(
                  api.verb.toLowerCase() as HttpMethods,
                  this.generateUrl(this.generateBaseUrl()),
                  this.generateHeader(values['truelore-key']),
                  JSON.parse(JSON.stringify({}))
                );
              } else {
                res = await apiStore!.apiTest(
                  api.verb.toLowerCase() as HttpMethods,
                  this.generateUrl(this.generateBaseUrl()),
                  this.generateHeader(values['truelore-key']),
                  JSON.parse(body)
                );
              }
            } else {
              res = await apiStore!.apiTest(
                api.verb.toLowerCase() as HttpMethods,
                this.generateUrl(this.generateBaseUrl()),
                this.generateHeader(values['truelore-key']),
                JSON.parse(JSON.stringify({}))
              );
            }
            this.setState({
              loading: false,
              status: res.status,
              response: JSON.stringify(res.message, null, 2)
            });
          } catch (e) {
            this.setState({
              loading: false
            });
          }
        } else {
          this.setState({
            loading: false
          });
        }
      });
    } else {
      message.error('请先获取token');
      this.setState({
        loading: false
      });
    }
  };

  render() {
    const { api, form } = this.props;
    const { getFieldDecorator } = form;
    const { queryParams, response, loading, status } = this.state;

    return (
      <div className="api-test">
        <div className="&-request">
          <Form onSubmit={this.handleSubmit}>
            <div className="url">
              <div className="&-access-url">
                <Row>
                  <Col span={2}>
                    <h3>请求链接:</h3>
                  </Col>
                  <Col span={22}>
                    <Input value={api.accessUrl} readOnly={true} />
                  </Col>
                </Row>
              </div>
              <div className="&-url-params">{this.renderUrlParamsTable(api.accessUrl)}</div>
              <div className="&-query-params">
                {Object.keys(queryParams).length !== 0 ? <h3>查询参数列表</h3> : null}
                {this.renderParamsTable('queries')}
              </div>
              <div className="&-header-params">
                {<h3>请求头部参数</h3>}
                {this.renderParamsTable('headers')}
                <FormItem key="truelore-key" label="truelore-key" {...formItemLayout} colon={false}>
                  {getFieldDecorator('truelore-key', {
                    rules: [{ required: true }],
                    initialValue: api.token
                  })(<Input readOnly={true} />)}
                </FormItem>
              </div>
              {this.renderReqBodyEditor(api)}
            </div>
            <FormItem className="button">
              <Button type="primary" htmlType="submit" style={{ width: 150 }} loading={loading}>
                执行
              </Button>
            </FormItem>
          </Form>
        </div>
        <div className="&-response">
          <h3>
            响应
            {status ? (
              <span
                style={{
                  padding: '0 6px',
                  color: status.toString().slice(0, 1) === '2' ? '#40B318' : '#F21A24'
                }}
              >
                {status}
              </span>
            ) : null}
          </h3>
          <TextArea value={response} readOnly={true} rows={8} />
        </div>
      </div>
    );
  }
}

export default Form.create()(ApiTest);
