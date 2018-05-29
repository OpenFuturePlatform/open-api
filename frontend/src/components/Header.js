import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom';
import {Button, Icon, Menu} from 'semantic-ui-react';

class Header extends Component {
    renderContent() {
        switch (this.props.auth) {
            case null:
                return (
                    <Menu fluid={true} style={{marginTop: '10px'}}>
                        <Menu.Item position="left">
                            <div>
                                <img
                                    src="/img/svg/logo-circle.svg"
                                    alt="logo"
                                />
                                <img
                                    style={{
                                        left: '8px',
                                        paddingBottom: '6px',
                                        paddingRight: '10px',
                                    }}
                                    src="/img/svg/logo-name.svg"
                                    alt="logo"
                                />
                            </div>
                        </Menu.Item>
                        <Menu.Item position="right">
                            <a className="item" href="/auth/google">
                                <Button color="google plus">
                                    <Icon name="google plus"/> Login with
                                    Google
                                </Button>
                            </a>
                        </Menu.Item>
                    </Menu>
                );
            case false:
                return (
                    <Menu fluid={true} style={{marginTop: '10px'}}>
                        <Menu.Item position="left">
                            <div>
                                <img
                                    src="/img/svg/logo-circle.svg"
                                    alt="logo"
                                />
                                <img
                                    style={{
                                        left: '8px',
                                        paddingBottom: '6px',
                                        paddingRight: '10px',
                                    }}
                                    src="/img/svg/logo-name.svg"
                                    alt="logo"
                                />
                            </div>
                        </Menu.Item>
                        <Menu.Item position="right">
                            <a className="item" href="/auth/google">
                                <Button color="google plus">
                                    <Icon name="google plus"/> Login with
                                    Google
                                </Button>
                            </a>
                        </Menu.Item>
                    </Menu>
                );
            default:
                return (
                    <Menu fluid={true} style={{marginTop: '10px'}}>
                        <Menu.Item position="left">
                            <div>
                                <img
                                    src="/img/svg/logo-circle.svg"
                                    alt="logo"
                                />
                                <img
                                    style={{
                                        left: '8px',
                                        paddingBottom: '6px',
                                        paddingRight: '10px',
                                    }}
                                    src="/img/svg/logo-name.svg"
                                    alt="logo"
                                />
                            </div>
                        </Menu.Item>
                        <Menu.Item position="right">
                            <Link className="item" to={'/scaffolds'}>
                                Open Tokens: {this.props.auth.credits}
                            </Link>
                            <Link className="item" to={'/scaffolds'}>
                                Scaffolds
                            </Link>
                            <a className="item" href="/api/logout">
                                Log Out
                            </a>
                        </Menu.Item>
                    </Menu>
                );
        }
    }

    render() {
        return <div>{this.renderContent()}</div>;
    }
}

const mapStateToProps = ({auth}) => {
    return {auth};
};

export default connect(mapStateToProps)(Header);
