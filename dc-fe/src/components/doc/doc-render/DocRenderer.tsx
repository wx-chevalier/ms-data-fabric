import './DocRender.scss';
import React from 'react';
import DocModel from '../../../models/DocModel';
import Previewer from '../../editor/previewer/Previewer';
import { converter } from '../../editor/Editor';

interface DocRenderProps {
  doc: DocModel;
}

/**
 * Doc渲染
 */
export default class DocRender extends React.Component<DocRenderProps> {
  render() {
    const doc = this.props.doc;

    return (
      <div className="content">
        <h1 className="&-title">{doc.title}</h1>
        <div className="&-info">
          <span className="&-creater">
            创建者：
            {doc.creater.username}
          </span>
          <span className="&-created-at">
            创建时间：
            {doc.createdAt}
          </span>
          <span className="&-updated-at">
            最后更新日期：
            {doc.updatedAt}
          </span>
        </div>
        <section className="&-content-body">
          <Previewer html={converter.makeHtml(doc.content)} emptyPreviewHtml={''} />
        </section>
      </div>
    );
  }
}
