import React from 'react';

interface PreviewerProps {
  className?: string;
  previewRef?: (ref: Previewer) => void;
  html: string;
  emptyPreviewHtml: string;
}

export interface MdePreviewState {}

export default class Previewer extends React.Component<PreviewerProps, MdePreviewState> {
  previewRef: HTMLDivElement;

  render() {
    const { html, className } = this.props;
    return (
      <div className={`mde-preview ${className || ''}`}>
        <div
          className="mde-preview-content"
          dangerouslySetInnerHTML={{ __html: html || '<p>&nbsp;</p>' }}
          ref={p => (this.previewRef = p as HTMLDivElement)}
        />
      </div>
    );
  }
}
