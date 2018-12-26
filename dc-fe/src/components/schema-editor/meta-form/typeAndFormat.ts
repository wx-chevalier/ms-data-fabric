import { CascaderOptionType } from 'antd/lib/cascader';

const typeAndFormat: CascaderOptionType[] = [
  {
    value: 'object',
    label: 'object'
  },
  {
    value: 'array',
    label: 'array'
  },
  {
    value: 'boolean',
    label: 'boolean'
  },
  {
    value: 'integer',
    label: 'integer',
    children: [
      {
        value: 'int32',
        label: 'int32'
      },
      {
        value: 'int64',
        label: 'int64'
      }
    ]
  },
  {
    value: 'number',
    label: 'number',
    children: [
      {
        value: 'float',
        label: 'float'
      },
      {
        value: 'double',
        label: 'double'
      }
    ]
  },
  {
    value: 'string',
    label: 'string',
    children: [
      {
        value: 'date-time',
        label: 'date-time'
      },
      {
        value: 'email',
        label: 'email'
      },
      {
        value: 'hostname',
        label: 'hostname'
      },
      {
        value: 'ipv4',
        label: 'ipv4'
      },
      {
        value: 'ipv6',
        label: 'ipv6'
      },
      {
        value: 'uri',
        label: 'uri'
      },
      {
        value: 'uriref',
        label: 'uriref'
      },
      {
        value: 'byte',
        label: 'byte'
      },
      {
        value: 'binary',
        label: 'binary'
      },
      {
        value: 'date',
        label: 'date'
      },
      {
        value: 'password',
        label: 'password'
      }
    ]
  }
];

export default typeAndFormat;
