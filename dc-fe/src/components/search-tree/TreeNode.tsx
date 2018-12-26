import * as React from 'react';
import { Menu } from 'antd';

export default class TreeNode {
  children?: Array<TreeNode>;
  key: string;
  title: string;
  props?: Object & {
    // tslint:disable
    self: any;
  };
  parentKey?: string;
}

export type NodeFilterFuncType = (key: string, title: string, props?: any) => boolean;

export type NodeRenderFuncType = (node: TreeNode, highlight?: string) => JSX.Element;

/**
 * 生成 key -> TreeNode 数据映射
 * 补全每个 TreeNode 数据的 parentKey
 *
 * @param {TreeNode} data
 */
export function indexingTree(data: Array<TreeNode>): { [key: string]: TreeNode } {
  let index = {};

  data.forEach(t => {
    if (t.children) {
      Object.assign(index, indexingTree(t.children));
      // $FlowFixMe
      t.children.forEach(tc => {
        tc.parentKey = t.key;
      });
    }
    index[t.key] = t;
  });

  return index;
}

export function expandKeys(
  toExpand: Array<string>,
  mapKeyToTreeNode: { [key: string]: TreeNode }
): Array<string> {
  let result = [];
  for (let k of toExpand) {
    let n = mapKeyToTreeNode[k];
    if (!n) {
      continue;
    } else {
      if (n.parentKey != null) {
        n = mapKeyToTreeNode[n.parentKey];
      }
      while (!!n) {
        if (result.indexOf(n.key) === -1) {
          result.push(n.key);
        }
        if (n.parentKey != null) {
          n = mapKeyToTreeNode[n.parentKey];
        } else {
          break;
        }
      }
    }
  }
  return result;
}

/**
 * @param {*} data
 * @param {*} filterFunc
 */
export function keysOfTreeNode(
  data: Array<TreeNode>,
  filterFunc?: NodeFilterFuncType
): Array<string> {
  let result: Array<string> = [];
  data.forEach(t => {
    if (!filterFunc || filterFunc(t.key, t.title, t.props)) {
      result.push(t.key);
    }
    if (t.children) {
      result.push(...keysOfTreeNode(t.children, filterFunc));
    }
  });
  return result;
}

/**
 * 返回字符串 s 被高亮后的结果
 * @param s
 * @param highlight
 */
export function highlighString(s: string, highlight?: string): JSX.Element {
  if (highlight == null) {
    return <span>{s}</span>;
  }
  const index = s.search(highlight);
  const beforeStr = s.substr(0, index);
  const afterStr = s.substr(index + highlight.length);
  return index == -1 ? (
    <span>{s}</span>
  ) : (
    <span>
      {beforeStr}
      <span style={{ color: '#f50' }}>{highlight}</span>
      {afterStr}
    </span>
  );
}

/**
 * @param {Array<TreeNode>} data
 * @param {string | null} highlight 高亮部分字符串
 * @return:
 */
export function buildTree(
  data: Array<TreeNode>,
  nodeRenderFunc?: NodeRenderFuncType,
  highlight?: string
): Array<JSX.Element> {
  return data.map(n => {
    let title;
    if (nodeRenderFunc == null) {
      title = highlighString(n.title, highlight);
    } else {
      title = nodeRenderFunc(n, highlight);
    }

    if (n.children) {
      return (
        <Menu.SubMenu key={n.key} title={title}>
          {buildTree(n.children, nodeRenderFunc, highlight)}
        </Menu.SubMenu>
      );
    } else {
      return <Menu.Item key={n.key}>{title}</Menu.Item>;
    }
  });
}

/**
 * @param data
 * @param includePredicate 根据节点数据判断是否保留节点（True 保留）
 */
export function filterTreeNode(
  data: Array<TreeNode>,
  includePredicate: (key: string, title: string, props?: any) => boolean
): Array<TreeNode> {
  return data.filter(n => {
    if (n.children) {
      n.children = filterTreeNode(n.children, includePredicate);
    }

    if (includePredicate(n.key, n.title, n.props) || n.children) {
      return true;
    } else {
      return false;
    }
  });
}
