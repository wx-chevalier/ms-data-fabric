import React, { Component } from 'react';
import { FormComponentProps } from 'antd/lib/form';
import { Form, Input, Cascader, Select } from 'antd';
import typeAndFormat from './typeAndFormat';
import './MetaForm.scss';
import { SchemaObject } from '../../../apis/OpenApi3Interface';
import { isObject } from 'util';
const FormItem = Form.Item;
// import { pick } from 'lodash';
const Option = Select.Option;

enum MetaField {
  DESCRIPTION = 'description',
  TYPE = 'type',
  FORMAT = 'format',
  TYPE_AND_FORMAT = 'type_and_format',
  NULLABLE = 'nullable',
  EXAMPLE = 'example'
}
// tslint:disable-next-line
export type FieldValues = Partial<{ [key in MetaField]: any }>;
export type FieldErrors = {
  [key in MetaField]: {
    errors: {
      message: string;
      field: MetaField;
    }[];
  }
};

interface MetaFormProps extends FormComponentProps {
  initialValues?: SchemaObject;
  onChange: (v: FieldValues) => void;
}

interface MetaFormProps extends FormComponentProps {}

interface MetaFormState {}

// tslint:disable-next-line
function pick(obj: SchemaObject, pList: string[], transformer: (v: any) => typeof v = v => v) {
  const newObj = {};
  pList.forEach(p => {
    if (obj[p] !== undefined) {
      newObj[p] = transformer(obj[p]);
    }
  });
  return newObj;
}

class MetaForm extends Component<MetaFormProps, MetaFormState> {
  render() {
    const form = this.props.form;
    const { getFieldDecorator } = form;
    return (
      <Form className="meta-form">
        <div className="&-row">
          <FormItem className="&-item-l">
            <div>类型与格式</div>
            {getFieldDecorator(MetaField.TYPE_AND_FORMAT, {})(
              <Cascader options={typeAndFormat} changeOnSelect={true} />
            )}
          </FormItem>
          <FormItem className="&-item-r">
            <div>示例</div>
            {getFieldDecorator(MetaField.EXAMPLE, {
              rules: [{ max: 255, message: '最大长度不能超过255' }]
            })(<Input />)}
          </FormItem>
        </div>
        <div className="&-row">
          <FormItem className="&-item-l">
            <div>可否为空</div>
            {getFieldDecorator(MetaField.NULLABLE, {})(
              <Select>
                <Option key="true" value={1}>
                  是
                </Option>
                <Option key="false" value={0}>
                  否
                </Option>
              </Select>
            )}
          </FormItem>
          <FormItem className="&-item-r">
            <div>描述</div>
            {getFieldDecorator(MetaField.DESCRIPTION, {
              rules: [{ max: 255, message: '最大长度不能超过255' }]
            })(<Input />)}
          </FormItem>
        </div>
      </Form>
    );
  }
}

export default Form.create({
  mapPropsToFields(props: MetaFormProps) {
    let values: FieldValues = {};
    const initialValues = props.initialValues;
    if (!!initialValues) {
      if (isObject(initialValues.example)) {
        initialValues.example = JSON.stringify(initialValues.example);
      }
      values = pick(initialValues, Object.keys(MetaField).map(k => MetaField[k]), v =>
        Form.createFormField({
          value: v
        })
      );
      values[MetaField.TYPE_AND_FORMAT] = Form.createFormField({
        value: [initialValues.type, initialValues.format]
      });
      values[MetaField.NULLABLE] = Form.createFormField({
        value: Number(!!initialValues.nullable)
      });
    }
    return values;
  },
  // tslint:disable-next-line
  onValuesChange(props: MetaFormProps, _: any, allValues: FieldValues) {
    const { TYPE, TYPE_AND_FORMAT, FORMAT, EXAMPLE } = MetaField;
    const values = {
      ...allValues
    };
    values.nullable = Boolean(values.nullable);
    if (allValues[TYPE_AND_FORMAT]) {
      values[TYPE] = allValues[TYPE_AND_FORMAT][0];
      values[FORMAT] = allValues[TYPE_AND_FORMAT][1];
    }
    if (typeof values[EXAMPLE] === 'string') {
      try {
        values[EXAMPLE] = JSON.parse(values[EXAMPLE]);
      } catch (err) {
        //
      }
    }
    delete values[TYPE_AND_FORMAT];

    props.onChange(values);
  }
})(MetaForm);
