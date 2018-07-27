import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { activateScaffold, deactivateScaffold, topUpTokenBalance } from '../actions/scaffold-activation';
import { MIN_CONTRACT_DEPOSIT } from '../const/index';
import { ScaffoldDeactivate } from './ScaffoldDeactivate';
import { ScaffoldActivate } from './ScaffoldActivate';
import styled from 'styled-components';
import { ScaffoldTopUpTokens } from './ScaffoldTopUpTokens';

const ButtonContainer = styled.div`
  padding-top: 10px;
`;

const RedText = styled.div`
  color: red;
`;

class ScaffoldStatus extends Component {
  validateTokenBalance() {
    const { tokenBalance } = this.props.summary;
    return tokenBalance >= MIN_CONTRACT_DEPOSIT;
  }

  renderActivateButton = () => {
    const { scaffold, summary, actions } = this.props;
    const { activated, developerAddress } = summary;
    const onDeactivate = () => actions.deactivateScaffold(scaffold);
    if (activated) {
      return <ScaffoldDeactivate developerAddress={developerAddress} onSubmit={onDeactivate} />;
    }
    if (!this.validateTokenBalance()) {
      return <ScaffoldTopUpTokens scaffold={scaffold} />;
    }
    return <ScaffoldActivate onSubmit={() => actions.activateScaffold(scaffold)} />;
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
  actions: bindActionCreators({ deactivateScaffold, activateScaffold, topUpTokenBalance }, dispatch)
});

export const ScaffoldStatusContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ScaffoldStatus);
