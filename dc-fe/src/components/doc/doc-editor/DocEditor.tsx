import './DocEditor.scss';
import React from 'react';
import DocForm from '../doc-form/DocForm';
import { RouteComponentProps } from 'react-router';
import DocStore from '../../../stores/DocStore';
import { inject, observer } from 'mobx-react';

interface DocEditorProps extends RouteComponentProps<{}> {
  docStore?: DocStore;
}

@inject('docStore')
@observer
export default class DocEditor extends React.Component<DocEditorProps, {}> {
  render() {
    const docStore = this.props.docStore!;
    const initialValues = () => {
      if (docStore.selectedDoc) {
        return {
          name: docStore.selectedDoc.title,
          content: docStore.selectedDoc.content
        };
      } else {
        return undefined;
      }
    };

    return (
      <div className="doc-editor">
        <DocForm {...this.props} initialValues={initialValues()} />
      </div>
    );
  }
}
