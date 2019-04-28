declare module 'synthetic-dom' {
  type AttrList = Array<Attr>;

  class Node {
    nodeType: number;
    nodeName: string;
    nodeValue: string;
  }

  export class ElementNode extends Node {
    _name: string;
    _attrMap: Map<string, Attr>;
    _isSelfClosing: boolean;
    childNodes: Array<Node>;
    attributes: AttrList;

    constructor(name: string, attributes?: AttrList, childNodes?: Array<Node>);

    appendChild(node: Node): void;

    tagName(): string;

    className(): string;

    getAttribute(name: string): string;

    toString(isXHTML?: boolean): string;
  }
}
