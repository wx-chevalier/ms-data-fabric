import 'react-mde/lib/styles/css/react-mde-all.css';
import React from 'react';
import ReactMde, { ReactMdeTypes } from 'react-mde';
import * as Showdown from 'showdown';

export const converter = new Showdown.Converter({
  tables: true,
  simplifiedAutoLink: true
});

interface EditorProps {
  setEditingContent: (content: string) => void;
  defaultContent?: string;
}

interface EditorState {
  mdeState: ReactMdeTypes.MdeState;
}

export default class Editor extends React.Component<EditorProps, EditorState> {
  constructor(props: EditorProps) {
    super(props);
    const defaultContent = this.props.defaultContent;
    this.state = {
      mdeState: {
        markdown: defaultContent ? defaultContent : ''
      }
    };
  }

  handleValueChange = (mdeState: ReactMdeTypes.MdeState) => {
    this.setState({ mdeState });
  };

  render() {
    const { setEditingContent } = this.props;

    return (
      <div className="container">
        <ReactMde
          layout="tabbed"
          onChange={v => {
            setEditingContent(v.markdown ? v.markdown : '');
            this.handleValueChange(v);
          }}
          editorState={this.state.mdeState}
          generateMarkdownPreview={markdown => Promise.resolve(converter.makeHtml(markdown))}
        />
      </div>
    );
  }
}
