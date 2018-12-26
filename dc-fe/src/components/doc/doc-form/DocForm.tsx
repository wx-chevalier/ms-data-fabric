import './DocForm.scss';
import React from 'react';
import { Form, Input, Button } from 'antd';
import { FormComponentProps } from 'antd/lib/form';
import { ColumnProps } from 'antd/lib/table';
import Editor from '../../editor/Editor';
import { RouteComponentProps } from 'react-router';
import { withRouter } from 'react-router-dom';
import DocStore from '../../../stores/DocStore';
import { inject, observer } from 'mobx-react';
import { appendPath } from '../../../utils';
import DocModel from '../../../models/DocModel';

const FormItem = Form.Item;

export type TableColumns = ColumnProps<{ key: string; value: string }>[];

enum DocField {
  NAME = 'name'
}

type FieldValues = {
  [DocField.NAME]: string | undefined;
};

interface DocFormProps extends FormComponentProps, RouteComponentProps<{ pid: string }> {
  initialValues?: FieldValues & { content: string };
  docStore?: DocStore;
}

interface DocFormState {
  loading: boolean;
  error: string;
}

@inject('docStore')
@observer
class DocForm extends React.Component<DocFormProps, DocFormState> {
  constructor(props: DocFormProps) {
    super(props);
    this.state = {
      loading: false,
      error: ''
    };
  }

  handleSubmit = () => {
    const { form } = this.props;
    const docStore = this.props.docStore!;
    const pid = this.props.match.params.pid;
    const history = this.props.history;

    form.validateFields(async (err, values: FieldValues) => {
      this.setState({
        loading: true
      });
      if (!err) {
        try {
          if (docStore.editorStatus === 'create' && docStore.currentEditorContent.length !== 0) {
            const request: DocModel = await docStore.create(
              pid,
              values.name!,
              docStore.currentEditorContent
            );
            await docStore.loadDocListByPid(pid);
            docStore.initEditorStatus();
            history.push(
              appendPath(
                history.location.pathname
                  .split('/')
                  .slice(0, -1)
                  .join('/'),
                request.id
              )
            );
            form.resetFields();
            this.setState({
              loading: false
            });
          }
          if (docStore.editorStatus === 'update') {
            const request: DocModel = await docStore.update(
              docStore.selectedDoc!.id,
              values.name!,
              docStore.currentEditorContent,
              pid
            );
            await docStore.loadDocListByPid(pid);
            docStore.initEditorStatus();
            history.push(
              appendPath(
                history.location.pathname
                  .split('/')
                  .slice(0, -1)
                  .join('/'),
                request.id
              )
            );
            form.resetFields();
            this.setState({
              loading: false
            });
          }
        } catch (error) {
          this.setState({
            loading: false,
            error: error.message
          });
        }
      } else {
        this.setState({
          loading: false
        });
      }
    });
  };

  handleBack = () => {
    const form = this.props.form;
    const docStore = this.props.docStore!;
    const history = this.props.history;

    form.resetFields();
    // 清除store中的缓存
    docStore.initEditorStatus();
    history.goBack();
  };

  render() {
    const form = this.props.form;
    const docStore = this.props.docStore!;
    const { getFieldDecorator } = form;
    const loading = this.state.loading;
    const defaultContent = this.props.initialValues ? this.props.initialValues.content : undefined;

    return (
      <div className="doc-form">
        <Form>
          <FormItem label="名称" labelCol={{ span: 3 }} wrapperCol={{ span: 18 }}>
            {getFieldDecorator(DocField.NAME, {
              rules: [{ required: true, message: '请填写文档名称' }]
            })(<Input />)}
          </FormItem>
          <FormItem label="内容" labelCol={{ span: 3 }} wrapperCol={{ span: 18 }}>
            <Editor
              defaultContent={defaultContent}
              setEditingContent={(content: string) => {
                docStore.currentEditorContent = content;
              }}
            />
          </FormItem>
          <FormItem>
            <div className="doc-form-buttons">
              <Button className="back" onClick={this.handleBack}>
                返回
              </Button>
              <Button
                className="confirm"
                type="primary"
                onClick={this.handleSubmit}
                loading={loading}
              >
                确定
              </Button>
            </div>
          </FormItem>
        </Form>
      </div>
    );
  }
}

export default withRouter(
  Form.create({
    mapPropsToFields(props: DocFormProps) {
      const values = {};
      const initialValues = props.initialValues;
      if (!!initialValues) {
        Object.keys(initialValues).forEach(k => {
          values[k] = Form.createFormField({
            value: initialValues[k]
          });
        });
      }
      return values;
    }
  })(DocForm)
);
