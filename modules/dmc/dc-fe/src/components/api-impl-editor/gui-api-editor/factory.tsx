import React from 'react';
import InstructController from './InstructController';
import { Table, Point } from './interface';
import AliasColumn from './components/alias-column/AliasColumn';
import MockHover from './components/mock-hover/MockHover';
import ConditionEditor from './components/condition-editor/ConditionEditor';
// import JoinCondition from './components/join-condition/JoinCondition';
const RELATIONAL_OPERATORS = ['>', '<', '=', '>=', '<=', '!='];
const BOOL_OPRATORS = ['AND', 'OR'];
const Editor = require('./g6-editor/g6Editor');

export const controller = new InstructController();

export function positionStyle<T>(
  Component: React.ComponentType
): React.StatelessComponent<T & { left: number; top: number }> {
  // tslint:disable-next-line
  return (props: any) => {
    const { top, left, ...rest } = props;
    return (
      <div style={{ position: 'absolute', top, left }}>
        <Component {...rest} />
      </div>
    );
  };
}

// tslint:disable-next-line
const setAddedge = (page: any) => {
  page.changeAddEdgeModel({
    shape: 'flow-polyline'
  });
  // 输入锚点不可以连出边
  // tslint:disable-next-line
  page.on('hoveranchor:beforeaddedge', (ev: any) => {
    if (ev.anchor.type === 'input') {
      ev.cancel = true;
    }
    if (page.anchorHasBeenLinked(ev.item, ev.anchor)) {
      ev.cancel = true;
    }
  });

  // tslint:disable-next-line
  page.on('dragedge:beforeshowanchor', (ev: any) => {
    // 只允许目标锚点是输入，源锚点是输出，才能连接
    if (!(ev.targetAnchor.type === 'input' && ev.sourceAnchor.type === 'output')) {
      ev.cancel = true;
    }
    // 如果拖动的是目标方向，则取消显示目标节点中已被连过的锚点
    if (ev.dragEndPointType === 'target' && page.anchorHasBeenLinked(ev.target, ev.targetAnchor)) {
      ev.cancel = true;
    }
    // 如果拖动的是源方向，则取消显示源节点中已被连过的锚点
    if (ev.dragEndPointType === 'source' && page.anchorHasBeenLinked(ev.source, ev.sourceAnchor)) {
      ev.cancel = true;
    }

    // 不允许连接自身
    if (ev.source === ev.target) {
      ev.cancel = true;
    }
  });
};

