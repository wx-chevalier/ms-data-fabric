import * as React from 'react';
import { inject, observer } from 'mobx-react';
import { Button, Icon, message, Modal } from 'antd';
import { RouteComponentProps, withRouter } from 'react-router-dom';
import AceEditor from 'react-ace';
import { ProjectStore, APIImplEditorStore } from '../../../stores';
import { ArgEditor } from '../../arg-editor';
import { APICreationForm } from '../api-creation-form';
import './APIDevTool.scss';

export interface APIDevToolProps extends RouteComponentProps<{}> {
  onHeightChange: (height: number) => void;
  height?: number;
  defaultHeight?: number;
  apiImplEditorStore?: APIImplEditorStore;
  projectStore?: ProjectStore;
}

interface APIDevToolState {
  bodyHeight: number;
  parsing: boolean;
  requesting: boolean;
  showCreationForm: boolean;
  creating: boolean;
  creationErrMsg?: string;
}

@inject('apiImplEditorStore', 'projectStore')
@observer
class APIDevTool extends React.Component<APIDevToolProps, APIDevToolState> {
  static TOOLBAR_HEIGHT = 28;
  static DEFAULT_HEIGHT = 250;

  constructor(props: APIDevToolProps) {
    super(props);
    this.state = {
      bodyHeight: (this.props.defaultHeight || 250) - APIDevTool.TOOLBAR_HEIGHT,
      parsing: false,
      requesting: false,
      showCreationForm: false,
      creating: false
    };
  }

  toggleCreationForm = () => {
    this.setState({ showCreationForm: !this.state.showCreationForm });
  };

  onCreationFormSubmit = async (name: string, description: string) => {
    this.setState({ creating: true });
    try {
      let prj = this.props.projectStore!.selectedItem;
      if (!prj) {
        throw new Error('未知项目');
      }
      let api = await this.props.apiImplEditorStore!.createAPI(prj.id, name, description);
      if (api) {
        this.props.history.push(`${api.id}`);
      }
    } catch (e) {
      this.setState({ creationErrMsg: `创建失败: ${e && e.message}` });
    }
  };

  toggleMinimization = () => {
    if (this.bodyHeight > 0) {
      this.setState({ bodyHeight: 0 });
      this.props.onHeightChange(APIDevTool.TOOLBAR_HEIGHT);
    } else {
      this.setState({ bodyHeight: APIDevTool.DEFAULT_HEIGHT - APIDevTool.TOOLBAR_HEIGHT });
      this.props.onHeightChange(APIDevTool.DEFAULT_HEIGHT - APIDevTool.TOOLBAR_HEIGHT);
    }
  };

  parseImpl = async () => {
    if (this.bodyHeight === 0) {
      await this.toggleMinimization();
      this.setState({ parsing: true });
    } else {
      this.setState({ parsing: true });
    }
    try {
      this.props.apiImplEditorStore!.response = '';
      await this.props.apiImplEditorStore!.parseImpl();
    } catch (e) {
      message.error('解析错误: ' + e && e.message);
      this.props.apiImplEditorStore!.response =
        e && e.response && e.response.data && e.response.data.message;
    } finally {
      this.setState({ parsing: false });
    }
  };

  sendRequest = async () => {
    if (this.bodyHeight === 0) {
      await this.toggleMinimization();
      this.setState({ requesting: true });
    } else {
      this.setState({ requesting: true });
    }
    try {
      await this.props.apiImplEditorStore!.sendTestRequest();
    } catch (e) {
      message.error('请求错误: ' + e && e.message);
    } finally {
      this.setState({ requesting: false });
    }
  };

  get response() {
    let raw = this.props.apiImplEditorStore!.response;
    if (raw == null) {
      return '';
    }
    try {
      return JSON.stringify(JSON.parse(raw), undefined, 2);
    } catch (e) {
      return raw;
    }
  }

  get bodyHeight() {
    let bodyHeight = this.state.bodyHeight;
    if (this.props.height != null) {
      bodyHeight = this.props.height - APIDevTool.TOOLBAR_HEIGHT;
    }
    return bodyHeight < 0 ? 0 : bodyHeight;
  }

  render() {
    const bodyHeight = this.bodyHeight;
    const arg = this.props.apiImplEditorStore!.parameterSchema;

    return (
      <div className="api-dev-tool">
        <div className="&-toolbar">
          <div className="&-left">
            <Icon type={bodyHeight === 0 ? 'up' : 'down'} onClick={this.toggleMinimization} />
            <Button size="small" loading={this.state.parsing} onClick={this.parseImpl}>
              解析
            </Button>
            <Button size="small" loading={this.state.requesting} onClick={this.sendRequest}>
              发送请求
            </Button>
          </div>
          <div className="&-right">
            <Button type="primary" size="small" onClick={this.toggleCreationForm}>
              生成接口
            </Button>
          </div>
        </div>
        <div className="&-body" style={bodyHeight <= 0 ? { display: 'none' } : undefined}>
          <div className="&-parsed-args" style={{ minHeight: `${bodyHeight}px` }}>
            <header>
              <h4>解析所得参数</h4>
            </header>
            {arg && arg.argList.map((a, i) => <ArgEditor key={i} arg={a} />)}
          </div>
          <div className="&-response">
            <AceEditor
              mode="json"
              theme="tomorrow"
              name="response"
              fontSize={14}
              showPrintMargin={true}
              showGutter={true}
              highlightActiveLine={true}
              value={this.response}
              width="100%"
              style={{ borderRadius: '6px', width: '100%', height: `${bodyHeight}px` }}
            />
          </div>
        </div>
        <Modal
          visible={this.state.showCreationForm}
          footer={null}
          onCancel={this.toggleCreationForm}
        >
          <APICreationForm
            handleSubmit={this.onCreationFormSubmit}
            submitErrMsg={this.state.creationErrMsg}
            generating={this.state.creating}
            handleCancel={this.toggleCreationForm}
          />
        </Modal>
      </div>
    );
  }
}

export default withRouter(APIDevTool);
