declare module 'draft-js-export-markdown' {
  import { ContentState } from 'draft-js';

  export function stateToMarkdown(content: ContentState): string;
}
