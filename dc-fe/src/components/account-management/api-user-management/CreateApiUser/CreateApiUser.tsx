import './CreateApiUser.scss';
import React from 'react';
import { Form, Input, Button, TreeSelect } from 'antd';
import { FormComponentProps } from 'antd/lib/form';
import DataSourceModel from '../../../../models/datasource/DataSourceModel';

const FormItem = Form.Item;
const TreeNode = TreeSelect.TreeNode;

enum ApiUserField {
  USERNAME = 'username',
  PASSWORD = 'password',
  DATABASE = 'database'
}

type FieldValues = {
  [ApiUserField.USERNAME]: string | undefined;
  [ApiUserField.PASSWORD]: string | undefined;
  [ApiUserField.DATABASE]: string[] | undefined;
};

interface CreateApiUserProps extends FormComponentProps {
  dataSourceList: DataSourceModel[];
  onCancel: () => void;
  onOk: (
    userName: string,
    password: string,
    authedDbList: {
      databaseName: string;
      dsId: string;
    }[]
  ) => void;
}

interface CreateApiUserState {
  loading: boolean;
  error: string;
}

export class CreateApiUser extends React.Component<CreateApiUserProps, CreateApiUserState> {
  constructor(props: CreateApiUserProps) {
    super(props);
    this.state = {
      loading: false,
      error: ''
    };
  }

  // tslint:disable-next-line
  handleSubmit = (e: any) => {
    this.setState({
      loading: true
    });
    e.preventDefault();
    this.props.form.validateFields(async (err, values: FieldValues) => {
      if (!err) {
        const { username, password, database } = values;

        try {
          await this.props.onOk(
            username as string,
            password as string,
            (database || []).map(d => {
              const tmpArr = d.split('-');
              return {
                databaseName: tmpArr[1],
                dsId: tmpArr[0]
              };
            })
          );
          this.setState({
            loading: false
          });
        } catch (error) {
          this.setState({ loading: false, error: error.message });
        }
      }
    });
  };

  render() {
    const form = this.props.form;
    const { getFieldDecorator } = form;
    const loading = this.state.loading;
    const dataSourceList = this.props.dataSourceList;

    return (
      <div className="create-api-user">
        <Form onSubmit={this.handleSubmit}>
          <FormItem label="用户名" labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
            {getFieldDecorator(ApiUserField.USERNAME, {
              rules: [{ required: true, message: '请输入用户名' }]
            })(<Input placeholder="请输入用户名" />)}
          </FormItem>
          <FormItem label="登录密码" labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
            {getFieldDecorator(ApiUserField.PASSWORD, {
              rules: [{ required: true, message: '请输入登录密码' }]
            })(<Input placeholder="请输入登录密码" type="password" />)}
          </FormItem>
          <FormItem label="可操作数据库" labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
            {getFieldDecorator(ApiUserField.DATABASE, {
              rules: [{ required: true, message: '请选择可操作数据库', type: 'array' }]
            })(
              <TreeSelect
                style={{ width: '100%' }}
                dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                placeholder="Please select"
                allowClear={true}
                multiple={true}
                treeDefaultExpandAll={true}
              >
                {dataSourceList.map(d => {
                  return (
                    <TreeNode value={d.name} title={d.name} key={d.name} disabled={true}>
                      {d.schemaList.map(s => {
                        return (
                          <TreeNode
                            value={`${s.datasourceName}-${s.name}`}
                            title={s.name}
                            key={s.name}
                          />
                        );
                      })}
                    </TreeNode>
                  );
                })}
              </TreeSelect>
            )}
          </FormItem>
          <FormItem className="button-group">
            <Button onClick={this.props.onCancel}>放弃</Button>
            <Button type="primary" htmlType="submit" loading={loading}>
              提交
            </Button>
          </FormItem>
        </Form>
        <div className="&-err">{this.state.error}</div>
      </div>
    );
  }
}

export default Form.create()(CreateApiUser);
