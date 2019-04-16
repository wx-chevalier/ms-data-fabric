import { Table, BasicCondition, CompoundCondition, Column } from './interface';
import InstructController from './InstructController';
const Editor = require('./g6-editor/g6Editor');
const checkedIcon = require('./assets/checked.svg');
const uncheckedIcon = require('./assets/unchecked.svg');
const editIcon = require('./assets/edit.svg');
const addIcon = require('./assets/add.svg');
const deleteIcon = require('./assets/delete.svg');
export const TABLE_WIDTH = 184;
export const COL_HEIGHT = 26;
const TEXT_OFFSET_X = 30;
const ACTION1_OFFSET_X = 136;
const ACTION2_OFFSET_X = 160;
const FONT_SIZE = 12;
// const ICON_SIZE = 12;
const T_HEADER = COL_HEIGHT;
const PADDING = 8;
const BORDER_COLOR = '#e8e8e8';
const CENTER_OFFSET_Y = (COL_HEIGHT - FONT_SIZE) / 2;

Editor.Flow.registerNode('table', {
  // tslint:disable-next-line
  draw(item: any) {
    const group = item.getGraphicGroup();
    const model = item.getModel();
    if (typeof model.content === 'string') {
      model.content = JSON.parse(model.content);
    }
    const data = model.content as Table;
    const { columns, conditions } = data;
    const conditionLength = conditions ? conditions.length : 0;
    const columnLength = columns.length;
    const TABLE_HEIGHT = (conditionLength + columnLength + 1) * COL_HEIGHT;
    const leftTop = { x: -TABLE_WIDTH / 2, y: -TABLE_HEIGHT / 2 };

    // 大盒子
    const keyShape = group.addShape('rect', {
      attrs: {
        x: leftTop.x,
        y: leftTop.y,
        width: TABLE_WIDTH,
        height: TABLE_HEIGHT,
        stroke: BORDER_COLOR,
        fill: '#fff'
      },
      id: data.id
    });
    // 表头
    group.addShape('rect', {
      attrs: {
        fill: '#fafafa',
        width: TABLE_WIDTH - 2,
        height: COL_HEIGHT - 0.5,
        x: leftTop.x + 1,
        y: leftTop.y + 0.5
      }
    });
    group.addShape('text', {
      attrs: {
        x: leftTop.x + TEXT_OFFSET_X,
        y: leftTop.y + T_HEADER - CENTER_OFFSET_Y,
        fill: '#000',
        text: data.name,
        fontSize: FONT_SIZE
      }
    });
    group.addShape('text', {
      attrs: {
        x: leftTop.x + ACTION1_OFFSET_X,
        y: leftTop.y + T_HEADER - CENTER_OFFSET_Y,
        fill: '#000',
        text: '操作',
        fontSize: FONT_SIZE
      }
    });

    columns.forEach((c, i) => {
      // 选择图标
      const offsetY = T_HEADER + COL_HEIGHT * i + CENTER_OFFSET_Y;
      const y = leftTop.y + offsetY;
      const iconOffsetX = PADDING;
      const columnTextOffsetX = TEXT_OFFSET_X;
      const actionConditionOffsetX = ACTION1_OFFSET_X;
      const actionAliasOffsetX = ACTION2_OFFSET_X;
      const text = (c.text = `${getText(c)} (${c.type})`);

      group.addShape('image', {
        attrs: {
          width: FONT_SIZE,
          height: FONT_SIZE,
          x: leftTop.x + iconOffsetX,
          y,
          img: c.checked ? checkedIcon : uncheckedIcon
        },
        class: InstructController.instructBuilder(
          'toggleColumnCheck',
          { x: iconOffsetX, y: offsetY },
          i
        )
      });
      // 列名
      group.addShape('text', {
        attrs: {
          fill: 'rgba(0,0,0,0.65)',
          text: clipText(text),
          x: leftTop.x + columnTextOffsetX,
          y: y - 2,
          fontSize: FONT_SIZE,
          textBaseline: 'top'
        },
        class: InstructController.instructBuilder(
          'columnTip',
          { x: columnTextOffsetX, y: offsetY },
          i
        )
      });

      group.addShape('image', {
        attrs: {
          width: FONT_SIZE,
          height: FONT_SIZE,
          x: leftTop.x + actionAliasOffsetX,
          y,
          img: editIcon
        },
        class: InstructController.instructBuilder(
          'aliasColumn',
          { x: columnTextOffsetX, y: offsetY - 3 },
          i
        )
      });

      group.addShape('image', {
        attrs: {
          width: FONT_SIZE,
          height: FONT_SIZE,
          x: leftTop.x + actionConditionOffsetX,
          y,
          img: addIcon
        },
        class: InstructController.instructBuilder(
          'addBasicCondition',
          { x: actionConditionOffsetX, y: offsetY - CENTER_OFFSET_Y },
          i
        )
      });
    });

    if (Array.isArray(conditions)) {
      conditions.forEach((c, i) => {
        const index = columns.length + i;
        const offsetY = T_HEADER + COL_HEIGHT * index + CENTER_OFFSET_Y;
        const y = leftTop.y + offsetY;
        const conditionTextOffsetX = TEXT_OFFSET_X;
        const actionDeleteOffsetX = ACTION1_OFFSET_X;
        const actionConditionOffsetX = ACTION2_OFFSET_X;
        const iconOffsetX = PADDING;
        const text = (c.text = getText(c));

        // 条件
        group.addShape('image', {
          attrs: {
            width: FONT_SIZE,
            height: FONT_SIZE,
            x: leftTop.x + iconOffsetX,
            y,
            img: c.checked ? checkedIcon : uncheckedIcon
          },
          class: InstructController.instructBuilder(
            'toggleConditionCheck',
            { x: iconOffsetX, y: offsetY },
            i
          )
        });

        group.addShape('text', {
          attrs: {
            fill: 'rgba(0,0,0,0.65)',
            text: clipText(text),
            x: leftTop.x + conditionTextOffsetX,
            y,
            fontSize: FONT_SIZE,
            textBaseline: 'top'
          },
          class: InstructController.instructBuilder(
            'conditionTip',
            { x: conditionTextOffsetX, y: offsetY },
            i
          )
        });

        group.addShape('image', {
          attrs: {
            width: FONT_SIZE,
            height: FONT_SIZE,
            x: leftTop.x + actionDeleteOffsetX,
            y,
            img: deleteIcon
          },
          class: InstructController.instructBuilder(
            'deleteCondition',
            { x: conditionTextOffsetX, y: offsetY },
            i
          )
        });

        group.addShape('image', {
          attrs: {
            width: FONT_SIZE,
            height: FONT_SIZE,
            x: leftTop.x + actionConditionOffsetX,
            y,
            img: addIcon
          },
          class: InstructController.instructBuilder(
            'addCompoundCondition',
            { x: actionConditionOffsetX, y: offsetY - CENTER_OFFSET_Y },
            i
          )
        });
      });
    }

    // 分割线
    Array.from({ length: conditionLength + columnLength }).forEach((v, i) => {
      const y = leftTop.y + COL_HEIGHT * (i + 1);
      group.addShape('line', {
        attrs: {
          x1: leftTop.x,
          y1: y,
          x2: leftTop.x + TABLE_WIDTH,
          y2: y,
          stroke: '#E8E8E8'
        }
      });
    });

    return keyShape;
  },

  // tslint:disable-next-line
  anchor(item: any) {
    const model = item.getModel();
    const { columns, conditions } = model.content as Table;
    const conditionLength = conditions ? conditions.length : 0;
    const columnLength = columns.length;
    // tslint:disable-next-line
    const result: any[] = [];
    // tslint:disable-next-line
    columns.forEach((c: any, i: number) => {
      const y = (i + 1.5) / (columnLength + conditionLength + 1);
      result.push([
        0,
        y,
        {
          type: 'input'
        }
      ]);
      result.push([
        1,
        y,
        {
          type: 'output'
        }
      ]);
    });
    return result;
  }
});

function clipText(text: string) {
  return text.length > 18 ? text.slice(0, 15) + '...' : text;
}

function getText(data: Column | BasicCondition | CompoundCondition | string): string {
  if (typeof data === 'string') {
    return data;
  }
  if ('isCondition' in data) {
    return `${getText(data.left)} ${data.operator} ${getText(data.right)}`;
  } else {
    return data.alias || data.name;
  }
}
