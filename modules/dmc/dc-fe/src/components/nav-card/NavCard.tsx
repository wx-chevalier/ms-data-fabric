import React from 'react';
import './NavCard.scss';
import { Link } from 'react-router-dom';
import { Icon } from 'antd';

interface NavCardProps {
  title: string;
  description?: string;
  quantifier: string;
  content: string;
  to: string;
}

const NavCard: React.StatelessComponent<NavCardProps> = ({
  title,
  description,
  quantifier,
  content,
  to
}) => {
  return (
    <Link to={to}>
      <div className="nav-card">
        <div className="&-text">
          <div className="&-title">
            {title}
            <div className="&-sub-title">{description}</div>
          </div>
          <div className="&-content">
            <i>{content}</i>
            {quantifier}
          </div>
        </div>
        <Icon type="right" />
      </div>
    </Link>
  );
};

export default NavCard;
