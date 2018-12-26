import React, { Component } from 'react';
import AceEditor from 'react-ace';
import 'brace/mode/json';
import 'brace/theme/github';
import './JsonEditor.scss';
import { Alert } from 'antd';

interface JsonEditorProps {
  value: string;
  onChange: (v: string, obj: Object) => void;
  onError: (error?: string) => void;
}

interface JsonEditorState {
  jsonText: string;
  error: string;
}

export default class JsonEditor extends Component<JsonEditorProps, JsonEditorState> {
  constructor(props: JsonEditorProps) {
    super(props);
    this.state = {
      jsonText: props.value,
      error: ''
    };
  }

  handleJsonChange = (v: string) => {
    try {
      const obj = JSON.parse(v);
      this.props.onChange(v, obj);
      this.setState({
        error: ''
      });
    } catch (error) {
      this.props.onError('JSON 格式错误');
      this.setState({
        error: 'JSON 格式错误'
      });
    }
    this.setState({
      jsonText: v
    });
  };

  render() {
    const { jsonText, error } = this.state;
    return (
      <div className="json-editor">
        {error ? <Alert type="error" message={error} /> : null}
        <AceEditor
          mode="json"
          theme="github"
          name="schema"
          fontSize={14}
          showPrintMargin={false}
          showGutter={true}
          highlightActiveLine={true}
          width="100%"
          height="500px"
          setOptions={{
            enableBasicAutocompletion: false,
            enableLiveAutocompletion: false,
            enableSnippets: false,
            showLineNumbers: true,
            tabSize: 2
          }}
          value={jsonText}
          onChange={this.handleJsonChange}
        />
      </div>
    );
  }
}
