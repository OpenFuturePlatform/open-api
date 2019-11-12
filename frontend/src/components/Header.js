import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom';
import {Button, Dropdown, Icon, Menu} from 'semantic-ui-react';
import {LOGIN_URL} from '../const/index';

class Header extends Component {
  renderNotAuthorizedContent() {
    return (
      <Menu.Item position="right">
        <a className="item" href={LOGIN_URL}>
          <Button color="google plus">
            <Icon name="google plus" />
            Login with Google
          </Button>
        </a>
      </Menu.Item>
    );
  }

  renderAuthContent() {
    return (
      <Menu.Item position="right">
        <Link className="item" to={'/scaffolds'}>
          Open Tokens: {this.props.currentUser.credits}
        </Link>
        <Dropdown className="item" text="Scaffolds">
          <Dropdown.Menu>
            <Dropdown.Item text='Ethereum' as={Link} to={'/scaffolds/ethereum'}/>
            <Dropdown.Item text='Open' as={Link} to={'/scaffolds/open'}/>
          </Dropdown.Menu>
        </Dropdown>
        <Link className="item" to={'/keys'}>
          Key Management
        </Link>
        <a className="item" href="/logout">
          Log Out
        </a>
      </Menu.Item>
    );
  }

  renderContent() {
    if (this.props.isLoading) {
      return null;
    }

    if (!this.props.isAuthorized) {
      return this.renderNotAuthorizedContent();
    }

    return this.renderAuthContent();
  }

  render() {
    return (
      <Menu fluid={true} style={{ marginTop: '0' }}>
        <Menu.Item position="left">
          <Link to="/scaffolds">
            <img src="/img/svg/logo-circle.svg" alt="logo" />
            <img
              src="/img/svg/logo-name.svg"
              alt="logo"
              style={{
                left: '8px',
                paddingBottom: '6px',
                paddingRight: '10px'
              }}
            />
          </Link>
        </Menu.Item>
        {this.renderContent()}
      </Menu>
    );
  }
}

const mapStateToProps = ({ auth: { user, isAuthorized, isLoading } }) => ({
  currentUser: user,
  isAuthorized,
  isLoading
});

export default connect(mapStateToProps)(Header);
