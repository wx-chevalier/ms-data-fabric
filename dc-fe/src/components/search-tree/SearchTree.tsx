import './SearchTree.scss';

import { Input, Menu } from 'antd';
import * as React from 'react';
import TreeNode, {
  buildTree,
  expandKeys,
  indexingTree,
  keysOfTreeNode,
  NodeRenderFuncType
} from './TreeNode';

const Search = Input.Search;

export interface Props {
  /** 要渲染的数据  */
  data: Array<TreeNode>;
  /** 是否显示搜索框 */
  showSearch?: boolean;
  /** 点选指定节点 */
  onSelect?: (node: TreeNode) => void;
  /** 改变要展开的节点 */
  onChangeExpandedKeys?: (keys: Array<string>) => void;
  /** 改变当前选中的节点 */
  onChangeSelectedKeys?: (keys: Array<string>) => void;
  /** 定制节点渲染函数 */
  nodeRenderFunc?: NodeRenderFuncType;
  /** 默认展开的节点列表 */
  defaultExpandedKeys?: Array<string>;
  /** 默认选中的节点列表 */
  defaultSelectedKeys?: Array<string>;
}

/**
 * 当 props.showSearch = true 时，该内部状态才起作用
 */
export interface State {
  searchValue: string;
  expandedKeys: Array<string>;
}

export default class SearchTree extends React.Component<Props, State> {
  public static defaultProps: Partial<Props> = {
    showSearch: false
  };

  mapKeyTreeNode: { [key: string]: TreeNode };

  indexingData() {
    if (this.props.data) {
      this.mapKeyTreeNode = indexingTree(this.props.data);
    }
  }

  constructor(props: Props) {
    super(props);

    this.state = {
      searchValue: '',
      expandedKeys: []
    };
    this.indexingData();
  }

  componentDidUpdate() {
    this.indexingData();
  }

  onSearch(s: string) {
    let matchedParentKeys: Array<string>;
    if (s !== '') {
      let data: Array<TreeNode> = this.props.data;
      let matchedKeys = keysOfTreeNode(data, (key, title, props) => title.indexOf(s) > -1);

      matchedParentKeys = expandKeys(matchedKeys, this.mapKeyTreeNode);
    } else {
      matchedParentKeys = [];
    }

    this.setState({
      searchValue: s,
      expandedKeys: matchedParentKeys
    });
  }

  onExpand(expandedKeys: Array<string>) {
    if (this.props.onChangeExpandedKeys != null) {
      this.props.onChangeExpandedKeys(expandedKeys);
    }
  }

  onSelect(selectedKeys: Array<string>) {
    if (selectedKeys.length) {
      const { onSelect, onChangeSelectedKeys } = this.props;

      if (onSelect != null) {
        onSelect(this.mapKeyTreeNode[selectedKeys[0]]);
      }

      if (onChangeSelectedKeys != null) {
        onChangeSelectedKeys(selectedKeys);
      }
    }
  }

  render() {
    // 当前选中的条目
    let selectedKeys: Array<string> =
      this.props.defaultSelectedKeys == null ? [] : this.props.defaultSelectedKeys;

    // 当前展开的条目，如果有搜索值则使用搜索出来的条目（state.expandedKeys）否则使用 props 中的
    let expandedKeys: Array<string>;
    if (this.props.showSearch && this.state.searchValue !== '') {
      expandedKeys = this.state.expandedKeys;
    } else {
      expandedKeys = this.props.defaultExpandedKeys == null ? [] : this.props.defaultExpandedKeys;
    }

    return (
      <div className="search_tree__container">
        {this.props.showSearch && (
          <Search
            placeholder="Search"
            onChange={e =>
              // tslint:disable
              this.onSearch(e.target['value'])
            }
          />
        )}
        <Menu
          mode="inline"
          theme="light"
          onSelect={v => this.onSelect(v.selectedKeys)}
          onOpenChange={keys => this.onExpand(keys)}
          openKeys={expandedKeys}
          selectedKeys={selectedKeys}
        >
          {buildTree(this.props.data, this.props.nodeRenderFunc, this.state.searchValue)}
        </Menu>
      </div>
    );
  }
}
