import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {
  activateEthereumScaffold,
  deactivateEthereumScaffold,
  topUpTokenBalance
} from '../actions/ethereum-scaffold-activation';
import {MIN_CONTRACT_DEPOSIT} from '../const/index';
import {EthereumScaffoldDeactivate} from './EthereumScaffoldDeactivate';
import {EthereumScaffoldActivate} from './EthereumScaffoldActivate';
import styled from 'styled-components';
import {EthereumScaffoldTopUpTokens} from './EthereumScaffoldTopUpTokens';

const ButtonContainer = styled.div`
  padding-top: 10px;
`;

const RedText = styled.div`
  color: red;
`;

class EthereumScaffoldStatus extends Component {
  validateTokenBalance() {
    const { tokenBalance } = this.props.summary;
    return tokenBalance >= MIN_CONTRACT_DEPOSIT;
  }

  renderActivateButton = () => {
    const { scaffold, summary, actions } = this.props;
    const { activated, developerAddress } = summary;
    const onDeactivate = () => actions.deactivateEthereumScaffold(scaffold);
    if (activated) {
      return <EthereumScaffoldDeactivate developerAddress={developerAddress} onSubmit={onDeactivate} />;
    }
    if (!this.validateTokenBalance()) {
      return <EthereumScaffoldTopUpTokens scaffold={scaffold} />;
    }
    return <EthereumScaffoldActivate onSubmit={() => actions.activateEthereumScaffold(scaffold)} />;
  };

  render() {
    const { error, summary } = this.props;
    const { tokenBalance, activated } = summary;

    if (error) {
      return <RedText>{error}</RedText>;
    }
    return (
      <div>
        Status: {activated ? 'Active' : 'Disabled'} ({tokenBalance || 0} tokens)
        <ButtonContainer>{this.renderActivateButton()}</ButtonContainer>
      </div>
    );
  }
}

const mapStateToProps = (state, { scaffold, summary, error }) => ({
  summary,
  scaffold,
  fetchError: error
});

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({ deactivateEthereumScaffold, activateEthereumScaffold, topUpTokenBalance }, dispatch)
});

export const ScaffoldStatusContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(EthereumScaffoldStatus);
