import './DocDetail.scss';

import React from 'react';
import { RouteComponentProps } from 'react-router-dom';
import DocStore from '../../../stores/DocStore';
import UserStore from '../../../stores/UserStore';
import { observer, inject } from 'mobx-react';
import DocModel from '../../../models/DocModel';
import { Link } from 'react-router-dom';
import { appendPath } from '../../../utils';
import DocRender from '../doc-render/DocRenderer';

interface DocDetailProps extends RouteComponentProps<{ pid: string; did: string }> {
  docStore?: DocStore;
  userStore?: UserStore;
}

interface DocDetailState {}

@inject('docStore', 'userStore')
@observer
export default class DocDetail extends React.Component<DocDetailProps, DocDetailState> {
  constructor(props: DocDetailProps) {
    super(props);
    const did = this.props.match.params.did;
    const docStore = this.props.docStore!;
    docStore.selectDoc(docStore.docList.filter(doc => doc.id === did)[0]);
  }

  render() {
    const docStore = this.props.docStore!;
    const userStore = this.props.userStore!;
    const user = userStore.user!;
    const { pathname } = this.props.location;

    const doc: DocModel | null = docStore.selectedDoc;

    if (!doc) {
      return null;
    }

    return (
      <div className="doc-detail">
        <header className="&-header">
          <span className="&-title">{doc.title}</span>
          {!user.isQyUser ? (
            <Link
              to={appendPath(
                pathname
                  .split('/')
                  .slice(0, -1)
                  .join('/'),
                'edit'
              )}
              onClick={() => docStore.toggleEditorStatus('update')}
            >
              编辑
            </Link>
          ) : null}
        </header>
        <DocRender doc={doc} />
      </div>
    );
  }
}
