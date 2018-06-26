import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom';
import {Button, Icon, Menu} from 'semantic-ui-react';

class Header extends Component {

  renderNotAuthorizedContent() {
    return (
      <Menu.Item position="right">
        <a className="item" href="/auth/google">
          <Button color="google plus">
            <Icon name="google plus"/>
            Login with Google
          </Button>
        </a>
      </Menu.Item>
    );
  }

  renderAuthContent() {
    return (
      <Menu.Item position="right">
        <Link className="item" to={'/scaffolds'}>Open Tokens: {this.props.auth.credits}</Link>
        <Link className="item" to={'/scaffolds'}>Scaffolds</Link>
        <Link className="item" to={'/keys'}>Key Management</Link>
        <a className="item" href="/logout">Log Out</a>
      </Menu.Item>
    );
  }

  renderContent() {
    if (!this.props.auth) {
      return this.renderNotAuthorizedContent()
    }

    return this.renderAuthContent()
  }

  render() {
    return (
      <Menu fluid={true} style={{marginTop: '10px'}}>
        <Menu.Item position="left">
          <Link to="/scaffolds">
            <img src="/img/svg/logo-circle.svg" alt="logo"/>
            <img src="/img/svg/logo-name.svg" alt="logo" style={{
              left: '8px',
              paddingBottom: '6px',
              paddingRight: '10px',
            }}/>
          </Link>
        </Menu.Item>
        {this.renderContent()}
      </Menu>
    );
  }
}

const mapStateToProps = ({auth}) => ({auth});

export default connect(mapStateToProps)(Header);
