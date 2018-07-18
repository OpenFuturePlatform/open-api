import React from 'react';
import { withVisible } from '../components-ui/withVisible';
import { Button, Divider } from 'semantic-ui-react';
import { withSaving } from '../components-ui/withSaving';
import { ConfirmationModal } from '../components-ui/ConfirmationModal';
import { TransactionError } from '../components-ui/TransactionError';
import { MIN_CONTRACT_DEPOSIT } from '../const';
import { validateMMTokenBalance } from '../selectors/getMetaMaskError';
import { topUpTokenBalance } from '../actions/scaffold-activation';
import { connect } from 'react-redux';
import styled from 'styled-components';

const ErrorMessage = styled.div`
  display: inline-block;
  padding-left: 30px;
  color: red;
  vertical-align: top;
`;

const ScaffoldTopUpTokensComponent = ({ children, topUpError, ...props }) => (
  <div>
    <span>
      <Button primary onClick={props.onShow}>
        Top up token balance
      </Button>
      <ConfirmationModal {...props} submitDisabled={!!topUpError}>
        <div>
          You are about to top up Scaffold Token Balance. Proceed?
          <Divider />
          <span>PS: Please be patient this may take a while...</span>
          <TransactionError>{topUpError}</TransactionError>
        </div>
      </ConfirmationModal>
    </span>
    <ErrorMessage>
      Your scaffold is created but is inactive.<br />
      To activate your scaffold you need to have {MIN_CONTRACT_DEPOSIT} OPEN Tokens on scaffold contract.
    </ErrorMessage>
  </div>
);

const mapStateToProps = state => ({ topUpError: validateMMTokenBalance(state) });

const mapDispatchToProps = (dispatch, { scaffold }) => ({
  onSubmit: () => dispatch(topUpTokenBalance(scaffold))
});

export const ScaffoldTopUpTokens = connect(
  mapStateToProps,
  mapDispatchToProps
)(withVisible(withSaving(ScaffoldTopUpTokensComponent)));