export const editorFactory = (
  pageContainer: string,
  itemContainer: string,
  height: number,
  handleChange: () => void
) => {
  const editor = new Editor();
  const page = new Editor.Flow({
    graph: {
      container: pageContainer,
      height: height
    },
    noEndEdge: false,
    edgeResizeable: false,
    fitView: 'autoZoom'
  });
  const itemPannel = new Editor.Itempannel({
    container: itemContainer
  });
  editor.page = page;
  editor.itemPannel = itemPannel;

  // 状态改变，保存数据
  // tslint:disable-next-line
  page.on('statuschange', (ev: any) => {
    handleChange();
  });

  setAddedge(page);

  editor.add(page);
  editor.add(itemPannel);
  // editor.on('aftercommandexecute', (ev: any)=>{ console.log(page.getEdges(), 'rrr')});   // 执行命令后

  // tslint:disable-next-line
  page.on('node:click', (ev: any) => {
    controller.parseClickInstuct(ev);
  });

  // 表间其它关系暂时禁用
  // tslint:disable-next-line
  // page.on('edge:mouseenter', (ev: any) => {
  //   const { domX, domY } = ev;
  //   controller.show(
  //     <MockHover left={domX} top={domY} key={ev.item.id} text="点击此线可进行表的联合" />,
  //     false
  //   );
  // });

  // tslint:disable-next-line
  // page.on('edge:click', (ev: any) => {
  //   const { item, domX, domY } = ev;
  //   const model = item.getModel();
  //   const { content, source, target } = model;
  //   const sourceTable: Table = item.dataMap[source].content;
  //   const targetTable: Table = item.dataMap[target].content;
  //   const g = page.getGraph();
  //   let conditions = content || [];
  //   controller.show(
  //     <JoinCondition
  //       left={domX}
  //       top={domY}
  //       conditions={conditions}
  //       source={sourceTable}
  //       target={targetTable}
  //       operators={RELATIONAL_OPERATORS}
  //       key={item.id}
  //       onCancel={() => controller.clear()}
  //       onOk={newCondition => {
  //         g.update(item.id, { content: [...conditions, newCondition] });
  //         controller.clear();
  //       }}
  //       onDelete={result => {
  //         g.update(item.id, { content: result });
  //         conditions = result;
  //       }}
  //     />
  //   );
  // });

  // 选中控制
  controller.on('toggleColumnCheck', (item, offset: Point, index: number) => {
    const table: Table = item.getModel().content;
    const column = table.columns[index];
    column.checked = !column.checked;
    page.getGraph().update(item.id, { content: table });
  });

  controller.on('toggleConditionCheck', (item, offset: Point, index: number) => {
    const table: Table = item.getModel().content;
    const condition = table.conditions[index];
    condition.checked = !condition.checked;
    page.getGraph().update(item.id, { content: table });
  });

  // 列名提示
  controller.on('columnTip', (item, offset: Point, index: number) => {
    const table: Table = item.getModel().content;
    const column = table.columns[index];
    const { minX, minY } = item.bbox;
    controller.show(
      <MockHover left={minX + offset.x} top={minY + offset.y} key={item.id} text={column.text!} />,
      false
    );
  });

  // 重命名列
  controller.on('aliasColumn', (item, offset: Point, index: number) => {
    const table: Table = item.getModel().content;
    const column = table.columns[index];
    const { minX, minY } = item.bbox;
    const handleOk = (v: string) => {
      column.alias = v;
      controller.clear();
      page.getGraph().update(item.id, { content: table });
    };
    const handleCancel = (v: string) => {
      controller.clear();
    };
    controller.show(
      <AliasColumn
        left={minX + offset.x}
        top={minY + offset.y}
        key={item.id}
        onOk={handleOk}
        onCancel={handleCancel}
      />
    );
  });

  // 添加简单条件
  controller.on('addBasicCondition', (item, offset: Point, index: number) => {
    const table: Table = item.getModel().content;
    const column = table.columns[index];
    const { minX, minY } = item.bbox;
    const handleOk = (o: string, r: string) => {
      table.conditions = table.conditions || [];
      table.conditions.push({
        isCondition: true,
        type: 'basic',
        operator: o,
        left: column,
        right: r
      });
      controller.clear();
      page.getGraph().update(item.id, { content: table });
    };
    const handleCancel = (v: string) => {
      controller.clear();
    };
    controller.show(
      <ConditionEditor
        left={minX + offset.x}
        top={minY + offset.y}
        key={item.id}
        operators={RELATIONAL_OPERATORS}
        onOk={handleOk}
        onCancel={handleCancel}
      />
    );
  });

  // 条件提示
  controller.on('conditionTip', (item, offset: Point, index: number) => {
    const table: Table = item.getModel().content;
    const condition = table.conditions[index];
    const { minX, minY } = item.bbox;
    controller.show(
      <MockHover
        left={minX + offset.x}
        top={minY + offset.y}
        key={Date.now()}
        text={condition.text!}
      />,
      false
    );
  });

  controller.on('deleteCondition', (item, offset: Point, index: number) => {
    controller.clear();
    const table: Table = item.getModel().content;
    table.conditions.splice(index, 1);
    page.getGraph().update(item.id, { content: table });
  });

  // 添加逻辑条件
  controller.on('addCompoundCondition', (item, offset: Point, index: number) => {
    const table: Table = item.getModel().content;
    const condition = table.conditions[index];
    const { minX, minY } = item.bbox;
    const handleOk = (o: string, r: number) => {
      const right = table.conditions[r];
      table.conditions.push({
        isCondition: true,
        type: 'compound',
        operator: o,
        left: condition,
        right: right
      });
      controller.clear();
      page.getGraph().update(item.id, { content: table });
    };
    const handleCancel = (v: string) => {
      controller.clear();
    };
    controller.show(
      <ConditionEditor
        left={minX + offset.x}
        top={minY + offset.y}
        key={item.id}
        operators={BOOL_OPRATORS}
        rightOptions={table.conditions.map(v => (v === condition ? null : v))}
        onOk={handleOk}
        onCancel={handleCancel}
      />
    );
  });

  return editor;
};
