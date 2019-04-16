import React, { Component } from 'react';
import './SqlGuiEditor.scss';
import { Select, Icon } from 'antd';
import ActionMask from './components/action-mask/ActionMask';
import { editorFactory, controller } from './factory';
import { Table, JoinCondition } from './interface';
import { adaptTable, adaptJoinCond } from './adaptor';
import { inject, observer } from 'mobx-react';
import { DataBaseStore, APIImplEditorStore } from '../../../stores';
const cloneDeep = require('lodash.clonedeep');
const { Option } = Select;
require('./registerNode');
import { TABLE_WIDTH, COL_HEIGHT } from './registerNode';

interface SqlGuiEditorProps {
  dbStore?: DataBaseStore;
  apiImplEditorStore?: APIImplEditorStore;
  graphData?: object;
}

interface SqlGuiEditorState {}

@inject('dbStore', 'apiImplEditorStore')
@observer
export default class SqlGuiEditor extends Component<SqlGuiEditorProps, SqlGuiEditorState> {
  // tslint:disable-next-line
  editor: any;

  container = React.createRef<HTMLDivElement>();

  componentDidMount() {
    this.props.dbStore!.loadDatabaseList();
    const containerDiv = this.container.current!;
    const height = containerDiv.parentElement!.clientHeight;
    this.editor = editorFactory('page', 'item-pannel', height, this.handleSave);
    if (this.props.graphData) {
      this.editor.page.read(this.props.graphData);
    }
  }

  handleSave = () => {
    const graphData = this.editor.page.save();
    let nodes = graphData.nodes || [];
    let edges = graphData.edges || [];
    let tableMaps = this.calcTableMaps(nodes);
    let joinConditions = this.calcJoinConditions(edges, tableMaps);
    nodes = cloneDeep(nodes);
    edges = cloneDeep(edges);
    let tables = Object.keys(tableMaps.tableMap).map(k => tableMaps.tableMap[k]);
    // this.props.apiEditorStore!.create()
    this.props.apiImplEditorStore!.graphData = graphData;
    if (joinConditions.length !== 0) {
      this.props.apiImplEditorStore!.graphDef = {
        type: 'JOIN',
        tables: tables.map(adaptTable),
        conds: joinConditions.map(adaptJoinCond)
      };
    } else {
      this.props.apiImplEditorStore!.graphDef = adaptTable(tables[0]);
    }
  };

  // 更新左侧数据表列表
  // TODO 解绑 Event， 否则会有内存泄漏
  componentDidUpdate() {
    const { itemPannel } = this.editor;
    itemPannel._initContainer();
    itemPannel.bindEvent();
  }

  render() {
    const dbStore = this.props.dbStore!;
    const { dataBaseList, selectedDatabase } = dbStore;
    return (
      <div className="sql-gui-editor" ref={this.container}>
        <div id="page">
          {/* <div className="test">123</div> */}
          <ActionMask controller={controller} />
        </div>

        <div id="item-pannel">
          <p>数据源选择</p>
          <Select
            className="db-select"
            value={selectedDatabase ? selectedDatabase.dbId : undefined}
            onChange={(id: string) =>
              dbStore.selectDatabase(dataBaseList.find(db => db.dbId === id) || null)
            }
          >
            {dataBaseList.map(db => (
              <Option key={db.dbId} value={db.dbId}>
                {db.name}
              </Option>
            ))}
          </Select>
          <ul className="table-list">
            {selectedDatabase
              ? selectedDatabase.tableList!.map(t => (
                  <li
                    key={t.tableName}
                    className="getItem"
                    data-shape="table"
                    data-type="node"
                    data-size={`${TABLE_WIDTH}*${(t.columnList.length + 1) * COL_HEIGHT}`}
                    data-content={JSON.stringify(t.dataForG6)}
                  >
                    <Icon type="table" className="table-icon" />
                    <span>{t.tableName}</span>
                  </li>
                ))
              : null}
          </ul>
        </div>
      </div>
    );
  }

  /**
   * @returns
   *  - nodeIdTableMap: nodeId -> 原始的表信息
   *  - tableMap: tableId -> 过滤了勾选列/条件的表信息
   */
  private calcTableMaps(
    // tslint:disable-next-line
    nodes: any[]
  ): {
    nodeIdTableMap: { [nodeId: string]: Table };
    tableMap: { [tableId: string]: Table };
  } {
    // nodeId -> 原始的表信息
    const nodeIdTableMap: { [nodeId: string]: Table } = {};
    // tableId -> 过滤了勾选列/条件的表信息
    const tableMap: { [tableId: string]: Table } = {};

    // tslint:disable-next-line
    nodes.forEach((n: any) => {
      const table = n.content as Table;
      nodeIdTableMap[n.id] = table;
      // 建立映射
      tableMap[table.id] = {
        columns: table.columns.filter(c => c.checked),
        conditions: table.conditions.filter(c => c.checked),
        database: table.database,
        id: table.id,
        name: table.name
      };
    });

    return { nodeIdTableMap, tableMap };
  }

  /**
   * @param tables @see calcTableMaps
   * @returns srcTableId -> dstTableId -> Join 条件列表
   */
  private calcJoinConditions(
    // tslint:disable-next-line
    edges: any[],
    tables: {
      nodeIdTableMap: { [nodeId: string]: Table };
      tableMap: { [tableId: string]: Table };
    }
  ): JoinCondition[] {
    const { nodeIdTableMap } = tables;
    let joinConditions: JoinCondition[] = [];

    // tslint:disable-next-line
    edges.forEach((e: any) => {
      // source: source node id
      // target: target node id
      const { source, sourceAnchor, target, targetAnchor } = e;
      let srcTable = nodeIdTableMap[source];
      let dstTable = nodeIdTableMap[target];
      let srcTableColumns = srcTable!.columns || [];
      let dstTableColumns = dstTable!.columns || [];
      let leftColumn = srcTableColumns[Math.floor(sourceAnchor / 2)];
      let rightColumn = dstTableColumns[Math.floor(targetAnchor / 2)];
      if (leftColumn == null || rightColumn == null) {
        return;
      }
      joinConditions.push({
        isCondition: true,
        operator: '',
        type: 'join',
        leftTableId: srcTable.id,
        left: {
          name: leftColumn.name,
          type: leftColumn.type
        },
        rightTableId: dstTable.id,
        right: {
          name: rightColumn.name,
          type: rightColumn.type
        }
      });
    });

    return joinConditions;
  }
}
