import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import {
  activateScaffold,
  deactivateScaffold,
  deactivateScaffoldByApi,
  subscribeScaffoldActivation
} from '../actions/scaffold-activation';
import { subscribeEthAccount, unsubscribeEthAccount } from '../actions/eth-account';
import { Button } from 'semantic-ui-react';
import { MIN_CONTRACT_DEPOSIT } from '../const/index';
import { getMetaMaskError } from '../selectors/getMetaMaskError';

class ScaffoldStatus extends Component {
  constructor(props) {
    super(props);
    this.handleOnDeactivate = this.handleOnDeactivate.bind(this);
    this.handleOnActivate = this.handleOnActivate.bind(this);
  }

  componentDidMount() {
    const { scaffoldAddress } = this.props;
    const { activating, activatingHash } = this.props.ethAccount;
    this.props.actions.subscribeEthAccount();
    if (activating) {
      this.props.actions.subscribeScaffoldActivation(activatingHash, scaffoldAddress);
    }
  }

  componentWillUnmount() {
    this.props.actions.unsubscribeEthAccount();
  }

  handleOnDeactivate() {
    const { scaffoldAddress, abi, developerAddress } = this.props;
    this.props.actions.deactivateScaffold(scaffoldAddress, abi, developerAddress);
  }

  handleOnDeactivateByApi() {
    const { scaffoldAddress } = this.props;
    this.props.actions.deactivateScaffoldByApi(scaffoldAddress);
  }

  handleOnActivate() {
    const { scaffoldAddress, ethAccount } = this.props;
    this.props.actions.activateScaffold(scaffoldAddress, ethAccount.account);
  }

  validateTokenBalance() {
    const { tokenBalance } = this.props.ethAccount;
    return tokenBalance >= MIN_CONTRACT_DEPOSIT;
  }

  renderDeactivateButton() {
    const { metaMaskError } = this.props;
    const loading = this.props.ethAccount.activating;
    const disabled = loading;

    return (
      <div style={{ display: 'flex' }}>
        <div style={{ paddingRight: 30 }}>
          <Button loading={loading} disabled={disabled} onClick={this.handleOnDeactivate}>
            Deactivate
          </Button>
        </div>
        <div style={{ color: 'red', paddingTop: 8 }}>{metaMaskError}</div>
      </div>
    );
  }

  renderMetaMaskMessage() {
    const { metaMaskError } = this.props;
    if (metaMaskError) {
      return metaMaskError;
    }

    if (!this.validateTokenBalance()) {
      return `MetaMask account have no OPEN Tokens. Select account with ${MIN_CONTRACT_DEPOSIT} OPEN tokens or top up the balance.`;
    }

    return '';
  }

  renderMessage() {
    const { scaffoldAddress } = this.props;
    const activationAllowed = this.validateTokenBalance();

    if (activationAllowed) {
      return null;
    }

    return (
      <div>
        <div style={{ color: 'red' }}>Your scaffold is created but is inactive.</div>
        <div style={{ color: 'red' }}>
          To activate your scaffold you need to have {MIN_CONTRACT_DEPOSIT} OPEN Tokens on scaffold contract.
        </div>
        <div style={{ color: 'red' }}>
          You can transfer OPEN Tokens to <i className="selectable">{scaffoldAddress}</i>, or ...
        </div>
        <div style={{ color: 'red' }}>Use MetaMask: {this.renderMetaMaskMessage()}</div>
      </div>
    );
  }

  renderActivateButton() {
    const loading = this.props.ethAccount.activating;
    const activationAllowed = this.validateTokenBalance();
    const disabled = loading || !activationAllowed;
    return (
      <div style={{ display: 'flex' }}>
        <div style={{ paddingRight: 30 }}>
          <Button primary loading={loading} disabled={disabled} onClick={this.handleOnActivate}>
            Activate
          </Button>
        </div>
        {this.renderMessage()}
      </div>
    );
  }

  render() {
    const { error, tokenBalance } = this.props;
    const status = tokenBalance >= MIN_CONTRACT_DEPOSIT;

    if (error) {
      return <div style={{ color: 'red' }}>{error}</div>;
    }

    return (
      <div>
        Status: {status ? 'Active' : 'Disabled'} ({this.props.tokenBalance || 0} tokens)
        <div style={{ marginTop: '10px' }}>
          {status ? this.renderDeactivateButton() : this.renderActivateButton()}
          {/*{this.renderDeactivateButton()} {this.renderActivateButton()}*/}
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state, { scaffoldAddress, abi, summary: { tokenBalance, developerAddress }, error }) => {
  const { ethAccount } = state;
  const metaMaskError = getMetaMaskError(state);
  return { ethAccount, developerAddress, scaffoldAddress, abi, tokenBalance, metaMaskError, fetchError: error };
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(
    {
      deactivateScaffold,
      deactivateScaffoldByApi,
      activateScaffold,
      subscribeEthAccount,
      unsubscribeEthAccount,
      subscribeScaffoldActivation
    },
    dispatch
  )
});

export const ScaffoldStatusContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ScaffoldStatus);
