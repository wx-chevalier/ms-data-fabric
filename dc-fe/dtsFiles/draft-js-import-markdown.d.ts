declare module 'draft-js-import-markdown' {
  import Draft, { ContentState } from 'draft-js';
  import { ElementNode } from 'synthetic-dom';

  type Style = string;
  type BlockData = { [key: string]: any };
  type PartialBlock = {
    type?: string;
    data?: BlockData;
  };

  type EntityMutability = 'IMMUTABLE' | 'MUTABLE' | 'SEGMENTED';

  type DataMap<T> = { [key: string]: T };

  type CustomStyle = {
    type: 'STYLE';
    style: Style;
  };

  type CustomEntity = {
    type: 'ENTITY';
    entityKey: string;
  };

  type SyntheticElement = ElementNode;
  type DOMElement = SyntheticElement | Element;

  type ElementStyles = { [tagName: string]: Style };
  type CustomBlockFn = (element: DOMElement) => PartialBlock;
  type CustomInlineFn = (
    element: DOMElement,
    creators: {
      Style: (style: string) => CustomStyle;
      Entity: (type: string, data: DataMap<any>, mutability?: EntityMutability) => CustomEntity;
    }
  ) => CustomStyle | CustomEntity;

  export interface Options {
    elementStyles?: ElementStyles;
    blockTypes?: { [key: string]: string };
    customBlockFn?: CustomBlockFn;
    customInlineFn?: CustomInlineFn;
  }

  export function stateFromMarkdown(markdown: string, options?: Options): ContentState;
}
