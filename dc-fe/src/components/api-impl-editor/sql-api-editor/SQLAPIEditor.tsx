import * as React from 'react';
import { inject, observer } from 'mobx-react';
import AceEditor from 'react-ace';
import { DatabaseTreeView } from '../../database-view';
import { APIImplEditorStore, DataBaseStore } from '../../../stores';
import { DatabaseModel } from '../../../models/datasource';
import { Tabs } from 'antd';

import 'brace/mode/mysql';
import 'brace/mode/json';
import 'brace/theme/tomorrow';

import './SQLAPIEditor.scss';

const TabPane = Tabs.TabPane;

export interface SQLAPIEditorProps {
  dbStore?: DataBaseStore;
  apiImplEditorStore?: APIImplEditorStore;
}

export interface SQLAPIEditorState {
  multiModeSelected?: DatabaseModel;
}

@inject('dbStore', 'apiImplEditorStore')
@observer
export default class SQLAPIEditor extends React.Component<SQLAPIEditorProps, SQLAPIEditorState> {
  constructor(props: SQLAPIEditorProps) {
    super(props);
    this.state = {};
  }

  componentWillMount() {
    this.props.dbStore!.loadDatabaseList();
  }

  selectDatabase = async (db: DatabaseModel | null) => {
    if (this.props.apiImplEditorStore!.singleDatabaseMode) {
      await this.props.apiImplEditorStore!.selectDatabase(db);
    } else {
      this.setState({ multiModeSelected: db || undefined });
    }
  };

  toggleSingleMode = () => {
    this.props.apiImplEditorStore!.toggleSingleDatabaseMode();
  };

  render() {
    const singleDbMode: boolean = this.props.apiImplEditorStore!.singleDatabaseMode;
    return (
      <div className="sql-api-editor">
        <div className="&-database-view">
          <Tabs activeKey={singleDbMode ? 'single' : 'multi'} onChange={this.toggleSingleMode}>
            <TabPane tab="单库操作" key="single">
              <div className="sql-api-editor-database-tip">
                单库操作模式，你可以对选定的库进行增删改查操作
              </div>
              <DatabaseTreeView
                databaseList={this.props.dbStore!.dataBaseList.slice()}
                selectedDatabase={this.props.apiImplEditorStore!.selectedDatabase || undefined}
                selectDatabase={this.selectDatabase}
              />
            </TabPane>
            <TabPane tab="多库操作" key="multi">
              <div className="sql-api-editor-database-tip">
                多库操作模式，你可以对多个库进行联查，但也只能进行查询
              </div>
              <DatabaseTreeView
                databaseList={this.props.dbStore!.dataBaseList.slice()}
                selectedDatabase={this.state.multiModeSelected}
                selectDatabase={this.selectDatabase}
              />
            </TabPane>
          </Tabs>
        </div>
        <div className="&-sql-editor">
          <AceEditor
            mode="mysql"
            theme="tomorrow"
            name="sqlEditor"
            onChange={this.props.apiImplEditorStore!.updateSQL}
            fontSize={14}
            showPrintMargin={true}
            showGutter={true}
            highlightActiveLine={true}
            value={this.props.apiImplEditorStore!.sql}
            width="100%"
            height="100%"
            style={{ borderRadius: '6px' }}
            setOptions={{
              enableSnippets: false,
              showLineNumbers: true,
              tabSize: 2
            }}
          />
        </div>
      </div>
    );
  }
}
